package com.example.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.multilanguage.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class PermissionsActivity extends AppCompatActivity implements DialogManager.NoticeDialogListener {
    public static final int PERMISSIONS_GRANTED = 0;
    public static final int PERMISSIONS_DENIED = 1;

    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final String EXTRA_PERMISSIONS = "manalsoftech.com.cargoproject.android.permissions.EXTRA_PERMISSIONS";
    private static final String EXTRA_RATIONALE_PARAMS = "RATIONALE_PARAMS";
    private static final String PACKAGE_URL_SCHEME = "package:";

    private PermissionsChecker mPermissionChecker;
    private RationaleDialogModel mRationalDialogParams;
    private boolean mRequiresCheck;



    public static void startActivityForResult(Activity activity, int requestCode, RationaleDialogModel rationaleDialogParams, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        intent.putExtra(EXTRA_RATIONALE_PARAMS, rationaleDialogParams);
        activity.startActivityForResult(intent, requestCode, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("This Activity needs to be launched using the static startActivityForResult() method.");
        }
        setContentView(R.layout.activity_permissions);

        mPermissionChecker = new PermissionsChecker(this);
        mRequiresCheck = true;
        mRationalDialogParams = (RationaleDialogModel) getIntent().getSerializableExtra(EXTRA_RATIONALE_PARAMS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequiresCheck) {
            String[] permissions = getPermissions();
            if (mPermissionChecker.lacksPermissions(permissions)) {
                requestPermissions(permissions);
            } else {
                allPermissionsGranted();
            }
        } else {
            mRequiresCheck = true;
        }
    }

    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    private void allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            mRequiresCheck = true;
            allPermissionsGranted();
        } else {
            mRequiresCheck = false;
            showMissingPermissionDialog();
        }
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void showMissingPermissionDialog() {
        if (mRationalDialogParams != null) {
            String action = getString(R.string.not_now_text);
            DialogManager dialogManager = new DialogManager(this);

            //Failure Action can be null or empty
            if (!TextUtils.isEmpty(mRationalDialogParams.action)) {
                action = mRationalDialogParams.action;
            }

            dialogManager.showDialog(this, mRationalDialogParams.message,
                    DialogManager.DIALOGTYPE.DIALOG, 0, DialogManager.MSGTYPE.ERROR,
                    mRationalDialogParams.title, "Settings",
                    action, "");
        }
    }

    private void startAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    @Override
    public void onDialogPositiveClick(int dialogId) {
        startAppSettings();
    }

    @Override
    public void onDialogNegativeClick(int dialogId) {
        setResult(PERMISSIONS_DENIED);
        finish();
    }

    @Override
    public void onDialogNeutralClick(int dialogId) {
        //not needed
    }
}
