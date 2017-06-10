package com.enenim.mybakingapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enenim.mybakingapp.R;
import com.enenim.mybakingapp.model.Recipe;

import java.util.List;


public class GridListAdapter /*extends BaseAdapter*/ {
/*
    private Context context;
    private List<Recipe> recipes;

    *//**
     * Constructor methodgridListAdapter.setRecipes(null);
     * @param recipes The list of recipes to display
     *//*
    public GridListAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    public GridListAdapter(){

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
    *//**
     * Returns the number of items the adapter will display
     *//*
    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    *//**
     * Creates a new View for each item referenced by the adapter
     *//*
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater)
                   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.grid_view_item, null);
        }

        TextView gridItemTextView = (TextView) convertView
                .findViewById(R.id.grid_item_text_view);
        gridItemTextView.setText(recipes.get(position).getName());

        ImageView gridItemImageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
        gridItemImageView.setImageResource(R.drawable.body4);

        return convertView;
    }*/

}
