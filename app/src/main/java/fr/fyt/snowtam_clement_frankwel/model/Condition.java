package fr.fyt.snowtam_clement_frankwel.model;

/**
 * Created by frank on 22/11/2017.
 */

public enum Condition {

    CLEAR_AND_DRY(" Clear and dry ", "0"), //
    DAMP(" Damp ", "1"), //
    WET_OR_WATER_PATCHES(" Wet or water patches ", "2"), //
    RIME_OR_FROST_COVERED(" Rime or frost covered ", "3"), //
    DRY_SNOW(" Dry snow ", "4"), //
    WET_SNOW(" Wet snow ", "5"), //
    SLUSH(" Slush ", "6"), //
    ICE(" Ice ", "7"), //
    COMPACTED_OR_ROLLED_SNOW(" Compacted or rolled snow ", "8"), //
    FROZEN_RUTS_OR_RIDGES(" Frozen ruts or ridges ", "9"),
    NC(" NC ", "1000");

    private final String label;
    private final String code;

    Condition(final String label, final String code) {
        this.label = label;
        this.code = code;
    }

    public static String getTheLabel(String code){
        String theValue = "ERROR";
        for (Condition condition : values()) {
            if (condition.code.equalsIgnoreCase(code)) {
                theValue = condition.getLabel();
            }
        }
        return theValue;
    }

    public static Condition valueOfByCode(final String code) {

        for (Condition condition : values()) {
            if (condition.code.equalsIgnoreCase(code)) {
                return condition;
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
