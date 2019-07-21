package com.onshop.yamuna.onshoppeeserver.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onshop.yamuna.onshoppeeserver.Common.Common;
import com.onshop.yamuna.onshoppeeserver.Interface.ItemClickListener;
import com.onshop.yamuna.onshoppeeserver.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener{
    public TextView textmessages;
    public ImageView imageviews;
    public ItemClickListener itemClickListener;
    public SpiceViewHolder(@NonNull View itemView) {
        super(itemView);
        textmessages=(TextView)itemView.findViewById(R.id.spicesname);
        imageviews=(ImageView)itemView.findViewById(R.id.imgs);

        itemView.setOnCreateContextMenuListener(this); //seeing the options update,delete..

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action");
        menu.add(0,0,getAdapterPosition(),Common.UPDATE);
        menu.add(0,1,getAdapterPosition(),Common.DELETE);

    }
}
