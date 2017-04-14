package com.example.user.auca;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.textView);
        new Task().execute("Bitch","en-ru");

    }

    class Task extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params) {
            String urlRequest = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170411T212807Z.453f1236ba984a10.1c90eef56783085c1db1dfde89ed1e15f31926ef";
            String inputText =  params[0];
            String lang = params[1];
            String outputText = "";
            String jsonResponse = "";
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = null;
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                if(urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
            }catch (IOException e) {
                e.printStackTrace();
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            String text = "";
            JSONObject baseJsonResponse = null;
            try {
                baseJsonResponse = new JSONObject(jsonResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray textArray = null;
            try {
                textArray = baseJsonResponse.getJSONArray("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                text = textArray.getString(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(jsonResponse);
            tv.setText(text);
        }
    }
}
