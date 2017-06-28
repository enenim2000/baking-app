package com.enenim.mybakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.enenim.mybakingapp.R;
import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.model.Recipe;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, Constants {

    private Context mContext;
    private static ArrayList<Recipe> recipes;

    public WidgetRemoteViewsFactory(Context context){
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        try {
            new NetworkUtil().execute().get();
        } catch (InterruptedException | ExecutionException e) {
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (recipes == null)return 0;
        return recipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.recipe_list_item);
        rv.setTextViewText(R.id.widget_recipe_text, recipes.get(position).getName());
        Gson gson = new Gson();
        Recipe recipe = new Recipe();
        recipe.setName(recipes.get(position).getName());
        recipe.setServings(recipes.get(position).getServings());
        recipe.setIngredients( recipes.get(position).getIngredients());
        recipe.setSteps(recipes.get(position).getSteps());

        Intent intent = new Intent();
        intent.putExtra(KEY_RECIPE_JSON, gson.toJson(recipe));

        rv.setOnClickFillInIntent(R.id.background, intent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public class NetworkUtil extends AsyncTask<Void, Void, ArrayList<Recipe>> {

        @Override
        protected ArrayList<Recipe> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                Uri builtUri = Uri.parse(FULL_URL_PATH)
                        .buildUpon()
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod(KEY_METHOD_GET);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                JSONArray bakingArray = new JSONArray(buffer.toString());
                recipes = new ArrayList<>();
                for (int i = 0; i < bakingArray.length(); i++) {
                    recipes.add(new Recipe(bakingArray.getJSONObject(i)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception e) {
                }
                return recipes;
            }
        }
    }
}
