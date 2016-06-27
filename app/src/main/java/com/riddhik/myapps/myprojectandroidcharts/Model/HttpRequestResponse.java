package com.riddhik.myapps.myprojectandroidcharts.Model;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rkakadia on 6/27/2016.
 */

public class HttpRequestResponse {

    public OkHttpClient client = new OkHttpClient();

    public String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
        return response.body().string().toString();  //use string() to get proper response format
    }
}
