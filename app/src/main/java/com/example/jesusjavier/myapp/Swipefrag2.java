package com.example.jesusjavier.myapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Swipefrag2 extends Fragment {

    FirebaseUser currentFirebaseUser;
    EditText petName,petRaza,petColor;
    Button btnUpdate,btnDelete;
    DatabaseReference myRef;
    FirebaseDatabase database;
    String nombre,raza,color;
    Animal animal;

    public Swipefrag2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_swipefrag2, container, false);
        petName=(EditText)view.findViewById(R.id.petName);
        petRaza=(EditText)view.findViewById(R.id.petRaza);
        petColor=(EditText)view.findViewById(R.id.petColor);
        btnUpdate=(Button)view.findViewById(R.id.btnUpdate);
        btnDelete=(Button)view.findViewById(R.id.btndelete);
        database=FirebaseDatabase.getInstance();


        leer();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                myRef=database.getReference("users").child(""+currentFirebaseUser.getUid());
                nombre=petName.getText().toString();
                raza=petRaza.getText().toString();
                color=petColor.getText().toString();
                if(nombre.isEmpty()){ petName.setError("rellene el campo"); return;}
                if(raza.isEmpty()){ petRaza.setError("rellene el campo");return;}
                if(color.isEmpty()){ petColor.setError("rellene el campo");return;}
                Map<String, Object> newData = new HashMap<>();
                newData.put("color", color);
                newData.put("nombre", nombre);
                newData.put("raza", raza);
                myRef.updateChildren(newData);
                Toast.makeText(getContext(), "Datos actualizados", Toast.LENGTH_SHORT).show();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrar();
            }
        });

        return view;
    }

    public void leer(){

        // Read from the database
        myRef=database.getReference("users");
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        animal=new Animal();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(currentFirebaseUser.getUid()).exists()){
                  animal=dataSnapshot.child(currentFirebaseUser.getUid()).getValue(Animal.class);

                    petName.setText(animal.getNombre());
                    petColor.setText(animal.getColor());
                    petRaza.setText(animal.getRaza());

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void borrar(){
        myRef=database.getReference("users").child(""+currentFirebaseUser.getUid());
        myRef.removeValue();

    }



}
