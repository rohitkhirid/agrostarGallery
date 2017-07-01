package rohitkhirid.com.galleryappagrostar.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import rohitkhirid.com.galleryappagrostar.constants.Constants;

/**
 * Created by rohitkhirid on 7/1/17.
 */

public class Utils {
    private Context mContext;

    private volatile static Utils mUtils;

    public static void init(Context context) {
        if (mUtils == null) {
            synchronized (Utils.class) {
                if (mUtils == null) {
                    mUtils = new Utils(context);
                }
            }
        }
    }

    public Utils(Context context) {
        mContext = context;
    }

    public static Utils getInstance() {
        return mUtils;
    }

    public void showToast(String message) {
        if (Constants.IS_DEVELOPING) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
        DebugLog.d(message);
    }

    /**
     * reads file from our directory
     *
     * @return
     */
    public ArrayList<String> getImageListFromAppStorage() {
        ArrayList<String> filePaths = new ArrayList<>();
        File storageDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                Constants.CACHE_TEMP_IMAGES_SUBDIR);
        File[] files = storageDir.listFiles();
        for (File file : files) {
            if (file.length() == 0) {
                continue;
            }
            filePaths.add(file.getAbsolutePath());
        }
        DebugLog.d("# of files : " + filePaths.size());
        return filePaths;
    }

    /**
     * @param string
     * @return true if string is null or blank ("")
     */
    public boolean isEmpty(String string) {
        if (string == null || string.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * checks if text is non-empty if yes sets else make textview gone
     *
     * @param textView
     * @param text
     */
    public void setTextToTextView(@NonNull TextView textView, String text) {
        if (textView == null) {
            return;
        }
        if (!Utils.getInstance().isEmpty(text)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}