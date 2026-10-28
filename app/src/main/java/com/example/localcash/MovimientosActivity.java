package com.example.localcash;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localcash.adapters.MovimientosAdapter;
import com.example.localcash.database.DatabaseHelper;
import com.example.localcash.models.Movimiento;
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

        new AlertDialog.Builder(this)
                .setTitle("Nuevo Movimiento")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String montoStr = etMonto.getText().toString().trim();
                    if (!montoStr.isEmpty()) {
                        double monto = Double.parseDouble(montoStr);
                        String tipo = rgTipo.getCheckedRadioButtonId() == R.id.rbIngreso ? "Ingreso" : "Egreso";
                        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

                        dbHelper.insertarMovimiento(cuentaId, tipo, monto, 0.0, 0.0, fecha);
                        cargarMovimientos();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}