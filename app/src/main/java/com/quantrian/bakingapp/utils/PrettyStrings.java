package com.quantrian.bakingapp.utils;

import android.util.Log;

import com.quantrian.bakingapp.models.Ingredient;

/**
 * Created by Vinnie on 1/4/2018.
 * This class provides helpers to clean up the strings in the Widget and the MasterList Adapter
 */

public class PrettyStrings {

    //Concats the Ingredient quantity, measure, and name into one string.
    public static String ingredientToString(Ingredient ingredient){
    StringBuilder ingredientsText= new StringBuilder();
        ingredientsText.append(PrettyStrings.prettyNumbers(ingredient.quantity))
                .append(" ")
                .append(ingredient.measure.toLowerCase())
                .append("   ")
                .append(ingredient.ingredient);
    return ingredientsText.toString();
    }

    //Eliminates trailing 0s after decimal and converts some decimals to common fractions used in
    //cooking.
    public static String prettyNumbers(float f){
        String prettyNumber;
        if (f == (long) f){
            prettyNumber=String.format("%d",(long) f);
        } else {
            prettyNumber=String.format("%s",f);
        }

        String[] intermediateStr = prettyNumber.split("\\.");

        if(intermediateStr.length>1) {
            switch (intermediateStr[1]) {
                case "25":
                    prettyNumber = intermediateStr[0] + " ¼";
                    break;
                case "5":
                    prettyNumber = intermediateStr[0] + " ½";
                    break;
                case ".75":
                    prettyNumber = intermediateStr[0] + " ¾";
                    break;
                default:
                    break;
            }
            //Handles the case of something like 0.5
            if(intermediateStr[0].equals("0")){
                prettyNumber=prettyNumber.replace("0 ","");
            }
        }

        return prettyNumber;
    }
}
