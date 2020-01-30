package com.rail.travelwithme.ui.mapview;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;
import com.rail.travelwithme.MainActivity;
import com.rail.travelwithme.R;
import com.rail.travelwithme.data.AppLocation;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    Boolean mLocationPermissionGranted = false;
    FusedLocationProviderClient mFusedLocationProviderClient;
    Location currentLocation;
    private GoogleMap mMap;
    private Marker mNewLocation;
    private Polyline currentPolyline;
    Button getDirection;


    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mapview, null, false);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fetchLastLocation();




        return view;
    }

    private void fetchMapAsync() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void fetchLastLocation() {
        Task<Location> task = mFusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    fetchMapAsync();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        long tsLong = System.currentTimeMillis() / 1000;
        String ts = Long.toString(tsLong);

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("I am here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        googleMap.addMarker(markerOptions);


        //New Marker to nearby location
        Double nearbyLatitude = currentLocation.getLatitude() + 0.02;
        Double nearbyLongitude = currentLocation.getLongitude() + 0.02;

        LatLng nearByPosition = new LatLng(nearbyLatitude, nearbyLongitude);

        MarkerOptions nearBymarkerOptions = new MarkerOptions().position(nearByPosition)
                .title("Near By Location");

        mNewLocation = mMap.addMarker(nearBymarkerOptions
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true));

        mNewLocation.setTag(0);

        writeNewLocation(ts,
                Double.toString(latLng.latitude),
                Double.toString(latLng.longitude));

//        Log.i("info", calculateDistance(latLng,
//                nearByPosition) + " : " + ts + " : "
//        );

//        calculateDistance(latLng, nearByPosition);

//        getDirectionxyz(markerOptions, nearBymarkerOptions);
    }

    private void calculateDistance(LatLng latLng, LatLng nearByPosition) {
        double distance = SphericalUtil.computeDistanceBetween(latLng, nearByPosition) / 1000;
        SphericalUtil.interpolate(latLng, nearByPosition, 0.5);
        Toast toast = Toast.makeText(getActivity(),
                "This distance between your current location and destination is: " +
                        "" + round(distance, 2) + " KM", Toast.LENGTH_LONG);
        toast.show();

        MarkerOptions origin = new MarkerOptions().position(latLng)
                .title("I am here");

        MarkerOptions destination = new MarkerOptions().position(nearByPosition)
                .title("Near By Location");
//        getDirection = getView().findViewById(R.id.do_email_picker);
        new FetchURL(getActivity()).execute(getUrl(origin.getPosition(), destination.getPosition(), "driving"), "driving");

//        return distance;
    }

    private void writeNewLocation(String timestamp, String latitute, String longitute) {

        DatabaseReference users_locations =
                getFireBaseDataBaseInstance("Users_Locations");

        AppLocation appLocation = new AppLocation(timestamp, latitute, longitute);

        users_locations.child(timestamp).setValue(appLocation);

    }

    public DatabaseReference getFireBaseDataBaseInstance(String dbChildName) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        return database.child(dbChildName);
    }

//    private void getDirectionxyz(final MarkerOptions origin, final MarkerOptions destination){
//        getDirection = getView().findViewById(R.id.do_email_picker);
//        getDirection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new FetchURL(getActivity()).execute(getUrl(origin.getPosition(), destination.getPosition(), "driving"), "driving");
//            }
//        });
//    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.map_key);
        return url;
    }

//    @Override
//    public void onTaskDone(Object... values) {
//        if (currentPolyline != null)
//            currentPolyline.remove();
//        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
//    }

}