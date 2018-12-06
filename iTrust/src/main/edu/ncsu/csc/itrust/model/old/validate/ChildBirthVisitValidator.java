package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;

public class ChildBirthVisitValidator extends BeanValidator<ChildBirthVisitBean> {
	/**
	 * The default constructor.
	 */
	public ChildBirthVisitValidator() {
	}
	
	@Override
	public void validate(ChildBirthVisitBean c) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		if(c.getPreferredDeliveryType() == DeliveryType.NS) {
			errorList.addIfNotNull("Preferred delivery type must be set!");
		}
		
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
}
