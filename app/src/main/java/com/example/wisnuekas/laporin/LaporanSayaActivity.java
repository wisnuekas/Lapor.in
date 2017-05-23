package com.example.wisnuekas.laporin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LaporanSayaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_saya);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView cancel = (TextView) findViewById(R.id.cancelLaporan1);
        cancel.setOnClickListener(new DeleteListener());

    }

    protected class DeleteListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LaporanSayaActivity.this);

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
                    Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();

        }
    }
}
