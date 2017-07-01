package activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import Utils.Utils;
import constants.IntentConstants;
import rohitkhirid.com.galleryappagrostar.R;

/**
 * we cannot extend this activity from base activity,
 * as only logged in activities can be extended from base activity
 * <p>
 * if we extend it from base activity inception of activities can happen ;-)
 */
public class LoginActivity extends AppCompatActivity {
    private String mTag = "LoginActivity";

    private GoogleApiClient mGoogleApiClient;

    private SignInButton mGoogleSignInButton;

    public static void startMe(Activity activity) {
        Intent loginActivityIntent = new Intent(activity, LoginActivity.class);
        activity.startActivity(loginActivityIntent);
        activity.finish();
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
            Log.d(mTag, "onConnectionFailed : " + connectionResult.getErrorMessage());
        }
    };

    private void initUi() {
        mGoogleSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(mTag, "onButtonClick");
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, IntentConstants.INTENT_CODE_GOOGLE_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(mTag, "requestCode : " + requestCode);
        Log.d(mTag, "resultCode : " + resultCode);
        if (requestCode == IntentConstants.INTENT_CODE_GOOGLE_SIGN_IN && resultCode == Activity.RESULT_OK) {
            Log.d(mTag, "onActivityResult from google sign in");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Log.d(mTag, "log-in success");
                GoogleSignInAccount account = result.getSignInAccount();
                Log.d(mTag, account.getDisplayName());
                Utils.getInstance().showToast("Success");
            } else {
                Log.d(mTag, "log-in failed");
                Utils.getInstance().showToast("failed");
            }
        }
    }
}
