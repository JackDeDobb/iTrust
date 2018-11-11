package edu.ncsu.csc.itrust.model.old.beans;

/**
 * Stores information about a UltrasoundRecord
 */
public class UltrasoundRecordBean {
	
	private long id;
	private long visitID;
	private float crownRumpLength;
	private float biparietalDiameter;
	private float headCircumference;
	private float femurLength;
	private float occipitofrontalDiameter;
	private float abdominalCircumference;
	private float humerusLength;
	private float estimatedFetalWeight;
	private String imagePath;
	
	public UltrasoundRecordBean() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getVisitID() {
		return visitID;
	}

	public void setVisitID(long visitID) {
		this.visitID = visitID;
	}

	public float getCrownRumpLength() {
		return crownRumpLength;
	}

	public void setCrownRumpLength(float crownRumpLength) {
		this.crownRumpLength = crownRumpLength;
	}

	public float getBiparietalDiameter() {
		return biparietalDiameter;
	}

	public void setBiparietalDiameter(float biparietalDiameter) {
		this.biparietalDiameter = biparietalDiameter;
	}

	public float getHeadCircumference() {
		return headCircumference;
	}

	public void setHeadCircumference(float headCircumference) {
		this.headCircumference = headCircumference;
	}

	public float getFemurLength() {
		return femurLength;
	}

	public void setFemurLength(float femurLength) {
		this.femurLength = femurLength;
	}

	public float getOccipitofrontalDiameter() {
		return occipitofrontalDiameter;
	}

	public void setOccipitofrontalDiameter(float occipitofrontalDiameter) {
		this.occipitofrontalDiameter = occipitofrontalDiameter;
	}

	public float getAbdominalCircumference() {
		return abdominalCircumference;
	}

	public void setAbdominalCircumference(float abdominalCircumference) {
		this.abdominalCircumference = abdominalCircumference;
	}

	public float getHumerusLength() {
		return humerusLength;
	}

	public void setHumerusLength(float humerusLength) {
		this.humerusLength = humerusLength;
	}

	public float getEstimatedFetalWeight() {
		return estimatedFetalWeight;
	}

	public void setEstimatedFetalWeight(float estimatedFetalWeight) {
		this.estimatedFetalWeight = estimatedFetalWeight;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
