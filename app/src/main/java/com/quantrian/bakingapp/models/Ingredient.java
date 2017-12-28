package com.quantrian.bakingapp.models;

/**
 * Created by Vinnie on 12/27/2017.
 */

public class Ingredient {

    public int quantity;
    public String measure;
    public String ingredientName;

    public Ingredient(int n, String measure, String ingredient_name){
        this.quantity=n;
        this.measure=measure;
        this.ingredientName=ingredient_name;
    }
}
