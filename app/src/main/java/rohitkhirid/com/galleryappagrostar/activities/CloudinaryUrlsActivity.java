package rohitkhirid.com.galleryappagrostar.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;

import rohitkhirid.com.galleryappagrostar.R;
import rohitkhirid.com.galleryappagrostar.adapters.UrlsAdapter;
import rohitkhirid.com.galleryappagrostar.constants.IntentConstants;
import rohitkhirid.com.galleryappagrostar.database.RDatabaseHelper;
import rohitkhirid.com.galleryappagrostar.utils.DebugLog;
import rohitkhirid.com.galleryappagrostar.utils.SharedPreferenceManager;

/**
 * lists urls of images uploaded from database
 *
 * received to broadcast {@link IntentConstants#BROADCAST_UI_CHANGE_URL_ADAPTER} for ui changes
 */
public class CloudinaryUrlsActivity extends BaseActivity {

    private SuperRecyclerView mUrlsRecyclerView;
    private UrlsAdapter mUrlsAdapter;

    private RDatabaseHelper mDatabaseHelper;
    private ArrayList<RDatabaseHelper.DataBaseEntry> mUrls;

    public static void startMe(Activity activity) {
        Intent intent = new Intent(activity, CloudinaryUrlsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloudinary_urls);

        mDatabaseHelper = new RDatabaseHelper(mActivity);
        initUi();

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mActivity);
        localBroadcastManager.registerReceiver(mUrlAddedReceiver, new IntentFilter(IntentConstants.BROADCAST_UI_CHANGE_URL_ADAPTER));
    }

    private BroadcastReceiver mUrlAddedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DebugLog.d("onReceive");
            refreshAdapter();
        }
    };

    private void initUi() {
        mUrlsRecyclerView = (SuperRecyclerView) findViewById(R.id.urls_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mUrlsRecyclerView.setLayoutManager(linearLayoutManager);

        mUrls = mDatabaseHelper.getAllUrls();
        mUrlsAdapter = new UrlsAdapter(mActivity, mUrls);
        mUrlsRecyclerView.setAdapter(mUrlsAdapter);
    }

    private void refreshAdapter() {
        ArrayList<RDatabaseHelper.DataBaseEntry> urls =mDatabaseHelper.getAllUrls();
        mUrls.clear();
        mUrls.addAll(urls);
        mUrlsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void freeUpResources() {
        onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mUrlAddedReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
