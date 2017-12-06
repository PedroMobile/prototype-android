package com.photozig.prototype.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.github.pwittchen.reactivenetwork.library.ConnectivityStatus;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.photozig.prototype.BaseApplication;
import com.photozig.prototype.R;

import java.io.File;
import java.util.Random;

public class Utils {


    public static int randomInRange(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public static int compareBoolean(boolean lhs, boolean rhs) {
        if( lhs && ! rhs ) {
            return +1;
        }
        if( ! lhs && rhs ) {
            return -1;
        }
        return 0;
    }

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static void displayAlert(Context context, String title, String string, DialogInterface.OnClickListener onPositiveClickListener){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(string);
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setPositiveButton(context.getString(R.string.action_ok), onPositiveClickListener);
        alert.show();
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();

    }

    public static boolean isNetworkAvailable() {
        ConnectivityStatus connectivityStatus = new ReactiveNetwork().getConnectivityStatus(BaseApplication.getApp());
        return connectivityStatus != ConnectivityStatus.OFFLINE && connectivityStatus != ConnectivityStatus.UNKNOWN;
    }
}
