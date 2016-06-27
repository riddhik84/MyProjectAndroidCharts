package com.riddhik.myapps.myprojectandroidcharts.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.riddhik.myapps.myprojectandroidcharts.MPAndroidLineChartActivity;
import com.riddhik.myapps.myprojectandroidcharts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rkakadia on 6/27/2016.
 */
public class StockSyncTask extends AsyncTask<String, Void, ArrayList<StockHistoryParcelable>> {

    private final String LOG_TAG = StockSyncTask.class.getSimpleName();

    private final Context mContext;
    ArrayList<StockHistoryParcelable> stockHistoryData;
    StockHistoryDataCallback callback;

    public StockSyncTask(Context context, MPAndroidLineChartActivity object) {
        mContext = context;
        callback = object;
    }

    @Override
    protected ArrayList<StockHistoryParcelable> doInBackground(String... params) {
        Log.d(LOG_TAG, "rkakadia inside doInBackground() method");

        if (params.length == 0) {
            return null;
        }

        final String YAHOO_URL = "http://chartapi.finance.yahoo.com/instrument/1.0/";
        final String CHART_DATA_URL = "/chartdata;type=quote;range=1y/json";

        String stock_quote = params[0];

        String jsonResponse = "";

        try {

            String query = YAHOO_URL + stock_quote + CHART_DATA_URL;
            Log.d(LOG_TAG, "rkakadia Stock query = " + query);

            URL stockUrl = new URL(query);
            Log.d(LOG_TAG, "rkakadia Stock URL = " + stockUrl.toString());

            //Request/Response
            HttpRequestResponse hrr = new HttpRequestResponse();
            jsonResponse = hrr.doGetRequest(stockUrl.toString());
            Log.d(LOG_TAG, "rkakadia json response: " + jsonResponse);

            //Get data from Json and insert it in table
            getStockdataFromJson(jsonResponse);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON Exception", e);
            return null;
        } finally {

        }
        return null;
    }

    public ArrayList<StockHistoryParcelable> getStockdataFromJson(String jsonResponseString) throws JSONException {
        Log.d(LOG_TAG, "rkakadia inside getStockdataFromJson() method");

        if (jsonResponseString != null && jsonResponseString.length() > 1) {
            Log.d(LOG_TAG, "rkakadia jsonResponseString len " + jsonResponseString.length());

            stockHistoryData = new ArrayList<>();

            final String MAIN_TAG = "finance_charts_json_callback";
            final String SERIES = "series";
            final String DATE = "Date";
            final String CLOSE = "close";

            final String META = "meta";
            final String COMPANY_NAME = "Company-Name";

            String json_string = jsonResponseString.substring(jsonResponseString.indexOf("(") + 1, jsonResponseString.lastIndexOf(")"));
            JSONObject in = new JSONObject(json_string);

            JSONObject meta = in.getJSONObject(META);
            String company_name = meta.getString(COMPANY_NAME);
            Log.d(LOG_TAG, "rkakadia company name: " + company_name);

            JSONArray stockArray = in.getJSONArray(SERIES);
            Log.d(LOG_TAG, "rkakadia JSON array length " + stockArray.length());

            for (int i = 0; i < stockArray.length(); i += 30) {
                JSONObject stockEntry = stockArray.getJSONObject(i);
                String date = stockEntry.getString(DATE);
                double close = stockEntry.getDouble(CLOSE);
                stockHistoryData.add(new StockHistoryParcelable(date, close));
            }

            if (callback != null) {
                callback.onSuccess(stockHistoryData);
            }
        }

        return stockHistoryData;
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(ArrayList<StockHistoryParcelable> shb) {
        Log.d(LOG_TAG, "Inside  onPostExecute ");
    }

    public interface StockHistoryDataCallback {
        void onSuccess(ArrayList<StockHistoryParcelable> list);

        void onFailure();
    }
}