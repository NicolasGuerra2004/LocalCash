package com.guerra.localcash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guerra.localcash.R;
import com.guerra.localcash.models.Deuda;
import java.util.List;

public class DeudasAdapter extends RecyclerView.Adapter<DeudasAdapter.DeudaViewHolder> {

    private List<Deuda> deudaList;

    public DeudasAdapter(List<Deuda> deudaList) {
        this.deudaList = deudaList;
    }

    @NonNull
    @Override
    public DeudaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deuda, parent, false);
        return new DeudaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeudaViewHolder holder, int position) {
        Deuda deuda = deudaList.get(position);
        holder.tvDeudaDescripcion.setText(deuda.getDescripcionCompra());
        holder.tvDeudaContacto.setText("Deudor: " + deuda.getNombreContacto());
        holder.tvDeudaMonto.setText("$" + deuda.getMontoAdeudado());
    }

    @Override
    public int getItemCount() {
        return deudaList.size();
    }

    public static class DeudaViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeudaDescripcion, tvDeudaContacto, tvDeudaMonto;

        public DeudaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeudaDescripcion = itemView.findViewById(R.id.tvDeudaDescripcion);
            tvDeudaContacto = itemView.findViewById(R.id.tvDeudaContacto);
            tvDeudaMonto = itemView.findViewById(R.id.tvDeudaMonto);
        }
    }
}