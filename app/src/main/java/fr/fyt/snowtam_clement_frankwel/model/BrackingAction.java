package fr.fyt.snowtam_clement_frankwel.model;

/**
 * Created by frank on 22/11/2017.
 */

public enum BrackingAction {

    POOR(" Poor ", "1"), //
    MEDIUM_TO_POOR(" Medium to poor ", "2"), //
    MEDIUM(" Medium ", "3"), //
    MEDIUM_TO_GOOD(" Medium to good ", "4"), //
    GOOD(" Good ", "5"), //
    POOR_DOUBLE(" Poor ", "6"), //
    MEDIUM_TO_POOR_DOUBLE(" Medium to poor ", "26"), //
    MEDIUM_DOUBLE(" Medium ", "30"), //
    MEDIUM_TO_GOOD_DOUBLE(" Medium to good ", "36"), //
    GOOD_DOUBLE(" Good ", "40"),
    BRD(" Brakemeter-Dynometer ", "BRD"),
    GRT(" Grip tester ", "GRT"),
    MUM(" Mu-meter ", "MUM"),
    RFT(" Runway friction tester ", "RFT"),
    SFH(" Surface friction tester (high-pressure tire) ", "SFH"),
    SFL(" Surface friction tester (low-pressure tire) ", "SFL"),
    SKH(" Skiddometer (high-pressure tire) ", "SKH"),
    SKL(" Skiddometer (low-pressure tire) ", "SKL"),
    TAP(" Tapley meter ", "TAP"),
    NC(" NC ", "9");

    private final String label;
    private final String code;

    BrackingAction(final String label, final String code) {
        this.label = label;
        this.code = code;
    }

    public static String getTheLabel(String code){
        String theValue = "ERROR";
        for (BrackingAction brackingAction : values()) {
            if (brackingAction.code.equalsIgnoreCase(code)) {
                theValue = brackingAction.getLabel();
            }
        }
        return theValue;
    }

    public static BrackingAction valueOfByCode(final String code) {

        for (BrackingAction brackingAction : values()) {
            if (brackingAction.code.equalsIgnoreCase(code)) {
                return brackingAction;
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
