package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;

/**
 * Validates a UltrasoundRecordBean
 */
public class UltrasoundRecordValidator extends BeanValidator<UltrasoundRecordBean> {
	/**
	 * The default constructor.
	 */
	public UltrasoundRecordValidator() {
	}
	
	@Override
	public void validate(UltrasoundRecordBean ur) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		
		
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
