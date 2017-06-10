package com.enenim.mybakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.fragment.RecipeStepDescriptionDetailFragment;
import com.enenim.mybakingapp.model.Step;

public class RecipeStepDescriptionDetailFragmentHostActivity extends AppCompatActivity implements Constants {
    private Step step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_description_detail_fragment_host);

        Intent intent = getIntent();
        if(intent.hasExtra(KEY_STEP)){
            step = intent.getParcelableExtra(KEY_STEP);
            addDetailFragment(step);
        }
    }

    public void addDetailFragment(Step step){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RecipeStepDescriptionDetailFragment fragment = RecipeStepDescriptionDetailFragment.newInstance(step);
        fragmentTransaction.add(R.id.recipe_step_description_detail_fragment_host_container,fragment);
    }
}
