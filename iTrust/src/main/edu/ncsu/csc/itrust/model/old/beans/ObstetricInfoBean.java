package edu.ncsu.csc.itrust.model.old.beans;

import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;

import java.util.Calendar;
import java.util.Date;

public class ObstetricInfoBean {
	private long MID;
	private long recordID;
	private long yearOfConception;
	private long hoursInLabor;
	private long weightGain;
	private DeliveryType dType;
	private long numBirths;
	private Date lmp;
	private Date edd;
	private Date initDate;
	
	public long getMID() {
		return MID;
	}
	public void setMID(long mid) {
		MID = mid;
	}
	public long getRecordId() {
		return recordID;
	}
	public void setRecordId(long recID) {
		recordID = recID;
	}
	public long getYearsOfConception() {
		return yearOfConception;
	}
	public void setYearsOfConception(long year) {
		this.yearOfConception = year;
	}
	public long getNumberOfHoursInLabor() {
		return hoursInLabor;
	}
	public void setNumberOfHoursInLabor(long hoursInLabor) {
		this.hoursInLabor = hoursInLabor;
	}
	public long getWeightGainDuringPregnancy() {
		return weightGain;
	}
	public void setWeightGainDuringPregnancy(long weightGain) {
		this.weightGain = weightGain;
	}
	public DeliveryType getDeliveryType() {
		return dType;
	}
	public void setDeliveryType(DeliveryType dType) {
		this.dType = dType;
	}
	public long getNumBirths() {
		return numBirths;
	}
	public void setNumBirths(long numBirths) {
		this.numBirths = numBirths;
	}
	public Date getLMP() {
		return lmp;
	}
	public void setLMP(Date lmp) {
		this.lmp = lmp;
	}
	public Date getEDD() {
		return edd;
	}
	public void setEDD() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(lmp);
		cal.add(Calendar.DATE, 280); // add 10 days
		edd = cal.getTime();
	}
	public Date getInitDate() {
		return initDate;
	}
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}
}
