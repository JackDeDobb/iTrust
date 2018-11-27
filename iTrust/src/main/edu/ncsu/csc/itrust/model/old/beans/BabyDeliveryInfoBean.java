package edu.ncsu.csc.itrust.model.old.beans;

import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.Gender;

import java.sql.Timestamp;

public class BabyDeliveryInfoBean {
	private long id;
	private long childBirthVisitId;
	private Gender gender = Gender.NotSpecified;
	private Timestamp birthTime;
	private DeliveryType deliveryType = DeliveryType.NS;
	private boolean isEstimated = false;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getChildBirthVisitId() {
		return childBirthVisitId;
	}
	
	public void setChildBirthVisitId(long childBirthVisitId) {
		this.childBirthVisitId = childBirthVisitId;
	}
	
	public Gender getGender() {
		return gender;
	}
	
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public void setGender(String genderStr) {
		this.gender = Gender.parse(genderStr);
	}
	
	public Timestamp getBirthTime() {
		return birthTime;
	}
	
	public void setBirthTime(Timestamp birthTime) {
		this.birthTime = birthTime;
	}
	
	public DeliveryType getDeliveryType() {
		return deliveryType;
	}
	
	public void setDeliveryType(DeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}
	
	public void setDeliveryType(String deliveryTypeStr) {
		this.deliveryType = DeliveryType.parse(deliveryTypeStr);
	}
	
	public boolean isEstimated() {
		return isEstimated;
	}
	
	public void setEstimated(boolean isEstimated) {
		this.isEstimated = isEstimated;
	}

}
