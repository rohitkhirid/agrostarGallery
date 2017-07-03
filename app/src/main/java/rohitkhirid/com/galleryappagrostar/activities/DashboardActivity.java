package rohitkhirid.com.galleryappagrostar.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;

import rohitkhirid.com.galleryappagrostar.R;
import rohitkhirid.com.galleryappagrostar.adapters.ImagesAdapter;
import rohitkhirid.com.galleryappagrostar.constants.IntentConstants;
import rohitkhirid.com.galleryappagrostar.database.RDatabaseHelper;
import rohitkhirid.com.galleryappagrostar.docpicker.DocumentPicker;
import rohitkhirid.com.galleryappagrostar.utils.DebugLog;
import rohitkhirid.com.galleryappagrostar.utils.SharedPreferenceManager;
import rohitkhirid.com.galleryappagrostar.utils.Utils;

/**
 * home activity, user is redirected here after login
 *
 * has list of images in gallery
 *
 * and option in overflow menu to {@link AccountDetailsActivity}, {@link CloudinaryUrlsActivity}
 * and option to logout
 *
 * listens to broadcast {@link IntentConstants#BROADCAST_UI_CHANGE_IMAGE_ADAPTER} for ui changes
 */
public class DashboardActivity extends BaseActivity {
    private static final int MENU_ACCOUNT_DETAILS = 1;
    private static final int MENU_CLOUDANIRY_URLS = 2;
    private static final int MENU_LOGOUT = 3;

    private Button mSelectPhotoButton;
    private SuperRecyclerView mImageRecyclerView;

    private ArrayList<RDatabaseHelper.DataBaseEntry> mImagePaths;
    private ImagesAdapter mImagesAdapter;

    private RDatabaseHelper mDatabaseHelper;

    public static void startMe(Activity activity) {
        Intent dashboardActivityIntent = new Intent(activity, DashboardActivity.class);
        activity.startActivity(dashboardActivityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ACCOUNT_DETAILS, 1, R.string.label_account_details);
        menu.add(0, MENU_CLOUDANIRY_URLS, 2, R.string.label_cloudinary_urls);
        menu.add(0, MENU_LOGOUT, 3, R.string.label_logout);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case MENU_ACCOUNT_DETAILS:
                DebugLog.d("account details");
                AccountDetailsActivity.startMe(mActivity);
                break;

            case MENU_LOGOUT:
                DebugLog.d("logout");
                Utils.getInstance().showConfirmDialog(mActivity, getString(R.string.message_logout_confirmation), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferenceManager.getInstance().destroy();
                        LoginActivity.startMe(mActivity);
                        finish();
                    }
                }, null, getString(R.string.label_logout), getString(R.string.label_cancel));
                break;

            case MENU_CLOUDANIRY_URLS:
                DebugLog.d("cloudinary urls");
                CloudinaryUrlsActivity.startMe(mActivity);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mDatabaseHelper = new RDatabaseHelper(mActivity);
        mDatabaseHelper.printTable();

        initUi();

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mActivity);
        localBroadcastManager.registerReceiver(mImageAddedReceiver, new IntentFilter(IntentConstants.BROADCAST_UI_CHANGE_IMAGE_ADAPTER));
    }

    private BroadcastReceiver mImageAddedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DebugLog.d("broadcast received");
            refreshAdapter();
        }
    };

    private void initUi() {
        mSelectPhotoButton = (Button) findViewById(R.id.select_photo_button);
        mSelectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugLog.d("Select Photo Click");
                DocumentPicker.startMe(mActivity);
            }
        });

        mImagePaths = mDatabaseHelper.getAll();
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
            ArrayList<RDatabaseHelper.DataBaseEntry> imagePaths = mDatabaseHelper.getAll();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mImageAddedReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
