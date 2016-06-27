package com.riddhik.myapps.myprojectandroidcharts;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        Button MPAndroidChart = (Button) rootview.findViewById(R.id.mpandroidchart);
        Button MPAndroidLineChart = (Button) rootview.findViewById(R.id.mpandroidlinechart);

        Button Sample = (Button) rootview.findViewById(R.id.sample);


        MPAndroidChart.setOnClickListener(this);
        MPAndroidLineChart.setOnClickListener(this);
        Sample.setOnClickListener(this);

        return rootview;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mpandroidchart: {
                Intent MPAndroidChartIntent = new Intent(getActivity(), MPAndroidChartActivity.class);
                startActivity(MPAndroidChartIntent);
                break;
            }
            case R.id.mpandroidlinechart: {
                Intent MPAndroidLineChartIntent = new Intent(getActivity(), MPAndroidLineChartActivity.class);
                startActivity(MPAndroidLineChartIntent);
                break;
            }
            case R.id.sample: {
                Intent Sample = new Intent(getActivity(), SampleActivity.class);
                startActivity(Sample);
                break;
            }
        }

    }
}
