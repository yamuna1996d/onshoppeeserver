package com.onshop.yamuna.onshoppeeserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.onshop.yamuna.onshoppeeserver.Common.Common;
import com.onshop.yamuna.onshoppeeserver.Interface.ItemClickListener;
import com.onshop.yamuna.onshoppeeserver.Models.Category;
import com.onshop.yamuna.onshoppeeserver.ViewHolder.MenuViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView name;
    FirebaseDatabase database;
    DatabaseReference categories;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseRecyclerAdapter<Category,MenuViewHolder>adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MaterialEditText se;
    Button btsel,btup;
    Category newcategory; ///for new category
    Uri saveuri;
    DrawerLayout drawer;
    private final int Pick_image=71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database=FirebaseDatabase.getInstance();
        categories=database.getReference("Category");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              showDialog();// for adding category dialog
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headview=navigationView.getHeaderView(0);
        name=(TextView)headview.findViewById(R.id.text_fullName);
        name.setText(Common.currentonlineUser.getName());

        recyclerView=(RecyclerView)findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadlist();

    }

    private void showDialog() {
        AlertDialog.Builder alert=new AlertDialog.Builder(Home.this);
        alert.setTitle("Add new Category");
        alert.setMessage("Please Enter Full Information");
        LayoutInflater inflater=this.getLayoutInflater();
        View addspice=inflater.inflate(R.layout.addnewspice,null);
        se=addspice.findViewById(R.id.txname);
        btsel=addspice.findViewById(R.id.select);
        btup=addspice.findViewById(R.id.upload);
        btsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); //select image from gallery and save url in firebase
            }
        });

        btup.setOnClickListener(new View.OnClickListener() {
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
                if (newcategory !=null){
                    categories.push().setValue(newcategory);
                   Snackbar.make(drawer,"New Category"+newcategory.getName()+"was added",Snackbar.LENGTH_SHORT).show();

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
                    Toast.makeText(Home.this,"Uploaded !!!",Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new Category
                            newcategory=new Category(se.getText().toString(),uri.toString());
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pD.dismiss();
                            Toast.makeText(Home.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
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
    //press ctrl+o


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Pick_image && resultCode==RESULT_OK && data!=null &&data.getData()
                !=null){
            saveuri=data.getData();
            btsel.setText("Image Selected !");


        }
    }

    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Pick_image);

    }

    private void loadlist() {
        adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.menuitem,
                MenuViewHolder.class,categories) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Category category, int i) {
                menuViewHolder.textmessage.setText(category.getName());
                Picasso.with(getBaseContext()).load(category.getImage())
                        .into(menuViewHolder.imageview);

                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent i=new Intent(Home.this,Spicelist.class);
                        i.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(i);

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        }

        else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //update and delete functions
    //press ctrl+o


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.UPDATE)){
            showUpdateDialoge(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else{
            if (item.getTitle().equals(Common.DELETE)){
                deleteCategory(adapter.getRef(item.getOrder()).getKey());
            }
        }
        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {
        categories.child(key).removeValue();
        Toast.makeText(this,"Spice deleted !!!!",Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialoge(final String key, final Category item) {
        //copy from showDialoge
        AlertDialog.Builder alert=new AlertDialog.Builder(Home.this);
        alert.setTitle("Update Category");
        alert.setMessage("Please Enter Full Information");
        LayoutInflater inflater=this.getLayoutInflater();
        View addspice=inflater.inflate(R.layout.addnewspice,null);
        se=addspice.findViewById(R.id.txname);
        btsel=addspice.findViewById(R.id.select);
        btup=addspice.findViewById(R.id.upload);

        se.setText(item.getName());

        btsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); //select image from gallery and save url in firebase
            }
        });

        btup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        alert.setView(addspice);
        alert.setIcon(R.drawable.cart);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setName(se.getText().toString());
                categories.child(key).setValue(item);


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

    private void changeImage(final Category item) {
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
                    Toast.makeText(Home.this,"Uploaded !!!",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Home.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
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
