package fr.fyt.snowtam_clement_frankwel.model;

/**
 * Created by frank on 22/11/2017.
 */

public enum Month {

    JAN(" January ", "01"), //
    FEB(" February ", "02"), //
    MARCH(" March: ", "03"), //
    APRIL(" April ", "04"), //
    MAY(" May ", "05"), //
    JUNE(" June ", "06"), //
    JULY(" July ", "07"), //
    AUGUST(" August ", "08"), //
    SEPTEMBER(" September ", "09"), //
    OCTOBER(" October ", "10"), //
    NOVEMBER(" November ", "11"), //
    DECEMBER(" December ", "12");

    private final String label;
    private final String code;

    Month(final String label, final String code) {
        this.label = label;
        this.code = code;
    }

    public static Month valueOfByCode(final String code) {

        for (Month month : values()) {
            if (month.code.equalsIgnoreCase(code)) {
                return month;
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
