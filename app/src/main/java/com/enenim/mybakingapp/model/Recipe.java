package com.enenim.mybakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable{
    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("ingredients")
    public List<Ingredient> ingredients = new ArrayList<>();

    @SerializedName("steps")
    public List<Step> steps = new ArrayList<>();

    @SerializedName("servings")
    private int servings;

    @SerializedName("image")
    private String image;

    public Recipe(){

    }

    //Parcel Constructor
    public Recipe(Parcel in) {
        setId(in.readLong());
        setName(in.readString());
        in.readTypedList(ingredients, Ingredient.CREATOR);
        in.readTypedList(steps, Step.CREATOR);
        setServings(in.readInt());
        setImage(in.readString());
    }

    public Recipe(JSONObject jsonRecipe) throws JSONException {

        setName(jsonRecipe.getString("name"));
        setId(jsonRecipe.getLong("id"));
        setServings(jsonRecipe.getInt("servings"));
        setImage(jsonRecipe.getString("image"));

        setIngredients(new ArrayList<Ingredient>());
        JSONArray ingredients = jsonRecipe.getJSONArray("ingredients");
        for (int i = 0; i < ingredients.length(); i++) {
            getIngredients().add(new Ingredient(ingredients.getJSONObject(i)));
        }

        setSteps(new ArrayList<Step>());
        JSONArray stepsJA = jsonRecipe.getJSONArray("steps");
        for (int i = 0; i < stepsJA.length(); i++) {
            getSteps().add(new Step(stepsJA.getJSONObject(i)));
        }
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getName());
        dest.writeTypedList (ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(getServings());
        dest.writeString(getImage());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Recipe{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", ingredients=").append(ingredients);
        sb.append(", steps=").append(steps);
        sb.append(", servings=").append(servings);
        sb.append(", image='").append(image).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
