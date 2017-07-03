package rohitkhirid.com.galleryappagrostar;

import android.app.Application;

import rohitkhirid.com.galleryappagrostar.constants.Constants;
import rohitkhirid.com.galleryappagrostar.utils.SharedPreferenceManager;
import rohitkhirid.com.galleryappagrostar.utils.Utils;
import rohitkhirid.com.galleryappagrostar.utils.WrapperCloudinary;

/**
 * Created by rohitkhirid on 7/1/17.
 */
public class GalleryApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // common utilities
        Utils.init(this);

        // shared preferance manager
        SharedPreferenceManager.init(this);

        // logginh out on app up, for development only
        if (Constants.LOG_OUT_ON_APP_UP) {
            SharedPreferenceManager.getInstance().destroy();
        }

        // image server
        WrapperCloudinary.init(this);
    }
}
