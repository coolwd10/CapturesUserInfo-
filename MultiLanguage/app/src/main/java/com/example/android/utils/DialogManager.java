package com.example.android.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.multilanguage.R;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class DialogManager {

    NoticeDialogListener mListener;


    private DialogManager() {
    }

    public DialogManager(NoticeDialogListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("NoticeDialogListener object can't be null");
        } else {
            this.mListener = listener;
        }


    }

    public void showDialog(Activity activity, String msg, Enum type, int id, MSGTYPE messageType, String title, String positiveBtnName, String negativeBtnName, String neutralBtnName) {
        showAlertDialog(activity, msg, type, id, messageType, title, positiveBtnName, negativeBtnName, neutralBtnName, true);
    }

    public void showNonCancellableDialog(Activity activity, String msg, Enum type, int id,
                                         MSGTYPE messageType, String title, String positiveBtnName,
                                         String negativeBtnName, String neutralBtnName) {
        showAlertDialog(activity, msg, type, id, messageType, title, positiveBtnName, negativeBtnName, neutralBtnName, false);
    }

    public void showAlertDialog(Activity activity, String msg, Enum type,
                                int id, MSGTYPE messageType, String title,
                                String positiveBtnName, String negativeBtnName, String neutralBtnName,
                                boolean isCancellable) {
        showAlertDialog(activity, msg, type,
                id, messageType, title,
                null, 0,
                positiveBtnName, negativeBtnName, neutralBtnName, 0,
                isCancellable);
    }

    public void showAlertDialog(@NonNull Activity activity, String msg, Enum type,
                                final int id, MSGTYPE messageType, String title,
                                String subTitle, @DrawableRes int warningImageId,
                                String positiveBtnName, String negativeBtnName, String neutralBtnName, @ColorRes int ctButtonBackgroundColor,
                                boolean isCancellable) {
        if (activity != null) {

            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(isCancellable);
            dialog.setCanceledOnTouchOutside(isCancellable);
            dialog.setContentView(R.layout.dialog_generic);

            TextView titleTextView = findById(dialog, R.id.dialog_title);
            TextView subtitleTextView = findById(dialog, R.id.dialog_subtitle);
            TextView messageTextView = findById(dialog, R.id.dialog_msg);
            TextView dialogButtonPositive = findById(dialog, R.id.btn_positive);
            TextView dialogButtonCancel = findById(dialog, R.id.btn_negative);
            TextView dialogButtonNeutral = findById(dialog, R.id.btn_neutral);
            ImageView imgWarning = findById(dialog, R.id.img_warning);

            if (warningImageId == 0) {
                imgWarning.setVisibility(GONE);
            } else {
                imgWarning.setVisibility(VISIBLE);
                imgWarning.setImageResource(warningImageId);
            }
            if (isEmpty(title) && warningImageId != 0) {
                titleTextView.setVisibility(GONE);
            } else if (isEmpty(title)) {
                titleTextView.setVisibility(VISIBLE);
                titleTextView.setText(R.string.app_name);
            } else {
                titleTextView.setVisibility(VISIBLE);
                titleTextView.setText(title);
            }

            showHideSetView(subTitle, subtitleTextView);
            messageTextView.setText(msg);

            if (ctButtonBackgroundColor != 0) {
                dialogButtonPositive.setBackgroundResource(ctButtonBackgroundColor);
                dialogButtonNeutral.setBackgroundResource(ctButtonBackgroundColor);
                dialogButtonCancel.setBackgroundResource(ctButtonBackgroundColor);

                int textColor = ContextCompat.getColor(activity, R.color.white);
                dialogButtonPositive.setTextColor(textColor);
                dialogButtonNeutral.setTextColor(textColor);
                dialogButtonCancel.setTextColor(textColor);
            }

            if (!(isEmpty(positiveBtnName) && isEmpty(negativeBtnName) && isEmpty(neutralBtnName))) {
                showHideSetView(positiveBtnName, dialogButtonPositive);
                showHideSetView(negativeBtnName, dialogButtonCancel);
                showHideSetView(neutralBtnName, dialogButtonNeutral);
            } else {
                dialogButtonCancel.setVisibility(GONE);
                dialogButtonNeutral.setVisibility(GONE);
            }
            dialogButtonPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    mListener.onDialogPositiveClick(id);
                }
            });
            dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    mListener.onDialogNegativeClick(id);
                }
            });
            dialogButtonNeutral.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    mListener.onDialogNeutralClick(id);
                }
            });
            dialog.show();
        }
    }

    @SuppressWarnings({"unchecked", "UnusedDeclaration"}) // Checked by runtime cast. Public API.
    private <T extends View> T findById(Dialog dialog, int dialog_title) {
        return ((T) dialog.findViewById(dialog_title));
    }

    private void showHideSetView(String btnName, TextView view) {
        if (!isEmpty(btnName)) {
            view.setVisibility(VISIBLE);
            view.setText(btnName);
        } else {
            view.setVisibility(GONE);
        }
    }

    public enum DIALOGTYPE {
        DIALOG, SNACKBAR
    }

    public enum MSGTYPE {
        INFO, ERROR, WARNING
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(int dialogId);

        public void onDialogNegativeClick(int dialogId);

        public void onDialogNeutralClick(int dialogId);
    }

}

