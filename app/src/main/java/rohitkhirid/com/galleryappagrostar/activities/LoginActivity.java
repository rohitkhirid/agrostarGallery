package rohitkhirid.com.galleryappagrostar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import rohitkhirid.com.galleryappagrostar.R;
import rohitkhirid.com.galleryappagrostar.constants.IntentConstants;
import rohitkhirid.com.galleryappagrostar.utils.DebugLog;
import rohitkhirid.com.galleryappagrostar.utils.SharedPreferenceManager;
import rohitkhirid.com.galleryappagrostar.utils.Utils;

/**
 * we cannot extend this activity from base activity,
 * as only logged in activities can be extended from base activity
 * <p>
 * if we extend it from base activity inception of activities can happen ;-)
 */
public class LoginActivity extends AppCompatActivity {
    private GoogleApiClient mGoogleApiClient;

    private SignInButton mGoogleSignInButton;

    public static void startMe(Activity activity) {
        Intent loginActivityIntent = new Intent(activity, LoginActivity.class);
        activity.startActivity(loginActivityIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, mOnConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    private GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            DebugLog.d("onConnectionFailed : " + connectionResult.getErrorMessage());
        }
    };

    private void initUi() {
        mGoogleSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugLog.d("onButtonClick");
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, IntentConstants.INTENT_CODE_GOOGLE_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentConstants.INTENT_CODE_GOOGLE_SIGN_IN && resultCode == Activity.RESULT_OK) {
            DebugLog.d("onActivityResult from google sign in");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                DebugLog.d("log-in success");
                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null) {
                    DebugLog.d("setting status to shared preference");
                    SharedPreferenceManager.getInstance().setUserLoginStatus(true);
                    SharedPreferenceManager.getInstance().writeGoogleAccount(account);
                    DebugLog.d("redirecting user to dashboard screen");
                    DashboardActivity.startMe(this);
                    finish();
                } else {
                    Utils.getInstance().showToast("Totally unexpected, Google you can't do this!");
                }
            } else {
                DebugLog.d("log-in failed");
                Utils.getInstance().showToast("failed");
            }
        }
    }

    private void freeUpResources() {
        mGoogleSignInButton = null;
        mGoogleApiClient = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        freeUpResources();
    }
}
