package com.example.nyaritabor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class TaboroklistajaActivity extends AppCompatActivity {


    private int querrylimit = 10;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<TaborItem> mItemList;
    private TaborItemAdapter mAdapter;
    private int gridNumber = 1;
    private boolean viewRow = true;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private NotificationHandler notificationHandler;



    private static final String LOG_TAG = TaboroklistajaActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taboroklistaja);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Ismert felhasználó" + user.toString());
        } else {
            Log.d(LOG_TAG, "Ismeretlen felhasználó ");
            finish();
        }


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();
        mAdapter = new TaborItemAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");



        querryData();
        //Log.i(LOG_TAG,"idaigjo1 ");

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(powerReciever, filter);

        notificationHandler = new NotificationHandler(this);
    }

    BroadcastReceiver powerReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action == null) {
                return;

            }
            switch (action) {
                case Intent.ACTION_POWER_CONNECTED:
                    querrylimit = 10;
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    querrylimit = 5;
                    break;
            }
            querryData();
        }
    };

    private void querryData() {

        mItemList.clear();

        mItems.orderBy("nev").limit(querrylimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                TaborItem item = doc.toObject(TaborItem.class);
                item.setId(doc.getId());
                mItemList.add(item);
            }
            if (mItemList.size() == 0) {
                intializeData();
                querryData();
            }
            mAdapter.notifyDataSetChanged();
        });


    }



    private void intializeData() {

        String[] itemsList = getResources().getStringArray(R.array.Taborok_neve);
        String[] itemsInfo = getResources().getStringArray(R.array.Taborok_leirasa);
        String[] itemsPrice = getResources().getStringArray(R.array.Tabor_ara);
        TypedArray ItemsImageResource = getResources().obtainTypedArray(R.array.Tabor_kepek);
        TypedArray ItemsRate = getResources().obtainTypedArray(R.array.Tabor_ertekelesek);
        //TypedArray ItemsFerohely = getResources().obtainTypedArray(R.array.Tabor_ferohely);
       // TypedArray Itemsjelentkezett = getResources().obtainTypedArray(R.array.Tabor_jelentkezett);
        int[] ItemsFerohely = getResources().getIntArray(R.array.Tabor_ferohely);
        int[] Itemsjelentkezett = getResources().getIntArray(R.array.Tabor_jelentkezett);
        //TypedArray Itemsemails = getResources().obtainTypedArray(R.array.Tabor_jelenkezettemail);
        String[] Itemsjelentkezettemail = getResources().getStringArray(R.array.Tabor_jelenkezettemail);



        for (int i = 0; i < itemsList.length; i++) {
            mItems.add(new TaborItem(itemsList[i], itemsInfo[i], itemsPrice[i],
                            ItemsRate.getFloat(i, 0),
                            ItemsImageResource.getResourceId(i, 0),
                            ItemsFerohely[i],
                            Itemsjelentkezett[i],
                            Itemsjelentkezettemail[i]
                            )).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(LOG_TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            // Itt tudsz további műveleteket végezni, ha szükséges

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(LOG_TAG, "Error adding document", e);
                            // Hiba esetén itt tudsz hibakezelést végezni
                        }
                    });
            //mItemList.add(new TaborItem(itemsList[i], itemsInfo[i],itemsPrice[i],ItemsRate.getFloat(i,0),ItemsImageResource.getResourceId(i,0)));
            //Log.i(LOG_TAG,"item feltoltese ");
        }

        ItemsImageResource.recycle();

    }

    public void kijelentkezes(View view) {
        Log.d(LOG_TAG, "kattintás ide: kijelentkezes");
        FirebaseAuth.getInstance().signOut();
        finish();

    }

    public void nezetvaltoztat(View view) {
        GridLayoutManager lm =(GridLayoutManager) mRecyclerView.getLayoutManager();
        Log.d(LOG_TAG, "kattintás ide: beallitasok");
        if(lm.getSpanCount() == 1)
        {
            lm.setSpanCount(2);
        }else{
            lm.setSpanCount(1);
        }

    }

    public void taboraim(View view) {
        final String[] useremail = new String[1];

        Log.d(LOG_TAG, "kattintás ide: jelentkezett");
        AtomicReference<String> usertaborai = new AtomicReference<>("Ahová jelentkeztél:");




        mItems.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                TaborItem item = doc.toObject(TaborItem.class);
                String[] emails=item.getJelentkezettemail().split(",");
                for (String email : emails) {
                    if (Objects.equals(email, user.getEmail())) {
                        usertaborai.set(usertaborai + "\n" + (item.getNev()));
                    }
                }
            }
            Log.d(LOG_TAG, String.valueOf(usertaborai)); // kiiratás ez így jó
            notificationHandler.send(String.valueOf(usertaborai));
            Toast.makeText(getApplicationContext(), String.valueOf(usertaborai), Toast.LENGTH_LONG).show();
        });


            //vissza kéne adni a usertaborait neki
        //notificationHandler.send(String.valueOf(usertaborai));
        //Log.d(LOG_TAG, "értesités előtte");
        //notificationHandler.send("?!?!?!?");

    }

    public void kereses(View view) {
        EditText szoveg = findViewById(R.id.keresesET);
        mAdapter.getFilter().filter(szoveg.getText().toString());
    }

    public void frissitesjelentkezessel(TaborItem item){
        if(user.isAnonymous()){
            Log.e(LOG_TAG,"Regisztráció szükséges!");
            Toast.makeText(getApplicationContext(), "Regisztráció szükséges!", Toast.LENGTH_SHORT).show();

        }else{
            String emails = item.getJelentkezettemail();
            String[] emailtb=emails.split(",");
            boolean benne=false;
            for(int i = 0; i< emailtb.length;i++){
                if(Objects.equals(emailtb[i], user.getEmail())){
                    benne=true;
                }
            }
            if(benne){
                Log.e(LOG_TAG,"Már jelentkeztél!");
                Toast.makeText(getApplicationContext(), "Már jelentkeztél!", Toast.LENGTH_SHORT).show();
            }else{
                String ujemailes;
                if(emails == ""){
                    ujemailes= user.getEmail();
                }else{
                    ujemailes= emails+","+user.getEmail();
                }
                mItems.document(item._getId()).update("jelentkezett_db", item.getJelentkezett_db() + 1);
                mItems.document(item._getId()).update("jelentkezettemail",ujemailes );
                querryData();
                Log.e(LOG_TAG,"Sikeresen jelentkeztél!");
                Toast.makeText(getApplicationContext(), "Sikeresen jelentkeztél!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void jelentkezesVissazvonasa(TaborItem item) {

        if(user.isAnonymous()){
            Log.e(LOG_TAG,"Regisztráció szükséges!");
            Toast.makeText(getApplicationContext(), "Regisztráció szükséges!", Toast.LENGTH_SHORT).show();
        }else{
            String emails = item.getJelentkezettemail();
            String[] emailtb=emails.split(",");
            boolean benne=false;
            for(int i = 0; i< emailtb.length;i++){
                if(Objects.equals(emailtb[i], user.getEmail())){
                    benne=true;
                }
            }
            if(benne){
                String ujemailes="";
                for(int i = 0; i< emailtb.length;i++){
                    if(Objects.equals(emailtb[i], user.getEmail())){
                        //semmit ezt ki kell hagyni
                    }else{
                        if(ujemailes == ""){
                            ujemailes= emailtb[i];
                        }else{
                            ujemailes= emails+","+emailtb[i];
                        }
                    }
                }

                mItems.document(item._getId()).update("jelentkezett_db", item.getJelentkezett_db() - 1);
                mItems.document(item._getId()).update("jelentkezettemail",ujemailes );
                querryData();
                Log.e(LOG_TAG,"Sikeresen visszavontad a jelentkezés!");
                Toast.makeText(getApplicationContext(), "Sikeresen visszavontad a jelentkezés!", Toast.LENGTH_SHORT).show();
            }else{
                Log.e(LOG_TAG,"Még nem is voltál jelentkezve!");
                Toast.makeText(getApplicationContext(), "Még nem is voltál feljelentkezve!", Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void taborletrehozasoldalra(){
        Intent intent = new Intent(this,taborletrehoz.class);
        startActivity(intent);
    }
    public void ujtaborletrehozasa(View view) {
        taborletrehozasoldalra();

    }
}