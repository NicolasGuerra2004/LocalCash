package com.example.localcash.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localcash.R;
import com.example.localcash.models.Movimiento;
import java.util.List;

public class MovimientosAdapter extends RecyclerView.Adapter<MovimientosAdapter.MovimientoViewHolder> {

    private List<Movimiento> movimientoList;

    public MovimientosAdapter(List<Movimiento> movimientoList) {
        this.movimientoList = movimientoList;
    }

    @NonNull
    @Override
    public MovimientoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movimiento, parent, false);
        return new MovimientoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovimientoViewHolder holder, int position) {
        Movimiento movimiento = movimientoList.get(position);
        holder.tvTipoMovimiento.setText(movimiento.getTipo());
        holder.tvMontoMovimiento.setText("$" + movimiento.getMonto());
        holder.tvFechaMovimiento.setText(movimiento.getFecha());

        if (movimiento.getTipo().equals("Ingreso")) {
            holder.tvMontoMovimiento.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            holder.tvMontoMovimiento.setTextColor(Color.parseColor("#F44336"));
        }

        if (movimiento.getLatitud() != 0.0 && movimiento.getLongitud() != 0.0) {
            holder.ivIconoMapa.setVisibility(View.VISIBLE);
        } else {
            holder.ivIconoMapa.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (movimiento.getLatitud() != 0.0 && movimiento.getLongitud() != 0.0) {
                android.content.Intent intent = new android.content.Intent(v.getContext(), com.example.localcash.MapaMovimientoActivity.class);
                intent.putExtra("LATITUD", movimiento.getLatitud());
                intent.putExtra("LONGITUD", movimiento.getLongitud());
                intent.putExtra("TIPO", movimiento.getTipo());
                intent.putExtra("MONTO", movimiento.getMonto());
                v.getContext().startActivity(intent);
            } else {
                android.widget.Toast.makeText(v.getContext(), "Este movimiento no tiene ubicación guardada", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movimientoList.size();
    }

    public static class MovimientoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipoMovimiento, tvMontoMovimiento, tvFechaMovimiento;
        ImageView ivIconoMapa;

        public MovimientoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipoMovimiento = itemView.findViewById(R.id.tvTipoMovimiento);
            tvMontoMovimiento = itemView.findViewById(R.id.tvMontoMovimiento);
            tvFechaMovimiento = itemView.findViewById(R.id.tvFechaMovimiento);
            ivIconoMapa = itemView.findViewById(R.id.ivIconoMapa);
        }
    }
}