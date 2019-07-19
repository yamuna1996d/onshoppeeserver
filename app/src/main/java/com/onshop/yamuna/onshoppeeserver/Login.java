package com.onshop.yamuna.onshoppeeserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onshop.yamuna.onshoppeeserver.Common.Common;
import com.onshop.yamuna.onshoppeeserver.Models.Admin;

public class Login extends AppCompatActivity {
    EditText ed1, ed2;
    Button b;
    private ProgressDialog loadingBar;
    private String parentDbName = "Admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed1 = (EditText) findViewById(R.id.loginuser);
        ed2 = (EditText) findViewById(R.id.loginpass);
        b = (Button) findViewById(R.id.liner1);
        loadingBar = new ProgressDialog(this);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAdmin();
            }
        });
    }

    private void loadAdmin() {
        String phone = ed1.getText().toString();
        String password = ed2.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            AllowAccessToAccount(phone, password);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()) {
                    Admin adminData = dataSnapshot.child(parentDbName).child(phone).getValue(Admin.class);

                    if (adminData.getPassword().equals(password)) {
                        if (adminData.getPhone().equals(phone)) {
                            Toast.makeText(Login.this, "Logged in successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(Login.this, Home.class);
                            Common.currentonlineUser = adminData;
                            startActivity(intent);
                        }
                    }
                } else {
                    Toast.makeText(Login.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
