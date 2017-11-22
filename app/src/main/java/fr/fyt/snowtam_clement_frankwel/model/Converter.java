package fr.fyt.snowtam_clement_frankwel.model;

import android.util.Log;

import fr.fyt.snowtam_clement_frankwel.R;

/**
 * Created by frank on 22/11/2017.
 */

public class Converter {

    public Converter(){

    }

    public String convertTime(String time){
        String timeConverted = " ";

       if(time.length()==8){
            timeConverted = timeConverted + time.substring(2, 4) + " ";
            timeConverted = timeConverted + Month.getTheLabel(time.substring(0, 2)) + " ";
            timeConverted = timeConverted + time.substring(4, 6) + "h";
            timeConverted = timeConverted + time.substring(6, 8) + " UTC";
        }
        else {
            timeConverted = "error";
        }
        return timeConverted;
    }

    public String convertCondition(String condition){
        String conditionConverted = "";

        String conditions[] = condition.split("/");

        for(int i=0; i<conditions.length; i++){
            if(i == 0){
                conditionConverted = conditionConverted + Condition.getTheLabel(conditions[i]);
            }
            else{
                conditionConverted = conditionConverted + ", " + Condition.getTheLabel(conditions[i]);
            }
        }
        return conditionConverted;
    }

    public String convertBreakingAction(String breakingAction){
        String baConverted = "";

        String breakingActions[] = breakingAction.split("/");

        for(int i=0; i<breakingActions.length; i++){
            if(i == 0){
                baConverted = baConverted + BrackingAction.getTheLabel(breakingActions[i]);
            }
            else{
                baConverted = baConverted + ", " + BrackingAction.getTheLabel(breakingActions[i]);
            }
        }
        return baConverted;
    }
}
