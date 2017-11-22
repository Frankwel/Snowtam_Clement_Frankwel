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
        String timeConverted = "";
        //time = "10130958";
        //time.replaceAll(" ", "");
       if(time.length()==8){
            timeConverted = timeConverted + time.substring(2, 4) + " ";
            timeConverted = timeConverted + Month.valueOfByCode(time.substring(0, 2)) + " ";
            timeConverted = timeConverted + time.substring(4, 6) + "h";
            timeConverted = timeConverted + time.substring(6, 8) + " UTC";
        }
        else {
            timeConverted = "error";
        }
        return timeConverted;
    }

   // public String concertRunway
}
