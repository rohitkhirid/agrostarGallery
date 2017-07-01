package rohitkhirid.com.galleryappagrostar.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rohitkhirid.com.galleryappagrostar.constants.Constants;

/**
 * Created by rohitkhirid on 7/1/17.
 */

public class ImageUtils {
    public static String createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        // storing file some external cache under doc picker directory
        File storageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                Constants.CACHE_TEMP_IMAGES_SUBDIR);

        storageDir.mkdir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image.getAbsolutePath();
    }
}
