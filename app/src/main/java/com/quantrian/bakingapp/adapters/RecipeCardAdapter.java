package com.quantrian.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Recipe;
import com.quantrian.bakingapp.utils.ImageTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Vinnie on 12/29/2017.
 */

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.ViewHolder>{
    private ArrayList<Recipe> recipes;
    private Context context;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {this.listener = listener;}

    public RecipeCardAdapter(Context context1, ArrayList<Recipe> recipes1){
        this.context = context1;
        this.recipes = recipes1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForItem = R.layout.card_recipe;
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForItem, parent, shouldAttachToParentImmediately);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //NOT REQUIRED AFTER REMOVAL OF STAGGEREDGRIDLAYOUT MANAGER.
        //
        //Explanation of this solution at:
        //https://stackoverflow.com/questions/21889735/
        //      resize-image-to-full-width-and-variable-height-with-picasso
        /*holder.iv_thumbnail.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (holder.iv_thumbnail.getWidth()>0)
                            holder.iv_thumbnail.getViewTreeObserver()
                                  .removeOnGlobalLayoutListener(this);

        */

                        String imageUrl = recipes.get(position).image;
                        //Picasso won't allow an empty string to revert to placeholder despite this
                        //being a common use case in the real world.  The thing just crashes.
                        // I think the dev responsible is a D---
                        // https://github.com/square/picasso/issues/899

                        if (imageUrl.equals("")) {
                            Picasso.with(context).load(R.drawable.baking_placeholder)
                                    //.transform(ImageTransformation.getTransformation(holder.iv_thumbnail))
                                    .into(holder.iv_thumbnail);
                        } else {
                        Picasso.with(context)
                                .load(imageUrl)
                                .placeholder(R.drawable.baking_placeholder)
                                //.transform(ImageTransformation.getTransformation(holder.iv_thumbnail))
                                .into(holder.iv_thumbnail);
                        }
                //    }
                //});
        holder.tv_servings.setText("Serves: "+String.valueOf(recipes.get(position).servings));
        holder.tv_title.setText(recipes.get(position).name);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_thumbnail;
        private TextView tv_title;
        private TextView tv_servings;

        public ViewHolder(final View view) {
            super(view);

            iv_thumbnail = view.findViewById(R.id.thumbnail);
            tv_title = view.findViewById(R.id.Recipe_title);
            tv_servings = view.findViewById(R.id.Recipe_servings);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}
