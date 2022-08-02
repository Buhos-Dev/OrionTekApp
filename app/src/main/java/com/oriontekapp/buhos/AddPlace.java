package com.oriontekapp.buhos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddPlace extends AppCompatActivity {

    TextView textView_user;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_place);

        //find views
        textView_user = findViewById(R.id.user);
        floatingActionButton = findViewById(R.id.floatingActionButton2);
        recyclerView_place = findViewById(R.id.recyclerview_place);





























    }
}