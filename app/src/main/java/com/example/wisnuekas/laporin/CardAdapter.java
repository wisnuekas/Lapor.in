package com.example.wisnuekas.laporin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by wisnuekas on 6/12/17.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private ImageLoader imageLoader;
    protected Context context;

    //List of superHeroes
    List<DataLaporan> dataLaporans;

    public CardAdapter(List<DataLaporan> dataLaporans, Context context){
        super();
        //Getting all the superheroes
        this.dataLaporans = dataLaporans;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.laporan_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DataLaporan dataLaporan =  dataLaporans.get(position);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(dataLaporan.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        holder.imageView.setImageUrl(dataLaporan.getImageUrl(), imageLoader);
        holder.textViewDate.setText(dataLaporan.getNameImg());
        holder.textViewAnnotation.setText(dataLaporan.getAnnotation());
        holder.textViewCoordinate.setText(dataLaporan.getCoordinate());
    }

    @Override
    public int getItemCount() {
        return dataLaporans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public NetworkImageView imageView;
        public TextView textViewDate;
        public TextView textViewAnnotation;
        public TextView textViewCoordinate;
        public TextView textViewDelete;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.image_view);
            textViewDate = (TextView) itemView.findViewById(R.id.date_laporan);
            textViewAnnotation = (TextView) itemView.findViewById(R.id.annotation_view);
            textViewCoordinate = (TextView) itemView.findViewById(R.id.coordinate_view);
            textViewDelete = (TextView) itemView.findViewById(R.id.cancelLaporan1);

            textViewDelete.setOnClickListener(new DeleteListener());
            textViewCoordinate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, MapsViewActivity.class);
                    i.putExtra("COORDINATE", textViewCoordinate.getText());
                    context.startActivity(i);
                }
            });
        }

    }

    protected class DeleteListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            // Setting Dialog Title
            alertDialog.setTitle("Konfirmasi pembatalan laporan...");

            // Setting Dialog Message
            alertDialog.setMessage("Apakah anda ingin membatalkan laporan ini?");

            // Setting Icon to Dialog
//            alertDialog.setIcon(R.drawable.delete);

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {

                    // Write your code here to invoke YES event
                    Toast.makeText(context, "Ya", Toast.LENGTH_SHORT).show();
                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    Toast.makeText(context, "Tidak", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();

        }
    }

}