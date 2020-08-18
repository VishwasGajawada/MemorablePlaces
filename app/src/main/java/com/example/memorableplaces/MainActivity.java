package com.example.memorableplaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> locations = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;
    ListView listView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.share_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.clear){
            ArrayList<String> latitudes = new ArrayList<>();
            ArrayList<String> longitudes = new ArrayList<>();
            places.clear();
            locations.clear();
            places.add("Add a new place...");
            locations.add(new LatLng(0,0));
            latitudes.add(Double.toString(0));
            longitudes.add(Double.toString(0));
            try {
                sharedPreferences.edit().putString("places",ObjectSerializer.serialize(places)).apply();
                sharedPreferences.edit().putString("lats",ObjectSerializer.serialize(latitudes)).apply();
                sharedPreferences.edit().putString("lons",ObjectSerializer.serialize(longitudes)).apply();
                arrayAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();

        places.clear();
        locations.clear();
        latitudes.clear();
        longitudes.clear();



        sharedPreferences = this.getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);

        try{
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats",ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lons",ObjectSerializer.serialize(new ArrayList<String>())));

        }catch (Exception e){
            e.printStackTrace();
        }


        if(places.size()>0 && latitudes.size()>0 && longitudes.size()>0){
            if(places.size() == latitudes.size() && places.size() == longitudes.size()){
                for(int i=0;i<latitudes.size();i++){
                    locations.add(new LatLng(Double.parseDouble(latitudes.get(i)),Double.parseDouble(longitudes.get(i))));

                }
            }
        }else{
            places.add("Add a new place...");
            locations.add(new LatLng(0,0));
        }

        listView = findViewById(R.id.listView);


        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("placeNumber",i);

                startActivity(intent);
            }


        });

    }
}