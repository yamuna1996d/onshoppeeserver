package com.onshop.yamuna.onshoppeeserver.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.onshop.yamuna.onshoppeeserver.Interface.ItemClickListener;
import com.onshop.yamuna.onshoppeeserver.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView txtid,txtstat,txtphone,txtaddress;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtaddress=(TextView)itemView.findViewById(R.id.orderaddress);
        txtphone=(TextView)itemView.findViewById(R.id.orderphone);
        txtid=(TextView)itemView.findViewById(R.id.orderid);
        txtstat=(TextView)itemView.findViewById(R.id.orderstatus);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(android.view.View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
             menu.setHeaderTitle("Select the action");
             menu.add(0,0,getAdapterPosition(),"Update");
        menu.add(0,1,getAdapterPosition(),"Update");
    }
}
