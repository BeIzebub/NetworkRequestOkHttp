package com.kg.networkrequest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Request request = new Request.Builder()
//                .url("https://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22")
//                .build();

        HttpUrl.Builder builder= HttpUrl.parse("https://samples.openweathermap.org/data/2.5/weather").newBuilder();
        builder.addQueryParameter("lat", "40");
        builder.addQueryParameter("lon", "43");
        builder.addQueryParameter("appid", "b6907d289e10d714a6e88b30761fae22");

        Request request = new Request.Builder()
                .url(builder.build().toString())
                .build();

        // With AsyncTask
        new WeatherTask().execute(request);

        // OkHttp Abstract version
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d("NETWORK", "onPostExecute: " +
                        response.body().string());
            }
        });
    }

    class WeatherTask extends AsyncTask<Request, Void, Response> {

        @Override
        protected Response doInBackground(Request... requests) {
            try {
                return client.newCall(requests[0]).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Response response) {
            if( response == null) {
                // fail
            } else {
                try {
                    Log.d("NETWORK", "onPostExecute: " +
                            response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
