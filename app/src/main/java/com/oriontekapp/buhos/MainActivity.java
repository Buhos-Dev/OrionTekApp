package com.oriontekapp.buhos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();


    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    ImageButton button_edit_company;
    TextView text_company;

     FirebaseDatabase database;
     DatabaseReference myRef;
     RecyclerView.Adapter adapter;
     List<Upload> uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        getSupportActionBar().hide();

        //fiind views
        floatingActionButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerview);
        button_edit_company = findViewById(R.id.edit_company);
        text_company = findViewById(R.id.company_name_edit);

        //
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploads = new ArrayList<Upload>();
        //creating adapter
        adapter = new MyAdapter(getApplicationContext(), uploads);
        //adding adapter to recyclerview

        myRef = FirebaseDatabase.getInstance().getReference("Business");
        

        //adding an event listener to fetch values
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                   Upload upload = snapshot.getValue(Upload.class);
                    Log.d(TAG,"Valor ==> "+upload);
                    uploads.add(upload);
                }

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
              //  progressDialog.dismiss();
            }
        });






        button_edit_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //

            }

        });




    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}