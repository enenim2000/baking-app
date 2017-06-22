package com.enenim.mybakingapp.util;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.enenim.mybakingapp.SimpleIdlingResource;
import com.enenim.mybakingapp.model.Recipe;
import com.enenim.mybakingapp.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

public class RetrofitUtil {

    private ProgressBar mLoadingIndicator;
    private ApiInterface apiService;

    private static ArrayList<Recipe> recipes = new ArrayList<>();

    public interface RequestCallBack {
        void onDone(ArrayList<Recipe> recipesList);
    }

    public RetrofitUtil(){

    }

    public RetrofitUtil(ProgressBar mLoadingIndicator, ApiInterface apiService){
        this.mLoadingIndicator = mLoadingIndicator;
        this.apiService = apiService;
    }

    public void processRequest(final RequestCallBack callback,
                                       @Nullable final SimpleIdlingResource idlingResource){

        /**
         * The IdlingResource is null in production as set by the @Nullable annotation which means
         * the value is allowed to be null.
         *
         * If the idle state is true, Espresso can perform the next action.
         * If the idle state is false, Espresso will wait until it is true before
         * performing the next action.
         */
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        Call<List<Recipe>> call = apiService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                if(response != null){
                    recipes = (ArrayList<Recipe>) response.body();
                    Timber.d("Succesfully fetched data from rest service, recipes[Recipe{....}] --> ", recipes);
                }

                callback.onDone(recipes);

                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

    }

    public ProgressBar getmLoadingIndicator() {
        return mLoadingIndicator;
    }

    public void setmLoadingIndicator(ProgressBar mLoadingIndicator) {
        this.mLoadingIndicator = mLoadingIndicator;
    }

    public ApiInterface getApiService() {
        return apiService;
    }

    public void setApiService(ApiInterface apiService) {
        this.apiService = apiService;
    }
}
