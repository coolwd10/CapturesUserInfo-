package com.example.android.mvp.main.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mvp.core.base.BaseActivity;
import com.example.android.mvp.main.home.HomeActivity;
import com.example.android.mvp.main.login.mvp.ILoginView;
import com.example.android.mvp.main.login.mvp.LoginPresenter;
import com.example.android.utils.Constants;
import com.example.android.utils.DialogManager;
import com.example.android.utils.ProUtility;
import com.example.android.utils.SharedPreferencesManager;
import com.example.multilanguage.MainApplication;
import com.example.multilanguage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.NonNull;

//import butterknife.BindView;
//import butterknife.ButterKnife;


public class LoginActivity extends BaseActivity implements ILoginView, DialogManager.NoticeDialogListener {

    private static final String TAG = "LoginActivity";

    public static final int REQUEST_CODE_ASK_PERMISSIONS = 321;


    @Inject
    LoginPresenter mLoginPresenter;

    private DialogManager mDialogManager;

    private FirebaseAuth mAuth;
    private String mVerificationId;
    EditText otp;

    private EditText mEnterMobNumber;


    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDI();
        String va = getResources().getString(R.string.tv_info);
        mLoginPresenter.attachScreen(this);
        mDialogManager = new DialogManager(this);

        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

//        ButterKnife.bind(this);

        mEnterMobNumber = findViewById(R.id.etv_mob);

        Button button = (Button) findViewById(R.id.btn_english);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLoginView("en");
            }
        });

        Button btn_hindi = (Button) findViewById(R.id.btn_hindi);
        btn_hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLoginView("hi");
            }
        });

        Button submit = findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginPresenter.doLogin(mEnterMobNumber.getText().toString());
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public int layoutId() {
        return R.layout.activity_login;
    }

    private void initDI() {
        ((MainApplication) getApplicationContext()).getmAppComponent().inject(this);
    }


    private void loadLoginView(String langType) {
        setLanguage(langType);
    }


    void showCustomDialog(String code) {
        Dialog dialog = new Dialog(LoginActivity.this,
                android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.user_input_dialog_box);

        Button submitButton = (Button) dialog.findViewById(R.id.btnsubmit);
        EditText mOtp = (EditText) dialog.findViewById(R.id.etv_otp);
        TextView resendOtp = (TextView) dialog.findViewById(R.id.tv_resend_otp);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOtp.getText().toString() != null
                        && mOtp.getText().toString().length() > 0) {
                    verifyVerificationCode(mOtp.getText().toString());
                }
            }
        });

        dialog.show();

    }


    @Override
    public void showProgress() {
        ProUtility.showProgressDialog(this, getResources().getString(R.string.login_authenticating), true);
    }

    @Override
    public void hideProgress() {
        ProUtility.hideProgressDialog();
    }

    @Override
    public void showError(int errorType) {
        String title = "";
        String btn_ok = "";
        String msg = "";
        if (errorType == Constants.ERROR_TYPE_MOB_NUM) {

            msg = getResources().getString(R.string.msg_phone_invalid);
            btn_ok = getResources().getString(R.string.global_OK_label);
            title = getResources().getString(R.string.warning);
        }

        mDialogManager.showAlertDialog(LoginActivity.this, msg,
                DialogManager.DIALOGTYPE.DIALOG, 102, DialogManager.MSGTYPE.INFO, title,
                btn_ok, null, null, true);
    }

    private boolean validNo(String no) {
        if (no.isEmpty() || no.length() < 10) {
            mEnterMobNumber.setError("Enter a valid mobile");
            mEnterMobNumber.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void sendVerificationCode(String no) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + no,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            hideProgress();


//            sharedPreferencesManager.putBoolean("MobVerificationDone", true);
//            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);

            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                //verifying the code
                verifyVerificationCode(code);
            } else {
                showCustomDialog(code);
            }
        }

        public void onCodeAutoRetrievalTimeOut(String var1) {
            hideProgress();
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            hideProgress();
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        hideProgress();
        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            sharedPreferencesManager.putBoolean("MobVerificationDone", true);
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }


                        }
                    }
                });
    }

    @Override
    public void onDialogPositiveClick(int dialogId) {

    }

    @Override
    public void onDialogNegativeClick(int dialogId) {

    }

    @Override
    public void onDialogNeutralClick(int dialogId) {

    }
}




