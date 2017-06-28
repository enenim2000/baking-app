package com.enenim.mybakingapp.util;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.enenim.mybakingapp.SimpleIdlingResource;
import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.model.Recipe;
import com.enenim.mybakingapp.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

public class RetrofitUtil implements Constants{

    private ProgressBar mLoadingIndicator;
    private ApiInterface apiService;
    private TextView messageTextView;
    private RecyclerView recyclerView;

    private static ArrayList<Recipe> recipes = new ArrayList<>();

    public TextView getMessageTextView() {
        return messageTextView;
    }

    public void setMessageTextView(TextView messageTextView) {
        this.messageTextView = messageTextView;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

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
                mLoadingIndicator.setVisibility(View.GONE);

                if(response != null){
                    recipes = (ArrayList<Recipe>) response.body();
                    getMessageTextView().setText(TEXT_EMPTY);
                    getMessageTextView().setVisibility(View.GONE);
                    getRecyclerView().setVisibility(View.VISIBLE);
                    Timber.d("Succesfully fetched data from rest service, recipes:[Recipe{....}] --> ", recipes);
                }

                callback.onDone(recipes);

                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>>call, Throwable t) {
                mLoadingIndicator.setVisibility(View.GONE);
                getRecyclerView().setVisibility(View.GONE);
                getMessageTextView().setText(ERROR_MESSAGE_RETROFITUTIL);
                getMessageTextView().setVisibility(View.VISIBLE);
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
