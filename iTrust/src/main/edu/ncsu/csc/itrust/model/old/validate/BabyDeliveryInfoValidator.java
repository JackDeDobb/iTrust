package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.Gender;

public class BabyDeliveryInfoValidator extends BeanValidator<BabyDeliveryInfoBean> {
	/**
	 * The default constructor.
	 */
	public BabyDeliveryInfoValidator() {
	}
	
	@Override
	public void validate(BabyDeliveryInfoBean b) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		if(b.getChildBirthVisitId() <= 0) {
			errorList.addIfNotNull("Child birth visit ID must be set!");
		}
		
		if(b.getGender() == Gender.NotSpecified) {
			errorList.addIfNotNull("Gender must be specified!");
		}
		
		if(b.getDeliveryType() == DeliveryType.NS) {
			errorList.addIfNotNull("Delivery Type must be specified!");
		}

		if(b.getBirthTime() == null) {
			errorList.addIfNotNull("Birth time must be set!");
		}
		
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
}
