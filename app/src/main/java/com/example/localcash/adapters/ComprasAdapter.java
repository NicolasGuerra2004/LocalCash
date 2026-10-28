package com.example.localcash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localcash.R;
import com.example.localcash.models.Compra;
import java.util.List;

public class ComprasAdapter extends RecyclerView.Adapter<ComprasAdapter.CompraViewHolder> {

    private List<Compra> compraList;

    public ComprasAdapter(List<Compra> compraList) {
        this.compraList = compraList;
    }

    @NonNull
    @Override
    public CompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compra, parent, false);
        return new CompraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompraViewHolder holder, int position) {
        Compra compra = compraList.get(position);
        holder.tvDescripcionCompra.setText(compra.getDescripcion());
        holder.tvMontoCompra.setText("$" + compra.getMonto());
        holder.tvFechaCompra.setText(compra.getFecha());
    }

    @Override
    public int getItemCount() {
        return compraList.size();
    }

    public static class CompraViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcionCompra, tvMontoCompra, tvFechaCompra;

        public CompraViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescripcionCompra = itemView.findViewById(R.id.tvDescripcionCompra);
            tvMontoCompra = itemView.findViewById(R.id.tvMontoCompra);
            tvFechaCompra = itemView.findViewById(R.id.tvFechaCompra);
        }
    }
}