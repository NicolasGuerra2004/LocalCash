package com.guerra.localcash.views;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guerra.localcash.R;
import com.guerra.localcash.adapters.ComprasAdapter;
import com.guerra.localcash.database.DatabaseHelper;
import com.guerra.localcash.models.Compra;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ComprasFragment extends Fragment {

    private RecyclerView rvCompras;
    private ComprasAdapter adapter;
    private List<Compra> compraList;
    private DatabaseHelper dbHelper;
    private int tipoFiltroActual = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compras, container, false);

        rvCompras = view.findViewById(R.id.rvCompras);
        RadioGroup rgFiltro = view.findViewById(R.id.rgFiltroCompras);
        FloatingActionButton fabAgregar = view.findViewById(R.id.fabAgregarCompra);

        dbHelper = new DatabaseHelper(getContext());
        compraList = new ArrayList<>();
        adapter = new ComprasAdapter(compraList);

        rvCompras.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCompras.setAdapter(adapter);

        cargarCompras();

        rgFiltro.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbHistorial) {
                tipoFiltroActual = 0;
            } else if (checkedId == R.id.rbAgendadas) {
                tipoFiltroActual = 1;
            }
            cargarCompras();
        });

        fabAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        return view;
    }

    private void cargarCompras() {
        compraList.clear();
        Cursor cursor = dbHelper.obtenerCompras(tipoFiltroActual);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String descripcion = cursor.getString(1);
                double monto = cursor.getDouble(2);
                String fecha = cursor.getString(3);
                int esAgendada = cursor.getInt(4);
                compraList.add(new Compra(id, descripcion, monto, fecha, esAgendada));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void mostrarDialogoAgregar() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_agregar_compra, null);
        EditText etDescripcion = dialogView.findViewById(R.id.etDescripcionCompra);
        EditText etMonto = dialogView.findViewById(R.id.etMontoCompra);
        CheckBox cbAgendada = dialogView.findViewById(R.id.cbEsAgendada);

        cbAgendada.setChecked(tipoFiltroActual == 1);

        new AlertDialog.Builder(getContext())
                .setTitle("Nueva Compra")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String descripcion = etDescripcion.getText().toString().trim();
                    String montoStr = etMonto.getText().toString().trim();
                    if (!descripcion.isEmpty() && !montoStr.isEmpty()) {
                        double monto = Double.parseDouble(montoStr);
                        int esAgendada = cbAgendada.isChecked() ? 1 : 0;
                        String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                        dbHelper.insertarCompra(descripcion, monto, fecha, esAgendada);
                        cargarCompras();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}