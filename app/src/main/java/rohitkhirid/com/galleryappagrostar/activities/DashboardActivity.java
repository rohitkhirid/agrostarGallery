package rohitkhirid.com.galleryappagrostar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;

import rohitkhirid.com.galleryappagrostar.ImagesAdapter;
import rohitkhirid.com.galleryappagrostar.R;
import rohitkhirid.com.galleryappagrostar.constants.IntentConstants;
import rohitkhirid.com.galleryappagrostar.docpicker.DocumentPicker;
import rohitkhirid.com.galleryappagrostar.utils.DebugLog;
import rohitkhirid.com.galleryappagrostar.utils.SharedPreferenceManager;
import rohitkhirid.com.galleryappagrostar.utils.Utils;

public class DashboardActivity extends BaseActivity {
    private static final int MENU_SEE_REMOTE_IMAGES = 1;
    private static final int MENU_ACCOUNT_DETAILS = 2;
    private static final int MENU_LOGOUT = 3;

    private Button mSelectPhotoButton;
    private SuperRecyclerView mImageRecyclerView;

    private ArrayList<String> mImagePaths;
    private ImagesAdapter mImagesAdapter;

    public static void startMe(Activity activity) {
        Intent dashboardActivityIntent = new Intent(activity, DashboardActivity.class);
        activity.startActivity(dashboardActivityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_SEE_REMOTE_IMAGES, 1, R.string.label_remote_images);
        menu.add(0, MENU_ACCOUNT_DETAILS, 2, R.string.label_account_details);
        menu.add(0, MENU_LOGOUT, 3, R.string.label_logout);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SEE_REMOTE_IMAGES:
                DebugLog.d("remote images");
                break;

            case MENU_ACCOUNT_DETAILS:
                DebugLog.d("account details");
                AccountDetailsActivity.startMe(mActivity);
                break;

            case MENU_LOGOUT:
                DebugLog.d("logout");
                SharedPreferenceManager.getInstance().destroy();
                LoginActivity.startMe(mActivity);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initUi();
    }

    private void initUi() {
        mSelectPhotoButton = (Button) findViewById(R.id.select_photo_button);
        mSelectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugLog.d("Select Photo Click");
                DocumentPicker.startMe(mActivity);
            }
        });

        mImagePaths = Utils.getInstance().getImageListFromAppStorage();
        mImagesAdapter = new ImagesAdapter(mActivity, mImagePaths);

        mImageRecyclerView = (SuperRecyclerView) findViewById(R.id.images_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mImageRecyclerView.setLayoutManager(linearLayoutManager);
        mImageRecyclerView.setAdapter(mImagesAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentConstants.INTENT_CODE_DOCUMENT_PICKER && resultCode == Activity.RESULT_OK) {
            DebugLog.d("Got images");
            ArrayList<Parcelable> images = data.getParcelableArrayListExtra(IntentConstants.INTENT_KEY_IMAGE_FILEPATH);
            DebugLog.d("adding " + images.size() + " to adapter");
            refreshAdapter();
        }
    }

    private void refreshAdapter() {
        try {
            ArrayList<String> imagePaths = Utils.getInstance().getImageListFromAppStorage();
            mImagePaths.clear();
            mImagePaths.addAll(imagePaths);
            mImagesAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void freeUpResources() {
        onDestroy();
    }
}
