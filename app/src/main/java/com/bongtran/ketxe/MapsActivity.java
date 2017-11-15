package com.bongtran.ketxe;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.bongtran.ketxe.Util.Util;
import com.bongtran.ketxe.interfaces.LocationChangeCallback;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationChangeCallback {

    private GoogleMap mMap;
    private Location mCurrentLocation = null;
    private Marker marker, currentMaker;
    public double latitude = 0;
    public double longitude = 0;
    protected LatLng latLng = null;

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
        LatLng vanThanh = new LatLng(10.8061317, 106.6902376);
        mMap.addMarker(new MarkerOptions().position(vanThanh).title("Văn Thánh"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(vanThanh));
    }

    private Marker addMarker(LatLng lng, String title, boolean isFlat, int iconID) {
        Marker marker = null;
        if (mMap != null) {
            marker = mMap.addMarker(new MarkerOptions()
                    .flat(isFlat)
                    .position(lng)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(iconID))
                    .title(title));
            marker.showInfoWindow();
        }
        return marker;
    }

    @Override
    public void onLocationChangeCallBack(Location location) {
        if (location == null) {
            return;
        }
        mCurrentLocation = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(latLng1);
        CameraUpdate cameraUpdate;
        if (true) {
            long distance = getDistanceInMeters(location.getLatitude(),
                    location.getLongitude());

            String dist = Util.covertDistance(distance);
            if (currentMaker != null) {
                currentMaker.remove();
                currentMaker = addMarker(latLng1, dist, true, R.drawable.map_location_icon_tt);
            } else {
                currentMaker = addMarker(latLng1, dist, true, R.drawable.map_location_icon_tt);
            }
            builder.include(latLng);
            LatLngBounds bounds = builder.build();
            int padding = 95;
            if (getDistanceInMeters(location.getLatitude(),
                    location.getLongitude()) > 250) {
                cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            } else {
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            }
//            setUpMapIfNeeded();
            if (mMap != null) {
                try {
                    mMap.moveCamera(cameraUpdate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            office_distance.setText(dist);
        } else {
//            setUpMapIfNeeded();
            if (currentMaker != null) {
                currentMaker.remove();
                currentMaker = addMarker(latLng1, "", true, R.drawable.map_location_icon_tt);
            } else {
                currentMaker = addMarker(latLng1, "", true, R.drawable.map_location_icon_tt);
            }
            if (mMap != null) {
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng1, 17);
                mMap.moveCamera(cameraUpdate);
            }
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(getApplication().getBaseContext());
        List<Address> address;
        LatLng latLng;
        try {
            address = coder.getFromLocationName(strAddress, 5);

            if (address == null || address.size() == 0) {
                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            latLng = new LatLng(location.getLatitude(), location.getLongitude());

            return latLng;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected long getDistanceInMeters(double lat2, double lon2) {
        int EARTH_RADIUS_KM = 6371;
        double lat1Rad, lat2Rad, deltaLonRad, dist;
        long nDist;
        if (latLng != null) {
            lat2Rad = Math.toRadians(lat2);
            lat1Rad = Math.toRadians(lat2);
            deltaLonRad = Math.toRadians(lon2 - lat2);
            dist = Math.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLonRad)) * EARTH_RADIUS_KM;
            nDist = Math.round(dist * 1000);
            return nDist;
        }

        return 0;
    }
}
