package com.suzanelsamahy.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suzanelsamahy.bakingapp.R;
import com.suzanelsamahy.bakingapp.Utilities;
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

//        //Render image using Picasso library
//        if (!TextUtils.isEmpty(feedItem.getThumbnail())) {
//            Picasso.with(mContext).load(feedItem.getThumbnail())
//                    .error(R.drawable.placeholder)
//                    .placeholder(R.drawable.placeholder)
//                    .into(customViewHolder.imageView);
//        }

        //Setting text view title
        customViewHolder.textView.setText(recipeItem.getName());
        customViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int pos = (int) view.getTag();
                onItemClickListener.onItemClick(recipeItem,i);
            }
        });


        if (Utilities.isFavorite(mContext, recipeItem.getId()) == 1) {
            customViewHolder.favIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.btn_star_big_on_pressed));
        } else {
            customViewHolder.favIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.btn_star_big_off));
        }


        customViewHolder.favIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isFavorite(mContext, recipeItem.getId()) == 0) {
                    customViewHolder.favIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.btn_star_big_on_pressed));
                    view.setActivated(true);
                    onItemClickListener.onFavClick(recipeItem, true);
                } else {
                    customViewHolder.favIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.btn_star_big_off));
                    view.setActivated(false);
                    onItemClickListener.onFavClick(recipeItem, false);
                }

//
//
//                if(view.isActivated()){
//                    customViewHolder.favIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.btn_star_big_off));
//                    view.setActivated(false);
//                    onItemClickListener.onFavClick(view,false);
//                } else {
//                    customViewHolder.favIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.btn_star_big_on_pressed));
//                    view.setActivated(true);
//                    onItemClickListener.onFavClick(view,true);
//                }


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

        @BindView(R.id.fav_iv)
        ImageView favIv;

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

        void onFavClick(Recipe recipe, boolean click);
    }

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
