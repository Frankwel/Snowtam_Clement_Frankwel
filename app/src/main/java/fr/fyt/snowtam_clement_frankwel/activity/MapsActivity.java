package fr.fyt.snowtam_clement_frankwel.activity;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView airportCode;
    private Button switchMap;
    private boolean mapView = false;



    private Snowtam snowtam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        switchMap = (Button)findViewById(R.id.switchMap);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Get the snowtam
        Gson gson = new Gson();
        Type type = new TypeToken<Snowtam>(){}.getType();
        snowtam = gson.fromJson((String)getIntent().getSerializableExtra("snowtam"), type);

        airportCode = (TextView)findViewById(R.id.mapTViewCode);

        airportCode.setText(snowtam.getAirportName());

        switchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mapView){
                    mMap.setMapType(MAP_TYPE_SATELLITE);
                    mapView =true;
                }
                else{
                    mMap.setMapType(MAP_TYPE_NORMAL);
                    mapView=false;
                }

            }
        });
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
        LatLng airport = new LatLng(snowtam.getLat(), snowtam.getLng());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(airport,12));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(snowtam.getLat(), snowtam.getLng()))
                .title(snowtam.getAirportName()));
        mMap.setMinZoomPreference(11.0f);
        mMap.setMaxZoomPreference(13.0f);
    }
}
