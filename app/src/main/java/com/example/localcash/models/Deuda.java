package com.example.localcash.models;

public class Deuda {
    private int id;
    private String descripcionCompra;
    private String nombreContacto;
    private double montoAdeudado;
    private int estadoPago;

    public Deuda(int id, String descripcionCompra, String nombreContacto, double montoAdeudado, int estadoPago) {
        this.id = id;
        this.descripcionCompra = descripcionCompra;
        this.nombreContacto = nombreContacto;
        this.montoAdeudado = montoAdeudado;
        this.estadoPago = estadoPago;
    }

    public int getId() { return id; }
    public String getDescripcionCompra() { return descripcionCompra; }
    public String getNombreContacto() { return nombreContacto; }
    public double getMontoAdeudado() { return montoAdeudado; }
    public int getEstadoPago() { return estadoPago; }
}