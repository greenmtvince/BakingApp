package com.quantrian.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Step;

import java.util.ArrayList;

/**
 * Created by Vinnie on 1/1/2018.
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Step> mSteps;
    private RecipeCardAdapter.OnItemClickListener mListener;


    public void setOnItemClickListener(RecipeCardAdapter.OnItemClickListener listener)
    {mListener = listener;}

    public RecipeDetailAdapter(Context context, ArrayList<Step> steps){
        mContext=context;
        mSteps=steps;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForItem = R.layout.recipe_detail_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutIdForItem,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.stepName.setText((position+1)+". "+mSteps.get(position).shortDescription);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView stepName;

        public ViewHolder(final View itemView) {
            super(itemView);

            stepName = itemView.findViewById(R.id.recipe_detail_item_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}