package com.develop.windexit.admin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.develop.windexit.admin.Common.Common;
import com.develop.windexit.admin.R;

import info.hoang8f.widget.FButton;



public class OrderViewHolder extends RecyclerView.ViewHolder implements  View.OnCreateContextMenuListener {

    public TextView txtOrderId, txtOrderAddress, txtOrderStatus, txtOrderPhone,txtorderDate,txtComment;
    public FButton btnEdit, btnDetail;

    public OrderViewHolder(View itemView) {
        super(itemView);
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtorderDate = itemView.findViewById(R.id.order_date);

        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        txtComment = itemView.findViewById(R.id.order_comment);

        btnEdit = itemView.findViewById(R.id.btnEdit);
       // btnRemove = itemView.findViewById(R.id.btnRemove);
        btnDetail = itemView.findViewById(R.id.btnDetail);
       //btnDirection = itemView.findViewById(R.id.btnDirection);

       itemView.setOnCreateContextMenuListener(this);
         //itemView.setOnLongClickListener(this);
         //itemView.setOnClickListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        contextMenu.setHeaderTitle("Select the action");
        //contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);


    }
}






   /*
    public void setItemCliclListener(ItemCliclListener itemCliclListener) {
        this.itemCliclListener = itemCliclListener;
    }

    @Override
    public void onClick(View v) {
       itemCliclListener.onClick(v, getAdapterPosition(), false);

    }
    @Override
    public boolean onLongClick(View v) {
        itemCliclListener.onClick(v, getAdapterPosition(), true);
        return true;
    }*/