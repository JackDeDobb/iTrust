package edu.ncsu.csc.itrust.model.old.enums;

/**
 * Enumeration of the supported appointment types.
 */
public enum AppointmentType {
    GENERAL_CHECKUP("General Checkup"),
    MAMMOGRAM("Mammogram"),
    PHYSICAL("Physical"),
    COLONOSCOPY("Colonoscopy"),
    ULTRASOUND("Ultrasound"),
    CONSULTATION("Consultation"),
    OBSTETRIC_OV("Obstetric Office Visit"),
    CHILDBIRTH("Childbirth");


    private String name;

    AppointmentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Parses string into a given AppointmentType.
     */
    static AppointmentType parse(String s) {
        if (s.equals("General Checkup")) {
            return GENERAL_CHECKUP;
        } else if (s.equals("Mammogram")) {
            return MAMMOGRAM;
        } else if (s.equals("Physical")) {
            return PHYSICAL;
        } else if (s.equals("Ultrasound")) {
            return ULTRASOUND;
        } else if (s.equals("Consultation")) {
            return CONSULTATION;
        } else if (s.equals("Obstetric Office Visit")) {
            return OBSTETRIC_OV;
        } else if (s.equals("Childbirth")) {
            return CHILDBIRTH;
        } else {
            throw new IllegalArgumentException("Unknown appointment type string provided.");
        }
    }
}
