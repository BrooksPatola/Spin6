package com.example.brooksyp.spin6;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LayoutInflater Minflater;

    List<Station> stations = new ArrayList<Station>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng toronto = new LatLng(43.642604, -79.387117);
        mMap.addMarker(new MarkerOptions().position(toronto).title("Marker in Toronto"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto, 16));


        new JSONParser().execute();
        mMap.setInfoWindowAdapter(new BikeShareWindowAdapter());
    }

    public class JSONParser extends AsyncTask<Void, Void, Void> {

        int numStations;
        String[] lat;
        String[] lng;
        String[] stationName;
        String[] bikesAv;
        String[] docksAv;
        String[] totalDocks;
        String[] inService;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MapsActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {


            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://feeds.bikesharetoronto.com/stations/stations.json";
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray StationBeanList = jsonObj.getJSONArray("stationBeanList");

                    numStations = StationBeanList.length();
                    lat = new String[numStations];
                    lng = new String[numStations];
                    stationName = new String[numStations];
                    bikesAv = new String[numStations];
                    docksAv = new String[numStations];
                    totalDocks = new String[numStations];
                    inService =  new String[numStations];
                                    //stations = new ArrayList<Station>();

                    // looping through all stations
                    for (int i = 0; i < StationBeanList.length(); i++) {

                        JSONObject mStation = StationBeanList.getJSONObject(i);

                        //String id = mStation.getString("id");
                        String StationName = mStation.getString("stationName");
                        String latitude = mStation.getString("latitude");
                        String longitude = mStation.getString("longitude");
                        String docksAvail = mStation.getString("availableDocks");
                        String bikesAvail = mStation.getString("availableBikes");
                        String totalDocksAvail = mStation.getString("totalDocks");
                        String inServ = mStation.getString("statusValue");

                        stationName[i] = StationName;
                        lat[i] = latitude;
                        lng[i] = longitude;
                        docksAv[i] = docksAvail;
                        bikesAv[i] = bikesAvail;
                        totalDocks[i] = totalDocksAvail;
                        inService[i] = inServ;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            int myImage;

            for (int i = 0; i < numStations; i++){

                myImage = R.drawable.mymarker;
                mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(lat[i]), Double.parseDouble(lng[i])))
                .title(stationName[i])
                        .snippet(//"Bikes:
                               // + " "
                               // + "Docks:"
                                  bikesAv[i] + ","
                                          +docksAv[i] + ","
                                          +totalDocks[i] + ","
                                          +inService[i]

                        )

                .icon(BitmapDescriptorFactory.fromResource(myImage)));


            }
         //   Station.populateStations(stations);
        }

    } // end of JSONParser

    public class BikeShareWindowAdapter implements GoogleMap.InfoWindowAdapter{

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        View myBikeShareView = getLayoutInflater().inflate(R.layout.custom_info_window, null);


        @Override
        public View getInfoContents(Marker marker){

            String Name = marker.getTitle();
            String Snip = marker.getSnippet();

            TextView tvTitle = (TextView) myBikeShareView.findViewById(R.id.station_name);
            TextView numBikes = (TextView) myBikeShareView.findViewById(R.id.bikes_avail);
            TextView numDocks = (TextView) myBikeShareView.findViewById(R.id.docks_avail);
            TextView totDocks = (TextView) myBikeShareView.findViewById(R.id.total_docks);
            TextView inServ = (TextView) myBikeShareView.findViewById(R.id.in_service);

            String[] SnipInfo =  Snip.split(",");

            String myBikes = SnipInfo[0];
            String myDocks = SnipInfo[1];
            String myTotalDocks = SnipInfo[2];
            String myService = SnipInfo[3];


            tvTitle.setText(Name);
            numBikes.setText(myBikes);
            numDocks.setText(myDocks);
            totDocks.setText(myTotalDocks);
            inServ.setText(myService);

            return myBikeShareView;
        }



    } // end of BikeShareWindowAdapter

} // end of MapsActivity