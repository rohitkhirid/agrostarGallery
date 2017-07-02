package rohitkhirid.com.galleryappagrostar.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rohitkhirid on 7/1/17.
 * <p>
 * wrapper over android shared preferances
 * <p>
 * all keys should be written in class {@link SharedPreferenceKeys}
 */
public class SharedPreferenceManager {
    private Context mContext;

    private volatile static SharedPreferenceManager mSharedPreferenceManager;

    private SharedPreferenceManager(Context context) {
        mContext = context;
    }

    public static void init(Context context) {
        if (mSharedPreferenceManager == null) {
            synchronized (SharedPreferenceManager.class) {
                if (mSharedPreferenceManager == null) {
                    mSharedPreferenceManager = new SharedPreferenceManager(context);
                }
            }
        }
    }

    /**
     * shared preference should not have any key except from this keys
     */
    private class SharedPreferenceKeys {
        private static final String PREF_FILE_NAME = "shared_preference_file_name";

        private static final String PREF_IS_USER_LOGGED_IN = "shared_preference_logged_in_status";

        private static final String PREF_DISPLAY_NAME = "shared_preference_display_name";
        private static final String PREF_GIVEN_NAME = "shared_preference_given_name";
        private static final String PREF_FAMILY_NAME = "shared_preference_family_name";
        private static final String PREF_PHOTO_URL = "shared_preference_photo_url";

        // set of public id's for cloudnary images
        private static final String PREF_PHOTO_PUBLIC_IDS = "shared_preference_cloudinary_public_ids";
    }

    public static SharedPreferenceManager getInstance() {
        return mSharedPreferenceManager;
    }

    public void setUserLoginStatus(boolean isLoggedIn) {
        write(SharedPreferenceKeys.PREF_IS_USER_LOGGED_IN, isLoggedIn);
    }

    public String getUserProfilePicLink() {
        SharedPreferences sharedpreferences =
                mContext.getSharedPreferences(SharedPreferenceKeys.PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getString(SharedPreferenceKeys.PREF_PHOTO_URL, null);
    }

    public String getUserProfileName() {
        SharedPreferences sharedpreferences =
                mContext.getSharedPreferences(SharedPreferenceKeys.PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getString(SharedPreferenceKeys.PREF_DISPLAY_NAME, null);
    }

    /**
     * @return boolean with key {@link SharedPreferenceKeys#PREF_IS_USER_LOGGED_IN}
     */
    public boolean isUserLoggedIn() {
        return read(SharedPreferenceKeys.PREF_IS_USER_LOGGED_IN);
    }

    /**
     * should be called at logout
     */
    public void destroy() {
        SharedPreferences sharedpreferences =
                mContext.getSharedPreferences(SharedPreferenceKeys.PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().apply();
    }

    public void writeGoogleAccount(@NonNull GoogleSignInAccount account) {
        try {
            write(SharedPreferenceKeys.PREF_DISPLAY_NAME, account.getDisplayName());
            write(SharedPreferenceKeys.PREF_GIVEN_NAME, account.getGivenName());
            write(SharedPreferenceKeys.PREF_FAMILY_NAME, account.getFamilyName());
            if (account.getPhotoUrl() != null) {
                write(SharedPreferenceKeys.PREF_PHOTO_URL, account.getPhotoUrl().toString());
            }
        } catch (Exception e) {
            Utils.getInstance().showToast("Crashed in writeGoogleAccount : " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     * writes string to sharedPreference with name {@link SharedPreferenceKeys#PREF_FILE_NAME}
     *
     * @param key
     * @param value
     */
    private void write(String key, String value) {
        DebugLog.d("Writing string to shared preferences : " + value + " for key : " + key);
        SharedPreferences sharedpreferences =
                mContext.getSharedPreferences(SharedPreferenceKeys.PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        // using apply() over commit() is more efficient (asynchronus)
        // https://developer.android.com/reference/android/content/SharedPreferences.Editor.html#apply()
        editor.apply();
    }

    /**
     * writes boolean to sharedPreference with name {@link SharedPreferenceKeys#PREF_FILE_NAME}
     *
     * @param key
     * @param value
     */
    private void write(String key, boolean value) {
        DebugLog.d("Writing boolean to shared preferences : " + value + " for key : " + key);
        SharedPreferences sharedpreferences =
                mContext.getSharedPreferences(SharedPreferenceKeys.PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        // using apply() over commit() is more efficient (asynchronus)
        // https://developer.android.com/reference/android/content/SharedPreferences.Editor.html#apply()
        editor.apply();
    }

    /**
     * reads boolean
     *
     * @param key
     * @return
     */
    private boolean read(String key) {
        DebugLog.d("reading boolean with key :  " + key);
        SharedPreferences sharedpreferences =
                mContext.getSharedPreferences(SharedPreferenceKeys.PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(SharedPreferenceKeys.PREF_IS_USER_LOGGED_IN, false);
    }

    public void addImagePublicId(String publicId) {
        SharedPreferences sharedpreferences =
                mContext.getSharedPreferences(SharedPreferenceKeys.PREF_FILE_NAME, Context.MODE_PRIVATE);
        Set<String> stringSet = sharedpreferences.getStringSet(SharedPreferenceKeys.PREF_PHOTO_PUBLIC_IDS, null);
        if (stringSet == null) {
            stringSet = new HashSet<>();
        }
        stringSet.add(publicId);
    }
}

