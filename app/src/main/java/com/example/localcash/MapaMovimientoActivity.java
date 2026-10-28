package com.example.localcash;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapaMovimientoActivity extends AppCompatActivity {

    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_mapa_movimiento);

        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        double lat = getIntent().getDoubleExtra("LATITUD", 0.0);
        double lon = getIntent().getDoubleExtra("LONGITUD", 0.0);
        String tipo = getIntent().getStringExtra("TIPO");
        double monto = getIntent().getDoubleExtra("MONTO", 0.0);

        TextView tvDetalle = findViewById(R.id.tvDetalleMapa);
        tvDetalle.setText(tipo + " - $" + monto);

        GeoPoint startPoint = new GeoPoint(lat, lon);
        map.getController().setZoom(18.0);
        map.getController().setCenter(startPoint);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle(tipo + " registrado aquí");
        map.getOverlays().add(startMarker);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }
}