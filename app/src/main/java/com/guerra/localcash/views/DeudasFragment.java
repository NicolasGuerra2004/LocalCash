package com.guerra.localcash.views;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guerra.localcash.R;
import com.guerra.localcash.adapters.DeudasAdapter;
import com.guerra.localcash.database.DatabaseHelper;
import com.guerra.localcash.models.Deuda;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DeudasFragment extends Fragment {

    private RecyclerView rvDeudas;
    private DeudasAdapter adapter;
    private List<Deuda> deudaList;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deudas, container, false);

        rvDeudas = view.findViewById(R.id.rvDeudas);
        FloatingActionButton fabAgregar = view.findViewById(R.id.fabAgregarDeuda);

        dbHelper = new DatabaseHelper(getContext());
        deudaList = new ArrayList<>();
        adapter = new DeudasAdapter(deudaList);

        rvDeudas.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDeudas.setAdapter(adapter);

        cargarDeudas();

        fabAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        return view;
    }

    private void cargarDeudas() {
        deudaList.clear();
        Cursor cursor = dbHelper.obtenerDeudasDetalle();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String descripcion = cursor.getString(1);
                String nombreContacto = cursor.getString(2);
                double monto = cursor.getDouble(3);
                int estadoPago = cursor.getInt(4);
                deudaList.add(new Deuda(id, descripcion, nombreContacto, monto, estadoPago));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void mostrarDialogoAgregar() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_agregar_deuda, null);
        EditText etDescripcion = dialogView.findViewById(R.id.etDeudaDescripcion);
        EditText etMontoTotal = dialogView.findViewById(R.id.etDeudaMontoTotal);
        LinearLayout llContactosContainer = dialogView.findViewById(R.id.llContactosContainer);

        List<CheckBox> checkBoxes = new ArrayList<>();
        Cursor cContactos = dbHelper.obtenerContactos();
        if (cContactos.moveToFirst()) {
            do {
                int id = cContactos.getInt(0);
                String nombre = cContactos.getString(1);
                CheckBox cb = new CheckBox(getContext());
                cb.setText(nombre);
                cb.setTag(id);
                llContactosContainer.addView(cb);
                checkBoxes.add(cb);
            } while (cContactos.moveToNext());
        }
        cContactos.close();

        new AlertDialog.Builder(getContext())
                .setTitle("Dividir Gasto")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String descripcion = etDescripcion.getText().toString().trim();
                    String montoStr = etMontoTotal.getText().toString().trim();

                    if (!descripcion.isEmpty() && !montoStr.isEmpty()) {
                        double montoTotal = Double.parseDouble(montoStr);

                        int seleccionados = 0;
                        for (CheckBox cb : checkBoxes) {
                            if (cb.isChecked()) seleccionados++;
                        }

                        if (seleccionados > 0) {
                            double montoPorPersona = montoTotal / (seleccionados + 1);
                            String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                            long compraId = dbHelper.insertarCompra(descripcion, montoTotal, fecha, 0);

                            for (CheckBox cb : checkBoxes) {
                                if (cb.isChecked()) {
                                    int contactoId = (int) cb.getTag();
                                    dbHelper.insertarDeuda(compraId, contactoId, montoPorPersona);
                                }
                            }
                            cargarDeudas();
                        } else {
                            Toast.makeText(getContext(), "Debes seleccionar al menos un contacto", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}