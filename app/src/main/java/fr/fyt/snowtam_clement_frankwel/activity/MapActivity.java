package fr.fyt.snowtam_clement_frankwel.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.MapView;

import fr.fyt.snowtam_clement_frankwel.R;

/**
 * Created by frank on 15/11/2017.
 */

public class MapActivity extends AppCompatActivity {

    MapView mapView;
    Button btnViewCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = (MapView)findViewById(R.id.mapView);
        btnViewCode = (Button)findViewById(R.id.mapBtnViewCode);

        btnViewCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
