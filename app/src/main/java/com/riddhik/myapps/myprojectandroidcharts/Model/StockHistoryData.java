package com.riddhik.myapps.myprojectandroidcharts.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rkakadia on 6/27/2016.
 */
public class StockHistoryData implements Parcelable {

    public String date;
    public double close;

    public final Parcelable.Creator<StockHistoryData> CREATOR = new Parcelable.Creator<StockHistoryData>() {
        @Override
        public StockHistoryData createFromParcel(Parcel parcel) {
            return new StockHistoryData(parcel);
        }

        @Override
        public StockHistoryData[] newArray(int size) {
            return new StockHistoryData[size];
        }
    };

    public StockHistoryData(String _date, double _close) {
        date = _date;
        close = _close;
    }

    private StockHistoryData(Parcel in) {
        date = in.readString();
        close = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(date);
        parcel.writeDouble(close);
    }
}
