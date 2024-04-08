package com.example.myapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DangKyActivity extends AppCompatActivity {

    Boolean check=true;
    Boolean checknut1=true, checknut2=true;
    Boolean ck1 = false, ck2 = false, ck3 = false;
    private Button btnDangNhap,btnDangKi;
    private EditText etUser, etName,etPass,etConfirm,dtMail;
    private ImageButton btPass,btConfirm;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        NetworkChangeReceiver networkChangeReceiver= new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

//        networkChangeReceiver.onReceive();
        dtMail = findViewById(R.id.edtMail);
        progressBar = findViewById(R.id.progressBar);
        btnDangNhap = findViewById(R.id.btnSigin);
        btnDangKi = findViewById(R.id.btnSignUp);
        etUser = findViewById(R.id.edtUser);
        etName = findViewById(R.id.edtName);
        etPass = findViewById(R.id.edtPassWd);
        etConfirm = findViewById(R.id.edtConFirm);
        btPass = findViewById(R.id.btnPass);
        btConfirm =findViewById(R.id.btnConfim);
        btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =etName.getText().toString().trim();
                String Us  = etUser.getText().toString().trim();
                String pass = etPass.getText().toString().trim();
                String email = dtMail.getText().toString().trim();
                User us = new User(Us,name,pass,email);
                Check(us);
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangKyActivity.this, DangNhapMainActivity.class);
                startActivity(intent);
            }
        });
        btPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checknut2){
                    etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                checknut2=!checknut2;
            }
        });
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checknut1){
                    etConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    etConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                checknut1=!checknut1;
            }
        });


    }
    public void Check(User us){
        if (isEmpty(etUser)) {
            etUser.setError("Enter the UserName");
        }
        if (isEmpty(etPass)) {
            etPass.setError("Enter the Password!");
        }
        if (isEmpty(etConfirm)) {
            etConfirm.setError("Enter the Confirm Password!");
        }
        if (isEmpty(etName)) {
            etName.setError("Enter the Name!");
        }
        if (isEmpty(dtMail)) {
            dtMail.setError("Enter the Email!");
        }
        if(isPass(etPass, etConfirm)==false){
            Toast.makeText(this, "Enter the Confirm Password!", Toast.LENGTH_SHORT).show();
        }
        if(isEmpty(dtMail)==false&&isEmpty(etUser)==false&&isEmpty(etPass)==false&&isEmpty(etConfirm)==false&&isEmpty(etName)==false&&isPass(etPass, etConfirm)==true) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("DuyLinh")
                    .whereEqualTo("user",etUser.getText().toString())
                    .get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()&&task.getResult()!=null&&task.getResult().getDocuments().size()>0){
                            Toast.makeText(this,"Da ton tai!",Toast.LENGTH_SHORT).show();
                        }else{
//                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("DuyLinh")
                            .add(us)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(DangKyActivity.this,"Success",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(DangKyActivity.this,"Fail",Toast.LENGTH_SHORT).show();

                        }
                    });
                        }
                    });
        }
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean isPass(EditText text1, EditText text2){
        if(text1.getText().toString().equals(text2.getText().toString())){
            return true;
        }else{
            return false;
        }
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}