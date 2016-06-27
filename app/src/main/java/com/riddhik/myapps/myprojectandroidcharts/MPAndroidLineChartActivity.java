package com.riddhik.myapps.myprojectandroidcharts;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.riddhik.myapps.myprojectandroidcharts.Model.HttpRequestResponse;
import com.riddhik.myapps.myprojectandroidcharts.Model.StockHistoryParcelable;
import com.riddhik.myapps.myprojectandroidcharts.Model.StockSyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MPAndroidLineChartActivity extends AppCompatActivity {
    //implements StockSyncTask.StockHistoryDataCallback {

    final String LOG_TAG = MPAndroidLineChartActivity.class.getSimpleName();

    //StockSyncTask stockSync;
    //ArrayList<StockHistoryParcelable> stockParcelable = null;
    LineChart lineChart;
    String quote = "YHOO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpandroid_line_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //GetChartData
//        try {
//            stockSync = new StockSyncTask(this, this);
//            stockSync.execute(quote);
//            //stockSync.getStockdataFromJson(quote);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            StockAsyncTask t1 = new StockAsyncTask(this);
            t1.execute(quote);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    @Override
//    public void onSuccess(ArrayList<StockHistoryParcelable> shp) {
//        Log.d(LOG_TAG, "rkakadia inside onSuccess()");
//        lineChart = (LineChart) findViewById(R.id.linechart);
//        stockParcelable = shp;
//
//        ArrayList<Entry> entries = new ArrayList<>();
//        ArrayList<String> labels = new ArrayList<>();
//
//        for (int i = 0; i < this.stockParcelable.size(); i++) {
//
//            StockHistoryParcelable stockParcel = this.stockParcelable.get(i);
//            String dateValue = formatDate(stockParcel.date);
//            //String dateValue = stockParcel.date;
//            Log.d(LOG_TAG, "rkakadia dateValue " + dateValue);
//            double closeValue = stockParcel.close;
//            Log.d(LOG_TAG, "rkakadia closeValue " + closeValue);
//
//            entries.add(new Entry((float) closeValue, i));
//            labels.add(dateValue);
//        }
//
//        LineDataSet dataset = new LineDataSet(entries, "Close Values");
//        dataset.setDrawCubic(true);
//        dataset.setDrawFilled(true);
//        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        LineData data = new LineData(labels, dataset);
//
//        lineChart.setDescription("LineChart for " + quote);
//        lineChart.setData(data); // set the data and list of lables into chart
//
//        Log.d(LOG_TAG, "rkakadia End of execution...");
//    }
//
//    @Override
//    public void onFailure() {
//        Log.d(LOG_TAG, "rkakadia inside onFailure()");
//
//        String errorMessage = "";
//        Toast.makeText(this, "Error in showing chart", Toast.LENGTH_LONG).show();
//    }

    public String formatDate(String dateData) {
        //Sample date: 20160428
        //Convert to: 2016-04-28
        StringBuilder sb = new StringBuilder(dateData);
        sb.insert(4, '-');
        sb.insert(7, '-');
        return sb.toString();
    }

    class StockAsyncTask extends AsyncTask<String, Void, ArrayList<StockHistoryParcelable>> {

        private final String LOG_TAG = StockAsyncTask.class.getSimpleName();

        final Context mContext;
        //ArrayList<StockHistoryParcelable> stockHistoryData;

        public StockAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected ArrayList<StockHistoryParcelable> doInBackground(String... params) {
            Log.d(LOG_TAG, "rkakadia inside doInBackground() method");
            ArrayList<StockHistoryParcelable> ArrayList_SHP = new ArrayList<>();

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
                ArrayList_SHP = getStockdataFromJson(jsonResponse);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "JSON Exception", e);
                return null;
            } finally {

            }
            return ArrayList_SHP;
        }

        public ArrayList<StockHistoryParcelable> getStockdataFromJson(String jsonResponseString) throws JSONException {
            Log.d(LOG_TAG, "rkakadia inside getStockdataFromJson() method");
            ArrayList<StockHistoryParcelable> ArrayList_SHB = new ArrayList<>();

            if (jsonResponseString != null && jsonResponseString.length() > 1) {
                Log.d(LOG_TAG, "rkakadia jsonResponseString len " + jsonResponseString.length());

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
                    ArrayList_SHB.add(new StockHistoryParcelable(date, close));
                }
            }

            return ArrayList_SHB;
        }

        protected void onPostExecute(ArrayList<StockHistoryParcelable> SHP) {
            Log.d(LOG_TAG, "Inside  onPostExecute ");
            ArrayList<StockHistoryParcelable> ArrayList_SHB = new ArrayList<>();
            ArrayList_SHB = SHP;

            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();

            for (int i = 0; i < ArrayList_SHB.size(); i++) {

                StockHistoryParcelable stockParcel = ArrayList_SHB.get(i);
                String dateValue = formatDate(stockParcel.date);
                //String dateValue = stockParcel.date;
                Log.d(LOG_TAG, "rkakadia dateValue " + dateValue);
                double closeValue = stockParcel.close;
                Log.d(LOG_TAG, "rkakadia closeValue " + closeValue);

                entries.add(new Entry((float) closeValue, i));
                labels.add(dateValue);
            }

            LineDataSet dataset = new LineDataSet(entries, "Close Values");
            dataset.setDrawCubic(true);
            dataset.setDrawFilled(true);
            dataset.setColors(ColorTemplate.COLORFUL_COLORS);
            LineData data = new LineData(labels, dataset);

            lineChart = (LineChart) findViewById(R.id.linechart);
            lineChart.setDescription("LineChart for " + quote);
            lineChart.setData(data); // set the data and list of lables into chart

            Log.d(LOG_TAG, "rkakadia End of execution...");
        }
    }
}
