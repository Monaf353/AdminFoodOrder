package com.develop.windexit.admin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.develop.windexit.admin.Interface.ItemCliclListener;
import com.develop.windexit.admin.R;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.widget.FButton;

/**
 * Created by WINDEX IT on 07-Apr-18.
 */

public class ClientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView client_name,client_phone;
    public CircleImageView client_image;

    public FButton btn_Remove,btnDetails,btnAddBalance;
    private ItemCliclListener itemCliclListener;

    public ClientViewHolder(View itemView) {
        super(itemView);

        client_name = itemView.findViewById(R.id.client_name);
        client_phone = itemView.findViewById(R.id.client_phone);
        client_image =(CircleImageView) itemView.findViewById(R.id.client_image);

        btnDetails = itemView.findViewById(R.id.btnDetails);
        btn_Remove = itemView.findViewById(R.id.btnRemove);

       // btnAddBalance = itemView.findViewById(R.id.btnAddBalance);
    }
    public void setItemCliclListener(ItemCliclListener itemCliclListener) {
        this.itemCliclListener = itemCliclListener;
    }
    @Override
    public void onClick(View v) {
        itemCliclListener.onClick(v, getAdapterPosition(), false);
    }
}
