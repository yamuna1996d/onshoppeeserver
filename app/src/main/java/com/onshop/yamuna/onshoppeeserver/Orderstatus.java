package com.onshop.yamuna.onshoppeeserver;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.onshop.yamuna.onshoppeeserver.Common.Common;
import com.onshop.yamuna.onshoppeeserver.Interface.ItemClickListener;
import com.onshop.yamuna.onshoppeeserver.Models.Category;
import com.onshop.yamuna.onshoppeeserver.Models.Request;
import com.onshop.yamuna.onshoppeeserver.ViewHolder.MenuViewHolder;
import com.onshop.yamuna.onshoppeeserver.ViewHolder.OrderViewHolder;

public class Orderstatus extends AppCompatActivity {
    MaterialSpinner spinner;
    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderstatus);
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        recyclerView=(RecyclerView)findViewById(R.id.listorder);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadOrder();
    }

    private void loadOrder() {
        adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class,R.layout.order,
                OrderViewHolder.class,requests) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, final Request request, int position) {
                orderViewHolder.txtid.setText(adapter.getRef(position).getKey());
                orderViewHolder.txtstat.setText(Common.convertStringtoStatus(request.getStatus()));
                orderViewHolder.txtaddress.setText(request.getAddress());
                orderViewHolder.txtphone.setText(request.getPhone());

                orderViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent track=new Intent(Orderstatus.this,Google_map.class);
                        Common.currentRequest=request;
                        startActivity(track);

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE))
            showDialogeBox(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        else if (item.getTitle().equals(Common.DELETE)){
            deleteOrder(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void deleteOrder(String key) {
        requests.child(key).removeValue();

    }

    private void showDialogeBox(String key, final Request item) {
        final AlertDialog.Builder alert= new AlertDialog.Builder(Orderstatus.this);
        alert.setTitle("Update Order");
        alert.setMessage("Please choose status");
        LayoutInflater inflater=this.getLayoutInflater();
        final View view=inflater.inflate(R.layout.updateorder,null);
        spinner=(MaterialSpinner)view.findViewById(R.id.statussp);
        spinner.setItems("Placed","On the way","Shipped");
        alert.setView(view);
        final String localKey=key;
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
                requests.child(localKey).setValue(item);

            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           dialog.dismiss();

            }
        });
        alert.show();

    }
}
