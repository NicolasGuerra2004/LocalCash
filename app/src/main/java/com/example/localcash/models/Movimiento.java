package com.example.localcash.models;

public class Movimiento {
    private int id;
    private int cuentaId;
    private String tipo;
    private double monto;
    private double latitud;
    private double longitud;
    private String fecha;

    public Movimiento(int id, int cuentaId, String tipo, double monto, double latitud, double longitud, String fecha) {
        this.id = id;
        this.cuentaId = cuentaId;
        this.tipo = tipo;
        this.monto = monto;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public int getCuentaId() { return cuentaId; }
    public String getTipo() { return tipo; }
    public double getMonto() { return monto; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public String getFecha() { return fecha; }
}