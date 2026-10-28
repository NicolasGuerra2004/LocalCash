package com.example.localcash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localcash.R;
import com.example.localcash.models.Cuenta;
import java.util.List;

public class CuentasAdapter extends RecyclerView.Adapter<CuentasAdapter.CuentaViewHolder> {

    private List<Cuenta> cuentaList;

    public CuentasAdapter(List<Cuenta> cuentaList) {
        this.cuentaList = cuentaList;
    }

    @NonNull
    @Override
    public CuentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuenta, parent, false);
        return new CuentaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuentaViewHolder holder, int position) {
        Cuenta cuenta = cuentaList.get(position);
        holder.tvNombreCuenta.setText(cuenta.getNombre());
        holder.tvSaldoCuenta.setText("$" + cuenta.getSaldo());

        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(v.getContext(), com.example.localcash.MovimientosActivity.class);
            intent.putExtra("CUENTA_ID", cuenta.getId());
            intent.putExtra("CUENTA_NOMBRE", cuenta.getNombre());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cuentaList.size();
    }

    public static class CuentaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreCuenta, tvSaldoCuenta;

        public CuentaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreCuenta = itemView.findViewById(R.id.tvNombreCuenta);
            tvSaldoCuenta = itemView.findViewById(R.id.tvSaldoCuenta);
        }
    }
}