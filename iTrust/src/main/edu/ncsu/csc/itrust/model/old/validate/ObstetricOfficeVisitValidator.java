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

		errorList.addIfNotNull(checkFormat(
				"patientMID",
				oov.getPatientMID(),
				ValidationFormat.POSITIVE_NONZERO_INT,
				false));

		errorList.addIfNotNull(checkFormat(
				"hcpMID",
				oov.getHcpMID(),
				ValidationFormat.POSITIVE_NONZERO_INT,
				false));
		
		if(oov.getNumberOfBabies() <= 0) {
			errorList.addIfNotNull("Number of babies must be greater than 0!");
		}
		if(oov.getWeight() <= 0) {
			errorList.addIfNotNull("Weight must be greater than 0!");
		}

		if(oov.getSystolicBloodPressure() <= 0) {
			errorList.addIfNotNull("Systolic blood pressure must be greater than 0!");
		}

		if(oov.getDiastolicBloodPressure() <= 0) {
			errorList.addIfNotNull("Diastolic blood pressure must be greater than 0!");
		}

		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
}
