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
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForcast extends AppCompatActivity {

    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forcast);

        ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);

    }

}
class ForecastQuery extends AsyncTask< String, Integer, String>{

    TextView currentTemp;
    TextView minTemp;
    TextView maxTemp;
    TextView uvRating;
    String weatherURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
    String uvURL = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";




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


            //From part 3, slide 20
            String parameter = null;

            int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

            while(eventType != XmlPullParser.END_DOCUMENT)
            {

                if(eventType == XmlPullParser.START_TAG)
                {
                    //If you get here, then you are pointing at a start tag
                    if(xpp.getName().equals("Temperature"))
                    {
                        //If you get here, then you are pointing to a <Weather> start tag
                        String value = xpp.getAttributeValue(null,    "value");
                        publishProgress(25);
                        String min = xpp.getAttributeValue(null,    "min");
                        publishProgress(50);
                        String max = xpp.getAttributeValue(null, "max");
                        publishProgress(75);
                        currentTemp.setText(value);
                        minTemp.setText(min);
                        maxTemp.setText(max);

                        Bitmap image = null;
                        String iconName = null;
                        String imgURL = "http://openweathermap.org/img/w/" + iconName + ".png";
                        URL img_url = new URL(imgURL);
                        HttpURLConnection connection = (HttpURLConnection) img_url.openConnection();
                        connection.connect();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == 200) {
                            image = BitmapFactory.decodeStream(connection.getInputStream());
                        }
                        publishProgress(100);

                        FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                        outputStream.flush();
                        outputStream.close();

                        FileInputStream fis = null;
                        try {    fis = openFileInput(image);   }
                        catch (FileNotFoundException e) {    e.printStackTrace();  }
                        Bitmap bm = BitmapFactory.decodeStream(fis);


                        //open the connection
                        URL uv_URL = new URL(uvURL);
                        HttpURLConnection uvConnection = (HttpURLConnection) uv_URL.openConnection();
                        uvConnection.connect();
                        //wait for data:
                        InputStream uvResponse = uvConnection.getInputStream();
                        //JSON reading:   Look at slide 26
                        //Build the entire string response:
                        BufferedReader reader = new BufferedReader(new InputStreamReader(uvResponse, "UTF-8"), 8);
                        StringBuilder sb = new StringBuilder();

                        String line = null;
                        while ((line = reader.readLine()) != null)
                        {
                            sb.append(line + "\n");
                        }
                        String result = sb.toString(); //result is the whole string

                        // convert string to JSON: Look at slide 27:
                        JSONObject uvReport = new JSONObject(result);

                        //get the double associated with "value"
                        double uvRating = uvReport.getDouble("value");

                        Log.i("MainActivity", "The uv is now: " + uvRating) ;


                    }

                    else if(xpp.getName().equals("AMessage"))
                    {
                        parameter = xpp.getAttributeValue(null, "message"); // this will run for <AMessage message="parameter" >
                    }
                    else if(xpp.getName().equals("Weather"))
                    {
                        parameter = xpp.getAttributeValue(null, "outlook"); //this will run for <Weather outlook="parameter"
                        parameter = xpp.getAttributeValue(null, "windy"); //this will run for <Weather windy="paramter"  >
                    }
                    else if(xpp.getName().equals("Temperature"))
                    {
                        xpp.next(); //move the pointer from the opening tag to the TEXT event
                        parameter = xpp.getText(); // this will return  20
                    }
                }
                eventType = xpp.next(); //move to the next xml event and store it in a variable
            }




        }
        catch (Exception e)
        {

        }


        return "Done";
    }

    //Type 2
    public void onProgressUpdate(Integer ... args)
    {

    }
    //Type3
    public void onPostExecute(String fromDoInBackground)
    {
        Log.i("HTTP", fromDoInBackground);
    }
}
