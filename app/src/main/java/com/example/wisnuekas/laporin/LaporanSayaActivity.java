package com.example.wisnuekas.laporin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LaporanSayaActivity extends AppCompatActivity {

    //Creating List Laporan
    private List<DataLaporan> listDataLaporans;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_saya);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        listDataLaporans = new ArrayList<>();

        //Calling method to get data
        getData();
    }



    //This method will get data from the web api
    private void getData(){
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

        //Creating a json array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();

                        //calling method to parse json array
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    //This method will parse json data
    private void parseData(JSONArray array){
        for(int i = 0; i<array.length(); i++) {
            DataLaporan dataLaporan = new DataLaporan();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                dataLaporan.setImageUrl(json.getString(Config.TAG_IMAGE_URL));
                dataLaporan.setNameImg(json.getString(Config.TAG_NAME));
                dataLaporan.setAnnotation(json.getString(Config.TAG_ANNOTATION));
                dataLaporan.setCoordinate(json.getString(Config.TAG_COORDINATE));
                dataLaporan.setDate(json.getString(Config.TAG_DATE));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            listDataLaporans.add(dataLaporan);
        }

        //Finally initializing our adapter
        adapter = new CardAdapter(listDataLaporans, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}
