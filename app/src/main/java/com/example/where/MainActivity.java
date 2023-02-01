package com.example.where;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String LOG_TAG = MainActivity.class.getName();
    DatabaseReference mMarkers = FirebaseDatabase.getInstance().getReference().child("Markers");
    /*Интерфейс обратного вызова, когда карта готова к использованию.
    Как только экземпляр этого интерфейса установлен в объекте MapFragment или MapView,
    метод onMapReady(GoogleMap) запускается, когда карта готова к использованию,
    и предоставляет ненулевой экземпляр GoogleMap.*/
    GoogleMap map; //Объект GoogleMap предоставляет доступ к данным карты и ее представлению
    /*GoogleMap — точка входа для управления базовыми функциями и данными карты.
    Ваше приложение может получить доступ к объекту GoogleMap только после того,
    как он будет получен из объекта SupportMapFragment или MapView .
     */
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //Объект SupportMapFragment управляет жизненным циклом карты и является родительским элементом для интерфейса приложения.
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        createMarkers(map);
        LatLng Almaty = new LatLng(43.2567, 76.9286);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Almaty, 10));
    }

    public void createMarkers(GoogleMap map) {
        mMarkers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Marker marker = ds.getValue(Marker.class);
                    assert marker != null;
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> getLatLng = geocoder.getFromLocationName((String) marker.address, 1);
                        Double latitude = getLatLng.get(0).getLatitude();
                        Double longitude = getLatLng.get(0).getLongitude();
                        LatLng markerLatLng;
                        markerLatLng = new LatLng(latitude, longitude);
                        if (marker.categories.equals("Автомат для питьевой воды")){
                            Log.e(LOG_TAG, "Категория: " + marker.categories);
                            Log.e(LOG_TAG, "Адрес: " + marker.address);
                            Log.e(LOG_TAG, "Описание: " + marker.description);
                            map.addMarker(new MarkerOptions().position(markerLatLng)
                                    .title("Категория: " + marker.categories)
                                    .snippet("Описание: " + marker.description)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        }
                        if (marker.categories.equals("Аптека")){
                            Log.e(LOG_TAG, "Категория: " + marker.categories);
                            Log.e(LOG_TAG, "Адрес: " + marker.address);
                            Log.e(LOG_TAG, "Описание: " + marker.description);
                            map.addMarker(new MarkerOptions().position(markerLatLng)
                                    .title("Категория: " + marker.categories)
                                    .snippet("Описание: " + marker.description)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        }
                        if (marker.categories.equals("Магазин")){
                            Log.e(LOG_TAG, "Категория: " + marker.categories);
                            Log.e(LOG_TAG, "Адрес: " + marker.address);
                            Log.e(LOG_TAG, "Описание: " + marker.description);
                            map.addMarker(new MarkerOptions().position(markerLatLng)
                                    .title("Категория: " + marker.categories)
                                    .snippet("Описание: " + marker.description)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        }
                        if (marker.categories.equals("Туалет")){
                            Log.e(LOG_TAG, "Категория: " + marker.categories);
                            Log.e(LOG_TAG, "Адрес: " + marker.address);
                            Log.e(LOG_TAG, "Описание: " + marker.description);
                            map.addMarker(new MarkerOptions().position(markerLatLng)
                                    .title("Категория: " + marker.categories)
                                    .snippet("Описание: " + marker.description)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                        }
                        if (marker.categories.equals("Круглосуточный магазин")){
                            map.addMarker(new MarkerOptions().position(markerLatLng)
                                    .title("Категория: " + marker.categories)
                                    .snippet("Описание: " + marker.description)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ToiletOnClick(View v) {
        Intent i = new Intent(this, New_toilet.class);
        startActivity(i);
    }
}