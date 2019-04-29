package com.example.android.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.multilanguage.R;



public class ProUtility {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9999;

    private static ProgressDialog mProgressDialog;


    //TODO : This was done for a review quickly, ideally everyone shud use this so that we control whether a loader is bloking or not !
    public static void showProgressDialog(Context activity, String message, boolean cancelable) {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                return;
            }

            mProgressDialog = new ProgressDialog(activity);
            if (message == null) {
                message = activity.getResources().getString(R.string.progress_dialog_message);
            }
            mProgressDialog.setMessage(message);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setCanceledOnTouchOutside(cancelable);
            mProgressDialog.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isProgressDialogShown() {
        boolean isShown = false;

        if (mProgressDialog != null) {
            isShown = mProgressDialog.isShowing();
        }

        return isShown;
    }

    public static void hideProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void hideKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (editText != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            editText.clearFocus();
        }
    }

    public static void showKeyboard(Context context, EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (editText != null) {
            imm.showSoftInput(editText, 0);
        }
    }

//    public static boolean checkPlayServices(Activity activity) {
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (apiAvailability.isUserResolvableError(resultCode)) {
//                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Toast.makeText(activity, "device does not support", Toast.LENGTH_SHORT).show();
//                activity.finish();
//            }
//            return false;
//        }
//        return true;
//    }




}
