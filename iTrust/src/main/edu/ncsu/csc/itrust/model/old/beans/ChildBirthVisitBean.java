package edu.ncsu.csc.itrust.model.old.beans;

import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;

public class ChildBirthVisitBean {
	private long id;
	private long visitId;
	private long obstetricInitId;
	private boolean previouslyScheduled;
	private DeliveryType dType = DeliveryType.NS;
	private boolean hasDelivered;
	private float pitocinDosage;
	private float nitrousOxideDosage;
	private float epiduralAnaesthesiaDosage;
	private float magnesiumSulfateDosage;
	private float rhImmuneGlobulinDosage;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getVisitId() {
		return visitId;
	}
	
	public void setVisitId(long visitId) {
		this.visitId = visitId;
	}
	
	public long getObstetricInitId() {
		return obstetricInitId;
	}
	
	public void setObstetricInitId(long obstetricInitId) {
		this.obstetricInitId = obstetricInitId;
	}
	
	public boolean isPreviouslyScheduled() {
		return previouslyScheduled;
	}
	
	public void setPreviouslyScheduled(boolean previouslyScheduled) {
		this.previouslyScheduled = previouslyScheduled;
	}
	
	public DeliveryType getdType() {
		return dType;
	}
	
	public void setdType(DeliveryType dType) {
		this.dType = dType;
	}
	
	public boolean isHasDelivered() {
		return hasDelivered;
	}
	
	public void setHasDelivered(boolean hasDelivered) {
		this.hasDelivered = hasDelivered;
	}
	
	public float getPitocinDosage() {
		return pitocinDosage;
	}
	
	public void setPitocinDosage(float pitocinDosage) {
		this.pitocinDosage = pitocinDosage;
	}
	
	public float getNitrousOxideDosage() {
		return nitrousOxideDosage;
	}
	
	public void setNitrousOxideDosage(float nitrousOxideDosage) {
		this.nitrousOxideDosage = nitrousOxideDosage;
	}
	
	public float getEpiduralAnaesthesiaDosage() {
		return epiduralAnaesthesiaDosage;
	}
	
	public void setEpiduralAnaesthesiaDosage(float epiduralAnaesthesiaDosage) {
		this.epiduralAnaesthesiaDosage = epiduralAnaesthesiaDosage;
	}
	
	public float getMagnesiumSulfateDosage() {
		return magnesiumSulfateDosage;
	}
	
	public void setMagnesiumSulfateDosage(float magnesiumSulfateDosage) {
		this.magnesiumSulfateDosage = magnesiumSulfateDosage;
	}
	
	public float getRhImmuneGlobulinDosage() {
		return rhImmuneGlobulinDosage;
	}
	
	public void setRhImmuneGlobulinDosage(float rhImmuneGlobulinDosage) {
		this.rhImmuneGlobulinDosage = rhImmuneGlobulinDosage;
	}
	
}
