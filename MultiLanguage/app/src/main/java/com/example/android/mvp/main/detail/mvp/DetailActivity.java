package com.example.android.mvp.main.detail.mvp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mvp.core.base.BaseActivity;
import com.example.data.model.Product;
import com.example.multilanguage.MainApplication;
import com.example.multilanguage.R;

import java.io.File;
import java.io.FileOutputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends BaseActivity implements IDetailView {

    private static final String TAG = "DetailActivity";

    @Inject
    DetailPresenter mDetailPresenter;

    @BindView(R.id.tv_name)
    TextView mName;
    @BindView(R.id.tv_adress)
    TextView mAdress;
    @BindView(R.id.img_profile)
    ImageView mProfileImage;

    @BindView(R.id.btn_share)
    Button mShare;

    private View rootView;
    private File mFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDI();
        ButterKnife.bind(this);
        mDetailPresenter.attachScreen(this);
        mDetailPresenter.read(getCurrentLanguage().getLanguage());
        rootView = getWindow().getDecorView().findViewById(android.R.id.content);
    }

    @OnClick(R.id.btn_share)
    void onShareBtnCkick() {
        Bitmap bitmap = getScreenShot(rootView);
        store(bitmap, "profile.jpg");
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


    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void store(Bitmap bm, String fileName) {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if (!dir.exists())
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (product == null) {
                    Toast.makeText(DetailActivity.this, getResources().getString(R.string.no_recored),
                            Toast.LENGTH_LONG).show();
                    mShare.setVisibility(View.GONE);
                    return;
                }

                mName.setText(product.getName());
                mAdress.setText(product.getAddress());

                String photoPath = Environment.getExternalStorageDirectory() + product.getImagePath();
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                if (bitmap != null) {
                    mProfileImage.setImageBitmap(bitmap);
                }
            }
        });
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




