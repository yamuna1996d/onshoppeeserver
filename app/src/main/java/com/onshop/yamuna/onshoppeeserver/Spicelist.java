package com.onshop.yamuna.onshoppeeserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.onshop.yamuna.onshoppeeserver.Interface.ItemClickListener;
import com.onshop.yamuna.onshoppeeserver.Models.Spice;
import com.onshop.yamuna.onshoppeeserver.ViewHolder.SpiceViewHolder;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class Spicelist extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;
    FirebaseDatabase databas;
    DatabaseReference spicelist;
    FirebaseStorage storage;
    StorageReference storageReference;
    String Categoryid="";
    FirebaseRecyclerAdapter<Spice,SpiceViewHolder>adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spicelist);

        databas=FirebaseDatabase.getInstance();
        spicelist=databas.getReference("Spice");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        recyclerView=(RecyclerView)findViewById(R.id.recycles);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (getIntent()!=null)
            Categoryid = getIntent().getStringExtra("CategoryId");

        if (!Categoryid.isEmpty())
            loadspice(Categoryid);



    }

    private void loadspice(String categoryid) {
        adapter=new FirebaseRecyclerAdapter<Spice, SpiceViewHolder>(Spice.class,R.layout.spice_list_layout,SpiceViewHolder.class,
                spicelist.orderByChild("menuId").equalTo(categoryid)) {
            @Override
            protected void populateViewHolder(SpiceViewHolder spiceViewHolder, Spice spice, int i) {
                spiceViewHolder.textmessages.setText(spice.getName());
                Picasso.with(getBaseContext()).load(spice.getImage())
                        .into(spiceViewHolder.imageviews);
                spiceViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
