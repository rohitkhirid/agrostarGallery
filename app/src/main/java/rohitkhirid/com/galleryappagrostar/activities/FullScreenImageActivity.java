package rohitkhirid.com.galleryappagrostar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import rohitkhirid.com.galleryappagrostar.R;
import rohitkhirid.com.galleryappagrostar.constants.IntentConstants;

/**
 * shows full screen
 *
 * either from image file or from url
 */
public class FullScreenImageActivity extends BaseActivity {
    private String mImagePath;

    private ImageView mImageView;
    private ProgressBar mProgressBar;

    public static void startMe(Activity activity, String filePath) {
        Intent fullScreenImageActivity = new Intent(activity, FullScreenImageActivity.class);
        fullScreenImageActivity.putExtra(IntentConstants.INTENT_KEY_FILE_PATH_FULLSCREEN, filePath);
        activity.startActivity(fullScreenImageActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        try {
            mImagePath = getIntent().getStringExtra(IntentConstants.INTENT_KEY_FILE_PATH_FULLSCREEN);
        } catch (Exception e) {
            finish();
            e.printStackTrace();
        }

        if (mImagePath == null) {
            finish();
        }

        initUi();

        if (mImagePath.contains("http://")) {
            mProgressBar.setVisibility(View.VISIBLE);
            Picasso.with(mActivity)
                    .load(mImagePath)
                    .into(mImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            mProgressBar.setVisibility(View.GONE);
                            mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_broken_image));
                        }
                    });
        } else {
            File file = new File(mImagePath);
            mProgressBar.setVisibility(View.VISIBLE);
            Picasso.with(mActivity)
                    .load(file)
                    .into(mImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            mProgressBar.setVisibility(View.GONE);
                            mImageView.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_broken_image));
                        }
                    });
        }
    }

    private void initUi() {
        mImageView = (ImageView) findViewById(R.id.imageview);
        mProgressBar = (ProgressBar) findViewById(R.id.image_load_progress_bar);
    }

    @Override
    protected void freeUpResources() {
        onDestroy();
    }
}
