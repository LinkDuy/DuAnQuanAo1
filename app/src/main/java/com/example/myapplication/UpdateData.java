package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdateData extends AppCompatActivity {
    EditText edtnewPass;
    EditText edtCFPass;
    Button btUpdate, btnBacKIn, btnBackOut;
    FirebaseFirestore db;
    String NameMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();

        NetworkChangeReceiver networkChangeReceiver= new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

        if(bd!=null){
            NameMail = bd.getString("meo");
        }

        db = FirebaseFirestore.getInstance();
        btUpdate=findViewById(R.id.btnnUpdate);
        edtnewPass = findViewById(R.id.edtNewPass);
        edtCFPass = findViewById(R.id.edtConfirmPass);
        btnBackOut = findViewById(R.id.btnBackLogout);

        btnBacKIn = findViewById(R.id.btnBackLogin);
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String neww = edtnewPass.getText().toString().trim();
                Updatedata(neww);
            }
        });
        btnBacKIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChange();
            }
        });
        btnBackOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChange1();
            }
        });
    }
    private void Updatedata(String newPass){
        if (isEmpty(edtnewPass)) {
            edtnewPass.setError("Enter the Password!");
        }
        if (isEmpty(edtCFPass)) {
            edtCFPass.setError("Enter the Confirm Password!");
        }
        if(isPass(edtnewPass, edtCFPass)==false){
            Toast.makeText(this, "Enter the Confirm Password!", Toast.LENGTH_SHORT).show();
        }
        if(isEmpty(edtnewPass)==false&&isEmpty(edtnewPass)==false&&isPass(edtnewPass, edtCFPass)==true){
            Map<String, Object> passchange = new HashMap<>();
            passchange.put("password",newPass);
            db.collection("DuyLinh").whereEqualTo("email",NameMail).
                    get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()&&!task.getResult().isEmpty()){
                                DocumentSnapshot documentSnapshot= task.getResult().getDocuments().get(0);
                                String id = documentSnapshot.getId();
                                db.collection("DuyLinh").document(id).update(passchange).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(UpdateData.this, "Update Success", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UpdateData.this, "Fail", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(UpdateData.this, "Fail strong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    public void onClickChange(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateData.this);
        builder.setTitle("THÔNG BÁO");
        builder.setMessage("Bạn muốn Thoát chứ ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Chuyển hướng đến cài đặt mạng của điện thoại
                Intent intent = new Intent(UpdateData.this,DangNhapMainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    public void onClickChange1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateData.this);
        builder.setTitle("THÔNG BÁO");
        builder.setMessage("Bạn muốn Thoát chứ ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Chuyển hướng đến cài đặt mạng của điện thoại
                Intent intent = new Intent(UpdateData.this,DangKyActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
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
}