package rohitkhirid.com.galleryappagrostar.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.io.File;

import rohitkhirid.com.galleryappagrostar.constants.IntentConstants;
import rohitkhirid.com.galleryappagrostar.utils.DebugLog;
import rohitkhirid.com.galleryappagrostar.utils.WrapperCloudnary;

/**
 * Created by rohitkhirid on 7/2/17.
 */
public class UploadService extends IntentService {
    private String mFilePath;

    public UploadService() {
        super(UploadService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            mFilePath = intent.getStringExtra(IntentConstants.INTENT_KEY_FILE_PATH_UPLOAD);
            DebugLog.d("mFilePath : " + mFilePath);
            File file = new File(mFilePath);
            WrapperCloudnary.getInstance().upload(file);
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
