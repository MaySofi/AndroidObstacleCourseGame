package com.example.obstaclecoursegame.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.obstaclecoursegame.R;
import com.example.obstaclecoursegame.Utilities.MySPv;
import com.example.obstaclecoursegame.Utilities.SignalGenerator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MapFragment extends Fragment {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment supportMapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        findViews(view);

        String fromSP = MySPv.getInstance().getString("MapFragment","");
        LatLng fromJson = new Gson().fromJson(fromSP, LatLng.class);
        Log.d("RecordActivity", "onCreateView: json:" + fromJson);
        return view;
    }

    private void findViews(View view) {
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_google);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        Dexter.withContext(view.getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getCurrentLocationMap(view.getContext());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }
    public void zoomOnRecord(double latitude, double longitude) {
        supportMapFragment.getMapAsync(googleMap -> {
            LatLng user = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions()
                    .position(user)
                    .title("User Marker"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user, 15));
        });
    }

    public LatLng getCurrentPlayerCoordinates() {
        String fromSP = MySPv.getInstance().getString("MapFragment","");
        return new Gson().fromJson(fromSP, LatLng.class);
    }
    private void getCurrentLocationMap(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> supportMapFragment.getMapAsync(googleMap -> {
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Current Location");
                googleMap.addMarker(markerOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                String json = new Gson().toJson(latLng);
                MySPv.getInstance().putString("MapFragment", json);
            } else {
                SignalGenerator.getInstance().toast("Permission Denied", Toast.LENGTH_SHORT);
            }
        }));
    }
}
