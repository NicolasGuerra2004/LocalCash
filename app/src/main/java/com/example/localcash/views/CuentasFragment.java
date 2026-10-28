package com.example.localcash.views;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localcash.R;
import com.example.localcash.adapters.CuentasAdapter;
import com.example.localcash.database.DatabaseHelper;
import com.example.localcash.models.Cuenta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class CuentasFragment extends Fragment {

    private RecyclerView rvCuentas;
    private CuentasAdapter adapter;
    private List<Cuenta> cuentaList;
    private DatabaseHelper dbHelper;
    private TextView tvSaldoTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuentas, container, false);

        rvCuentas = view.findViewById(R.id.rvCuentas);
        tvSaldoTotal = view.findViewById(R.id.tvSaldoTotal);
        FloatingActionButton fabAgregar = view.findViewById(R.id.fabAgregarCuenta);

        dbHelper = new DatabaseHelper(getContext());
        cuentaList = new ArrayList<>();
        adapter = new CuentasAdapter(cuentaList);

        rvCuentas.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCuentas.setAdapter(adapter);

        cargarCuentas();

        fabAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarCuentas();
    }

    private void cargarCuentas() {
        cuentaList.clear();
        double total = 0;
        Cursor cursor = dbHelper.obtenerCuentas();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                double saldo = cursor.getDouble(2);
                cuentaList.add(new Cuenta(id, nombre, saldo));
                total += saldo;
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
        tvSaldoTotal.setText("$" + total);
    }

    private void mostrarDialogoAgregar() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_agregar_cuenta, null);
        EditText etNombre = dialogView.findViewById(R.id.etNombreCuenta);
        EditText etSaldo = dialogView.findViewById(R.id.etSaldoInicial);

        new AlertDialog.Builder(getContext())
                .setTitle("Nueva Cuenta")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String saldoStr = etSaldo.getText().toString().trim();
                    if (!nombre.isEmpty()) {
                        double saldo = saldoStr.isEmpty() ? 0 : Double.parseDouble(saldoStr);
                        dbHelper.insertarCuenta(nombre, saldo);
                        cargarCuentas();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}