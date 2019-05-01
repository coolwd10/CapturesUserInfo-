package com.example.android.mvp.main.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.mvp.core.base.BaseActivity;
import com.example.android.mvp.main.detail.mvp.DetailActivity;
import com.example.android.mvp.main.home.mvp.HomePresenter;
import com.example.android.mvp.main.home.mvp.IHomeView;
import com.example.android.utils.Permissions;
import com.example.android.utils.PermissionsActivity;
import com.example.android.utils.PermissionsChecker;
import com.example.android.utils.RationaleDialogModel;
import com.example.data.model.Product;
import com.example.multilanguage.MainApplication;
import com.example.multilanguage.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeActivity extends BaseActivity implements IHomeView {

    private static final String TAG = "LoginActivity";
    public static int CAMERA = 1;
    private static final String IMAGE_DIRECTORY = "/profileImage";

    private static final String[] MESSAGE_PERMISSIONS = new String[]{Permissions.CAMERA,
            Permissions.READ_EXTERNAL_STORAGE, Permissions.WRITE_EXTERNAL_STORAGE};
    @Inject
    HomePresenter mHomePresenter;

    private Locale mLocale;
    @BindView(R.id.input_name)
    EditText mName;

    @BindView(R.id.input_lastname)
    EditText mAddress;

    private ImageView mProfileImage;

    private boolean isUpdateButton;
    private PermissionsChecker mPermissionChecker;

    private String mImagePath;

    @OnClick(R.id.btn_english)
    public void onSignUpButtonClicked() {
        loadLoginView("en");
    }

    @OnClick(R.id.btn_hindi)
    public void onRegistrationClicked() {
        loadLoginView("hi");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDI();

        mLocale = getCurrentLanguage();
        mHomePresenter.attachScreen(this);

        mHomePresenter.read(mLocale.getLanguage());
        mPermissionChecker = new PermissionsChecker(this);

        ButterKnife.bind(this);
        mProfileImage = findViewById(R.id.img_profile);
        profileImageClick();

    }


    @OnClick(R.id.profile_btn_edit)
    public void onSaveUpdateBtnClicked() {
        if (mAddress == null || mName == null || mAddress.length() == 0 || mName.length() == 0) {
            Toast.makeText(HomeActivity.this, getResources().getString(R.string.empty_feild_error),
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (isUpdateButton) {
            mHomePresenter.update(mLocale.getLanguage(),
                    mName.getText().toString(), mAddress.getText().toString(), mImagePath);
        } else {
            mHomePresenter.create(mLocale.getLanguage(),
                    mName.getText().toString(), mAddress.getText().toString(), mImagePath);
        }
    }

    @OnClick(R.id.btn_share)
    public void shareBtnClick() {
        Intent i = new Intent(HomeActivity.this,
                DetailActivity.class);
        startActivity(i);
    }

     public void profileImageClick() {
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

    }

    private boolean checkPermission() {
        if (mPermissionChecker.lacksPermissions(MESSAGE_PERMISSIONS)) {
            startPermissionsActivity();
            return false;
        }
        takePhotoFromCamera();
        return true;
    }

    public static final int REQUEST_CODE_ASK_PERMISSIONS = 321;

    private void startPermissionsActivity() {
        RationaleDialogModel permissionParams = new RationaleDialogModel();
        permissionParams.title = "Error";
        permissionParams.message = "Need These permission.";
        permissionParams.action = "Not Now";
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE_ASK_PERMISSIONS, permissionParams, MESSAGE_PERMISSIONS);
    }

    private void permissionDenied() {
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
    }

    private void permissionAccepted() {
        takePhotoFromCamera();
    }

    private void takePhotoFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            mProfileImage.setImageBitmap(thumbnail);
            mImagePath = IMAGE_DIRECTORY+"/"+mLocale.getLanguage()+".jpg";
             saveImage(thumbnail);
            Toast.makeText(HomeActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, mLocale.getLanguage() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public int layoutId() {
        return R.layout.activity_profile;
    }

    private void initDI() {
        ((MainApplication) getApplicationContext()).getmAppComponent().inject(this);
    }


    private void loadLoginView(String langType) {
        setLanguage(langType);
    }


    @Override
    public void sendVerificationCode(String no) {

    }

    @Override
    public void setUserInfo(Product product) {
        if (product == null) {
            return;
        }

        String photoPath = Environment.getExternalStorageDirectory() + product.getImagePath();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mName.setText(product.getName() == null ? "" : product.getName());
                mAddress.setText(product.getAddress() == null ? "" : product.getAddress());

//                final File file = new File(Environment.getExternalStorageDirectory()
//                        .getAbsolutePath(), "hi");
//
//                File downloadsFolder= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//                Uri file1=Uri.fromFile(new File(downloadsFolder,"person.png"));

                Bitmap bitmap1 = BitmapFactory.decodeFile(photoPath);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

                if(bitmap!=null) {
                    mProfileImage.setImageBitmap(bitmap);
                }

            }
        });
        if (product.getName() != null && product.getName().length() > 0 &&
                product.getAddress() != null && product.getAddress().length() > 0) {
            isUpdateButton = true;
        }
    }

//    public static String getCacheFilename() {
//        File f = getSavePath();
//        return f.getAbsolutePath() + "/cache.png";
//    }
//
//    public static File getSavePath() {
//        File path;
//        if (hasSDCard()) { // SD card
//            path = new File(getSDCardPath() + "/Tegaky/");
//            path.mkdir();
//        } else {
//            path = Environment.getDataDirectory();
//        }
//        return path;
//    }
//
//    public static boolean hasSDCard() { // SD????????
//        String status = Environment.getExternalStorageState();
//        return status.equals(Environment.MEDIA_MOUNTED);
//    }
//    public static String getSDCardPath() {
//        File path = Environment.getExternalStorageDirectory();
//        return path.getAbsolutePath();
//    }

    @Override
    public void showToastMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HomeActivity.this, getResources().getString(R.string.update_rec),
                        Toast.LENGTH_LONG).show();
            }
        });

        isUpdateButton = true;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(int errorType) {

    }
}




