package com.enenim.mybakingapp.fragment;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enenim.mybakingapp.MainActivity;
import com.enenim.mybakingapp.R;
import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.model.Step;
import com.enenim.mybakingapp.util.CommonUtil;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class RecipeStepDescriptionDetailFragment extends Fragment implements Constants {
    private static final String TAG = RecipeStepDescriptionDetailFragment.class.getSimpleName();
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

        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_STEP)) {
            if (getArguments() != null) {
                step = getArguments().getParcelable(KEY_STEP);
            }
        }
        else {
            step = savedInstanceState.getParcelable(KEY_STEP);
        }

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment mediaPlayerFragment = MediaPlayerFragment.newInstance(step);
        fragmentTransaction.add(R.id.exo_player_fragment_container, mediaPlayerFragment);
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_step_description_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView instructionTextView = (TextView)getActivity().findViewById(R.id.text_view_step_instruction);
        instructionTextView.setText(step.getDescription());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_STEP,  step);
        super.onSaveInstanceState(outState);
    }
}
