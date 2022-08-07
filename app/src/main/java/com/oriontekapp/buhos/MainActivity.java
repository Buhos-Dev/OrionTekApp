package com.oriontekapp.buhos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();


    FloatingActionButton button_add;
    ImageButton button_edit_company;
    TextView text_company;
    String my_company = null;
    ProgressBar progressBar;

     FirebaseDatabase database;
     DatabaseReference myRef;
     DatabaseReference myRef2;
     RecyclerView recyclerView;
     RecyclerView.Adapter adapter;
     List<Upload> uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hide actionBar
        getSupportActionBar().hide();

        //.myRef2.child("company").setValue("OrionTek");

        //fiind views
        button_add = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerview);
        button_edit_company = findViewById(R.id.edit_company);
        text_company = findViewById(R.id.company_name_edit);
        progressBar = findViewById(R.id.progress_Bar);

        //
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploads = new ArrayList<Upload>();
        //creating adapter
        adapter = new MyAdapter(getApplicationContext(), uploads);
        //adding adapter to recyclerview

        myRef = FirebaseDatabase.getInstance().getReference().child("/Business/Users");
        myRef2 = FirebaseDatabase.getInstance().getReference().child("Work");

        //start loading
        progressBar.setVisibility(View.VISIBLE);

        //adding an event listener to fetch values
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //clear old data
                uploads.clear();
            for (DataSnapshot ds : snapshot.getChildren()) {

                    Upload value = ds.getValue(Upload.class);

                    Log.d(TAG,"Valor2 ==> "+value.getClient());
                    uploads.add(value);
                    //hide loading
                }
                recyclerView.setAdapter(adapter);
                //stop loading
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
              //  progressDialog.dismiss();
            }
        });


        button_edit_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit campany name
                Alert_edit();
            }

        });

        //add new client
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calldialog();
            }
        });

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                 my_company = snapshot.child("company").getValue(String.class);
                //set name company
                text_company.setText(my_company);
                Log.d(TAG,"My Work --> "+my_company);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //

            }
        });

    }
    private void Alert_edit() {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        //builder.setTitle("Nuevo cliente.");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.inflate_edit_work, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = customLayout.findViewById(R.id.my_edit_bussines);
                //add new user
                String key = myRef.push().getKey();
                //
                myRef2.child("company").setValue(editText.getText().toString().trim());
               // myRef.child(key).child("key").setValue(key);
                adapter.notifyDataSetChanged();

            }
        }).setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // create and show
        // the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
    private void Calldialog() {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        //builder.setTitle("Nuevo cliente.");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.inflate_add_bussiness, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = customLayout.findViewById(R.id.my_edit);
                String data = editText.getText().toString().trim();
                //add new user
                String key = myRef.push().getKey();
                //
                myRef.child(key).child("client").setValue(data.replace(".",""));
                myRef.child(key).child("key").setValue(key);
                adapter.notifyDataSetChanged();



            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // create and show
        // the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}