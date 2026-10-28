package com.example.localcash.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "localcash.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Cuentas (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, saldo REAL)");

        db.execSQL("CREATE TABLE Movimientos (id INTEGER PRIMARY KEY AUTOINCREMENT, cuenta_id INTEGER, tipo TEXT, monto REAL, latitud REAL, longitud REAL, fecha TEXT, FOREIGN KEY(cuenta_id) REFERENCES Cuentas(id))");

        db.execSQL("CREATE TABLE Contactos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT)");

        db.execSQL("CREATE TABLE Compras (id INTEGER PRIMARY KEY AUTOINCREMENT, descripcion TEXT, monto REAL, fecha TEXT, es_agendada INTEGER)");

        db.execSQL("CREATE TABLE Deudas (id INTEGER PRIMARY KEY AUTOINCREMENT, compra_id INTEGER, contacto_id INTEGER, monto_adeudado REAL, estado_pago INTEGER, FOREIGN KEY(compra_id) REFERENCES Compras(id), FOREIGN KEY(contacto_id) REFERENCES Contactos(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Deudas");
        db.execSQL("DROP TABLE IF EXISTS Compras");
        db.execSQL("DROP TABLE IF EXISTS Contactos");
        db.execSQL("DROP TABLE IF EXISTS Movimientos");
        db.execSQL("DROP TABLE IF EXISTS Cuentas");
        onCreate(db);
    }
    public long insertarCuenta(String nombre, double saldo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("saldo", saldo);
        return db.insert("Cuentas", null, values);
    }

    public Cursor obtenerCuentas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Cuentas", null);
    }

    public long insertarMovimiento(int cuentaId, String tipo, double monto, double latitud, double longitud, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cuenta_id", cuentaId);
        values.put("tipo", tipo);
        values.put("monto", monto);
        values.put("latitud", latitud);
        values.put("longitud", longitud);
        values.put("fecha", fecha);

        if (tipo.equals("Ingreso")) {
            db.execSQL("UPDATE Cuentas SET saldo = saldo + " + monto + " WHERE id = " + cuentaId);
        } else if (tipo.equals("Egreso")) {
            db.execSQL("UPDATE Cuentas SET saldo = saldo - " + monto + " WHERE id = " + cuentaId);
        }

        return db.insert("Movimientos", null, values);
    }

    public Cursor obtenerMovimientos(int cuentaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Movimientos WHERE cuenta_id = ? ORDER BY id DESC", new String[]{String.valueOf(cuentaId)});
    }

    public long insertarCompra(String descripcion, double monto, String fecha, int esAgendada) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("descripcion", descripcion);
        values.put("monto", monto);
        values.put("fecha", fecha);
        values.put("es_agendada", esAgendada);
        return db.insert("Compras", null, values);
    }

    public Cursor obtenerCompras(int esAgendada) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Compras WHERE es_agendada = ? ORDER BY id DESC", new String[]{String.valueOf(esAgendada)});
    }
}