package edu.ncsu.csc.itrust.model.personnel;

public enum Speciality {
    OBGYN ("OB/GYN"),
    SURGEON ("Surgeon"),
    OPTOMETRIST ("Optometrist"),
    OPTHAMOLOGIST ("Opthamologist"),
    GENERAL_PHYSICIAN ("General Physician"),
    PEDIATRICIAN ("Pediatrician"),
    ADMINISTRATOR ("Administrator"),
    NEUROLOGIST ("Neurologist"),
    BLOOD ("Blood"),
    TISSUE ("Tissue"),
    GENERAL ("General"),
    DEFAULT (null);

    private final String repr;

    private Speciality() {
        this.repr = this.name();
    }

    private Speciality(String repr) {
        this.repr = repr;
    }

    @Override
    public String toString() {
        return this.repr;
    }
}
