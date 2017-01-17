package com.example.gabrielnicolae.weatherapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DateFormat;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.Date;
import java.net.*;
import java.io.*;

import data.CityPreference;
import data.JSONWeatherParser;
import data.WeatherHTTPClient;
import model.Weather;
import utils.InsertRequest;
import utils.SelectRequest;
import utils.Utils;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView temp;
    private ImageView iconView;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;
    private TextView graphLink;

    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cityName = (TextView) findViewById(R.id.cityText);
        iconView = (ImageView) findViewById(R.id.Icon);
        temp = (TextView) findViewById(R.id.tempText);
        description = (TextView) findViewById(R.id.cloudText);
        humidity = (TextView) findViewById(R.id.humText);
        pressure = (TextView) findViewById(R.id.presText);
        wind = (TextView) findViewById(R.id.windText);
        sunrise = (TextView) findViewById(R.id.riseText);
        sunset = (TextView) findViewById(R.id.sunsetText);
        updated = (TextView) findViewById(R.id.updateText);

        CityPreference cityPreference = new CityPreference(MainActivity.this);
        renderWeatherData(cityPreference.getCity());

        graphLink = (TextView) findViewById(R.id.graph);

        graphLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent graphIntent = new Intent(MainActivity.this, GraphActivity.class);
                MainActivity.this.startActivity(graphIntent);
            }
        });

    }

    public void renderWeatherData(String city){

        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&units=metric"});
    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iconView.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImage(params[0]);
        }

        private Bitmap downloadImage(String code) {
            int respnseCode = -1;
            Bitmap bitmap = null;
            try {
                //HttpURLConnection con = (HttpURLConnection) (new URL(Utils.ICON_URL + code + ".png")).openConnection();
                HttpURLConnection con = (HttpURLConnection) (new URL("https://static.pexels.com/photos/54200/pexels-photo-54200.jpeg")).openConnection();
                con.setDoInput(true);
                con.connect();

                respnseCode = con.getResponseCode();
                if(respnseCode == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = con.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                }
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class WeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {

            // instantiate http client
            String data = ((new WeatherHTTPClient()).getWeatherData(params[0]));
            weather.iconData = weather.currentCondition.getIcon();
            weather = JSONWeatherParser.getWeather(data);

            Log.v("Data: ", weather.currentCondition.getDescription());

            new DownloadImageAsyncTask().execute(weather.iconData);
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {

            super.onPostExecute(weather);

            Calendar c = Calendar.getInstance();
            int seconds = c.get(Calendar.SECOND);
            int minutes = c.get(Calendar.MINUTE);
            int hour = c.get(Calendar.HOUR);
            int day = c.get(Calendar.DAY_OF_WEEK);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            //SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy, hh:mm");
            DateFormat df = DateFormat.getTimeInstance();
            String sunriseDate = df.format(new Date(weather.location.getSunrise()));
            String sunsetDate = df.format(new Date(weather.location.getSunset()));

            DecimalFormat decimalFormat = new DecimalFormat("#.#");

            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());

            cityName.setText(weather.location.getCity() + ", " + weather.location.getCountry());
            temp.setText("" + tempFormat + "°C");
            humidity.setText("Umiditate: " + weather.currentCondition.getHumidity() + "%");
            pressure.setText("Presiune: " + weather.currentCondition.getPressure() + "hPa");
            wind.setText("Vânt: " + weather.wind.getSpeed() + "mps");
            sunrise.setText("Răsărit: " + sunriseDate);
            sunset.setText("Apus: " +  sunsetDate);
            updated.setText("Ultimul update: " + hour + ":" + minutes + ":" + seconds);
            description.setText("Condiții: " + weather.currentCondition.getCondition() + " (" +
                weather.currentCondition.getDescription() + ")");

            if(weather.location.getCity().equalsIgnoreCase("bucharest")){
                graphLink.setVisibility(View.VISIBLE);
            } else {
                graphLink.setVisibility(View.INVISIBLE);
            }


            if(weather.location.getCity().equalsIgnoreCase("bucharest")) {
                Response.Listener<String> responseStringListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean succes = jsonObject.getBoolean("succes");

                            if (succes) {
                                ;
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Inserare eșuată ... ").setNegativeButton("Din nou", null).create().show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                InsertRequest insertRequest = new InsertRequest(weather.currentCondition.getTemperature(), weather.currentCondition.getHumidity(), weather.wind.getSpeed(), weather.currentCondition.getPressure(), responseStringListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(insertRequest);

            }

        }
    }

    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Schimbă orașul");

        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Bucharest,ro");
        builder.setView(cityInput);
        builder.setPositiveButton("Alege", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                CityPreference cityPreference = new CityPreference(MainActivity.this);
                cityPreference.setCity(cityInput.getText().toString());

                String newCity = cityPreference.getCity();

                renderWeatherData(newCity);
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_locationID) {
            showInputDialog();
        }

        return super.onOptionsItemSelected(item);
    }
}
