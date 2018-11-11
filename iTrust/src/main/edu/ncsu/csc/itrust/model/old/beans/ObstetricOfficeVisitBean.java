package edu.ncsu.csc.itrust.model.old.beans;

public class ObstetricOfficeVisitBean {
	
	private long visitId;
	private long obstetricRecordID;
	private float weight;
	private float bloodPressure;
	private float fetalHeartRate;
	private int lowLyingPlacentaObserved;
	private int numberOfBabies;
	
	public ObstetricOfficeVisitBean() {
	}
	
	public long getVisitId() {
		return visitId;
	}

	public void setVisitId(long visitId) {
		this.visitId = visitId;
	}

	public long getObstetricRecordID() {
		return obstetricRecordID;
	}

	public void setObstetricRecordID(long obstetricRecordID) {
		this.obstetricRecordID = obstetricRecordID;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getBloodPressure() {
		return bloodPressure;
	}

	public void setBloodPressure(float bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	public float getFetalHeartRate() {
		return fetalHeartRate;
	}

	public void setFetalHeartRate(float fetalHeartRate) {
		this.fetalHeartRate = fetalHeartRate;
	}

	public int getLowLyingPlacentaObserved() {
		return lowLyingPlacentaObserved;
	}

	public void setLowLyingPlacentaObserved(int lowLyingPlacentaObserved) {
		this.lowLyingPlacentaObserved = lowLyingPlacentaObserved;
	}

	public int getNumberOfBabies() {
		return numberOfBabies;
	}

	public void setNumberOfBabies(int numberOfBabies) {
		this.numberOfBabies = numberOfBabies;
	}
}
