package com.example.android3dpw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnPrimary;
    Button btnSecondary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPrimary = findViewById(R.id.btnPrimary);
        btnSecondary = findViewById(R.id.btnSecondary);
        btnPrimary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Primary Button Clicked", Toast.LENGTH_SHORT).show();
                openPrimaryActivity();

            }

        });
        btnSecondary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "secondary Button Clicked", Toast.LENGTH_SHORT).show();
                openSecondaryActivity();
            }

        });
    }
    public void openPrimaryActivity(){
        Intent intent = new Intent(this, ServerActivity.class);
        startActivity(intent);
    }
    public void openSecondaryActivity(){
        Intent intent = new Intent(this, ClientActivity.class);
        startActivity(intent);
    }
}