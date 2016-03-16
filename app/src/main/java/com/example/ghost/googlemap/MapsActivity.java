package com.example.ghost.googlemap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.location.LocationListener;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements LocationListener, View.OnClickListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private TextView tvLatidue, tvLongitude, tvAltitude;
    private String provider;
    private Button btnSatelite, btnHybrid, btnNormal, btnFind;
    private String loc;

    Geocoder geocoder;
    Timer timer;
    LocationListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_main);

        btnSatelite = (Button) findViewById(R.id.btnSatellite);
        btnHybrid = (Button) findViewById(R.id.btnHybrid);
        btnNormal = (Button) findViewById(R.id.btnNormal);
        btnFind = (Button) findViewById(R.id.btnFind);


        btnHybrid.setOnClickListener(this);
        btnSatelite.setOnClickListener(this);
        btnNormal.setOnClickListener(this);
        btnFind.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Log.d("Map", "provider : " + provider + " selected");
            onLocationChanged(location);

        } else {
            Log.d("Map", "Location==null");
        }

        showCurrentLocation();
        setUpMapIfNeeded();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    //public void onMapReady(GoogleMap googleMap) {
    //  mMap = googleMap;

    // Add a marker in Sydney and move the camera
    //  LatLng sydney = new LatLng(-34, 151);
    // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    //}
    protected void showCurrentLocation() {
        geocoder = new Geocoder(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100, this);

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000,
                100,
                this
        );

    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();


            if (mMap != null) {
                setUpMap();
            }

        }
    }

    private void setUpMap() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //      Location location = locationManager.getLastKnownLocation(provider);
//        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Marker"));
    }

    @Override
    public void onLocationChanged(Location location) {
        tvLongitude = (TextView) findViewById(R.id.textView);
        tvLatidue = (TextView) findViewById(R.id.textView2);
       // tvAltitude = (TextView) findViewById(R.id.textView3);

        tvLongitude.setText(location.getLongitude() + "");
        tvLatidue.setText(location.getLatitude() + "");
       // tvAltitude.setText(location.getAltitude() + "");
        setMarker(new LatLng(location.getLatitude(), location.getLongitude()), "marker");

    }

    public void setMarker(LatLng latlng, String title) {


        if (mMap != null) {


            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            String country = "";

            if (location != null) {


                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);


                    for (Address address : addresses) {


                        Marker hamburg = mMap.addMarker(new MarkerOptions().position(latlng).snippet(address.getAddressLine(0) + "," + address.getCountryName().toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)).flat(true)
                                .title("I'm Here"));

                        // tvAltitude.append(country+=addresses.get(0).getCountryName().toString());


                    }

                } catch (IOException e) {
                    Log.e("LocateMe", "Could not get Geocoder data", e);
                }
            } else {
                AlertDialog.Builder alertbox1 = new AlertDialog.Builder(MapsActivity.this);
                alertbox1.setMessage("No GPS or network ..Signal please fill the location manually!");
                alertbox1.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                alertbox1.show();
            }


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                Toast.makeText(MapsActivity.this, "Tipe Hybrid", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btnSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                Toast.makeText(MapsActivity.this, "Tipe Satelite", Toast.LENGTH_SHORT).show();

                break;

            case R.id.btnNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                Toast.makeText(MapsActivity.this, "Tipe Normal", Toast.LENGTH_SHORT).show();

                break;

            case R.id.btnFind:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
                alertDialog.setTitle("Find");
                alertDialog.setMessage("Enter Location");

                final EditText input = new EditText(MapsActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                //alertDialog.setIcon(R.drawable.key);

                alertDialog.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                loc = input.getText().toString();


                                Criteria criteria = new Criteria();
                                criteria.setAccuracy(Criteria.ACCURACY_FINE);

                                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                               // Location lat = locationManager.getLastKnownLocation(provider);

                                if(mMap!=null) {



                                        try {
                                            List<Address> addresses = geocoder.getFromLocationName(loc, 50);


                                            Address location = addresses.get(0);
                                            location.getLatitude();
                                            location.getLongitude();
                                            tvAltitude = (TextView) findViewById(R.id.textView3);

                                            tvAltitude.setText(location.getCountryName());

                                            if (location != null){

                                            LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());





                                                mMap.addMarker(
                                                        new MarkerOptions()
                                                                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                                .snippet("Hello World!")
                                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
                                                        .flat(true)
                                                        .title("I'm here!"));

                                                // tvAltitude.append(country+=addresses.get(0).getCountryName().toString());

                                                mMap.setMyLocationEnabled(true);
                                                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));


                                            }else{
                                                AlertDialog.Builder alertbox1 = new AlertDialog.Builder(MapsActivity.this);
                                                alertbox1.setMessage("No GPS or network ..Signal please fill the location manually!");
                                                alertbox1.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                    }
                                                });
                                                alertbox1.show();
                                            }


                                        } catch (IOException e) {
                                            Log.e("Location", "Location not found", e);
                                        }
                                }


                            }
                        });

                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();



                break;

        }


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
