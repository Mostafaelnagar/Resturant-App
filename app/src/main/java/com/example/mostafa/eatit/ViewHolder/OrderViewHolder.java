package com.example.mostafa.eatit.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mostafa.eatit.InterFace.ItemClicklistener;
import com.example.mostafa.eatit.R;

/**
 * Created by mostafa on 10/2/2017.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtOrderId, txtOrderStatus,txtOrderPhone,txtOrderAddress;
    private ItemClicklistener itemClicklistener;

    public void setItemClicklistener(ItemClicklistener itemClicklistener) {
        this.itemClicklistener = itemClicklistener;
    }

    public OrderViewHolder(View itemView) {
        super(itemView);
        txtOrderId = (TextView) itemView.findViewById(R.id.Order_id);

        txtOrderStatus = (TextView) itemView.findViewById(R.id.Order_Status);
        txtOrderPhone = (TextView) itemView.findViewById(R.id.Order_Phone);
        txtOrderAddress = (TextView) itemView.findViewById(R.id.Order_Address);
        itemView.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        itemClicklistener.onClick(view, getAdapterPosition(), false);

    }
}
