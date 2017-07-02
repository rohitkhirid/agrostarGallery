package rohitkhirid.com.galleryappagrostar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import rohitkhirid.com.galleryappagrostar.R;
import rohitkhirid.com.galleryappagrostar.utils.SharedPreferenceManager;

public class AccountDetailsActivity extends BaseActivity {
    private ImageView mProfileImageView;
    private TextView mProfileNameTextView;
    private Button mLogoutButton;
    private ProgressBar mImageLoadingProgressBar;

    public static void startMe(Activity activity) {
        Intent accountDetailsIntent = new Intent(activity, AccountDetailsActivity.class);
        activity.startActivity(accountDetailsIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        initUi();
    }

    private void initUi() {
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance();

        mImageLoadingProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mImageLoadingProgressBar.setVisibility(View.VISIBLE);

        mProfileImageView = (ImageView) findViewById(R.id.profile_image_view);
        Picasso.with(mActivity).load(sharedPreferenceManager.getUserProfilePicLink()).into(mProfileImageView, new Callback() {
            @Override
            public void onSuccess() {
                mImageLoadingProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                mImageLoadingProgressBar.setVisibility(View.GONE);
                mProfileImageView.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_broken_image));
            }
        });

        mProfileNameTextView = (TextView) findViewById(R.id.profile_name_textview);
        mProfileNameTextView.setText(sharedPreferenceManager.getUserProfileName());

        mLogoutButton = (Button) findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceManager.getInstance().destroy();
                LoginActivity.startMe(mActivity);
                finish();
            }
        });
    }

    @Override
    protected void freeUpResources() {
        onDestroy();
    }
}