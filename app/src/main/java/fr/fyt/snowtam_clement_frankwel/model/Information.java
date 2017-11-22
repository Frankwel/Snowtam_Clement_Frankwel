package fr.fyt.snowtam_clement_frankwel.model;

import fr.fyt.snowtam_clement_frankwel.R;

/**
 * Created by frank on 20/11/2017.
 */

public enum Information {

    MOUNTH(" Location: ", "ff"), //
    TIME_OF_OBSERVATION(" Time of observation: ", "B"), //
    RUNWAY(" Runway: ", "C"), //
    RUNWAY_LENGTH (" Runway lenght: ", "D"), //
    RUNWAY_WIDTH (" Runway width: ", "E"), //
    DEPOSIT_DEPTH(" Deposit depth: ", "F"), //
    MEAN_DEPTH(" Mean depth: ", "G"), //
    BRAKING_ACTION(" Braking action: ", "H"), //
    TAXIWAY(" Taxiway: ", "N"), //
    REMARK(" Remark: ", "T");

    private final String label;
    private final String code;

    Information(final String label, final String code) {
        this.label = label;
        this.code = code;
    }

    public static Information valueOfByCode(final String code) {

        for (Information information : values()) {
            if (information.code.equalsIgnoreCase(code)) {
                return information;
            }
        }
        throw new IllegalArgumentException("Le code demandee n'existe pas.");
    }

    public String getLabel() {
        return label;
    }

    public String getCode() {
        return code;
    }

}
