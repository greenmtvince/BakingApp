package com.quantrian.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Recipe;
import com.quantrian.bakingapp.utils.DynamicHeightNetworkImageView;
import com.quantrian.bakingapp.utils.ImageLoaderHelper;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.iv_thumbnail.setImageUrl
                ("https://upload.wikimedia.org/wikipedia/commons/3/3d/Choc-Cake-1.png",
                        ImageLoaderHelper.getInstance(context).getImageLoader());
        holder.iv_thumbnail.setAspectRatio(1.0923f);
        holder.tv_servings.setText("Serves: "+String.valueOf(recipes.get(position).servings));
        holder.tv_title.setText(recipes.get(position).name);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private DynamicHeightNetworkImageView iv_thumbnail;
        private TextView tv_title;
        private TextView tv_servings;

        public ViewHolder(final View view) {
            super(view);

            iv_thumbnail = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail);
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
