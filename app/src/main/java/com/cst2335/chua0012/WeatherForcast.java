package com.cst2335.chua0012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        currentTemp =  findViewById(R.id.currentTemp);
        maxTemp =  findViewById(R.id.maxTemp);
        minTemp = findViewById(R.id.minTemp);
        uvRating =  findViewById(R.id.UVRating);
        imageView = findViewById(R.id.imageView);

        String weatherURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
        String uvURL = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";
        // String imgURL = "http://openweathermap.org/img/w/";

        ForecastQuery req = new ForecastQuery();
        req.execute(weatherURL,uvURL);  //Type 1
        //req.onPostExecute(uvURL);
        // req.onPostExecute(imgURL);

    }


    class ForecastQuery extends AsyncTask< String, Integer, String>{

        String currentTemp2;
        String minTemp2;
        String maxTemp2;
        String uvRating2;
        Bitmap image;
        //String weatherURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
        //String uvURL = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";


        //Type3                     Type1
        public String doInBackground(String ... args)
        {

            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");


                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

            /*URL weather_URL = new URL(weatherURL);
            HttpURLConnection weatherConnection = (HttpURLConnection) weather_URL.openConnection();
            weatherConnection.connect();

            //wait for data:
            InputStream Response = urlConnection.getInputStream();*/

                while(eventType != XmlPullParser.END_DOCUMENT)
                {

                    if(eventType == XmlPullParser.START_TAG) {
                        //If you get here, then you are pointing at a start tag
                        if (xpp.getName().equals("temperature")) {
                            //If you get here, then you are pointing to a <Weather> start tag
                            currentTemp2 = xpp.getAttributeValue(null, "value");
                            progressBar.setProgress(25);
                            minTemp2 = xpp.getAttributeValue(null, "min");
                            progressBar.setProgress(50);
                            maxTemp2 = xpp.getAttributeValue(null, "max");
                            progressBar.setProgress(75);
                            //currentTemp.setText(value);
                            //minTemp.setText(min);
                            //maxTemp.setText(max);

                        }

                        else if(xpp.getName().equals("weather"))

                        {
                            String iconName = xpp.getAttributeValue(null, "icon");

                            if (fileExistance(iconName + ".png")) { //if image already exists
                                FileInputStream fis = null;
                                try {
                                    fis = openFileInput(iconName + ".png");
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                image = BitmapFactory.decodeStream(fis);


                            } else {
                                URL imgurl = null; //the following code is from instructions
                                try {
                                    imgurl = new URL("http://openweathermap.org/img/w/" +
                                            iconName + ".png");
                                    HttpURLConnection connection = (HttpURLConnection) imgurl.openConnection(); // to download the image.
                                    connection.connect();
                                    int responseCode = connection.getResponseCode();
                                    if (responseCode == 200) {
                                        image = BitmapFactory.decodeStream(connection.getInputStream());

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                FileOutputStream outputStream = null; //the following code is from lab instructions
                                try {
                                    outputStream = openFileOutput(iconName + ".png",
                                            Context.MODE_PRIVATE);
                                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                            }
                            Log.i("HTTP", "the file " + iconName + "exists");

                        }

                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

                URL jsonURL = new URL(args[1]); //[1]
                //open the connection
                HttpURLConnection jsonConnection = (HttpURLConnection) jsonURL.openConnection();

                //wait for data:
                InputStream jsonResponse = jsonConnection.getInputStream();


                BufferedReader reader = new BufferedReader(new InputStreamReader(jsonResponse, "UTF-8"), 8); //[1]
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON
                JSONObject uvReport = new JSONObject(result); //[1]

                //get the double associated with "value"
                uvRating2 = uvReport.getString("value"); //[1] UV value from second URl.

                Log.i("WeatherForecast", "The uv is now: " + uvRating2);


            }

            catch (Exception e)
            {

            }
            progressBar.setProgress(100);

            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0); //bar is set back to 0.
        }

        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            Log.i("HTTP", fromDoInBackground);
            currentTemp.setText("Current Temperature is " + currentTemp2);
            minTemp.setText("Current temperature minimum value is " + minTemp2);
            maxTemp.setText("Current temperature maximum value is " + maxTemp2);
            uvRating.setText("Current UV rating is " + uvRating2);
            imageView.setImageBitmap(image);
            progressBar.setVisibility(View.INVISIBLE);
        }

        public boolean fileExistance(String fname) { //method to check if image is already present. [1]
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

    }

}

