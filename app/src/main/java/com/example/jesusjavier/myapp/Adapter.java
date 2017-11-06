package com.example.jesusjavier.myapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Jesus Javier on 30/10/2017.
 */

 class Adapter extends ArrayAdapter<Animal> {
    public Adapter(Context context, Animal[] animales) {

        super(context, R.layout.lista_animal,animales);
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        LayoutInflater inflater =LayoutInflater.from(getContext());
        View item =inflater.inflate(R.layout.lista_animal,null);

        Animal animal=getItem(position);

        ImageView imagenMascota=(ImageView)item.findViewById(R.id.fotoMascota);
        imagenMascota.setImageResource(animal.getImagenAnimal());

        TextView nombreMascota =(TextView)item.findViewById(R.id.nombremascota);
        nombreMascota.setText(animal.getNombre());

        TextView razaMascota=(TextView)item.findViewById(R.id.razaMascota);
        razaMascota.setText(animal.getRaza());

        TextView ubicacionMascota =(TextView)item.findViewById(R.id.colorMascota);
        ubicacionMascota.setText(animal.getColor());


        return (item);
    }
}
