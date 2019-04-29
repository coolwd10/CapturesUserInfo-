package com.example.android.mvp.main.detail.mvp;

import android.content.ActivityNotFoundException;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mvp.core.base.BaseActivity;
import com.example.android.mvp.main.home.HomeActivity;
import com.example.android.mvp.main.home.mvp.HomePresenter;
import com.example.android.mvp.main.home.mvp.IHomeView;
import com.example.android.utils.Permissions;
import com.example.android.utils.PermissionsActivity;
import com.example.android.utils.PermissionsChecker;
import com.example.android.utils.RationaleDialogModel;
import com.example.data.model.Product;
import com.example.multilanguage.BuildConfig;
import com.example.multilanguage.MainApplication;
import com.example.multilanguage.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;

//import butterknife.BindView;
//import butterknife.ButterKnife;


public class DetailActivity extends BaseActivity implements IDetailView {

    private static final String TAG = "DetailActivity";

    @Inject
    DetailPresenter mDetailPresenter;

    private TextView mName;
    private TextView mAdress;
    private ImageView mProfileImage;
    private Button mShare;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDI();
        mDetailPresenter.attachScreen(this);
        mDetailPresenter.read(getCurrentLanguage().getLanguage());
        mName =  findViewById(R.id.tv_name);
        mAdress =  findViewById(R.id.tv_adress);
        mProfileImage =  findViewById(R.id.img_profile);
        mShare =  findViewById(R.id.btn_share);
        onShareBtnCkick();

        rootView = getWindow().getDecorView().findViewById(android.R.id.content);

    }

    private void onShareBtnCkick() {
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Bitmap bitmap = getScreenShot(rootView);
                store(bitmap,"profile.jpg");
                //shareImage(mFile);

                Uri imgUri = Uri.parse(mFile.getAbsolutePath());
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("image/jpeg");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");

                whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
                whatsappIntent.setType("image/jpeg");
                whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DetailActivity.this, "Whatsapp have not been installed.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    File mFile;
    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public  void store(Bitmap bm, String fileName){
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        mFile = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(mFile);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "No App Available", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public int layoutId() {
        return R.layout.profile_view;
    }

    private void initDI() {
        ((MainApplication) getApplicationContext()).getmAppComponent().inject(this);
    }


    @Override
    public void setUserInfo(Product product) {
        if(product==null){
            Toast.makeText(DetailActivity.this, getResources().getString(R.string.no_recored),
                    Toast.LENGTH_LONG).show();
        }

        mName.setText(product.getName());
        mAdress.setText(product.getAddress());

        String photoPath = Environment.getExternalStorageDirectory() + product.getImagePath();
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        if(bitmap!=null) {
            mProfileImage.setImageBitmap(bitmap);
        }
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




