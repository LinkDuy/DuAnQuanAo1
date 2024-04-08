package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.Context;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHoder> {
    ArrayList<User> userArrayList;
    MainRecycel mainRecycel;
    public UserAdapter(MainRecycel mainActivity, ArrayList<User> userArrayList) {
        this.mainRecycel = mainActivity;
        this.userArrayList = userArrayList;
    }
    @NonNull
    @Override
    public UserViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new UserViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHoder holder, int position) {
        User us = userArrayList.get(position);
        holder.txtUSER.setText(us.getUser());
        holder.txtNAME.setText(us.getName());
        holder.txtPASS.setText(us.getPassword());
    }

    @Override
    public int getItemCount() {

        return userArrayList.size();
    }

    public class UserViewHoder extends RecyclerView.ViewHolder {
        private TextView txtNAME;
        private TextView txtPASS;
        private TextView txtUSER;
        private Button btnUpDate;
//        private Button btnDele;

        public UserViewHoder(@NonNull View itemView) {
            super(itemView);
            txtUSER = itemView.findViewById(R.id.tvUser);
//            btnDele = itemView.findViewById(R.id.btnDelete);
            txtNAME = itemView.findViewById(R.id.tvName);
            txtPASS = itemView.findViewById(R.id.tvPass);
            btnUpDate = itemView.findViewById(R.id.btnUpdate);
        }
    }
}
