package edu.ncsu.csc.itrust.model.old.enums;

/**
 * Yes or No.  Not specified is provided as well, for incomplete forms or patient's discretion.
 */
public enum BooleanType {
	Yes("Yes"), No("No");
	private String name;

	private BooleanType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	public static BooleanType parse(String input) {
		for (BooleanType boolType : BooleanType.values()) {
			if (boolType.name.equals(input))
				return boolType;
		}
		return null;
	}
}
