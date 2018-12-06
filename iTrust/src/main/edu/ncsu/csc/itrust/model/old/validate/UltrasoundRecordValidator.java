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

		errorList.addIfNotNull(checkFormat("visit id", ur.getVisitID(), ValidationFormat.POSITIVE_NONZERO_INT, false));
		
		if(ur.getCrownRumpLength() <= 0) {
			errorList.addIfNotNull("Crown rump length must be greater than 0!");
		}
		if(ur.getBiparietalDiameter() <= 0) {
			errorList.addIfNotNull("Biparietal diameter must be greater than 0!");
		}
		if(ur.getHeadCircumference() <= 0) {
			errorList.addIfNotNull("Head circumference must be greater than 0!");
		}
		if(ur.getFemurLength() <= 0) {
			errorList.addIfNotNull("Femur length must be greater than 0!");
		}
		if(ur.getOccipitofrontalDiameter() <= 0) {
			errorList.addIfNotNull("Head circumference must be greater than 0!");
		}
		
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
