package com.example.jesusjavier.myapp;

/**
 * Created by Jesus Javier on 30/10/2017.
 */

public class Animal {
    private String color,nombre,raza;
    private String duenho,correo;
    private int imagenAnimal;
    private boolean estado=false;

    public Animal(int imagenAnimal,String color,String nombre,String raza,String duenho,String correo) {
        this.imagenAnimal=imagenAnimal;
        this.color=color;
        this.nombre=nombre;
        this.raza=raza;
        this.duenho=duenho;
        this.correo=correo;
    }

    public Animal() {
    }

    public String getDuenho() {
        return duenho;
    }

    public void setDuenho(String duenho) {
        this.duenho = duenho;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getImagenAnimal() {
        return imagenAnimal;
    }

    public void setImagenAnimal(int imagenAnimal) {
        this.imagenAnimal = imagenAnimal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
