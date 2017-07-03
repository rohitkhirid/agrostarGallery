package rohitkhirid.com.galleryappagrostar.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import rohitkhirid.com.galleryappagrostar.database.RDatabaseHelper;
import rohitkhirid.com.galleryappagrostar.utils.DebugLog;
import rohitkhirid.com.galleryappagrostar.utils.WrapperCloudnary;

/**
 * Created by rohitkhirid on 7/2/17.
 */
public class UploadService extends IntentService {

    public static void startMe(Context context) {
        Intent startUploadServiceIntent = new Intent(context, UploadService.class);
        context.startService(startUploadServiceIntent);
    }

    public UploadService() {
        super(UploadService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            RDatabaseHelper rDatabaseHelper = new RDatabaseHelper(this);
            ArrayList<RDatabaseHelper.DataBaseEntry> filePaths = rDatabaseHelper.getAllPendingFiles();
            DebugLog.d("# of files : " + filePaths.size());
            for (RDatabaseHelper.DataBaseEntry entry : filePaths) {
                WrapperCloudnary.getInstance().upload(entry);
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
