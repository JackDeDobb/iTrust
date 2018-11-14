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

    public static Speciality fromString(String s) {
        for (Speciality speciality : Speciality.values()) {
            if (speciality.repr.equals(s)) {
                return speciality;
            }
        }
        throw new IllegalArgumentException("Unable to match specialty");
    }

    @Override
    public String toString() {
        return this.repr;
    }
}
