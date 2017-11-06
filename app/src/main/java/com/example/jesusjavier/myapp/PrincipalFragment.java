package com.example.jesusjavier.myapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends Fragment {

    int foto1 = R.drawable.uno;
    int foto2 = R.drawable.dos;
    int foto3 = R.drawable.tres;
    int foto4 = R.drawable.cuatro;
    int foto5 = R.drawable.cinco;

    private ListView listView;
    private Animal[] animales=new Animal[]{
            new Animal(foto1,"Café","Rocco","Pitbull","Null","Null"),
            new Animal(foto2,"Negro","Bosco","Lobo","Null","Null"),
            new Animal(foto3,"Negro","Kaiser","Doberman","Null","Null"),
            new Animal(foto4,"Negro","Tobby","Labrador","Null","Null"),
            new Animal(foto5,"Cafe","Firulais","Criollo","Null","Null"),
    };


    public PrincipalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_principal, container, false);

        Adapter adaptador=new Adapter(getContext(),animales);

        listView = (ListView) view.findViewById(R.id.Lista);
        listView.setAdapter(adaptador);

        return view;

    }

}

