package com.enenim.mybakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements Constants {
    private List<Recipe> recipes;
    private Context context;

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public DataAdapter(){

    }

    public DataAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_view_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ImageView grid_item_image = viewHolder.imageView;
        TextView grid_item_textView = viewHolder.textView;

        grid_item_textView.setText(recipes.get(position).getName());

        String imageUrl = BASE_URL + "/";
        if(!recipes.get(position).getImage().isEmpty()){
            imageUrl = imageUrl + recipes.get(position).getImage();
        }

        Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.recipe_card_icon_small)
                .error(R.drawable.recipe_card_icon_small)
                .into(grid_item_image);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        public ViewHolder(View view) {
            super(view);

            textView = (TextView)view.findViewById(R.id.grid_item_text_view);
            imageView = (ImageView)view.findViewById(R.id.grid_item_image);
        }
    }
}
