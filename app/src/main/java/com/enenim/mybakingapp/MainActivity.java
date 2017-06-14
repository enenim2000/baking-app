package com.enenim.mybakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.model.Recipe;
import com.enenim.mybakingapp.rest.ApiClient;
import com.enenim.mybakingapp.rest.ApiInterface;
import com.enenim.mybakingapp.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity implements Constants {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private List<Recipe> recipes;
    private DataAdapter dataAdapter;

    @BindView(R.id.recycler_view_recipe)
    RecyclerView recyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Timber.d("Activity Created");

        context = getApplicationContext();
        dataAdapter = new DataAdapter();
        dataAdapter.setContext(context);
        dataAdapter.setRecipes(null);

        if(savedInstanceState != null){
            super.onRestoreInstanceState(savedInstanceState);
            //TODO
        }

        mLoadingIndicator.setVisibility(View.VISIBLE);

        Timber.i("About to make rest call to api service");

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Call<List<Recipe>> call = apiService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                List<Recipe> recipes;

                if(response != null){
                    recipes = response.body();
                    Timber.d("Succesfully fetched data from rest service, recipes[Recipe{....}] --> ", recipes);
                }else {
                    recipes = new ArrayList<>();
                }

                initViews(recipes);

                Timber.i("Number of recipes received: " + recipes.size());
            }

            @Override
            public void onFailure(Call<List<Recipe>>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setLayoutManager();
    }

    @Override
    public void onResume(){
        super.onResume();
        //TODO
    }

    public void setLayoutManager(){
        if(CommonUtil.isXLargeScreen(this)){
            layoutManager = new GridLayoutManager(context, 3);
        }else if(this.getResources().getBoolean(R.bool.is_landscape)){
            layoutManager = new GridLayoutManager(context, 2);
        }else {
            layoutManager = new GridLayoutManager(context, 1);
        }
    }

    private void initViews(final List<Recipe> recipes){
        this.recipes = recipes;
        recyclerView.setHasFixedSize(true);
        setLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        dataAdapter.setRecipes(recipes);
        recyclerView.setAdapter(dataAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Recipe recipe = recipes.get(position);

                Intent intent = new Intent(MainActivity.this, RecipeStepDescriptionHostActivity.class);
                Bundle bundle = new Bundle();

                bundle.putParcelable(KEY_RECIPE, recipe);
                intent.putExtras(bundle);

                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //Recipe recipe = recipes.get(position);
                //Toast.makeText(context, "Long click, selected: " + recipe.getName(),Toast.LENGTH_LONG).show();
            }
        }));

    }
}
