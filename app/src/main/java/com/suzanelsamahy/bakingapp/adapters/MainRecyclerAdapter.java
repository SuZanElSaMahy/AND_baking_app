package com.suzanelsamahy.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suzanelsamahy.bakingapp.R;
import com.suzanelsamahy.bakingapp.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SuZan ElsaMahy on 25-Apr-18.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.CustomViewHolder> {

    private List<Recipe> recipeItemList;
    private Context mContext;

    public MainRecyclerAdapter(Context context, List<Recipe> feedItemList) {
        this.recipeItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_reciepe_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int i) {
        final Recipe recipeItem = recipeItemList.get(i);
        customViewHolder.textView.setText(recipeItem.getName());
        customViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(recipeItem,i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != recipeItemList ? recipeItemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.title_tv)
        TextView textView;
        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }


    public void setRecipes(List<Recipe> data) {
        recipeItemList.addAll(data);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Recipe item, int pos);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
