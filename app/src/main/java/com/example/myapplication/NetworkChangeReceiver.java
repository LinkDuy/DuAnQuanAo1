package com.example.myapplication;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NetworkChangeReceiver extends BroadcastReceiver {
    public NetworkChangeReceiver() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        //truy cap vao máy ảo (cho phép) ktra xem mạng đang hoạt động hay không
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        Log.d("notification", "msg: bắt đâù kiểm tra mạng  ");
        //nếu như mạng đang hđ và mạng đang đc connect
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            Log.d("notification", "msg: Mạng của mày vẫn có  ");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Mất kết nối mạng");
            builder.setMessage("Vui lòng kiểm tra lại kết nối mạng của bạn và thử lại.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Chuyển hướng đến cài đặt mạng của điện thoại
                    Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    context.startActivity(settingsIntent);
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
    }
}