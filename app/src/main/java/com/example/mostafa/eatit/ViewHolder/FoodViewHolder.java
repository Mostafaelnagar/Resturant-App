package com.example.mostafa.eatit.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafa.eatit.InterFace.ItemClicklistener;
import com.example.mostafa.eatit.R;

/**
 * Created by mostafa on 9/29/2017.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView food_Name;
    public ImageView food_Image,fav_Image;
    private ItemClicklistener itemClicklistener;

    public void setItemClicklistener(ItemClicklistener itemClicklistener) {
        this.itemClicklistener = itemClicklistener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);
        food_Name = (TextView) itemView.findViewById(R.id.food_name);
        food_Image = (ImageView) itemView.findViewById(R.id.food_image);
        fav_Image = (ImageView) itemView.findViewById(R.id.fav);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClicklistener.onClick(view, getAdapterPosition(), false);

    }
}
