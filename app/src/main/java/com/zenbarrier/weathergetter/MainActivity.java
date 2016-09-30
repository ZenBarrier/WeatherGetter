package com.zenbarrier.weathergetter;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public void getWeather(View view){
        WeatherTask weatherTask = new WeatherTask();
        EditText cityText = (EditText)findViewById(R.id.cityEditText);

        String location = cityText.getText().toString().trim();

        String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=";
        String apiKey = "&appid="+getResources().getString(R.string.open_weather_api_key);
        String query = apiUrl+location+apiKey;

        weatherTask.execute(query);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    class WeatherTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }
}
