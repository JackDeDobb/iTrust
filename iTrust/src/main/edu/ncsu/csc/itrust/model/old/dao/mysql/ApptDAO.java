package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ApptBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.personnel.Speciality;

public class ApptDAO {

	private transient final DAOFactory factory;
	private transient final ApptBeanLoader abloader;
	private transient final ApptTypeDAO apptTypeDAO;

	private static final int MIN_MID = 999999999;

	public ApptDAO(final DAOFactory factory) {
		this.factory = factory;
		this.apptTypeDAO = factory.getApptTypeDAO();
		this.abloader = new ApptBeanLoader();
	}

	public List<ApptBean> getAppt(final int apptID) throws SQLException, DBException {
		ResultSet results = null;
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM appointment WHERE appt_id=?")) {
			stmt.setInt(1, apptID);
			results = stmt.executeQuery();
			final List<ApptBean> abList = this.abloader.loadList(results);
			return abList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public List<ApptBean> getApptsFor(final long mid) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = (mid >= MIN_MID)
						? conn.prepareStatement(
								"SELECT * FROM appointment WHERE doctor_id=? AND sched_date > NOW() ORDER BY sched_date;")
						: conn.prepareStatement(
								"SELECT * FROM appointment WHERE patient_id=? AND sched_date > NOW() ORDER BY sched_date;")) {
			stmt.setLong(1, mid);

			ResultSet results = stmt.executeQuery();
			List<ApptBean> abList = this.abloader.loadList(results);
			results.close();
			return abList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public List<ApptBean> getAllApptsFor(long mid) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = (mid >= MIN_MID)
						? conn.prepareStatement("SELECT * FROM appointment WHERE doctor_id=? ORDER BY sched_date;")
						: conn.prepareStatement("SELECT * FROM appointment WHERE patient_id=? ORDER BY sched_date;")) {
			stmt.setLong(1, mid);

			final ResultSet results = stmt.executeQuery();
			final List<ApptBean> abList = this.abloader.loadList(results);
			results.close();
			return abList;
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}

	public void scheduleAppt(final ApptBean appt) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = this.abloader.loadParameters(conn.prepareStatement(
						"INSERT INTO appointment (appt_type, patient_id, doctor_id, sched_date, comment) "
								+ "VALUES (?, ?, ?, ?, ?)"),
						appt)) {
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Schedules next available appointment for a given starting time and HCP.
	 * TODO: Hardcoded 1hr duration right now.
	 */
	public void scheduleNextAvailableAppt(final ApptBean appt) throws SQLException,
			DBException {

		Connection conn = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		try {
			conn = factory.getConnection();

			// Get ascending list of next taken appointments.
			stmt1 = conn.prepareStatement(
				"SELECT appointment.sched_date as start_time, appointment.sched_date + INTERVAL appointmenttype.duration " +
						"MINUTE as end_time, appointmenttype.duration, appointment.appt_type as appt_type " +
						"FROM `appointment`, `appointmenttype` " +
						"WHERE appointment.sched_date >= ? " +
						"AND appointment.doctor_id = ? " +
						"AND appointment.appt_type = appointmenttype.appt_type " +
						"ORDER BY end_time ASC "
			);
			System.out.println("Appt Date: " + appt.getDate());
			System.out.println("Appt HCP: " + appt.getHcp());
			stmt1.setTimestamp(1, appt.getDate());
			stmt1.setLong(2, appt.getHcp());
			rs = stmt1.executeQuery();

			// Iterate through, and check if current appointment overlap
			// with desired date
			// Otherwise accept and schedule.

			Timestamp startAppt = appt.getDate();
			int apptDurationInMinutes = apptTypeDAO.getApptType(appt.getApptType()).getDuration();
			Timestamp endAppt = addTime(startAppt, apptDurationInMinutes, ChronoUnit.MINUTES);
			while (rs.next()) {
				Timestamp curStartTime = rs.getTimestamp("start_time");
				Timestamp curEndTime = rs.getTimestamp("end_time");
				if (endAppt.before(curStartTime)) {
					break;
				} else {
					startAppt = curEndTime;
					int curApptDurationInMinutes = apptTypeDAO.
							getApptType(rs.getString("appt_type")).getDuration();
					endAppt = addTime(startAppt, curApptDurationInMinutes,  ChronoUnit.MINUTES);
				}
				LocalDateTime ldt = endAppt.toLocalDateTime();
				if (ldt.getHour() > 16 ||
						(ldt.getMinute() > 0 && ldt.getHour() == 16)) {
					// Current time state is after 4pm.
					// Move forward to next day 9am.
					LocalDateTime nextDay = ldt.plusDays(1);
					LocalDateTime firstTimeNextDay = nextDay.minusHours(nextDay.getHour() - 9);
					startAppt = Timestamp.valueOf(firstTimeNextDay);
					endAppt = addTime(startAppt, apptDurationInMinutes, ChronoUnit.MINUTES);
				} else if (ldt.getHour() < 9) {
					// Current time state is before 9am.
					// Move forward to 9am.
					startAppt = Timestamp.valueOf(ldt.plusMinutes(9 - ldt.getHour()));
					endAppt = addTime(startAppt, apptDurationInMinutes, ChronoUnit.MINUTES);
				}
			}

			// Schedule new appointment.
			ApptBean newAppt = new ApptBean();
			newAppt.setDate(startAppt);
			newAppt.setApptType(appt.getApptType());
			newAppt.setPatient(appt.getPatient());
			newAppt.setComment(appt.getComment());
			newAppt.setHcp(appt.getHcp());
			newAppt.setPrice(appt.getPrice());

			scheduleAppt(newAppt);

		} catch (SQLException sqlEx) {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException ex) {}
			try {
				if (stmt1 != null)
					stmt1.close();
			} catch (SQLException ex) { }
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) { }

			sqlEx.printStackTrace();
			throw new DBException(sqlEx);
		} catch (DBException dbEx) {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException ex) {}
			try {
				if (stmt1 != null)
					stmt1.close();
			} catch (SQLException ex) { }
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) { }
			throw dbEx;
		}


	}


	private Timestamp addTime(Timestamp startTs, int amount, TemporalUnit unit) {
		return Timestamp.from(startTs.toInstant().plus(amount, unit));
	}

	public void editAppt(final ApptBean appt) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("UPDATE appointment SET appt_type=?, sched_date=?, comment=? WHERE appt_id=?")) {
			stmt.setString(1, appt.getApptType());
			stmt.setTimestamp(2, appt.getDate());
			stmt.setString(3, appt.getComment());
			stmt.setInt(4, appt.getApptID());

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public void removeAppt(final ApptBean appt) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM appointment WHERE appt_id=?")) {
			stmt.setInt(1, appt.getApptID());

			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public List<ApptBean> getAllHCPConflictsForAppt(final long mid, final ApptBean appt)
			throws SQLException, DBException {

		final int duration = apptTypeDAO.getApptType(appt.getApptType()).getDuration();

		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * " 
						+ "FROM appointment a, appointmenttype type " // and the corresponding types 
						+ "WHERE a.appt_type=type.appt_type AND " // match them with types
						+ "((DATE_ADD(a.sched_date, INTERVAL type.duration MINUTE)>? AND " // a1 ends after a2 starts AND
						+ "a.sched_date<=?) OR " // a1 starts before a2 OR
						+ "(DATE_ADD(?, INTERVAL ? MINUTE)>a.sched_date AND " // a2 ends after a1 starts AND
						+ "?<=a.sched_date)) AND " // a2 starts before a1 starts
						+ "a.doctor_id=? AND a.appt_id!=?;")
			) {
			stmt.setTimestamp(1, appt.getDate());
			stmt.setTimestamp(2, appt.getDate());
			stmt.setTimestamp(3, appt.getDate());
			stmt.setInt(4, duration);
			stmt.setTimestamp(5, appt.getDate());
			stmt.setLong(6, mid);
			stmt.setInt(7, appt.getApptID());

			final ResultSet results = stmt.executeQuery();

			final List<ApptBean> conflictList = this.abloader.loadList(results);
			results.close();
			return conflictList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public List<ApptBean> getAllPatientConflictsForAppt(final long mid, final ApptBean appt)
			throws SQLException, DBException {
		final int duration = apptTypeDAO.getApptType(appt.getApptType()).getDuration();
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * " 
						+ "FROM appointment a, appointmenttype type " // and the corresponding types
						+ "WHERE a.appt_type=type.appt_type AND " // match them with types
						+ "((DATE_ADD(a.sched_date, INTERVAL type.duration MINUTE)>? AND "// a1 ends after a2 starts AND
						+ "a.sched_date<=?) OR " // a1 starts before a2 OR
						+ "(DATE_ADD(?, INTERVAL ? MINUTE)>a.sched_date AND " // a2 ends after a1 starts AND
						+ "?<=a.sched_date)) AND " // a2 starts before a1 starts
						+ "a.patient_id=? AND a.appt_id!=?;")
			) {
			stmt.setTimestamp(1, appt.getDate());
			stmt.setTimestamp(2, appt.getDate());
			stmt.setTimestamp(3, appt.getDate());
			stmt.setInt(4, duration);
			stmt.setTimestamp(5, appt.getDate());
			stmt.setLong(6, mid);
			stmt.setInt(7, appt.getApptID());

			final ResultSet results = stmt.executeQuery();

			final List<ApptBean> conflictList = this.abloader.loadList(results);
			results.close();
			return conflictList;
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}

	/**
	 * Returns all past and future appointment conflicts for the doctor with the
	 * given MID
	 * 
	 * @param mid
	 * @throws SQLException
	 */
	public List<ApptBean> getAllConflictsForDoctor(final long mid) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT a1.* "
						+ "FROM appointment a1, appointment a2, " // all possible sets of 2 appts
						+ "appointmenttype type1,appointmenttype type2 " // and the corresponding types
						+ "WHERE a1.appt_id!=a2.appt_id AND " // exclude itself
						+ "a1.appt_type=type1.appt_type AND a2.appt_type=type2.appt_type AND " // match then with types
						+ "((DATE_ADD(a1.sched_date, INTERVAL type1.duration MINUTE)>a2.sched_date AND " // a1 ends after a2 starts AND
						+ "a1.sched_date<=a2.sched_date) OR" // a1 starts before a2 OR
						+ "(DATE_ADD(a2.sched_date, INTERVAL type2.duration MINUTE)>a1.sched_date AND " // a2 ends after a1 starts AND
						+ "a2.sched_date<=a1.sched_date)) AND " // a2 starts before a1 starts
						+ "a1.doctor_id=? AND a2.doctor_id=?;")
			) {
			stmt.setLong(1, mid);
			stmt.setLong(2, mid);

			final ResultSet results = stmt.executeQuery();

			final List<ApptBean> conflictList = this.abloader.loadList(results);
			results.close();
			return conflictList;
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}

	/**
	 * Returns all past and future appointment conflicts for the patient with
	 * the given MID
	 * 
	 * @param mid
	 * @throws SQLException
	 * @throws DBException
	 */
	public List<ApptBean> getAllConflictsForPatient(final long mid) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT a1.* "
						+ "FROM appointment a1, appointment a2, " // all possible sets of 2 appts
						+ "appointmenttype type1,appointmenttype type2 " // and the corresponding types
						+ "WHERE a1.appt_id!=a2.appt_id AND " // exclude itself
						+ "a1.appt_type=type1.appt_type AND a2.appt_type=type2.appt_type AND " // match them with types
						+ "((DATE_ADD(a1.sched_date, INTERVAL type1.duration MINUTE)>a2.sched_date AND " // a1 ends after a2 starts AND
						+ "a1.sched_date<=a2.sched_date) OR" // a1 starts before a2 OR
						+ "(DATE_ADD(a2.sched_date, INTERVAL type2.duration MINUTE)>a1.sched_date AND " // a2 ends after a1 starts AND
						+ "a2.sched_date<=a1.sched_date)) AND " // a2 starts before a1 starts
						+ "a1.patient_id=? AND a2.patient_id=?;")
			) {
			stmt.setLong(1, mid);
			stmt.setLong(2, mid);

			final ResultSet results = stmt.executeQuery();

			final List<ApptBean> conflictList = this.abloader.loadList(results);
			results.close();
			return conflictList;
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}

	/**Select appointments in n days.
	 *
	 * @param n days
	 * @return List of appointments
	 * @throws SQLException
	 * @throws DBException
	 */
	public  List<ApptBean> getApptForReminders(int n) throws SQLException, DBException{
		try {
			Connection conn = factory.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM appointment WHERE sched_date BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL ? DAY)");
			stmt.setInt(1, n);
			ResultSet rs = stmt.executeQuery();
			List<ApptBean> list = this.abloader.loadList(rs);
			rs.close();
			return list;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
