package com.example.nyaritabor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import javax.crypto.SecretKey;

public class RegisztracioActivity extends AppCompatActivity {

    private static  final String LOG_TAG = RegisztracioActivity.class.getName();
    private static final String Pref_Key = MainActivity.class.getPackage().toString();
    private static final int BIZTONSAGI_KULCS = 2023;
    EditText felhasznalonevEditText;
    EditText emailEditText;
    EditText jelszoEditText;
    EditText jelszoujraEditText;
    RadioGroup felhasznalotipusRadioGroup;
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisztracio);

        Bundle bundle = getIntent().getExtras();
        int biztonsagi_kulcs= bundle.getInt("BIZTONSAGI_KULCS");

        if(biztonsagi_kulcs != 2023){
            finish();
        }

        felhasznalonevEditText = findViewById(R.id.felhasznalonevEditText);
        emailEditText = findViewById(R.id.emailEditText);
        jelszoEditText = findViewById(R.id.jelszoEditText);
        jelszoujraEditText = findViewById(R.id.jelszoujraEditText);

        preferences = getSharedPreferences(Pref_Key, MODE_PRIVATE);
        String felhasznalonev = preferences.getString("felhasznalonev", "");
        String jelszo = preferences.getString("jelszo", "");

        felhasznalonevEditText.setText(felhasznalonev);
        jelszoEditText.setText(jelszo);
        jelszoujraEditText.setText(jelszo);
        felhasznalotipusRadioGroup = findViewById(R.id.felhasznalotipusRadioGroup);
        felhasznalotipusRadioGroup.check(R.id.taborozoRadioButton);

        mAuth = FirebaseAuth.getInstance();


    }

    public void regisztralas(View view) {
        String felhasznalonev = felhasznalonevEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String jelszo = jelszoEditText.getText().toString();
        String jelszoujra = jelszoujraEditText.getText().toString();

        if(!jelszo.equals(jelszoujra)){
            Log.e(LOG_TAG, "A két jelszó nem egyezik meg!");
            return;
        }else {

            Log.i(LOG_TAG, "Regisztrált:" + felhasznalonev + ", e-mail: " + email + ".");


            mAuth.createUserWithEmailAndPassword(email, jelszo).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d(LOG_TAG,"Sikeresen létrehozva!");
                        taborokraugras();
                    }else{
                        Log.d(LOG_TAG,"Sikertelene létrehozás!");
                        Toast.makeText(RegisztracioActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        
        int RG_Id = felhasznalotipusRadioGroup.getCheckedRadioButtonId();
        RadioButton radiobutton = felhasznalotipusRadioGroup.findViewById(RG_Id);
        String felhasznaloTipus = radiobutton.getText().toString();
        
    }

    public void megse(View view) {
        finish();
    }

    private void taborokraugras(/**/){
        Intent intent = new Intent(this,TaboroklistajaActivity.class);
        //intent.putExtra("BIZTONSAGI_KULCS", BIZTONSAGI_KULCS);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}