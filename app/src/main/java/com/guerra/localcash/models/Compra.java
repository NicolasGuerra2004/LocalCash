package com.guerra.localcash.models;

public class Compra {
    private int id;
    private String descripcion;
    private double monto;
    private String fecha;
    private int esAgendada;

    public Compra(int id, String descripcion, double monto, String fecha, int esAgendada) {
        this.id = id;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
        this.esAgendada = esAgendada;
    }

    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public double getMonto() { return monto; }
    public String getFecha() { return fecha; }
    public int getEsAgendada() { return esAgendada; }
}