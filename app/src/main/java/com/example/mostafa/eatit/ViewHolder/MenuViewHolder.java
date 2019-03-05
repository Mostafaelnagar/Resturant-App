package com.example.mostafa.eatit.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mostafa.eatit.InterFace.ItemClicklistener;
import com.example.mostafa.eatit.R;

/**
 * Created by mostafa on 9/3/2017.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtmenuName;
    public ImageView imageView;
    private ItemClicklistener itemClicklistener;

    public MenuViewHolder(View itemView) {
        super(itemView);
        txtmenuName = (TextView) itemView.findViewById(R.id.menu_name);
        imageView = (ImageView) itemView.findViewById(R.id.menu_image);
        itemView.setOnClickListener(this);

    }

    public void setItemClicklistener(ItemClicklistener itemClicklistener) {
        this.itemClicklistener = itemClicklistener;
    }

    @Override
    public void onClick(View view) {
        itemClicklistener.onClick(view, getAdapterPosition(), false);
    }
}
