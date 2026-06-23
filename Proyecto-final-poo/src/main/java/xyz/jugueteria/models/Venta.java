package xyz.jugueteria.models;

import java.sql.Timestamp;

public class Venta {
    private int idVenta;
    private int idCliente;
    private Timestamp fechaVenta;
    private double total;

    public Venta() {}

    public Venta(int idVenta, int idCliente, Timestamp fechaVenta, double total) {
        this.idVenta = idVenta;
        this.idCliente = idCliente;
        this.fechaVenta = fechaVenta;
        this.total = total;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public Timestamp getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Timestamp fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Venta #" + idVenta + " | Total: S/." + total;
    }
}
