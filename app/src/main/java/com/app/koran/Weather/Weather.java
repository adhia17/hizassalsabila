package com.app.koran.Weather;

/**
 * Created by MSI 95O on 5/6/2017.
 */

public class Weather {

    private String city;
    private double latitude;
    private double longitude;
    private double temperature;
    private double pressure;
    private double humidity;

    public Weather(String city, double latitude, double longitude, double temperature, double pressure, double humidity) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public Weather(String city, double latitude, double longitude) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
}
