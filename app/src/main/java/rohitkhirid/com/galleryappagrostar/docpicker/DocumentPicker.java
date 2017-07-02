package rohitkhirid.com.galleryappagrostar.docpicker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

import rohitkhirid.com.galleryappagrostar.R;
import rohitkhirid.com.galleryappagrostar.activities.BaseActivity;
import rohitkhirid.com.galleryappagrostar.constants.Constants;
import rohitkhirid.com.galleryappagrostar.constants.IntentConstants;
import rohitkhirid.com.galleryappagrostar.services.UploadService;
import rohitkhirid.com.galleryappagrostar.utils.DebugLog;
import rohitkhirid.com.galleryappagrostar.utils.ImageUtils;
import rohitkhirid.com.galleryappagrostar.utils.PermissionUtils;
import rohitkhirid.com.galleryappagrostar.utils.Utils;

public class DocumentPicker extends BaseActivity {

    /**
     * temp file made on external storage which is used by camera intent
     * to store captured image
     */
    private String mCameraImageFilePath = null;

    private View mCamera, mGallery;

    public static void startMe(Activity activity) {
        Intent documentPickerIntent = new Intent(activity, DocumentPicker.class);
        activity.startActivityForResult(documentPickerIntent, IntentConstants.INTENT_CODE_DOCUMENT_PICKER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_picker);

        initUi();
    }

    private void initUi() {
        mCamera = findViewById(R.id.camera);
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugLog.d("DocumentPicker : camera click");
                if (!PermissionUtils.checkCameraAndStoragePermission(mActivity)) {
                    DebugLog.d("Permissions not present, requesting permissions");
                    PermissionUtils.requestCameraStoragePermission(null, DocumentPicker.this);
                } else {
                    DebugLog.d("have required permissions");
                    getCameraImage();
                }
            }
        });

        mGallery = findViewById(R.id.gallery);
        mGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugLog.d("DocumentPicker : gallery click");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                if (Build.VERSION.SDK_INT >= 18) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IntentConstants.INTENT_CODE_GALLERY_IMAGE);
            }
        });
    }

    @Override
    protected void freeUpResources() {
        onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == IntentConstants.PERMISSION_REQUEST_CODE_CAMERA_STORAGE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    finish();
                    return;
                }
            }

            getCameraImage();
        }
    }

    private void getCameraImage() {
        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    mCameraImageFilePath = ImageUtils.createImageFile(mActivity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mCameraImageFilePath != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            Constants.GALLERY_FILE_PROVIDER,
                            new File(mCameraImageFilePath));
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, IntentConstants.INTENT_CODE_CAMERA_IMAGE);
                }
            } else {
                Utils.getInstance().showToast(getString(R.string.message_no_suitable_app));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentConstants.INTENT_CODE_CAMERA_IMAGE && resultCode == Activity.RESULT_OK) {
            DebugLog.d("onActivityResult for camera image");
            Intent intentWithUris = new Intent();
            ArrayList<Parcelable> parcelableArrayList = new ArrayList<>();
            File file = new File(mCameraImageFilePath);
            int fileSize = Integer.parseInt(String.valueOf(file.length()));
            DebugLog.d("file size : " + fileSize);
            if (fileSize != 0) {
                UploadService.startMe(mActivity, mCameraImageFilePath);
                parcelableArrayList.add(Uri.parse(file.getAbsolutePath()));
                intentWithUris.putParcelableArrayListExtra(IntentConstants.INTENT_KEY_IMAGE_FILEPATH, parcelableArrayList);
                setResult(Activity.RESULT_OK, intentWithUris);
            } else {
                DebugLog.d("image not captured");
            }
            finish();
        }

        if (requestCode == IntentConstants.INTENT_CODE_GALLERY_IMAGE && resultCode == Activity.RESULT_OK) {
            DebugLog.d("onActivityResult for gallery image");
            Parcelable[] parcelableUris = Utils.getInstance().getParcelableUrisFromData(data);
            if (parcelableUris == null) {
                finish();
            } else {
                Intent intentWithUris = new Intent();
                final ArrayList<Parcelable> parcelableArrayList = new ArrayList<>();
                for (Parcelable P : parcelableUris) {
                    if (P == null) {
                        continue;
                    }
                    if (ImageUtils.isImage(P.toString())) {
                        parcelableArrayList.add(P);
                    } else {
                        // TODO remove this when we start supporting other formats
                        Utils.getInstance().showToast("Only image files are allowed.");
                        DebugLog.e(P.toString() + " is not image file");
                    }
                }
                DebugLog.d("Total image selected : " + parcelableArrayList.size());
                intentWithUris.putParcelableArrayListExtra(IntentConstants.INTENT_KEY_IMAGE_FILEPATH, parcelableArrayList);
                setResult(Activity.RESULT_OK, intentWithUris);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        DebugLog.d("moving files");
                        Utils.getInstance().copyFileToGalleryDirectory(parcelableArrayList);
                        UploadService.startMe(mActivity, parcelableArrayList);
                    }
                });
                finish();
            }
        }
    }
}
