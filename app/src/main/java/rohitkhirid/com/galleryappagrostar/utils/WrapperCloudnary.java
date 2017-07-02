package rohitkhirid.com.galleryappagrostar.utils;

import android.content.Context;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import rohitkhirid.com.galleryappagrostar.constants.Constants;

/**
 * Created by rohitkhirid on 7/2/17.
 */
public class WrapperCloudnary {

    private volatile static WrapperCloudnary mCloudnaryUtils;

    private Context mContext;

    private volatile static Cloudinary mCloudnary;

    public static void init(Context context) {
        if (mCloudnaryUtils == null) {
            synchronized (WrapperCloudnary.class) {
                if (mCloudnaryUtils == null) {
                    mCloudnaryUtils = new WrapperCloudnary(context);
                }
            }
        }
    }

    private WrapperCloudnary(Context context) {
        mContext = context;
        Map config = new HashMap();
        config.put("cloud_name", Constants.CLOUD_NAME);
        config.put("api_key", Constants.API_KEY);
        config.put("api_secret", Constants.API_SECRET_KEY);
        mCloudnary = new Cloudinary(config);
    }

    public static WrapperCloudnary getInstance() {
        return mCloudnaryUtils;
    }

    public void upload(File file) {
        try {
            Map inputMap = new HashMap();
            inputMap.put("transformation", new Transformation().width(2000).height(1000).crop("limit"));
            inputMap.put("public_id", file.getName());
            Map map = mCloudnary.uploader().upload(file, inputMap);
            DebugLog.d("adding key to shared preferency : " + map.get("url").toString());
            SharedPreferenceManager.getInstance().addImagePublicId(map.get("url").toString());
        } catch (Exception e) {
            DebugLog.e("exception in cloudnary");
            e.printStackTrace();
        }
    }
}
