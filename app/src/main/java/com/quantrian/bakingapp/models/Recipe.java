package com.quantrian.bakingapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinnie on 12/27/2017.
 */

public class Recipe {
    public Recipe (){}

    public String name;
    public int id;
    public int servings;
    public String image;
    public ArrayList<Ingredient> ingredients;
    public ArrayList<Step> steps;

    /*public Recipe(int id, String name, int servings, String image, List<Ingredient> ingredients1,
                  List<Step> steps1){
        this.id=id;
        this.name=name;
        this.servings=servings;
        this.image=image;
        this.ingredients = ingredients1;
        this.steps= steps1;
    }*/
}
