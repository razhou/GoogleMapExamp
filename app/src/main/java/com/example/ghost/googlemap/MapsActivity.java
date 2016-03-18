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
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.text.TextWatcher;

public class MapsActivity extends FragmentActivity implements LocationListener, View.OnClickListener, TextWatcher {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private TextView tvLatidue, tvLongitude, tvAltitude;
    private String provider;
    private Button btnSatelite, btnHybrid, btnNormal, btnFind;
    private String loc;
    private EditText search;
    // String names[] ={"A","B","C","D"};
    int maxResults = 5;
    ListView lv_search;


    private ListAdapter adapter;

    Geocoder geocoder;
    Timer timer;
    LocationListener listener;

   //ListAdd addlist = new ListAdd();
    //coba variabel = new coba();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_main);

        btnSatelite = (Button) findViewById(R.id.btnSatellite);
        btnHybrid = (Button) findViewById(R.id.btnHybrid);
        btnNormal = (Button) findViewById(R.id.btnNormal);
        btnFind = (Button) findViewById(R.id.btnFind);
        search = (EditText) findViewById(R.id.edtSrc);
        lv_search = (ListView) findViewById(R.id.lv_src);


        btnHybrid.setOnClickListener(this);
        btnSatelite.setOnClickListener(this);
        btnNormal.setOnClickListener(this);
        btnFind.setOnClickListener(this);

        search.addTextChangedListener(this);


        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // lv_search.setVisibility(View.VISIBLE);

                return false;
            }
        });


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

                //LayoutInflater inflater = getLayoutInflater();
                //View alertlayout = inflater.inflate(R.layout.find_location, null);
                //AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
                //alertDialog.setTitle("Find");
                //alertDialog.setMessage("Enter Location");

                // search = (EditText)alertlayout.findViewById(R.id.edtLoc);

                // final int maxResults = 5;


                //final ListView loct = (ListView)alertlayout.findViewById(R.id.lv_loc);

                //final List<String> locationNameList;

                //  locationNameList = new ArrayList<String>();

                //  adapter = new ArrayAdapter<String>(MapsActivity.this, android.R.layout.simple_spinner_item,locationNameList);
                // loct.setAdapter(adapter);


                // final EditText input = new EditText(MapsActivity.this);
                //final ListView search = new ListView(MapsActivity.this);
                // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                //       LinearLayout.LayoutParams.MATCH_PARENT,
                //     LinearLayout.LayoutParams.MATCH_PARENT);
                //input.setLayoutParams(lp);
                // alertDialog.setView(alertlayout);
                //alertDialog.setView(search);

                //alertDialog.setIcon(R.drawable.key);


                //Criteria criteria = new Criteria();
                //criteria.setAccuracy(Criteria.ACCURACY_FINE);

                //   if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //  return;
                //  }
                // Location lat = locationManager.getLastKnownLocation(provider);

                //  if(mMap!=null) {


                // if (loc==null){
                //  Toast.makeText(MapsActivity.this,"Please Insert Your Location",Toast.LENGTH_SHORT).show();
                // }else {


                // try {
                //  final List<Address> addresses = geocoder.getFromLocationName(loc, maxResults);


                //  Address location = addresses.get(0);
                //  location.getLatitude();
                // location.getLongitude();
                // tvAltitude = (TextView) findViewById(R.id.textView3);

                // tvAltitude.setText(location.getCountryName());


                // if (location != null) {

                // input.setOnTouchListener(new View.OnTouchListener() {
                // @Override
                //public boolean onTouch(View v, MotionEvent event) {
                // / locationNameList.clear();

                // for (Address i : addresses) {
                //      if (i.getFeatureName() == null) {
                //  locationNameList.add("Unknown");
                //  } else {
                //   locationNameList.add(i.getFeatureName());
                // }
                // }

                //adapter.notifyDataSetChanged();


                //  return false;
                //  }
                // });


                //LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());


                //mMap.addMarker(
                // new MarkerOptions()
                // .position(new LatLng(location.getLatitude(), location.getLongitude()))
                // .snippet(location.getAddressLine(0) + "," + location.getCountryName())
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
                //.flat(true)
                //.title("Your Destination"));

                // tvAltitude.append(country+=addresses.get(0).getCountryName().toString());

                // mMap.setMyLocationEnabled(true);
                // mMap.getUiSettings().setMyLocationButtonEnabled(true);
                // mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                // mMap.animateCamera(CameraUpdateFactory.zoomTo(10));


                //} else {
                // AlertDialog.Builder alertbox1 = new AlertDialog.Builder(MapsActivity.this);
                //  alertbox1.setMessage("No GPS or network ..Signal please fill the location manually!");
                // alertbox1.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                // public void onClick(DialogInterface arg0, int arg1) {
                //  }
                // });
                //alertbox1.show();
                // }


                // } catch (IOException e) {
                //  Log.e("Location", "Location not found", e);
                // }
                // }
                //}


                //alertDialog.setPositiveButton("Ok",
                // new DialogInterface.OnClickListener() {
                //   public void onClick(DialogInterface dialog, int which) {


                // }
                // });

                // alertDialog.setNegativeButton("Cancel",
                //  new DialogInterface.OnClickListener() {
                //  public void onClick(DialogInterface dialog, int which) {
                //  dialog.cancel();
                // }
                //  });


                //alertDialog.show();


                //  break;

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


    public void Address(String InAdd) {
        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String, String>>();

        String [] from ={"feature","address"};
        lv_search = (ListView) findViewById(R.id.lv_src);

        List<String> locationNameList;

        locationNameList = new ArrayList<String>();

        adapter = new SimpleAdapter(this,list,R.layout.find_location,from,new int[]{R.id.txtFeature,R.id.txtAddress});
        lv_search.setAdapter(adapter);



        try {
            List<Address> addresses = geocoder.getFromLocationName(InAdd, maxResults);

            if ((addresses == null) || (addresses.isEmpty())) {
                lv_search.setVisibility(View.GONE);
                Toast.makeText(MapsActivity.this, "Location Unknown", Toast.LENGTH_SHORT).show();
            } else {
                Address location = addresses.get(0);
                location.getLatitude();
                location.getLongitude();
                tvAltitude = (TextView) findViewById(R.id.textView3);

                tvAltitude.setText(location.getCountryName());


                if (location != null) {

                    // input.setOnTouchListener(new View.OnTouchListener() {
                    // @Override
                    // public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(MapsActivity.this, "Found : " + addresses.size(), Toast.LENGTH_SHORT).show();

                    locationNameList.clear();

                    for (Address i : addresses) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        if (i.getFeatureName() == null) {
                            //locationNameList.add("Unknown");
                            item.put("feature","Not Found");
                            item.put("address","Not Found");
                        } else {

                           // locationNameList.add(i.getFeatureName());
                            item.put("feature",i.getFeatureName());
                            item.put("address",i.getAddressLine(0));

                        }
                        list.add(item);
                    }

                   // adapter.notifyDataSetChanged();

                    lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            lv_search.setVisibility(View.GONE);
                            HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getItem(position);
                            //String val = (String) lv_search.getItemAtPosition(position);

                            String result = (String)obj.get("feature");

                            try {
                                List<Address> locationList = geocoder.getFromLocationName(result, 1);
                                Address locList = locationList.get(0);

                                LatLng currentPosition = new LatLng(locList.getLatitude(), locList.getLongitude());
                                mMap.addMarker(
                                        new MarkerOptions()
                                                .position(new LatLng(locList.getLatitude(), locList.getLongitude()))
                                                .snippet(locList.getAddressLine(0) + "," + locList.getCountryName())
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
                                                .flat(true)
                                                .title("Your Destination"));

                                // tvAltitude.append(country+=addresses.get(0).getCountryName().toString());

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
                                mMap.setMyLocationEnabled(true);
                                 mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                 mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                                 mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
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
                    AlertDialog.Builder alertbox1 = new AlertDialog.Builder(MapsActivity.this);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        loc = search.getText().toString();
        lv_search = (ListView)findViewById(R.id.lv_src);
        //variabel.setLv_search((ListView)findViewById(R.id.lv_src));
        if((loc==null)||(loc=="")){
            lv_search.setVisibility(View.GONE);
            //variabel.getLv_search().setVisibility(View.GONE);

        }else{
           lv_search.setVisibility(View.VISIBLE);
            //variabel.getLv_search().setVisibility(View.VISIBLE);
            Address(loc);

        }










    }
}
