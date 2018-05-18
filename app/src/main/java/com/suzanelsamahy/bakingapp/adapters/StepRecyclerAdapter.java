package com.suzanelsamahy.bakingapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suzanelsamahy.bakingapp.R;
import com.suzanelsamahy.bakingapp.models.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SuZan ElsaMahy on 01-May-18.
 */

public class StepRecyclerAdapter extends RecyclerView.Adapter<StepRecyclerAdapter.CustomViewHolder> {

    private List<Step> recipeItemList;
    private Context mContext;

    int selectedPosition = -1;

    public StepRecyclerAdapter(Context context, List<Step> feedItemList) {
        this.recipeItemList = feedItemList;
        this.mContext = context;
    }


    @Override
    public StepRecyclerAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepRecyclerAdapter.CustomViewHolder holder, final int position) {
        final Step recipeItem = recipeItemList.get(position);

        holder.orderView.setText(String.valueOf(recipeItem.getId()));
        holder.textView.setText(recipeItem.getShortDescription());
        holder.textView.setTag(position);
        if (selectedPosition == position) {
            holder.textView.setBackgroundColor(Color.parseColor("#000000"));
        } else {
            holder.textView.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                selectedPosition = position;
                onItemClickListener.onItemClick(recipeItem, selectedPosition);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeItemList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_tv)
        TextView textView;

        @BindView(R.id.step_order_tv)
        TextView orderView;



        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Step item, int pos);
    }

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
