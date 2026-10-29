package com.guerra.localcash;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guerra.localcash.adapters.MovimientosAdapter;
import com.guerra.localcash.database.DatabaseHelper;
import com.guerra.localcash.models.Movimiento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovimientosActivity extends AppCompatActivity {

    private RecyclerView rvMovimientos;
    private MovimientosAdapter adapter;
    private List<Movimiento> movimientoList;
    private DatabaseHelper dbHelper;
    private int cuentaId;
    private LocationManager locationManager;
    private static final int LOCATION_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimientos);

        cuentaId = getIntent().getIntExtra("CUENTA_ID", -1);
        String cuentaNombre = getIntent().getStringExtra("CUENTA_NOMBRE");

        TextView tvNombreCuenta = findViewById(R.id.tvNombreCuentaMovimientos);
        tvNombreCuenta.setText(cuentaNombre);

        rvMovimientos = findViewById(R.id.rvMovimientos);
        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregarMovimiento);

        dbHelper = new DatabaseHelper(this);
        movimientoList = new ArrayList<>();
        adapter = new MovimientosAdapter(movimientoList);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        rvMovimientos.setLayoutManager(new LinearLayoutManager(this));
        rvMovimientos.setAdapter(adapter);

        cargarMovimientos();

        fabAgregar.setOnClickListener(v -> mostrarDialogoAgregar());
    }

    private void cargarMovimientos() {
        movimientoList.clear();
        Cursor cursor = dbHelper.obtenerMovimientos(cuentaId);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String tipo = cursor.getString(2);
                double monto = cursor.getDouble(3);
                double lat = cursor.getDouble(4);
                double lon = cursor.getDouble(5);
                String fecha = cursor.getString(6);
                movimientoList.add(new Movimiento(id, cuentaId, tipo, monto, lat, lon, fecha));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void mostrarDialogoAgregar() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_movimiento, null);
        RadioGroup rgTipo = dialogView.findViewById(R.id.rgTipoMovimiento);
        EditText etMonto = dialogView.findViewById(R.id.etMontoMovimiento);
        Switch swUbicacion = dialogView.findViewById(R.id.swUbicacion);

        swUbicacion.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                swUbicacion.setChecked(false);
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("Nuevo Movimiento")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String montoStr = etMonto.getText().toString().trim();
                    if (!montoStr.isEmpty()) {
                        double monto = Double.parseDouble(montoStr);
                        String tipo = rgTipo.getCheckedRadioButtonId() == R.id.rbIngreso ? "Ingreso" : "Egreso";
                        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

                        double lat = 0.0;
                        double lon = 0.0;

                        if (swUbicacion.isChecked()) {
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location == null) {
                                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                }
                                if (location != null) {
                                    lat = location.getLatitude();
                                    lon = location.getLongitude();
                                } else {
                                    Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        dbHelper.insertarMovimiento(cuentaId, tipo, monto, lat, lon, fecha);
                        cargarMovimientos();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}