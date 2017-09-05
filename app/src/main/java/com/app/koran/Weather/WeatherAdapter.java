package com.app.koran.Weather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.koran.R;

import java.util.ArrayList;

/**
 * Created by MSI 95O on 5/6/2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private ArrayList<Weather> weatherList;
    AdapterInterface adapterInterface;

    public WeatherAdapter(AdapterInterface adapterInterface) {
        this.adapterInterface = adapterInterface;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView cityName;
        public LinearLayout layout;
        public TextView cityCuaca;

        public ViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.layout);
            cityName = (TextView) view.findViewById(R.id.dd);
            cityCuaca = (TextView) view.findViewById(R.id.cuaca_kecil);
        }
    }


    public WeatherAdapter(ArrayList<Weather> weatherList, AdapterInterface adapterInterface) {
        this.weatherList = weatherList;
        this.adapterInterface = adapterInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Weather weather = weatherList.get(position);
        holder.cityName.setText(weather.getCity());
        holder.cityCuaca.setText(weather.getTemperature()+"");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                v.getContext().startActivity(new Intent(v.getContext(),DetailActivity.class).putExtra("latitude",weather.getLatitude()).putExtra("longitude",weather.getLongitude()));
                adapterInterface.onClick(weather.getLatitude(), weather.getLongitude(), holder.cityName.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }


    public interface AdapterInterface{
        void onClick(double lat, double lng, String cityName);
    }
}
