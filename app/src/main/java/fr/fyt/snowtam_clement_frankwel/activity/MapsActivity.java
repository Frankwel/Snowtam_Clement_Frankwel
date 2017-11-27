package fr.fyt.snowtam_clement_frankwel.activity;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import fr.fyt.snowtam_clement_frankwel.R;
import fr.fyt.snowtam_clement_frankwel.model.Snowtam;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView txt;

    private Snowtam snowtam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //getting snowtam
        Gson gson = new Gson();
        Type type = new TypeToken<Snowtam>(){}.getType();
        snowtam = gson.fromJson((String)getIntent().getSerializableExtra("snowtam"), type);

        txt = (TextView)findViewById(R.id.mapTViewCode);
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

        txt.setText("lat =" + String.valueOf(snowtam.getLat()) + " lng = " + String.valueOf(snowtam.getLng()));

        LatLng airport = new LatLng(snowtam.getLat(), snowtam.getLng());
        mMap.addMarker(new MarkerOptions().position(airport).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(airport));
    }
}
