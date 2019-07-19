package com.onshop.yamuna.onshoppeeserver.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onshop.yamuna.onshoppeeserver.Interface.ItemClickListener;
import com.onshop.yamuna.onshoppeeserver.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textmessage;
    public ImageView imageview;
    public ItemClickListener itemClickListener;
    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        textmessage=(TextView)itemView.findViewById(R.id.menuname);
        imageview=(ImageView)itemView.findViewById(R.id.img);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
