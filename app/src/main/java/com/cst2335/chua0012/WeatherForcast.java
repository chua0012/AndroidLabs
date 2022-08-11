package com.cst2335.chua0012;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WeatherForcast extends AppCompatActivity {

    ImageView imageView;
    TextView currentTemp;
    TextView minTemp;
    TextView maxTemp;
    TextView uvRating;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forcast);


    }
}