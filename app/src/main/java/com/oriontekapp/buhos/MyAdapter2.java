package com.oriontekapp.buhos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {

    private static final String TAG = MyAdapter2.class.getSimpleName();
    private  Context context;
    private List<Upload> uploads;
    private  DatabaseReference myRef,myRef2;



    public MyAdapter2(Context context, List<Upload> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_layout_2, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
       Upload  upload = uploads.get(position);
       holder.textViewName.setText(upload.getName()+""+upload.getApellido());


       //set status color in clients
       String status = upload.getStatus();
       if ((Objects.equals(status, "Activo"))){
           holder.status_color.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_status_verde));
       }else if(Objects.equals(status, "Inactivo")){
           holder.status_color.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_status_rojo));
       }

        //call the data base child
        myRef = FirebaseDatabase.getInstance().getReference().child("/Clients/"+upload.getClient());



        //pass data
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View view = inflater.inflate( R.layout.inflate_view_info, null );
                alertbox.setView(view);
                //find views
                TextView name,apellido,pais,ciudad,direccion,celular,status_client;
                RadioButton radioButton_active,radioButton_inactive;
                CardView cardView_status;
                name = view.findViewById(R.id.client_name_view);
                apellido = view.findViewById(R.id.client_last_name_view);
                pais = view.findViewById(R.id.client_country_view);
                ciudad = view.findViewById(R.id.client_city_view);
                direccion = view.findViewById(R.id.client_direction_view);
                celular = view.findViewById(R.id.client_cel_view);
                status_client = view.findViewById(R.id.client_status_view);
                radioButton_active = view.findViewById(R.id.radioButton_active);
                radioButton_inactive = view.findViewById(R.id.radioButton_inactive);
                cardView_status = view.findViewById(R.id.cardview_status_view);

                //set values
                ciudad.setText(upload.getCiudad());
                name.setText(upload.getName());
                apellido.setText(upload.getApellido());
                pais.setText(upload.getPais());
                direccion.setText(upload.getDireccion());
                celular.setText(upload.getCelular());
                status_client.setText(upload.getStatus());

                //set status color in view
                String status = upload.getStatus();
                if ((Objects.equals(status, "Activo"))){
                    cardView_status.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_status_verde));
                }else if(Objects.equals(status, "Inactivo")){
                    cardView_status.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_status_rojo));
                }

                //Radio button process
                radioButton_active.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myRef2 = FirebaseDatabase.getInstance().getReference().child("/Clients/"+upload.getClient()+"/"+upload.getKey());
                        radioButton_inactive.setChecked(false);
                        myRef2.child("status").setValue("Activo");
                        cardView_status.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_status_verde));
                        status_client.setText("Activo");

                    }
                });
                radioButton_inactive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myRef2 = FirebaseDatabase.getInstance().getReference().child("/Clients/"+upload.getClient()+"/"+upload.getKey());
                        radioButton_active.setChecked(false);
                        myRef2.child("status").setValue("Inactivo");
                        cardView_status.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_status_rojo));
                        status_client.setText("Inactivo");

                    }
                });


                alertbox.setNegativeButton("Cerrar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.cancel();
                            }
                        });
                alertbox.show().setCancelable(false);



                //Logs of data
                Log.d(TAG,"[Name]: "+upload.getName());
                Log.d(TAG,"[Apellido]: "+upload.getApellido());
                Log.d(TAG,"[Celular]: "+upload.getCelular());
                Log.d(TAG,"[Ciudad]: "+upload.getCiudad());
                Log.d(TAG,"[Direccion]: "+upload.getDireccion());
                Log.d(TAG,"[Pais]: "+upload.getPais());


            }
        });

        //delete value
        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,upload.getKey());
                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setTitle("[ SE ELIMINARA UN CLIENTE ]");
                alertbox.setMessage("¿Está seguro de que quiere eliminar este cliente?");
                alertbox.setIcon(R.drawable.ic_round_delete_24);

                alertbox.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                        //borrar empresa por posicion
                        myRef.child(upload.getKey()).removeValue();

                    }
                });

                alertbox.setNegativeButton("Cerrar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.cancel();
                            }
                        });

                alertbox.show().setCancelable(false);

            }
        });

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private ImageButton button_delete;
        private CardView status_color;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textView_name_2);
            button_delete = itemView.findViewById(R.id.button_delete_2);
            status_color = itemView.findViewById(R.id.status_color);

        }
    }
}