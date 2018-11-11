package com.develop.windexit.admin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.develop.windexit.admin.Common.Common;
import com.develop.windexit.admin.Interface.ItemCliclListener;
import com.develop.windexit.admin.R;


/**
 * Created by WINDEX IT on 16-Feb-18.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener

{

    public TextView txtMenuName;
    public ImageView imageView;

    private ItemCliclListener itemCliclListener;

    public MenuViewHolder(View itemView) {
        super(itemView);

        txtMenuName =  itemView.findViewById(R.id.menu_name);
        imageView =  itemView.findViewById(R.id.menu_image);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    public void setItemCliclListener(ItemCliclListener itemCliclListener) {
        this.itemCliclListener = itemCliclListener;
    }

    @Override
    public void onClick(View v) {
        itemCliclListener.onClick(v, getAdapterPosition(), false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);


    }
}
