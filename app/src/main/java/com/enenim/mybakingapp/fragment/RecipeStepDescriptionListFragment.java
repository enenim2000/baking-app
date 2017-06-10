package com.enenim.mybakingapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.enenim.mybakingapp.MainActivity;
import com.enenim.mybakingapp.R;
import com.enenim.mybakingapp.RecipeStepDescriptionHostActivity;
import com.enenim.mybakingapp.RecipeStepListDataAdapter;
import com.enenim.mybakingapp.RecyclerItemClickListener;
import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.model.Recipe;
import com.enenim.mybakingapp.model.Step;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeStepDescriptionListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeStepDescriptionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeStepDescriptionListFragment extends Fragment implements Constants {

    private OnFragmentInteractionListener mListener;
    private Recipe recipe;
    private List<Step> steps;
    private RecyclerView recyclerView;
    private RecipeStepListDataAdapter recipeStepListDataAdapter;

    private LayoutInflater inflater;
    private ViewPager viewPager;

    public RecipeStepDescriptionListFragment() {
        // Required empty public constructor
    }

    public static RecipeStepDescriptionListFragment newInstance(Recipe recipe) {
        RecipeStepDescriptionListFragment fragment = new RecipeStepDescriptionListFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(KEY_RECIPE);
            steps = recipe.getSteps();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //get an inflater to be used to create single pages
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Reference ViewPager defined in activity
        viewPager = (ViewPager)getActivity().findViewById(R.id.viewPager);
        //set the adapter that will create the individual pages
        viewPager.setAdapter(new MyPagesAdapter());


        recyclerView = (RecyclerView)getActivity().findViewById(R.id.recycler_view_recipe_step_description_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        recipeStepListDataAdapter = new RecipeStepListDataAdapter();
        recipeStepListDataAdapter.setContext(getActivity());
        recipeStepListDataAdapter.setSteps(recipe.getSteps());
        recyclerView.setAdapter(recipeStepListDataAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Update Detail pane for two pane mode device, or Replace fragment with recipe description detail fragment
                Step step = steps.get(position);
                Toast.makeText(getActivity(), "Long click, selected: " + step.getShortDescription(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Step step = steps.get(position);
                Toast.makeText(getActivity(), "Long click, selected: " + step.getShortDescription(), Toast.LENGTH_LONG).show();
            }
        }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_step_description_list, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Step step) {
        if (mListener != null) {
            mListener.onFragmentInteraction(step);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Step step);
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

            TextView textViewQuantity = (TextView)page.findViewById(R.id.ingredient_quantity);
            textViewQuantity.setText(String.valueOf(recipe.getIngredients().get(position).getQuantity()));

            TextView textViewDescription = (TextView)page.findViewById(R.id.ingredient_description);
            textViewDescription.setText(String.valueOf(recipe.getIngredients().get(position).getIngredient()));

            TextView textViewMeasure = (TextView)page.findViewById(R.id.ingredient_measure);
            textViewMeasure.setText(String.valueOf(recipe.getIngredients().get(position).getMeasure()));
            //Add the page to the front of the queue
            ((ViewPager) container).addView(page, 0);
            return page;
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //See if object from instantiateItem is related to the given view
            //required by API
            return arg0==(View)arg1;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            object=null;
        }
    }
}
