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
import com.google.android.exoplayer2.ExoPlayer;

import butterknife.ButterKnife;

public class RecipeStepDescriptionHostActivity extends AppCompatActivity implements RecipeStepDescriptionListFragment.OnListFragmentInteractionListener, MediaPlayerFragment.OnDetailFragmentInteractionListener, Constants {
    private Recipe recipe;
    private boolean isPortraitDetailViewShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if(savedInstanceState != null && savedInstanceState.containsKey(KEY_PORTRAIT_DETAIL_SHOWING)){
            if(getResources().getBoolean(R.bool.is_landscape) && savedInstanceState.getBoolean(KEY_PORTRAIT_DETAIL_SHOWING)){
                setContentView(R.layout.activity_recipe_step_description_host_video_in_full_view);
            }else {
                setContentView(R.layout.activity_recipe_step_description_host);
            }
        }else {
            setContentView(R.layout.activity_recipe_step_description_host);
        }*/

        setContentView(R.layout.activity_recipe_step_description_host);

        ButterKnife.bind(this);

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_RECIPE)){
            super.onRestoreInstanceState(savedInstanceState);
            recipe = savedInstanceState.getParcelable(KEY_RECIPE);
        }else {
            Intent intent = getIntent();
            if(intent.hasExtra(KEY_RECIPE)){
                recipe = intent.getParcelableExtra(KEY_RECIPE);
            }
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeStepDescriptionListFragment recipeStepDescriptionListFragment = RecipeStepDescriptionListFragment.newInstance(recipe);
        if(getResources().getBoolean(R.bool.is_landscape) || CommonUtil.isXLargeScreen(this)){ //Landscape or Tablets
            //fragmentTransaction.add(R.id.recipe_step_description_list_fragment_container, recipeStepDescriptionListFragment);
            fragmentTransaction.add(R.id.recipe_step_description_list_fragment_portrait_container, recipeStepDescriptionListFragment);
            fragmentTransaction.commit();
        }else { //Portrait
            fragmentTransaction.add(R.id.recipe_step_description_list_fragment_portrait_container, recipeStepDescriptionListFragment);
            fragmentTransaction.commit();
        }
    }

    public void onListFragmentInteraction(Step step){

        RecipeStepDescriptionDetailFragment recipeStepDescriptionDetailFragment = RecipeStepDescriptionDetailFragment.newInstance(step);

        /*if(findViewById(R.id.recipe_step_description_list_fragment_portrait_container) == null){//Landscape or Tablet
            //Replace the existing fragment on the right with a new one to show updated detail
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_description_detail_fragment_container, recipeStepDescriptionDetailFragment)
                    .commit();
        }else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_description_list_fragment_portrait_container, recipeStepDescriptionDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }*/

       if(getResources().getBoolean(R.bool.is_landscape) || CommonUtil.isXLargeScreen(this)){ //two-pane mode
           // Replace the existing fragment on the right with a new one to show updated detail
           getSupportFragmentManager().beginTransaction()
                   .replace(R.id.recipe_step_description_detail_fragment_container, recipeStepDescriptionDetailFragment)
                   .commit();
        }else {
           isPortraitDetailViewShowing = true;
           getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_description_list_fragment_portrait_container, recipeStepDescriptionDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelable(KEY_RECIPE, recipe);
        outState.putBoolean(KEY_PORTRAIT_DETAIL_SHOWING, isPortraitDetailViewShowing);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetailFragmentInteraction(Step step) {

    }
}
