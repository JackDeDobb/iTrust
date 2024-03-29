package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.HospitalBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.HospitalBeanLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PersonnelLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.enums.Role;
import edu.ncsu.csc.itrust.model.personnel.Speciality;

/**
 * Used for managing information related to personnel: HCPs, UAPs, Admins
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be
 * reflections of the database, that is, one DAO per table in the database (most
 * of the time). For more complex sets of queries, extra DAOs are added. DAOs
 * can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than
 * a factory. All DAOs should be accessed by DAOFactory (@see
 * {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 */
public class PersonnelDAO {

	private transient final DAOFactory factory;
	private transient final PersonnelLoader personnelLoader;
	private transient final HospitalBeanLoader hospBeanLoader;

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public PersonnelDAO(final DAOFactory factory) {
		this.factory = factory;
		personnelLoader = new PersonnelLoader();
		hospBeanLoader = new HospitalBeanLoader();
	}

	/**
	 * Returns the name for a given MID
	 * 
	 * @param mid
	 *            The MID of the personnel in question.
	 * @return A String representing the name of the personnel.
	 * @throws ITrustException
	 * @throws DBException
	 */
	public String getName(final long mid) throws ITrustException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT firstName, lastName FROM personnel WHERE MID=?");) {
			stmt.setLong(1, mid);
			ResultSet results = stmt.executeQuery();
			if (!results.next()) {
				throw new ITrustException("User does not exist");
			}
			final String result = results.getString("firstName") + " " + results.getString("lastName");
			results.close();
			return result;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public long getNextID(final Role role) throws DBException, ITrustException {
		long minID = role.getMidFirstDigit() * 1000000000L;
		minID = minID == 0 ? 1 : minID; // Do not use 0 as an ID.
		final long maxID = minID + 999999998L;
		long nextID = minID;

		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT MAX(users.mid) FROM users WHERE mid BETWEEN ? AND ?")) {
			stmt.setLong(1, minID);
			stmt.setLong(2, maxID);
			final ResultSet results = stmt.executeQuery();
			if (results.next()) {
				nextID = results.getLong(1) + 1;
				if (nextID < minID) {
					nextID = minID;
				}
			}
			results.close();
			return nextID;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Adds an empty personnel, and returns the MID.
	 * 
	 * @return A long indicating the new MID.
	 * @param role
	 *            A {@link Role} enum indicating the personnel's specific role.
	 * @throws DBException
	 * @throws ITrustException
	 */
	public long addEmptyPersonnel(final Role role) throws DBException, ITrustException {
		final long nextID = getNextID(role);

		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO personnel(MID, Role) VALUES(?,?)")) {
			stmt.setString(1, Long.valueOf(nextID).toString());
			stmt.setString(2, role.name());
			stmt.executeUpdate();
			return nextID;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Retrieves a PersonnelBean with all of the specific information for a
	 * given employee.
	 * 
	 * @param mid
	 *            The MID of the personnel in question.
	 * @return A PersonnelBean representing the employee.
	 * @throws DBException
	 */
	public PersonnelBean getPersonnel(final long mid) throws DBException {
		PersonnelBean bean = null;

		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM personnel WHERE MID = ?")) {
			stmt.setLong(1, mid);
			final ResultSet results = stmt.executeQuery();
			if (results.next()) {
				bean = personnelLoader.loadSingle(results);
			}
			results.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
		return bean;
	}

	/**
	 * Determines if a given person is an OBGYN.
	 * @param mid
	 *            The MID of the personnel in question.
	 * @return True, if given personnel is an OBGYN or False otherwise.
	 */
	public boolean isOBGYN(final long mid) throws DBException {
		try {
			Speciality speciality = Speciality.fromString(getPersonnel(mid).getSpecialty());
			return speciality == Speciality.OBGYN;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * Updates the demographics for a personnel.
	 * 
	 * @param p
	 *            The personnel bean with the updated information.
	 * @throws DBException
	 */
	public void editPersonnel(final PersonnelBean pBean) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = personnelLoader
						.loadParameters(conn.prepareStatement("UPDATE personnel SET AMID=?,firstName=?,lastName=?,"
								+ "phone=?, address1=?,address2=?,city=?, state=?, zip=?, specialty=?, email=?"
								+ " WHERE MID=?"), pBean)) {
			stmt.setLong(12, pBean.getMID());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Indicates whether a certain personnel is in the database.
	 * 
	 * @param pid
	 *            The MID of the personnel in question.
	 * @return A boolean indicating whether this personnel exists.
	 * @throws DBException
	 */
	public boolean checkPersonnelExists(final long pid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM personnel WHERE MID=?")) {
			stmt.setLong(1, pid);
			final ResultSet results = stmt.executeQuery();
			final boolean exists = results.next();
			results.close();
			return exists;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns all of the hospitals this LHCP is associated with.
	 * 
	 * @param mid
	 *            The MID of the personnel in question.
	 * @return A java.util.List of HospitalBeans.
	 * @throws DBException
	 */
	public List<HospitalBean> getHospitals(final long mid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM hcpassignedhos hah,hospitals h "
						+ "WHERE hah.HCPID=? AND hah.HosID=h.HospitalID ORDER BY HospitalName ASC")) {
			stmt.setLong(1, mid);
			final ResultSet results = stmt.executeQuery();
			final List<HospitalBean> hospitals = hospBeanLoader.loadList(results);
			results.close();
			return hospitals;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns all of the hospitals this uap's associated hcp is associated
	 * with.
	 * 
	 * @param mid
	 *            The MID of the personnel in question.
	 * @return A java.util.List of HospitalBeans.
	 * @throws DBException
	 */
	public List<HospitalBean> getUAPHospitals(final long mid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT hospitals.* FROM hcpassignedhos hah "
						+ "INNER JOIN hcprelations ON hah.HCPID = hcprelations.HCP "
						+ "INNER JOIN hospitals ON hah.hosID = hospitals.HospitalID "
						+ "WHERE hcprelations.UAP=? ORDER BY HospitalName ASC")) {
			stmt.setLong(1, mid);
			final ResultSet results = stmt.executeQuery();
			final List<HospitalBean> hospitals = hospBeanLoader.loadList(results);
			results.close();
			return hospitals;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns all personnel of specified specialty from the specified hospital.
	 * 
	 * @param hosid,
	 *            the ID of the Hospital to get personnel from
	 * @param specialty,
	 *            the type of specialty to search for
	 * @return A java.util.List of PersonnelBeans.
	 * @throws DBException
	 */
	public List<PersonnelBean> getPersonnelFromHospital(final String hosid, final String specialty) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = specialty.equalsIgnoreCase("all")
						? conn.prepareStatement(
								"SELECT * FROM hcpassignedhos hah inner join personnel p where hah.hosID = ? and hah.HCPID = p.MID and p.role = 'hcp'")
						: conn.prepareStatement(
								"SELECT * FROM hcpassignedhos hah inner join personnel p where hah.hosID = ? and hah.HCPID = p.MID and p.specialty = ? and p.role = 'hcp'");) {
			stmt.setString(1, hosid);
			if (!specialty.equalsIgnoreCase("all")) {
				stmt.setString(2, specialty);
			}

			// NOTE: There is a possible NullPointerException Threat here!
			final ResultSet results = stmt.executeQuery();
			final List<PersonnelBean> loadlist = personnelLoader.loadList(results);
			results.close();
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns all personnel of specified specialty from the specified hospital.
	 * 
	 * @param hosid,
	 *            the ID of the Hospital to get personnel from
	 * @param specialty,
	 *            the type of specialty to search for
	 * @return A java.util.List of PersonnelBeans.
	 * @throws DBException
	 */
	public List<PersonnelBean> getPersonnelFromHospital(final String hosid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM hcpassignedhos hah inner join personnel p where hah.hosID = ? and hah.HCPID = p.MID and p.role = 'hcp'")) {
			stmt.setString(1, hosid);

			final ResultSet results = stmt.executeQuery();
			final List<PersonnelBean> personnelFromHospital = personnelLoader.loadList(results);
			results.close();
			return personnelFromHospital;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns all personnel in the database.
	 * 
	 * @return A java.util.List of personnel.
	 * @throws DBException
	 */
	public List<PersonnelBean> getAllPersonnel() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM personnel where role in ('hcp','uap','er') ")) {
			final ResultSet results = stmt.executeQuery();
			final List<PersonnelBean> allPersonnel = personnelLoader.loadList(results);
			results.close();
			return allPersonnel;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns a list of UAPs who work for this LHCP.
	 * 
	 * @param hcpid
	 *            The MID of the personnel in question.
	 * @return A java.util.List of UAPs.
	 * @throws DBException
	 */
	public List<PersonnelBean> getUAPsForHCP(final long hcpid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM personnel WHERE MID IN (SELECT UAP FROM hcprelations WHERE HCP=?)")) {
			stmt.setLong(1, hcpid);
			final ResultSet results = stmt.executeQuery();
			final List<PersonnelBean> uaps = personnelLoader.loadList(results);
			results.close();
			return uaps;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Matches all personnel who have names LIKE (as in SQL) the first and last
	 * names passed in.
	 * 
	 * @param first
	 *            The first name to be searched for.
	 * @param last
	 *            The last name to be searched for.
	 * @return A java.util.List of personnel who match these names.
	 * @throws DBException
	 */
	public List<PersonnelBean> searchForPersonnelWithName(final String first, final String last) throws DBException {
		if ("%".equals(first) && "%".equals(last)) {
			return new Vector<PersonnelBean>();
		}

		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM personnel WHERE firstName LIKE ? AND lastName LIKE ?")) {
			stmt.setString(1, first);
			stmt.setString(2, last);
			final ResultSet results = stmt.executeQuery();
			final List<PersonnelBean> matchingPersonnel = personnelLoader.loadList(results);
			results.close();
			return matchingPersonnel;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns list of personnel who are Lab Techs.
	 * 
	 * @return List of personnel beans.
	 * @throws DBException
	 */
	public List<PersonnelBean> getLabTechs() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM personnel WHERE role = 'lt' ");
				final ResultSet results = stmt.executeQuery()) {
			final List<PersonnelBean> labTechnicians = personnelLoader.loadList(results);
			return labTechnicians;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns all experts with names "LIKE" with wildcards (as in SQL) the
	 * passed in parameters.
	 * 
	 * @param first
	 *            The expert's first name.
	 * @param last
	 *            The expert's last name.
	 * @return A java.util.List of ExpertBeans.
	 * @throws DBException
	 */
	public List<PersonnelBean> fuzzySearchForExpertsWithName(String first, String last) throws DBException {
		if (first.equals("%") && last.equals("%")) {
			return new Vector<PersonnelBean>();
		}
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM personnel WHERE firstName LIKE ? AND lastName LIKE ? AND role='hcp'");) {
			stmt.setString(1, "%" + first + "%");
			stmt.setString(2, "%" + last + "%");

			ResultSet rs = stmt.executeQuery();
			List<PersonnelBean> expertsList = personnelLoader.loadList(rs);
			rs.close();
			return expertsList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns all of the personnel who have a specialty of nutritionist
	 */
	public List<PersonnelBean> getAllNutritionists() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT * FROM personnel WHERE UPPER(specialty) = 'NUTRITIONIST'; ");
				ResultSet rs = ps.executeQuery()) {
			List<PersonnelBean> loadList = personnelLoader.loadList(rs);
			return loadList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns all of the personnel who have a specialty of ophthalmologist or
	 * optometrist
	 * 
	 * @return List of all personnel who have a specialty of ophthalmologist or
	 *         optometrist.
	 * @throws DBException
	 */
	public List<PersonnelBean> getAllOphthalmologyPersonnel() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM personnel "
						+ "WHERE specialty = 'Optometrist' or specialty = 'Ophthalmologist'; ");
				ResultSet rs = ps.executeQuery()) {
			List<PersonnelBean> allOpthamologyPersonnel = personnelLoader.loadList(rs);
			return allOpthamologyPersonnel;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
