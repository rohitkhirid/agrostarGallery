package rohitkhirid.com.galleryappagrostar.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import rohitkhirid.com.galleryappagrostar.constants.Constants;
import rohitkhirid.com.galleryappagrostar.constants.IntentConstants;
import rohitkhirid.com.galleryappagrostar.database.RDatabaseHelper;

/**
 * Created by rohitkhirid on 7/2/17.
 */
public class WrapperCloudinary {

    private volatile static WrapperCloudinary mCloudnaryUtils;

    private Context mContext;

    private volatile static Cloudinary mCloudnary;

    public static void init(Context context) {
        if (mCloudnaryUtils == null) {
            synchronized (WrapperCloudinary.class) {
                if (mCloudnaryUtils == null) {
                    mCloudnaryUtils = new WrapperCloudinary(context);
                }
            }
        }
    }

    private WrapperCloudinary(Context context) {
        mContext = context;
        Map config = new HashMap();
        config.put("cloud_name", Constants.CLOUD_NAME);
        config.put("api_key", Constants.API_KEY);
        config.put("api_secret", Constants.API_SECRET_KEY);
        mCloudnary = new Cloudinary(config);
    }

    public static WrapperCloudinary getInstance() {
        return mCloudnaryUtils;
    }

    public void upload(RDatabaseHelper.DataBaseEntry dataBaseEntry) {
        try {
            File file = new File(dataBaseEntry.filePath);
            Map inputMap = new HashMap();
            inputMap.put("transformation", new Transformation().width(2000).height(1000).crop("limit"));
            inputMap.put("public_id", file.getName());
            Map map = mCloudnary.uploader().upload(file, inputMap);
            DebugLog.d("adding key to shared preferency : " + map.get("url").toString());
            dataBaseEntry.remoteUrl = map.get("url").toString();
            RDatabaseHelper rDatabaseHelper = new RDatabaseHelper(mContext);
            rDatabaseHelper.update(dataBaseEntry);

            Intent intent = new Intent(IntentConstants.BROADCAST_UI_CHANGE_URL_ADAPTER);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
            localBroadcastManager.sendBroadcast(intent);
        } catch (Exception e) {
            DebugLog.e("exception in cloudnary");
            e.printStackTrace();
        }
    }
}
