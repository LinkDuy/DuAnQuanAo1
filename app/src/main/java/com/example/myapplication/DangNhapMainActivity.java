package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Random;

public class DangNhapMainActivity extends AppCompatActivity {
    EditText edtUser, edtPass;
    Button btnIn,btnUpdatee;
    private List<User> mListUser;
    private RecyclerView rcUsers;
    private UserAdapter userAdapter;
    ImageButton btnHienthi;
    TextView txtSigupp,txtForgot;
    Boolean check = true;
    Boolean ck1 = false, ck2 = false;
    private ProgressBar progressBar3;
    private String truyenDL;
    private String generatedOTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap_main);
        edtUser  = findViewById(R.id.edtUserin);
        edtPass = findViewById(R.id.edtPassin);
        btnIn = findViewById(R.id.btnSignin);
        btnHienthi = findViewById(R.id.btnPass1);
        txtSigupp = findViewById(R.id.txtSigup);
        progressBar3 = findViewById(R.id.progressBar3);
        txtForgot = findViewById(R.id.txtForgot);

        NetworkChangeReceiver networkChangeReceiver= new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar3.setVisibility(ProgressBar.VISIBLE);
                checkDataEntered();
            }
        });
        txtSigupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapMainActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });
        btnHienthi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check){
                    edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                check=!check;
            }
        });
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChange();
            }
        });
    }
    public void onClickChange(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //tao layout custom
        dialog.setContentView(R.layout.layout_dialog_update);
        //Khai bao window de chinh sua
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //khi ma click ra ngoai dialog k thoat ddc nen can 1 button de cancel
        dialog.setCancelable(false);

//        EditText etName = dialog.findViewById(R.id.edtUpdateName);
        Button btCancel = dialog.findViewById(R.id.btnCancel);
        Button btUpdate = dialog.findViewById(R.id.btnUpdate);
        EditText etPass = dialog.findViewById(R.id.edtUpdateUser);


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("DuyLinh")
                    .whereEqualTo("email",etPass.getText().toString())
                    .get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()&&task.getResult()!=null&&task.getResult().getDocuments().size()>0){
                            String email = etPass.getText().toString().trim();
                            truyenDL = email;
                            Log.d("otp", "email -> " + email);
                            sendOTP(email);
                            dialog.dismiss();
                            checkOTP();
                        }else{
                            Toast.makeText(DangNhapMainActivity.this, "Sai gmail", Toast.LENGTH_SHORT).show();
                        }
                    });
//            }
            }
        });
        dialog.show();
    }


    public void checkOTP(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //tao layout custom
        dialog.setContentView(R.layout.item_check_otp);
        //Khai bao window de chinh sua
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //khi ma click ra ngoai dialog k thoat ddc nen can 1 button de cancel
        dialog.setCancelable(false);

        Button btCancel = dialog.findViewById(R.id.btnCancelOTP);
        Button btUpdate = dialog.findViewById(R.id.btnCheckOTP);
        EditText edtOTP = dialog.findViewById(R.id.edtCheckOTP);


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = edtOTP.getText().toString().trim();
                verifyOTP(otp);
            }
        });
        dialog.show();
    }

    private void sendOTP(String email) {
        // Kiểm tra và xác thực email
        // Tạo mã OTP ngẫu nhiên
        generatedOTP = generateOTP();
        // Gửi OTP qua email
        EmailSender emailSender = new EmailSender(email, "OTP Verification", "Your OTP is: " + generatedOTP);
        emailSender.execute();
        Log.d("otp", "sendOTP: " + generatedOTP);
        Toast.makeText(this, "OTP sent to your email.", Toast.LENGTH_SHORT).show();
    }

    private void verifyOTP(String otp) {
        if (otp.equals(generatedOTP)) {
            // Xác thực thành công

            Toast.makeText(this, "OTP verification successful.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DangNhapMainActivity.this, UpdateData.class);
            intent.putExtra("meo",truyenDL);
            startActivity(intent);
        } else {
            // Xác thực thất bại
            Toast.makeText(this, "OTP verification failed.", Toast.LENGTH_SHORT).show();
        }
    }
    private String generateOTP() {
        // Tạo mã OTP ngẫu nhiên gồm 4 chữ số
        Random random = new Random();
        int otp = random.nextInt(9000) + 1000;
        return String.valueOf(otp);
    }
    void checkDataEntered(){
        if (isEmpty(edtUser)) {
            edtUser.setError("Enter the User Name!");
            progressBar3.setVisibility(ProgressBar.GONE);
        }else{
            ck1 = true;
        }
        if (isEmpty(edtPass)) {
            edtPass.setError("Enter the Password!");
            progressBar3.setVisibility(ProgressBar.GONE);
        }else{
            ck2 = true;
        }
        if(ck1&&ck2){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("DuyLinh")
                    .whereEqualTo("user",edtUser.getText().toString())
                    .whereEqualTo("password",edtPass.getText().toString())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()&&task.getResult()!=null&&task.getResult().getDocuments().size()>0){
                           progressBar3.setVisibility(ProgressBar.GONE);
                            Toast a = Toast.makeText(DangNhapMainActivity.this, "Success", Toast.LENGTH_SHORT);
                            a.show();
                            Intent intent = new Intent(DangNhapMainActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            progressBar3.setVisibility(ProgressBar.GONE);
                            Toast a = Toast.makeText(DangNhapMainActivity.this, "Fail", Toast.LENGTH_SHORT);
                            a.show();
                        }
                    });
        }
    }
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}