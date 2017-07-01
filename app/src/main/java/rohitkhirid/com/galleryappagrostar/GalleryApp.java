package rohitkhirid.com.galleryappagrostar;

import android.app.Application;

import Utils.Utils;

/**
 * Created by rohitkhirid on 7/1/17.
 */

public class GalleryApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
    }
}
