package rohitkhirid.com.galleryappagrostar.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * Created by rohitkhirid on 7/1/17.
 * <p>
 * wrapper over android runtime permission
 */
public class PermissionUtils {

    public static String[] PERMISSIONS_CAMERA_STORAGE = {Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void requestCameraStoragePermission(@NonNull Activity activity, int requestCode) {
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_CAMERA_STORAGE, requestCode);
        } else {
            DebugLog.e("Both fragment and activity are null");
        }
    }

    /**
     * checks if camera and stoarge permissions are present or not
     *
     * @param context
     * @return true if permission exists, false otherwise
     */
    public static boolean checkCameraAndStoragePermission(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}
