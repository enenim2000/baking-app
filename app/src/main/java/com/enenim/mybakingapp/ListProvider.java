package com.enenim.mybakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.enenim.mybakingapp.model.Recipe;
import com.enenim.mybakingapp.rest.ApiClient;
import com.enenim.mybakingapp.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
private ArrayList <Recipe> recipes;
private Context context = null;
private int appWidgetId;
 
    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID);
    }
 
    private ArrayList<Recipe> populateListItem() {
        ApiInterface apiService;
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Recipe>> call = apiService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if(response != null){
                    recipes = (ArrayList<Recipe>) response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>>call, Throwable t) {

            }
        });

        return recipes != null ? recipes : new ArrayList<Recipe>();
    }

    @Override
    public void onCreate() {
        recipes = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
        populateListItem();
    }

    @Override
    public void onDestroy() {
        recipes.clear();
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
        public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_row_item);
        Recipe recipe = recipes.get(position);
        remoteView.setTextViewText(R.id.item, recipe.getName());

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}