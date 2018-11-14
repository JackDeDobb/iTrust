package edu.ncsu.csc.itrust.model.old.enums;

/**
 * All possible delivery types
 * 
 *  
 * 
 */
public enum DeliveryType {
	Vaginal("Vaginal Delivery"),
	VaginalVacuum("Vaginal Delivery Vacuum Assist"),
	VaginalForceps("Vaginal Delivery Forceps Assist"),
	CSection("Caesarean Section"),
	Miscarriage("Miscarriage"),
	NS("N/S");
	
	private String name;

	private DeliveryType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	public static DeliveryType parse(String deliveryTypeStr) {
		for (DeliveryType type : DeliveryType.values()) {
			if (type.getName().equals(deliveryTypeStr)) {
				return type;
			}
		}
		return NS;
	}
}
