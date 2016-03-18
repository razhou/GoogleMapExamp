package com.example.ghost.googlemap;

import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;

/**
 * Created by ghost on 17/03/2016.
 */
public class coba extends FragmentActivity {
    ListView lv_search = (ListView)findViewById(R.id.lv_src);
    ArrayAdapter<String> adap;
    Geocoder geocoder;
    GoogleMap mMap;
    int maxResults = 5;
    TextView tvAltitude = (TextView)findViewById(R.id.textView3);

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public ListView getLv_search() {
        return lv_search;
    }

    public void setLv_search(ListView lv_search) {
        this.lv_search = lv_search;
    }

    public ArrayAdapter<String> getAdap() {
        return adap;
    }

    public void setAdap(ArrayAdapter<String> adap) {
        this.adap = adap;
    }

    public Geocoder getGeocoder() {
        return geocoder;
    }

    public void setGeocoder(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public TextView getTvAltitude() {
        return tvAltitude;
    }

    public void setTvAltitude(TextView tvAltitude) {
        this.tvAltitude = tvAltitude;
    }


    public void setAdap(ListAdd listAdd, int simple_spinner_item, List<String> locationNameList) {
    }
}
