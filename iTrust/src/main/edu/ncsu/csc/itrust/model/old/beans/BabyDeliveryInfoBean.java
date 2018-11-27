package edu.ncsu.csc.itrust.model.old.beans;

public class BabyDeliveryInfoBean {
	private long id;
	private long childBirthVisitId;
	gender enum('male', 'female'),
	birthTime TIMESTAMP,
	deliveryType enum('vaginal delivery','vaginal delivery vacuum assist','vaginal delivery forceps assist','caesarean section','miscarriage'),
	isEstimated BOOLEAN DEFAULT FALSE,
	
	

}
