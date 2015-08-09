package com.example.zoe.swagdragon;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class NotificationService extends Service implements LocationListener {
    ArrayList<LatLng> treeList = new ArrayList<LatLng>();
    public ArrayList<LatLng> closeTrees = new ArrayList<LatLng>();
    private Location origin;
    private NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;

    public NotificationService() {
    }

    @Override
    public void onCreate() {
        createTreeList ();
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.treenotify);
        mBuilder.setContentTitle("New trees detected around you!");
        mBuilder.setContentText("There are new notable trees around you, check it out and leave a message!");

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0, this);

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void createTreeList () {
        try {
            AssetManager assetManager = getAssets();
            InputStream csvStream = assetManager.open("PUP_NotableTrees.csv");
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            CSVReader csvReader = new CSVReader(csvStreamReader);
            String[] line;

            // throw away the header
            csvReader.readNext();

            while ((line = csvReader.readNext()) != null) {
                LatLng tree = new LatLng(Double.parseDouble(line[1]), Double.parseDouble(line[0]));
                treeList.add(tree);
                Log.v("*******line*****", Double.parseDouble(line[1]) + "" + line[0] + " " + line[6] + "");
            }
            origin = getMyLocation();
            closeTrees = createCloseTreeArray(treeList);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("********", "You are experiencing a java IOException");
            Log.e("*********", e.toString());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("**********called*******", "LOCATION CHANGED");
        for( int i=0;i<closeTrees.size();i++){
            Location treeLocation = new Location("");
            treeLocation.setLatitude(closeTrees.get(i).latitude);
            treeLocation.setLongitude(closeTrees.get(i).longitude);
            if (location.distanceTo(treeLocation) < 250) {
                // notificationID allows you to update the notification later on.
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());
                Log.v("*******NOTIFY*****", "Sent notification");
            }
        }

        if (location.distanceTo(origin) > 250) {
            origin = getMyLocation();
            closeTrees = createCloseTreeArray(treeList);
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

    private Location getMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);

        return myLocation;

    }

    private ArrayList<LatLng> createCloseTreeArray(ArrayList<LatLng> list) {
        Location myLocation = getMyLocation();
        ArrayList<LatLng> close = new ArrayList<LatLng>();
        for (int i = 0; i < list.size(); i++) {
            Location treeLocation = new Location("");
            treeLocation.setLatitude(list.get(i).latitude);
            treeLocation.setLongitude(list.get(i).longitude);
            if (myLocation.distanceTo(treeLocation) < 500) {
                close.add(list.get(i));
            }
        }
        return close;
    }
}
