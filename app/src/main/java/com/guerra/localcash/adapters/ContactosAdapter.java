package com.guerra.localcash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guerra.localcash.R;
import com.guerra.localcash.models.Contacto;
import java.util.List;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ContactoViewHolder> {

    private List<Contacto> contactoList;

    public ContactosAdapter(List<Contacto> contactoList) {
        this.contactoList = contactoList;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        Contacto contacto = contactoList.get(position);
        holder.tvNombreContacto.setText(contacto.getNombre());
    }

    @Override
    public int getItemCount() {
        return contactoList.size();
    }

    public static class ContactoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreContacto;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreContacto = itemView.findViewById(R.id.tvNombreContacto);
        }
    }
}