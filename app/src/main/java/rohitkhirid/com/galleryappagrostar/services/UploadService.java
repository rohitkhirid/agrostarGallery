package rohitkhirid.com.galleryappagrostar.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

import rohitkhirid.com.galleryappagrostar.constants.IntentConstants;
import rohitkhirid.com.galleryappagrostar.utils.DebugLog;
import rohitkhirid.com.galleryappagrostar.utils.WrapperCloudnary;

/**
 * Created by rohitkhirid on 7/2/17.
 */
public class UploadService extends IntentService {

    public static void startMe(Activity activity, String filePath) {
        Intent startUploadServiceIntent = new Intent(activity, UploadService.class);
        ArrayList<String> filePaths = new ArrayList<>();
        filePaths.add(filePath);
        startUploadServiceIntent.putStringArrayListExtra(IntentConstants.INTENT_KEY_FILE_PATHS_UPLOAD, filePaths);
        activity.startService(startUploadServiceIntent);
    }

    public static void startMe(Activity activity, ArrayList<Parcelable> parcelableArrayList) {
        ArrayList<String> filePaths = new ArrayList<>();
        for (Parcelable p : parcelableArrayList) {
            filePaths.add(p.toString());
        }
        Intent startUploadServiceIntent = new Intent(activity, UploadService.class);
        startUploadServiceIntent.putStringArrayListExtra(IntentConstants.INTENT_KEY_FILE_PATHS_UPLOAD, filePaths);
        activity.startService(startUploadServiceIntent);
    }

    public UploadService() {
        super(UploadService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            ArrayList<String> filePaths = intent.getStringArrayListExtra(IntentConstants.INTENT_KEY_FILE_PATHS_UPLOAD);
            DebugLog.d("# of files : " + filePaths.size());
            for (String filePath : filePaths) {
                File file = new File(filePath);
                WrapperCloudnary.getInstance().upload(file);
            }
            this.stopSelf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DebugLog.d("onDetroy from service");
    }
}
