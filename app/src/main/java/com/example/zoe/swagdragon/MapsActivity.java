package com.example.zoe.swagdragon;

import android.content.res.AssetManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available
    public ArrayList<Marker> treeMarkers = new ArrayList<Marker>();
    public ArrayList<Marker> closeTrees = new ArrayList<Marker>();
    private Location origin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        setUpTreeData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        //enable location services
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

    }

    private void setUpTreeData() {
        try {
            AssetManager assetManager = getAssets();
            InputStream csvStream = assetManager.open("PUP_NotableTrees.csv");
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            CSVReader csvReader = new CSVReader(csvStreamReader);
            String[] line;

            // throw away the header
            csvReader.readNext();

            while ((line = csvReader.readNext()) != null) {
                Marker newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(line[1]), Double.parseDouble(line[0])))
                        .title(line[6]).icon(BitmapDescriptorFactory.fromResource(R.drawable.tree)));
                treeMarkers.add(newMarker);
                Log.v("*******line*****", Double.parseDouble(line[1]) + "" + line[0] + " " + line[6] + "");
            }
            origin = getMyLocation();
            closeTrees = createCloseTreeArray(treeMarkers);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("********","You are experiencing a java IOException");
            Log.e("*********",e.toString());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        for( int i=0;i<closeTrees.size();i++){
            Location treeLocation = new Location("");
            treeLocation.setLatitude(closeTrees.get(i).getPosition().latitude);
            treeLocation.setLongitude(closeTrees.get(i).getPosition().longitude);
            if (location.distanceTo(treeLocation) < 25) {
                //TODO add notification code
            }
        }

        if (location.distanceTo(origin) > 250) {
            origin = getMyLocation();
            closeTrees = createCloseTreeArray(treeMarkers);
        }

    }

    private Location getMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);

        return myLocation;

    }

    /*private LatLng getMyLatLng(Location myLocation) {
        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        return latLng;
    }*/

    private ArrayList<Marker> createCloseTreeArray(ArrayList<Marker> list){
        Location myLocation = getMyLocation();
        ArrayList<Marker> close = new ArrayList<Marker>();
        for( int i=0;i<list.size();i++){
            Location treeLocation = new Location("");
            treeLocation.setLatitude(list.get(i).getPosition().latitude);
            treeLocation.setLongitude(list.get(i).getPosition().longitude);
            if (myLocation.distanceTo(treeLocation) < 500) {
                close.add(list.get(i));
            }
        }
        return close;
    }
}
