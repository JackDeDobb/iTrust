package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean;

public class ObstetricOfficeVisitValidator extends BeanValidator<ObstetricOfficeVisitBean> {
	/**
	 * The default constructor.
	 */
	public ObstetricOfficeVisitValidator() {
	}
	
	@Override
	public void validate(ObstetricOfficeVisitBean oov) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		errorList.addIfNotNull(checkFormat("obstetricRecordID", oov.getObstetricRecordID(), ValidationFormat.POSITIVE_NONZERO_INT, false));
		errorList.addIfNotNull(checkFormat("visit id", oov.getVisitId(), ValidationFormat.POSITIVE_NONZERO_INT, false));
		
		if(oov.getNumberOfBabies() <= 0) {
			errorList.addIfNotNull("Number of babies must be greater than 0!");
		}
		if(oov.getWeight() <= 0) {
			errorList.addIfNotNull("Weight must be greater than 0!");
		}

		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
}
