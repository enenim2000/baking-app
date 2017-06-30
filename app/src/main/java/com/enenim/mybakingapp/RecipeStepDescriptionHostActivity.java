package com.enenim.mybakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.fragment.MediaPlayerFragment;
import com.enenim.mybakingapp.fragment.RecipeStepDescriptionDetailFragment;
import com.enenim.mybakingapp.fragment.RecipeStepDescriptionListFragment;
import com.enenim.mybakingapp.model.Recipe;
import com.enenim.mybakingapp.model.Step;
import com.enenim.mybakingapp.util.CommonUtil;
import com.google.gson.Gson;

import java.util.List;

import butterknife.ButterKnife;

public class RecipeStepDescriptionHostActivity extends AppCompatActivity implements RecipeStepDescriptionListFragment.OnListFragmentInteractionListener, MediaPlayerFragment.OnMediaFragmentInteractionListener, Constants {
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_description_host);
        ButterKnife.bind(this);

        if( getSupportActionBar() != null){
            if(getResources().getBoolean(R.bool.is_landscape)){
                getSupportActionBar().hide();
            }else {
                getSupportActionBar().show();
            }
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_RECIPE)){
            super.onRestoreInstanceState(savedInstanceState);
            recipe = savedInstanceState.getParcelable(KEY_RECIPE);
        }else {
            Intent intent = getIntent();
            if(intent.hasExtra(KEY_RECIPE_JSON)){
                Gson gson = new Gson();
                String recipeJson = intent.getStringExtra(KEY_RECIPE_JSON);
                recipe = gson.fromJson(recipeJson, Recipe.class);
            } else if(intent.hasExtra(KEY_RECIPE)){
                recipe = intent.getParcelableExtra(KEY_RECIPE);
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeStepDescriptionListFragment recipeStepDescriptionListFragment = (RecipeStepDescriptionListFragment) fragmentManager.findFragmentByTag(TAG_LIST_FRAGMENT);
        if(recipeStepDescriptionListFragment == null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            recipeStepDescriptionListFragment = RecipeStepDescriptionListFragment.newInstance(recipe);
            fragmentTransaction.add(R.id.recipe_step_description_list_fragment_portrait_container, recipeStepDescriptionListFragment, TAG_LIST_FRAGMENT);
            fragmentTransaction.commit();
        }


    }

    public void onListFragmentInteraction(Step step, List<Step> steps, int position){

        RecipeStepDescriptionDetailFragment recipeStepDescriptionDetailFragment = RecipeStepDescriptionDetailFragment.newInstance(step, steps, position);

       if(getResources().getBoolean(R.bool.is_landscape) || CommonUtil.isXLargeScreen(this)){ //two-pane mode
           // Replace the existing fragment on the right with a new one to show updated detail
           getSupportFragmentManager().beginTransaction()
                   .replace(R.id.recipe_step_description_detail_fragment_container, recipeStepDescriptionDetailFragment)
                   .commit();
       }else {
           getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_description_list_fragment_portrait_container, recipeStepDescriptionDetailFragment)
                    .addToBackStack(null)
                    .commit();
       }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelable(KEY_RECIPE, recipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMediaFragmentInteraction(Step step) {

    }
}
