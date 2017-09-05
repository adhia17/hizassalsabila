package com.app.koran.Weather;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.koran.R;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {

    private ArrayList<Weather> cities;
    private RecyclerView recyclerView;
    private WeatherAdapter mAdapter;

    private final String API_KEY = "d60802d2761e2c6699c15a93b3af0622";
    private TextView cityname;
    private ImageView weather;
    private TextView temperature;
    private TextView humidity;
    private TextView pressure;
    private JSONObject json = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        initDataset();
        mAdapter = new WeatherAdapter(cities, new WeatherAdapter.AdapterInterface() {
            @Override
            public void onClick(double lat, double lng, String cityName) {
                cityname.setText(cityName);
                final String URL = WeatherURLBuilder(lat, lng);
                getCuaca(URL);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        cityname = (TextView) findViewById(R.id.city);
        weather = (ImageView) findViewById(R.id.weather);
        temperature = (TextView) findViewById(R.id.temperature);
        humidity = (TextView) findViewById(R.id.humidity);
        pressure = (TextView) findViewById(R.id.pressure);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final String URL = WeatherURLBuilder(-4.134586, 120.045464);
        getCuaca(URL);
    }

    public void getCuaca(final String URL) {
        Log.d("enter", "masuk1");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("enter", "masuk2");
                    json = JSONParser.getJSONObjectFromURL(URL);
                    Log.d("enter", "masuk3");
                    JSONArray jsonArray = json.getJSONArray("weather");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Glide.with(WeatherActivity.this).load(IconWeatherURLBuilder(jsonObject.getString("icon"))).into(weather);
                    temperature.setText("Suhu : " + json.getJSONObject("main").getDouble("temp") + " Celcius");
                    humidity.setText("Kelembapan : " + json.getJSONObject("main").getDouble("humidity"));
                    pressure.setText("Tekanan : " + json.getJSONObject("main").getDouble("pressure"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).run();
    }

    public void initDataset() {
        cities = new ArrayList<>();
        cities.add(new Weather("Kecamatan Belawa", -3.987414, 119.956033));
        cities.add(new Weather("Kecamatan Bola", -4.212259, 120.243931));
        cities.add(new Weather("Kecamatan Gilireng", -3.887359, 120.197005));
        cities.add(new Weather("Kecamatan Keera", -3.801936, 120.293356));
        cities.add(new Weather("Kecamatan Majauleng", 4.048552, 120.148244));
        cities.add(new Weather("Kecamatan Maniang Pajo", -3.907165, 120.080547));
        cities.add(new Weather("Kecamatan Pammana", -4.202832, 120.085741));
        cities.add(new Weather("Kecamatan Penrang", -4.076809, 120.246121));
        cities.add(new Weather("Kecamatan Pitumpanua", -3.716672, 120.365846));
        cities.add(new Weather("Kecamatan Sabbang Paru", -3.716672, 120.365846));
        cities.add(new Weather("Kecamatan Sajoanging", -3.716672, 120.365846));
        cities.add(new Weather("Kecamatan Takkalalla", -3.716672, 120.365846));
        cities.add(new Weather("Kecamatan Tana Sitolo", 4.047617, 120.056749));
        cities.add(new Weather("Kecamatan Tempe", -4.134586, 120.045464));
    }

    public String WeatherURLBuilder(double latitude, double longitude) {
        return "http://api.openweathermap.org/data/2.5/weather?lat="
                + String.valueOf(latitude)
                + "&lon=" + String.valueOf(longitude)
                + "&units=metric"
                + "&appid=" + API_KEY;
    }

    public String IconWeatherURLBuilder(String iconName) {
        return "http://openweathermap.org/img/w/" + iconName + ".png";
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();

                break;

        }

        return false;

    }


    @Override

    public void onBackPressed() {

        super.onBackPressed();

        finish();

    }
}
