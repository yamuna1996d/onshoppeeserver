package com.onshop.yamuna.onshoppeeserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.onshop.yamuna.onshoppeeserver.Common.Common;
import com.onshop.yamuna.onshoppeeserver.Interface.ItemClickListener;
import com.onshop.yamuna.onshoppeeserver.Models.Category;
import com.onshop.yamuna.onshoppeeserver.Models.Spice;
import com.onshop.yamuna.onshoppeeserver.ViewHolder.SpiceViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Button;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class Spicelist extends AppCompatActivity {
    MaterialEditText edname,edprice;
    Button sel,upd;
    Spice newspice;
    RelativeLayout rootLayout;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;
    FirebaseDatabase databas;
    DatabaseReference spicelist;
    FirebaseStorage storage;
    StorageReference storageReference;
    String Categoryid="";
    Uri saveuri;

    FirebaseRecyclerAdapter<Spice,SpiceViewHolder>adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spicelist);
        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);
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
                IsshowAddDialogue();

            }
        });
        if (getIntent()!=null)
            Categoryid = getIntent().getStringExtra("CategoryId");

        if (!Categoryid.isEmpty())
            loadspice(Categoryid);



    }

    private void IsshowAddDialogue() {
        AlertDialog.Builder alert=new AlertDialog.Builder(Spicelist.this);
        alert.setTitle("Add new Spice");
        alert.setMessage("Please Enter Full Information");
        LayoutInflater inflater=this.getLayoutInflater();
        View addspice=inflater.inflate(R.layout.new_spice_layout,null);
        edname=addspice.findViewById(R.id.txname);
        edprice=addspice.findViewById(R.id.txprice);
        sel=addspice.findViewById(R.id.select);
        upd=addspice.findViewById(R.id.upload);
        sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); //select image from gallery and save url in firebase
            }
        });

        upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImgae();
            }
        });

        alert.setView(addspice);
        alert.setIcon(R.drawable.cart);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (newspice !=null){
                    spicelist.push().setValue(newspice);
                    Snackbar.make(rootLayout,"New Category"+newspice.getName()+"was added",Snackbar.LENGTH_SHORT).show();

                }

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

    private void uploadImgae() {
        if (saveuri !=null){
            final ProgressDialog pD=new ProgressDialog(this);
            pD.setMessage("Uploading.......");
            pD.show();

            String imageName=UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(saveuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pD.dismiss();
                    Toast.makeText(Spicelist.this,"Uploaded !!!",Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new Category
                            newspice=new Spice();
                            newspice.setName(edname.getText().toString());
                            newspice.setPrice(edprice.getText().toString());
                            newspice.setmenuId(Categoryid);
                            newspice.setImage(uri.toString());
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pD.dismiss();
                            Toast.makeText(Spicelist.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    pD.setMessage("Uploaded"+progress+"%");
                }
            });

        }

    }

    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Common.Pick_image);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Common.Pick_image && resultCode==RESULT_OK && data!=null &&data.getData()
                !=null){
            saveuri=data.getData();
            sel.setText("Image Selected !");

        }
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

    //press ctrl+o

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)){
            showUpdatespice(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));

        }
        else if (item.getTitle().equals(Common.DELETE)){
          deleteSpice(adapter.getRef(item.getOrder()).getKey());

        }
        return super.onContextItemSelected(item);

    }

    private void deleteSpice(String key) {
        spicelist.child(key).removeValue();
    }

    private void showUpdatespice(final String key, final Spice item) {
        AlertDialog.Builder alert=new AlertDialog.Builder(Spicelist.this);
        alert.setTitle("Edit Spice");
        alert.setMessage("Please Enter Full Information");
        LayoutInflater inflater=this.getLayoutInflater();
        View addspice=inflater.inflate(R.layout.new_spice_layout,null);
        edname=addspice.findViewById(R.id.txname);
        edprice=addspice.findViewById(R.id.txprice);
        //set default value for view

        edname.setText(item.getName());
        edprice.setText(item.getPrice());


        sel=addspice.findViewById(R.id.select);
        upd=addspice.findViewById(R.id.upload);
        sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); //select image from gallery and save url in firebase
            }
        });

        upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImgae(item);
            }
        });

        alert.setView(addspice);
        alert.setIcon(R.drawable.cart);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                    item.setName(edname.getText().toString());
                    item.setPrice(edprice.getText().toString());

                    spicelist.child(key).setValue(item);
                    Snackbar.make(rootLayout,"Spice"+item.getName()+"was added",Snackbar.LENGTH_SHORT).show();



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

    private void changeImgae(final Spice item) {
        if (saveuri !=null){
            final ProgressDialog pD=new ProgressDialog(this);
            pD.setMessage("Uploading.......");
            pD.show();

            String imageName=UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(saveuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pD.dismiss();
                    Toast.makeText(Spicelist.this,"Uploaded !!!",Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            item.setImage(uri.toString());
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pD.dismiss();
                            Toast.makeText(Spicelist.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    pD.setMessage("Uploaded"+progress+"%");
                }
            });

        }
    }
}
