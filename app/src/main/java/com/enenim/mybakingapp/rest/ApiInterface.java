package com.enenim.mybakingapp.rest;

import com.enenim.mybakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiInterface {
    @GET("/android-baking-app-json")
    Call<List<Recipe>> getRecipes();
}