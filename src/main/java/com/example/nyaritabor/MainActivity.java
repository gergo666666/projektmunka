package com.example.nyaritabor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private static  final String LOG_TAG = MainActivity.class.getName();
    private static final String Pref_Key = MainActivity.class.getPackage().toString();
    private static final int BIZTONSAGI_KULCS = 2023;

    private static final int RC_SIGN_IN = 123;

    EditText felhasznalonevET;
    EditText jelszoET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        felhasznalonevET = findViewById(R.id.editTextFelhasznaloNev);
        jelszoET = findViewById(R.id.editTextJelszo);

        preferences = getSharedPreferences(Pref_Key, MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(LOG_TAG,"Google: "+account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

            }catch (ApiException e){
                Log.w(LOG_TAG,e);
            }
        }
    }

    public void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG,"Sikeresen bejelentkezve!");
                    taborokraugras();
                }else{
                    Log.d(LOG_TAG,"Sikertelen bejelentkezés!");
                    Toast.makeText(MainActivity.this, "Sikertelen bejelentkezés:"+task.getException().getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void login(View view) {

        String felhasznalonev = felhasznalonevET.getText().toString();
        String jelszo = jelszoET.getText().toString();
        if(felhasznalonev == "" || jelszo == ""){
            Log.e(LOG_TAG, "Ures");
        }else {
            // Log.i(LOG_TAG,"Bejelentkezett:" + felhasznalonev + ", jelszo: "+ jelszo + ".");
            mAuth.signInWithEmailAndPassword(felhasznalonev, jelszo).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "Sikeresen bejelentkezve!");
                        taborokraugras();
                    } else {
                        Log.d(LOG_TAG, "Sikertelen bejelentkezés!");
                        Toast.makeText(MainActivity.this, "Sikertelen bejelentkezés:" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }

            });
        }
        //szovegelker
        //kiiraslogba
    }
    private void taborokraugras(){
        Intent intent = new Intent(this,TaboroklistajaActivity.class);
        //intent.putExtra("BIZTONSAGI_KULCS", BIZTONSAGI_KULCS);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisztracioActivity.class);
        intent.putExtra("BIZTONSAGI_KULCS",BIZTONSAGI_KULCS);

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
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("felhasznalonev", felhasznalonevET.getText().toString());
        editor.putString("jelszo", jelszoET.getText().toString());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void vendeg(View view) {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "Vendégként bejelentkezve!");
                    taborokraugras();
                } else {
                    Log.d(LOG_TAG, "Sikertelen bejelentkezés!");
                    Toast.makeText(MainActivity.this, "Sikertelen bejelentkezés:" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void googleval(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);

    }
}