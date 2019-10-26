package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    // Will contain the raw JSON response as a string.
    String data = null;

    ProgressBar progressBar;
    TextView textView1,textView2,textView3,textViewStuId1,textViewStuId2,textViewStuId3;
    ImageView imgView1,imgView2,imgView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        new JSONRequest().execute();
    }

    private void findViews(){
        progressBar=findViewById(R.id.progressBar);
        textView1=findViewById(R.id.textViewName1);
        textView2=findViewById(R.id.textViewName2);
        textView3=findViewById(R.id.textViewName3);
        textViewStuId1=findViewById(R.id.textViewStudentID1);
        textViewStuId2=findViewById(R.id.textViewStudentID2);
        textViewStuId3=findViewById(R.id.textViewStudentID3);
        imgView1=findViewById(R.id.imageView1);
        imgView2=findViewById(R.id.imageView2);
        imgView3=findViewById(R.id.imageView3);
    }


    private class  JSONRequest extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("https://api.myjson.com/bins/zjv68");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    data = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    data = null;
                }
                data = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                data = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            //Read JSON
            try {
                JSONObject returnedData = new JSONObject(data);
                JSONArray studentArray = returnedData.getJSONArray("students");

                JSONObject student1 =studentArray.getJSONObject(0);
                String name1=student1.getString("studentName");
                textView1.setText(name1);
                String id1=student1.getString("studentID");
                textViewStuId1.setText(id1);
                String url1=student1.getString("studentPhoto");
                Picasso.get().load(url1).into(imgView1);

                JSONObject student2 =studentArray.getJSONObject(1);
                String name2=student2.getString("studentName");
                textView2.setText(name2);
                String id2=student2.getString("studentID");
                textViewStuId2.setText(id2);
                String url2=student2.getString("studentPhoto");
                Picasso.get().load(url2).into(imgView2);

                JSONObject student3 =studentArray.getJSONObject(2);
                String name3=student3.getString("studentName");
                textView3.setText(name3);
                String id3=student3.getString("studentID");
                textViewStuId3.setText(id3);
                String url3=student3.getString("studentPhoto");
                Picasso.get().load(url3).into(imgView3);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
