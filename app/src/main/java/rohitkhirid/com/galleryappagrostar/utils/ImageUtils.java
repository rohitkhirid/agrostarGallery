package rohitkhirid.com.galleryappagrostar.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
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

    public static boolean isImage(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            return isImage(file);
        }
        return false;
    }

    public static boolean isImage(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        return options.outWidth != -1 && options.outHeight != -1;
    }
}
