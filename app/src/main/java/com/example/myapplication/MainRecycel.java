package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainRecycel extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<User> UserArrayList;
    UserAdapter myUserAdapter;
    FirebaseFirestore db;
    Button btnUPDATE;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycel);

        recyclerView = findViewById(R.id.reCycel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        UserArrayList = new ArrayList<User>();
        myUserAdapter = new UserAdapter(MainRecycel.this,UserArrayList);
        recyclerView.setAdapter(myUserAdapter);
        EvenchangeListener();
    }


    private void EvenchangeListener() {
        db.collection("DuyLinh").orderBy("user", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Log.e("User error",error.getMessage());
                            return;
                        }
                        for(DocumentChange documentChange : value.getDocumentChanges()){
                            if(documentChange.getType()== DocumentChange.Type.ADDED){
                                UserArrayList.add(documentChange.getDocument().toObject(User.class));
                            }
                            myUserAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}