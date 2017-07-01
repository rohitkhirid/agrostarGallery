package rohitkhirid.com.galleryappagrostar.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import rohitkhirid.com.galleryappagrostar.utils.DebugLog;
import rohitkhirid.com.galleryappagrostar.utils.SharedPreferenceManager;
import rohitkhirid.com.galleryappagrostar.utils.Utils;

/**
 * Created by rohitkhirid on 7/1/17.
 * <p>
 * Parent class of all activities, checks if user is logged in in onCreate
 * if yes proceeds else redirects to login.
 * <p>
 * All activities which do not need the user to be logged in should not
 * extend this activity ex {@link LoginActivity} itself
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Activity mActivity;
    protected Utils mUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        mUtils = Utils.getInstance();

        if (!SharedPreferenceManager.getInstance().isUserLoggedIn()) {
            redirectToLogin();
        } else {
            DebugLog.d("User is logged in, show him/her the home page");
        }
    }

    /**
     * checks if user is logged in to the app,
     * if yes does nothing, else redirects user to login page
     */
    private void redirectToLogin() {
        DebugLog.d("logged out user, showing him login page");
        LoginActivity.startMe(mActivity);
        // finish current activity so onBackPress user is not again here
        finish();
    }

    /**
     * every child should free memory allocated to differant objects, especially static classes
     */
    protected abstract void freeUpResources();
}
