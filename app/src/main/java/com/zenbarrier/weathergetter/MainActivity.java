package com.zenbarrier.weathergetter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    public void getWeather(View view){
        WeatherTask weatherTask = new WeatherTask();
        EditText cityText = (EditText)findViewById(R.id.cityEditText);

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityText.getWindowToken(),0);

        String location = cityText.getText().toString().trim();
        try {
            location = URLEncoder.encode(location, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=";
        String apiKey = "&appid="+getResources().getString(R.string.open_weather_api_key);
        String query = apiUrl+location+apiKey;

        weatherTask.execute(query);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText cityText = (EditText)findViewById(R.id.cityEditText);
        cityText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    getWeather(cityText);
                    return true;
                }
                return false;
            }
        });
    }

    class WeatherTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while(data != -1){
                    result += (char)data;
                    data = reader.read();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Result",s);
            TextView resultView = (TextView)findViewById(R.id.resultTextView);
            resultView.setText("");
            String resultText = "";

            try {
                JSONObject jsonObject = new JSONObject(s);
                resultText += jsonObject.getString("name")+"\n";
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                for(int i = 0 ; i < jsonArray.length() ; i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    String main = object.getString("main");
                    String description = object.getString("description");
                    resultText += String.format("%s: %s\n",main,description);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            resultView.setText(resultText);
        }
    }
}
