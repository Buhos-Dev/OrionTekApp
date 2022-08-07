package com.oriontekapp.buhos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddPlace extends AppCompatActivity {

    private static final String TAG = AddPlace.class.getSimpleName();

   private RecyclerView recyclerView_place;
   private  RecyclerView.Adapter adapter;
   private List<Upload> uploads;

    TextView textView_user;
    ImageButton imageButton_back;
    TextView textView_back;
    ProgressBar progressBar_place;
    FloatingActionButton button_add;
    DatabaseReference myRef,myRef_whrite,myRef_clients;
    String key = null;
    String client_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_place);

        //hide actionBar
        getSupportActionBar().hide();

        //find views
        textView_user = findViewById(R.id.user);
        button_add = findViewById(R.id.floatingActionButton2);
        recyclerView_place = findViewById(R.id.recyclerview_place);
        progressBar_place = findViewById(R.id.progressBar_place);
        imageButton_back = findViewById(R.id.imageButton_back);
        textView_back = findViewById(R.id.textView_back);


        recyclerView_place.setHasFixedSize(true);
        recyclerView_place.setLayoutManager(new LinearLayoutManager(this));

        //get values
         key = getIntent().getStringExtra("key");
         client_name = getIntent().getStringExtra("client");

        if (client_name != null) {
            textView_user.setText(client_name);
        }

        uploads = new ArrayList<Upload>();
        //creating adapter
        adapter = new MyAdapter2(getApplicationContext(), uploads);
        //adding adapter to recyclerview

        //get database data
          myRef = FirebaseDatabase.getInstance().getReference("/Clients/"+client_name);

          //Start ProgressBar
        progressBar_place.setVisibility(View.VISIBLE);

        //adding an event listener to fetch values
       myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //clear old data
                uploads.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                     Upload value = ds.getValue(Upload.class);
                     uploads.add(value);
              }
                recyclerView_place.setAdapter(adapter);
                //stop loading
                progressBar_place.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //  progressDialog.dismiss();
            }
        });



                button_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //add direction
                        Log.d(TAG,"click add");

                        Calldialog();

                    }
                });
                //back activity
                imageButton_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
                //back activity
               textView_back.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       onBackPressed();
                   }
               });
            }




            private void Calldialog() {
                // Create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                //builder.setTitle("Nuevo cliente.");
                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.inflate_add_info_user, null);
                builder.setView(customLayout);
                // add a button
                builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Write in
                        String key2 = myRef.push().getKey();
                        myRef_whrite = FirebaseDatabase.getInstance().getReference("/Clients/"+client_name+"/"+key2);
                        myRef_clients = FirebaseDatabase.getInstance().getReference("/Clients/"+client_name);
                        //Get values from Edit
                        EditText name = customLayout.findViewById(R.id.client_name);
                        EditText apellido = customLayout.findViewById(R.id.client_last_name);
                        EditText direccion = customLayout.findViewById(R.id.client_direction);
                        EditText ciudad = customLayout.findViewById(R.id.client_city);
                        EditText pais = customLayout.findViewById(R.id.client_country);
                        EditText celular = customLayout.findViewById(R.id.client_cel);
                        Spinner  spinner_status = customLayout.findViewById(R.id.spinner_status);
                        //get text for spinnen for status values
                        String status_value = spinner_status.getSelectedItem().toString();

                        //if User client not is null or empty
                        if(!client_name.isEmpty()) {
                            //Put data in DB.
                            myRef_whrite.child("name").setValue(name.getText().toString());
                            myRef_whrite.child("apellido").setValue(apellido.getText().toString());
                            myRef_whrite.child("direccion").setValue(direccion.getText().toString());
                            myRef_whrite.child("ciudad").setValue(ciudad.getText().toString());
                            myRef_whrite.child("pais").setValue(pais.getText().toString());
                            myRef_whrite.child("celular").setValue(celular.getText().toString());
                            myRef_whrite.child("key").setValue(key2);
                            myRef_whrite.child("client").setValue(client_name);
                            myRef_whrite.child("status").setValue(status_value);
                        }
                        Log.d(TAG,"Get String of Spinner -->"+spinner_status.getSelectedItem().toString());
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