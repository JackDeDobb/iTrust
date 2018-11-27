package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;

public class ChildBirthVisitValidator extends BeanValidator<ChildBirthVisitBean> {
	/**
	 * The default constructor.
	 */
	public ChildBirthVisitValidator() {
	}
	
	@Override
	public void validate(ChildBirthVisitBean c) throws FormValidationException {
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
