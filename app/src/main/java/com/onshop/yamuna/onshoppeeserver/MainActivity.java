package com.onshop.yamuna.onshoppeeserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.rey.material.widget.Button;

public class MainActivity extends AppCompatActivity {
Button log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log=(Button)findViewById(R.id.join);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),Login.class);
                startActivity(in);
            }
        });

    }
}
