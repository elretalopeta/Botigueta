package com.example.eric.botigueta;
/**
 * Created by eric on 26/01/2016.
 */

public class Producto {
    String nombre;
    Integer cantidad = 0;
    Double precio;
    String img;

    Producto(String nombre, Double precio,  String img, Integer cantidad){
        this.img = img;
        this.nombre=nombre;
        this.precio=precio;
        this.cantidad=cantidad;
    }
    
    public void delCantidad(Integer cantidad){
        this.cantidad=this.cantidad-cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = this.cantidad+cantidad;
    }
    public String getImg() {
        return img;
    }

}
