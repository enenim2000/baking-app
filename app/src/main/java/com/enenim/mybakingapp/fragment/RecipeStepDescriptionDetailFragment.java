package com.enenim.mybakingapp.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.enenim.mybakingapp.R;
import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepDescriptionDetailFragment extends Fragment implements Constants {
    private static final String TAG = RecipeStepDescriptionDetailFragment.class.getSimpleName();
    private Step step;
    private List<Step> steps;
    private int position;
    private FragmentTransaction fragmentTransaction;
    private MediaPlayerFragment mediaPlayerFragment;
    private TextView instructionTextView;

    public RecipeStepDescriptionDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeStepDescriptionDetailFragment newInstance(Step step, List<Step> steps, int position) {
        RecipeStepDescriptionDetailFragment fragment = new RecipeStepDescriptionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_STEP, step);

        args.putParcelableArrayList(KEY_STEPS, (ArrayList<? extends Parcelable>) steps);
        args.putInt(KEY_STEP_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_STEP)) {
            if (getArguments() != null) {
                step = getArguments().getParcelable(KEY_STEP);
                steps = getArguments().getParcelableArrayList(KEY_STEPS);
                position = getArguments().getInt(KEY_STEP_POSITION);
            }
        }
        else {
            step = savedInstanceState.getParcelable(KEY_STEP);
            steps = savedInstanceState.getParcelableArrayList(KEY_STEPS);
            position = savedInstanceState.getInt(KEY_STEP_POSITION);
        }

        // find the retained fragment after configuration changed
        FragmentManager fragmentManager = getChildFragmentManager();

        mediaPlayerFragment = (MediaPlayerFragment) fragmentManager.findFragmentByTag(TAG_MEDIA_FRAGMENT);

        if(mediaPlayerFragment == null){
            fragmentTransaction = fragmentManager.beginTransaction();
            mediaPlayerFragment = MediaPlayerFragment.newInstance(step);
            fragmentTransaction.add(R.id.exo_player_fragment_container, mediaPlayerFragment, TAG_MEDIA_FRAGMENT);
            fragmentTransaction.commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_step_description_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        instructionTextView = (TextView)getActivity().findViewById(R.id.text_view_step_instruction);
        if(instructionTextView != null){
            instructionTextView.setText(step.getDescription());
        }

        ImageButton stepNextButton = (ImageButton) getActivity().findViewById(R.id.recipe_next_button);
        if(stepNextButton != null){
            stepNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextPosition = position + 1;
                    if( nextPosition < steps.size() ){

                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .remove(mediaPlayerFragment);

                        position = nextPosition;
                        step = steps.get(position);
                        if(instructionTextView != null){
                            instructionTextView.setText(step.getDescription());
                        }
                        mediaPlayerFragment = MediaPlayerFragment.newInstance(step);
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction().replace(R.id.exo_player_fragment_container, mediaPlayerFragment, TAG_MEDIA_FRAGMENT)
                                .commit();
                    }
                }
            });

        }

        ImageButton stepPrevButton = (ImageButton) getActivity().findViewById(R.id.recipe_prev_button);
        if(stepPrevButton != null){
            stepPrevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextPosition = position - 1;
                    if( nextPosition >= 0 ){

                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .remove(mediaPlayerFragment);

                        position = nextPosition;
                        step = steps.get(position);
                        instructionTextView.setText(step.getDescription());
                        mediaPlayerFragment = MediaPlayerFragment.newInstance(step);

                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction().replace(R.id.exo_player_fragment_container, mediaPlayerFragment, TAG_MEDIA_FRAGMENT)
                                .commit();
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_STEP,  step);
        outState.putParcelableArrayList(KEY_STEPS, (ArrayList<? extends Parcelable>) steps);
        outState.putInt(KEY_STEP_POSITION,  position);
        super.onSaveInstanceState(outState);
    }
}
