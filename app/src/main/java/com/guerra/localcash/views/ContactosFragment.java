package com.guerra.localcash.views;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guerra.localcash.R;
import com.guerra.localcash.adapters.ContactosAdapter;
import com.guerra.localcash.database.DatabaseHelper;
import com.guerra.localcash.models.Contacto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ContactosFragment extends Fragment {

    private RecyclerView rvContactos;
    private ContactosAdapter adapter;
    private List<Contacto> contactoList;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactos, container, false);

        rvContactos = view.findViewById(R.id.rvContactos);
        FloatingActionButton fabAgregar = view.findViewById(R.id.fabAgregarContacto);

        dbHelper = new DatabaseHelper(getContext());
        contactoList = new ArrayList<>();
        adapter = new ContactosAdapter(contactoList);

        rvContactos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContactos.setAdapter(adapter);

        cargarContactos();

        fabAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        return view;
    }

    private void cargarContactos() {
        contactoList.clear();
        Cursor cursor = dbHelper.obtenerContactos();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                contactoList.add(new Contacto(id, nombre));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void mostrarDialogoAgregar() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_agregar_contacto, null);
        EditText etNombre = dialogView.findViewById(R.id.etNombreContacto);

        new AlertDialog.Builder(getContext())
                .setTitle("Nuevo Contacto")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    if (!nombre.isEmpty()) {
                        dbHelper.insertarContacto(nombre);
                        cargarContactos();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}