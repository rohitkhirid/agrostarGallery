package rohitkhirid.com.galleryappagrostar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;

import rohitkhirid.com.galleryappagrostar.ImagesAdapter;
import rohitkhirid.com.galleryappagrostar.R;
import rohitkhirid.com.galleryappagrostar.constants.IntentConstants;
import rohitkhirid.com.galleryappagrostar.docpicker.DocumentPicker;
import rohitkhirid.com.galleryappagrostar.utils.DebugLog;
import rohitkhirid.com.galleryappagrostar.utils.Utils;

public class DashboardActivity extends BaseActivity {

    private Button mSelectPhotoButton;
    private SuperRecyclerView mImageRecyclerView;

    private ArrayList<String> mImagePaths;
    private ImagesAdapter mImagesAdapter;

    public static void startMe(Activity activity) {
        Intent dashboardActivityIntent = new Intent(activity, DashboardActivity.class);
        activity.startActivity(dashboardActivityIntent);
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
            for (Parcelable parcelable : images) {
                mImagePaths.add(parcelable.toString());
            }
            mImagesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void freeUpResources() {
        onDestroy();
    }
}
