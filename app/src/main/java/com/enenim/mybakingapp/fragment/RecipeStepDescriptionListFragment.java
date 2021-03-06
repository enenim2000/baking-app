package com.enenim.mybakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.enenim.mybakingapp.R;
import com.enenim.mybakingapp.RecipeStepListDataAdapter;
import com.enenim.mybakingapp.RecyclerItemClickListener;
import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.model.Recipe;
import com.enenim.mybakingapp.model.Step;
import com.enenim.mybakingapp.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepDescriptionListFragment extends Fragment implements Constants {

    private OnListFragmentInteractionListener mListener;

    private Recipe recipe;
    private List<Step> steps;
    private RecyclerView recyclerView;
    private RecipeStepListDataAdapter recipeStepListDataAdapter;
    private TextView ingredient_text_view;
    private Context context;
    private int position = 0;

    private LayoutInflater inflater;
    private ViewPager viewPager;

    public RecipeStepDescriptionListFragment() {
        // Required empty public constructor
    }

    public static RecipeStepDescriptionListFragment newInstance(Recipe recipe) {
        RecipeStepDescriptionListFragment fragment = new RecipeStepDescriptionListFragment();
        fragment.setRecipe(recipe);
        Bundle args = new Bundle();
        args.putParcelable(KEY_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_STEPS)) {
            if(recipe != null){
                steps = recipe.getSteps();
            } else if (getArguments() != null) {
                recipe = getArguments().getParcelable(KEY_RECIPE);
                steps = recipe.getSteps();
            }
        }
        else {
            steps = savedInstanceState.getParcelableArrayList(KEY_STEPS);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //viewPager = (ViewPager)getActivity().findViewById(R.id.viewPager);
        //viewPager.setAdapter(new MyPagesAdapter());

        //Use for testing espresso
        TextView testing_test_view = (TextView)getActivity().findViewById(R.id.testing_test_view);
        testing_test_view.setText(recipe.getName());

        ingredient_text_view = (TextView)getActivity().findViewById(R.id.ingredient_text_view);
        ingredient_text_view.setText(recipe.getIngredients().get(position).getIngredient());

        recyclerView = (RecyclerView)getActivity().findViewById(R.id.recycler_view_recipe_step_description_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        recipeStepListDataAdapter = new RecipeStepListDataAdapter();
        recipeStepListDataAdapter.setContext(getActivity());
        recipeStepListDataAdapter.setSteps(recipe.getSteps());
        recyclerView.setAdapter(recipeStepListDataAdapter);

        ImageButton stepNextButton = (ImageButton) getActivity().findViewById(R.id.ingredient_next_button);
        stepNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPosition = position + 1;
                if( nextPosition < recipe.getIngredients().size() ){
                    position = nextPosition;
                    ingredient_text_view.setText(recipe.getIngredients().get(position).getIngredient());
                }
            }
        });

        ImageButton stepPrevButton = (ImageButton) getActivity().findViewById(R.id.ingredient_prev_button);
        stepPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPosition = position - 1;
                if( nextPosition >= 0 ){
                    position = nextPosition;
                    ingredient_text_view.setText(recipe.getIngredients().get(position).getIngredient());
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Update Detail pane for two pane mode device, or Replace fragment with recipe description detail fragment
                if(getActivity().getResources().getBoolean(R.bool.is_landscape) || CommonUtil.isXLargeScreen(getActivity())){ //two-pane mode

                    RecipeStepDescriptionDetailFragment recipeStepDescriptionDetailFragment = RecipeStepDescriptionDetailFragment.newInstance(steps.get(position), steps, position);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_step_description_detail_fragment_container, recipeStepDescriptionDetailFragment)
                            .commit();
                }else {
                    RecipeStepDescriptionDetailFragment recipeStepDescriptionDetailFragment = RecipeStepDescriptionDetailFragment.newInstance(steps.get(position), steps, position);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_step_description_list_fragment_portrait_container, recipeStepDescriptionDetailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_STEPS, (ArrayList<? extends Parcelable>) steps);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_step_description_list, container, false);
    }

    public void onButtonPressed(Step step, List<Step> steps, int position) {
        if (mListener != null) {
            mListener.onListFragmentInteraction(step, steps, position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Step step, List<Step> steps, int position);
    }

    //Implement PagerAdapter Class to handle individual page creation
    class MyPagesAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            //Return total pages, here one for each data item
            return recipe.getIngredients().size();
        }
        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = inflater.inflate(R.layout.page, null);

            TextView textViewDescription = (TextView)page.findViewById(R.id.ingredient_description);
            textViewDescription.setText(String.valueOf(recipe.getIngredients().get(position).getIngredient()));

            ((ViewPager) container).addView(page, 0);

            return page;
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==(View)arg1;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            object=null;
        }
    }
}
