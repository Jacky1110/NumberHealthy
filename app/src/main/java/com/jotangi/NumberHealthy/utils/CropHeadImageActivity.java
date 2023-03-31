package com.jotangi.NumberHealthy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jotangi.NumberHealthy.BuildConfig;
import com.jotangi.NumberHealthy.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class CropHeadImageActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName() + "(TAG)";

    private Handler handler;
    private Bitmap bitmap = null;
    private MaskView vwMask;
    private ConstraintLayout bnCamera;
    private ConstraintLayout bnGallery;
    private ConstraintLayout bnCut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_crop_head_image);
        if (!allPermissionsGranted()) {
            getRuntimePermissions();
        }

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        /*if (bitmap == null) {
            DialogUtils.show2ButtonAndCancelDialog((ViewGroup) vwMask.getRootView(), R.string.msg_image_from, 0, R.string.camera, R.string.gallery, new DialogUtils.TwoButtonAndCancelClickListener() {
                @Override
                public void onButton1Clicked() {
                    getImageFromCamera();
                }

                @Override
                public void onButton2Clicked() {
                    getImageFromGallery();
                }

                @Override
                public void onCancel() {
                    setResultCancelled();
                }
            });
        }*/
    }

    private void initViews() {
        handler = new Handler();
        vwMask = findViewById(R.id.layout_mask);

        bnCamera = findViewById(R.id.layout_camera);
        bnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCamera();
            }
        });

        bnGallery = findViewById(R.id.layout_gallery);
        bnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromGallery();
            }
        });

        bnCut = findViewById(R.id.layout_cut);
        bnCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = vwMask.cropImage();
                if (bitmap != null) {
                    File file = FileUtils.getImageFile(CropHeadImageActivity.this, FileUtils.USER_HEAD_PHOTO_FILE);
                    if (file != null) {
                        FileUtils.saveBitmapToImageFile(bitmap, file);
                        setResultOk(file.getAbsolutePath());
                    }
                }
            }
        });
    }

    ActivityResultLauncher<Uri> launchCamera = registerForActivityResult(
            new ActivityResultContracts.TakePicture(), result ->
            {
                Log.d(TAG, "launchCamera::onActivityResult");
                if (!result)
                    return;

                try {
                    File imgfile = FileUtils.getCacheImageFile(CropHeadImageActivity.this, FileUtils.USER_HEAD_PHOTO_FILE);
                    if (imgfile != null) {
                        bitmap = BitmapUtils.loadAndRotateBitmap(imgfile.getAbsolutePath());
                        vwMask.setImageBitmap(bitmap);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            });

    private void getImageFromCamera() {
        Uri uri = FileUtils.getCacheImageUri(
                CropHeadImageActivity.this,
                FileUtils.USER_HEAD_PHOTO_FILE,
                BuildConfig.APPLICATION_ID);
        if (uri != null) {
            launchCamera.launch(uri);
        }
    }

    ActivityResultLauncher<Intent> launcherGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "launcherGallery::onActivityResult");
                    try {
                        if (result.getData() != null) {
                            Uri selectedImageUri = result.getData().getData();
                            bitmap = BitmapUtils.loadAndRotateBitmap(getBaseContext(), selectedImageUri);
                            vwMask.setImageBitmap(bitmap);
                        }
                    } catch (Exception exception) {
                        Log.d("TAG", "" + exception.getLocalizedMessage());
                    }
                }
            });

    private void getImageFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcherGallery.launch(pickPhoto);
    }

    private void setResultOk(String imagePath) {
        Intent intent = new Intent();
        intent.putExtra("path", imagePath);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void setResultCancelled() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private static final int PERMISSION_REQUESTS = 1;

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            initViews();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }

}