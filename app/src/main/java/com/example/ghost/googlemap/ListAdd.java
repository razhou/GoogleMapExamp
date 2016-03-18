package com.example.ghost.googlemap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghost on 17/03/2016.
 */
public class ListAdd extends FragmentActivity implements LocationListener {
    coba cb = new coba();

    public void Address(String InAdd) {

        //LayoutInflater inflater = getLayoutInflater();
        //View alrtlayout = inflater.inflate(R.layout.find_location, null);

        // AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
        //alertDialog.setTitle("Find");
        // alertDialog.setMessage("Enter Location");

        //final int maxResults = 5;
        // final EditText input = (EditText)alertlayout.findViewById(R.id.edtLoc);


        ///search.addTextChangedListener(this);
       // cb.setLv_search((ListView) findViewById(R.id.lv_src));
        //cb.setTvAltitude((TextView)findViewById(R.id.textView3));

        List<String> locationNameList;

        locationNameList = new ArrayList<String>();


        try {
            List<Address> addresses =cb.geocoder.getFromLocationName(InAdd,cb.getMaxResults());

            if ((addresses == null) || (addresses.isEmpty())) {
                cb.getLv_search().setVisibility(View.GONE);
                Toast.makeText(ListAdd.this, "Location Unknown", Toast.LENGTH_SHORT).show();
            } else {
                Address location = addresses.get(0);
                location.getLatitude();
                location.getLongitude();
                /// cb.setTvAltitude((TextView) findViewById(R.id.textView3));
                cb.getTvAltitude().setText(location.getCountryName());

                if (location != null) {

                    // input.setOnTouchListener(new View.OnTouchListener() {
                    // @Override
                    // public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(ListAdd.this, "Found : " + addresses.size(), Toast.LENGTH_SHORT).show();

                    locationNameList.clear();

                    for (Address i : addresses) {
                        if (i.getFeatureName() == null) {
                            locationNameList.add("Unknown");
                        } else {
                            locationNameList.add(i.getFeatureName());

                        }
                    }

                    cb.getAdap().notifyDataSetChanged();

                    cb.lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            cb.lv_search.setVisibility(View.GONE);
                            String val = (String) cb.lv_search.getItemAtPosition(position);

                            try {
                                List<Address> locationList = cb.geocoder.getFromLocationName(val, 1);
                                Address locList = locationList.get(0);

                                LatLng currentPosition = new LatLng(locList.getLatitude(), locList.getLongitude());
                               cb.mMap.addMarker(
                                        new MarkerOptions()
                                                .position(new LatLng(locList.getLatitude(), locList.getLongitude()))
                                                .snippet(locList.getAddressLine(0) + "," + locList.getCountryName())
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
                                                .flat(true)
                                                .title("Your Destination"));

                                // tvAltitude.append(country+=addresses.get(0).getCountryName().toString());

                                if (ActivityCompat.checkSelfPermission(ListAdd.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ListAdd.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                if (ActivityCompat.checkSelfPermission(ListAdd.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ListAdd.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                cb.mMap.setMyLocationEnabled(true);
                                cb.mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                cb.mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                               cb.mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    });

                    //  return false;
                    //  }
                    // });


                    //


                } else {
                    AlertDialog.Builder alertbox1 = new AlertDialog.Builder(ListAdd.this);
                    alertbox1.setMessage("No GPS or network ..Signal please fill the location manually!");
                    alertbox1.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
                    alertbox1.show();
                }
            }

        }catch(IOException e){
            Log.e("Location", "Location not found", e);
        }



    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
