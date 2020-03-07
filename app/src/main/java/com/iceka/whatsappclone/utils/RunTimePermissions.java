package com.iceka.whatsappclone.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.iceka.whatsappclone.R;

import java.util.ArrayList;

public class RunTimePermissions extends Activity {

    private Activity mActivity;
    private ArrayList<PermissionBean> arrayListPermission;
    private RunTimePermissionListener mRunTimePermissionListener;

    public RunTimePermissions(Activity activity) {
        this.mActivity = activity;
    }

    public void requestPermission(String[] permission, RunTimePermissionListener runTimePermissionListener) {
        this.mRunTimePermissionListener = runTimePermissionListener;
        arrayListPermission = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String s : permission) {
                PermissionBean permissionBean = new PermissionBean();
                if (ContextCompat.checkSelfPermission(mActivity, s) == PackageManager.PERMISSION_GRANTED) {
                    permissionBean.isAccept = true;
                } else {
                    permissionBean.isAccept = false;
                    permissionBean.permission = s;
                    arrayListPermission.add(permissionBean);
                }
            }

            if (arrayListPermission.size() <= 0) {
                runTimePermissionListener.permissionGranted();
            }

            String[] arrayPermissions = new String[arrayListPermission.size()];
            for (int i = 0; i < arrayListPermission.size(); i++) {
                arrayPermissions[i] = arrayListPermission.get(i).permission;
            }
            mActivity.requestPermissions(arrayPermissions, 10);
        } else {
            if (runTimePermissionListener != null) {
                runTimePermissionListener.permissionGranted();
            }
        }
    }

    private void callSettingActivity() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
        intent.setData(uri);
        mActivity.startActivity(intent);
    }

    private void checkUpdate() {
        boolean isGranted = true;
        for (int i = 0; i < arrayListPermission.size(); i++) {
            if (!arrayListPermission.get(i).isAccept) {
                isGranted = false;

            }
        }
        if (isGranted) {
            if (mRunTimePermissionListener != null) {
                mRunTimePermissionListener.permissionGranted();
            }
        } else {
            if (mRunTimePermissionListener != null) {
                setAllertMessage();
                mRunTimePermissionListener.permissionDenied();
            }
        }
    }

    private void setAllertMessage() {
        AlertDialog.Builder adb;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            adb = new AlertDialog.Builder(mActivity, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            adb = new AlertDialog.Builder(mActivity);
        }

        adb.setTitle(mActivity.getResources().getString(R.string.app_name));
        String msg = "<p>Dear User, </p>" +
                "<p>Seems like you have <b>\"Denied\"</b> the minimum requirement permission to access more features of application.</p>" +
                "<p>You must have to <b>\"Allow\"</b> all permission. We will not share your data with anyone else.</p>" +
                "<p>Do you want to enable all requirement permission ?</p>" +
                "<p>Go To : Settings >> App > " + mActivity.getResources().getString(R.string.app_name) + " Permission : Allow ALL</p>";

        adb.setMessage(Html.fromHtml(msg));
        adb.setPositiveButton("Allow All", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callSettingActivity();
                dialogInterface.dismiss();
            }
        });

        adb.setNegativeButton("Remind Me Later", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        if (!((Activity) mActivity).isFinishing() && msg.length() > 1) {
            adb.show();
        } else {
            Log.v("log_tag", "either activity finish or message length is 0");
        }
    }

    private void updatePermissionResult(String permissions, int grantResults) {
        for (int i = 0; i < arrayListPermission.size(); i++) {
            if (arrayListPermission.get(i).permission.equals(permissions)) {
                arrayListPermission.get(i).isAccept = grantResults == 0;
                break;
            }
        }
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            updatePermissionResult(permissions[i], grantResults[i]);
        }
        checkUpdate();
    }

    public interface RunTimePermissionListener {
        void permissionGranted();

        void permissionDenied();
    }

    public class PermissionBean {
        String permission;
        boolean isAccept;
    }

}
