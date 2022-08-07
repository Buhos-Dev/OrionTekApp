package com.oriontekapp.buhos;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<Upload> uploads;
    private  DatabaseReference myRef,myRef2;


    public MyAdapter(Context context, List<Upload> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Upload upload = uploads.get(position);
        holder.textViewName.setText(upload.getClient());


        //call the data base child
        myRef = FirebaseDatabase.getInstance().getReference().child("Business/Users");


        //pass data
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AddPlace.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("client",upload.getClient());
                intent.putExtra("key",upload.getKey());
                context.startActivity(intent);

            }
        });

        //delete value
        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setTitle("[ SE ELIMINARA UNA EMPRESA ]");
                alertbox.setMessage("¿Está seguro de que quiere eliminar esta empresa?");
                alertbox.setIcon(R.drawable.ic_round_delete_24);

                alertbox.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                        //borrar empresa por posicion y sus clientes
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

        public TextView textViewName;
        public ImageButton button_delete;
     //   public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textView_name);
            button_delete = itemView.findViewById(R.id.button_delete);

        }
    }
}