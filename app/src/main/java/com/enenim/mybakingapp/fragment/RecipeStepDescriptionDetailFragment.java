package com.enenim.mybakingapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enenim.mybakingapp.R;
import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.model.Step;

public class RecipeStepDescriptionDetailFragment extends Fragment implements Constants {
    private Step step;

    public RecipeStepDescriptionDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeStepDescriptionDetailFragment newInstance(Step step) {
        RecipeStepDescriptionDetailFragment fragment = new RecipeStepDescriptionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            step = getArguments().getParcelable(KEY_STEP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_step_description_detail, container, false);
    }
}
