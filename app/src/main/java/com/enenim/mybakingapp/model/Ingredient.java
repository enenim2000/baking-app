package com.enenim.mybakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingredient implements Parcelable{

    @Expose
    @SerializedName("quantity")
    private double quantity;

    @Expose
    @SerializedName("measure")
    private String measure;

    @Expose
    @SerializedName("ingredient")
    private String ingredient;

    public Ingredient(){

    }

    public Ingredient(JSONObject ingredientJson) {
        try {
            setQuantity(ingredientJson.getDouble("quantity"));
            setMeasure(ingredientJson.optString("measure"));
            setIngredient(ingredientJson.optString("ingredient"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Parcel Constructor
    public Ingredient(Parcel in) {
        setQuantity(in.readDouble());
        setMeasure(in.readString());
        setIngredient(in.readString());
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(getQuantity());
        dest.writeString(getMeasure());
        dest.writeString(getIngredient());
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ingredient{");
        sb.append("quantity=").append(quantity);
        sb.append(", measure='").append(measure).append('\'');
        sb.append(", ingredient='").append(ingredient).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
