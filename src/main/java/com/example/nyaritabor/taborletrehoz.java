package com.example.nyaritabor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class taborletrehoz extends AppCompatActivity {

    EditText nev;
    EditText ar;
    EditText info;
    EditText ferohely;
    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taborletrehozas);

        nev = findViewById(R.id.tabrornevEditText);
        ar = findViewById(R.id.arEditText);
        info = findViewById(R.id.infoEditText);
        ferohely = findViewById(R.id.ferohelyEditText);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");

    }


    public void taborveglegesit(View view) {
        String cnev = nev.getText().toString();
        String cinfo = info.getText().toString();
        String car = ar.getText().toString();
        float csillag =0;
        int kep = R.drawable.mernokos;
        int cferohely = Integer.parseInt(ferohely.getText().toString());
        int jelentkezett=0;
        String emails ="";

        TaborItem uj = new TaborItem(cnev,cinfo,car,csillag,kep,cferohely,jelentkezett,emails);

        mItems.add(uj)
                .addOnSuccessListener(documentReference -> {
                    // Sikeres hozzáadás esetén
                    Toast.makeText(getApplicationContext(), "Sikeresen létrehozva!", Toast.LENGTH_LONG).show();
                    // Itt tudsz további műveleteket végezni, ha szükséges
                })
                .addOnFailureListener(e -> {
                    // Hiba esetén
                    Toast.makeText(getApplicationContext(), "Hiba történt!", Toast.LENGTH_LONG).show();
                    // Itt tudsz kezelni bármilyen hibaüzenetet vagy hibakezelést
                });

        Intent intent = new Intent(this,TaboroklistajaActivity.class);
        startActivity(intent);

    }

}
