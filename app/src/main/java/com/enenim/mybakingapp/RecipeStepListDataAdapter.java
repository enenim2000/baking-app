package com.enenim.mybakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.model.Step;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeStepListDataAdapter extends RecyclerView.Adapter<RecipeStepListDataAdapter.ViewHolder> implements Constants {
    private List<Step> steps;
    private Context context;

    public RecipeStepListDataAdapter(){

    }

    public RecipeStepListDataAdapter(Context context, List<Step> steps) {
        this.context = context;
        this.steps = steps;
    }

    public List<Step> getRecipes() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_step_list_view_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ImageView list_item_image = viewHolder.imageView;
        TextView list_item_textView = viewHolder.textView;

        list_item_textView.setText(steps.get(position).getShortDescription());

        String imageUrl = BASE_URL + "/";
        if(!steps.get(position).getThumbnailURL().isEmpty()){
            imageUrl = imageUrl + steps.get(position).getThumbnailURL();
        }

        Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.icon_forward_arrow)
                .error(R.drawable.icon_forward_arrow)
                .into(list_item_image);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        public ViewHolder(View view) {
            super(view);

            textView = (TextView)view.findViewById(R.id.recipe_step_list_item_text_view);
            imageView = (ImageView)view.findViewById(R.id.recipe_step_list_item_image);
        }
    }
}
