package Utils;

import android.content.Context;
import android.widget.Toast;

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

    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public static Utils getInstance() {
        return mUtils;
    }
}
