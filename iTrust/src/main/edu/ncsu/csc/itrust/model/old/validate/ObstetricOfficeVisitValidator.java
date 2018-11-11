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
		
		
		
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
}
