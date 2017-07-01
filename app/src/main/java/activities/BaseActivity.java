package activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by rohitkhirid on 7/1/17.
 *
 * Parent class of all activities, checks if user is logged in in onCreate
 * if yes proceeds else redirects to login.
 *
 * All activities which do not need the user to be logged in should not
 * extend this activity ex {@link LoginActivity} itself
 */
public class BaseActivity extends AppCompatActivity {
    protected String mTag = "gallery_app";

    protected Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        redirectToLoginIfNotLoggedIn();
    }

    /**
     * checks if user is logged in to the app,
     * if yes does nothing, else redirects user to login page
     */
    private void redirectToLoginIfNotLoggedIn() {
        LoginActivity.startMe(mActivity);
        // finish current activity so onBackPress user is not again here
        finish();
    }
}
