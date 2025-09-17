package com.example.listycitylab3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddCityFragment.AddCityDialogListener {

    private LinearLayout layoutBackground;
    private ArrayList<City> dataList;
    private ListView cityList;
    private CityArrayAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] cities = { "Edmonton", "Vancouver", "Toronto" };
        String[] provinces = { "AB", "BC", "ON" };

        dataList = new ArrayList<City>();
        for (int i = 0; i < cities.length; i++) {
            dataList.add(new City(cities[i], provinces[i]));
        }

        FloatingActionButton fab = findViewById(R.id.button_add_city);
        layoutBackground = findViewById(R.id.layout_background);
        cityList = findViewById(R.id.city_list);
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        // when a listView item is clicked, get the city that was clicked and send the city with its info to AddCityFragment
        cityList.setOnItemClickListener((parent, view, position, id) -> {
            darkenLayoutBackground(layoutBackground);
            City selectedCity = dataList.get(position);
            // create new instance of AddCityFragment but with bundled arguments containing the clicked city
            AddCityFragment editCityFragment = AddCityFragment.newInstance(selectedCity, position);
            editCityFragment.show(getSupportFragmentManager(), "Edit City");
        });

        fab.setOnClickListener(view -> {
            darkenLayoutBackground(layoutBackground);
            AddCityFragment addCityFragment = new AddCityFragment();
            addCityFragment.show(getSupportFragmentManager(), "Add City");
        });
    }

    @Override
    public void addCity(City city) {
        cityAdapter.add(city);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void editCity(String cityName, String provinceName, int position) {
        City editedCity = dataList.get(position);
        editedCity.setName(cityName);
        editedCity.setProvince(provinceName);
    }

    @Override
    public void dismissFragment() {
        layoutBackground.setBackgroundColor(getColor(android.R.color.transparent));
    }

    public void darkenLayoutBackground(View view) {
        view.setBackgroundColor(getColor(R.color.transparent_black));
    }

    // https://developer.android.com/reference/android/app/AlertDialog.Builder
    // https://stackoverflow.com/questions/14439941/passing-data-between-fragments-to-activity
    // https://stackoverflow.com/questions/12525304/alertdialog-setondismisslistener-not-working
    // https://stackoverflow.com/questions/5425568/how-to-use-setarguments-and-getarguments-methods-in-fragments
    // https://stackoverflow.com/questions/1492554/set-transparent-background-of-an-imageview-on-android
}