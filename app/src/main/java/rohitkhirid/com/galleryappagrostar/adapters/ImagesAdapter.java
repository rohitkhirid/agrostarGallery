package rohitkhirid.com.galleryappagrostar.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import rohitkhirid.com.galleryappagrostar.R;
import rohitkhirid.com.galleryappagrostar.activities.FullScreenImageActivity;
import rohitkhirid.com.galleryappagrostar.constants.Constants;
import rohitkhirid.com.galleryappagrostar.database.RDatabaseHelper;
import rohitkhirid.com.galleryappagrostar.utils.DebugLog;
import rohitkhirid.com.galleryappagrostar.utils.Utils;

/**
 * Created by rohitkhirid on 7/1/17.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    private Activity mActivity;
    private ArrayList<RDatabaseHelper.DataBaseEntry> mFiles;

    public ImagesAdapter(Activity activity, ArrayList<RDatabaseHelper.DataBaseEntry> imagePaths) {
        mActivity = activity;
        mFiles = imagePaths;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DebugLog.d("onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plank_image, parent, false);
        return new ImagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBindCustomViewHolder(position);
    }

    @Override
    public int getItemCount() {
        if (mFiles == null) {
            return 0;
        }
        return mFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView, mStatusImageView;
        private TextView mFileNameTextView, mFileSizeTextView, mFileTimeStampTextView;
        private View mPlankView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.plank_image_view);
            mFileNameTextView = (TextView) view.findViewById(R.id.filename_textview);
            mFileSizeTextView = (TextView) view.findViewById(R.id.filesize_textview);
            mFileTimeStampTextView = (TextView) view.findViewById(R.id.timestamp_textview);
            mStatusImageView = (ImageView) view.findViewById(R.id.upload_status_image_view);
            mPlankView = view;
        }

        public void onBindCustomViewHolder(final int position) {
            final String filePath = mFiles.get(position).filePath;
            int uploadStatus = mFiles.get(position).successBit;
            mStatusImageView.setVisibility(View.VISIBLE);
            if (uploadStatus == RDatabaseHelper.BIT_SUCCESS) {
                mStatusImageView.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_check));
            } else if (uploadStatus == RDatabaseHelper.BIT_FAILURE) {
                mStatusImageView.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_waiting));
            } else {
                mStatusImageView.setVisibility(View.GONE);
            }
            if (!Utils.getInstance().isEmpty(filePath)) {
                File file = new File(filePath);
                if (file != null) {
                    Picasso.with(mActivity)
                            .load(file)
                            .resize(Constants.MAX_EDGE_THUMBNAIL, Constants.MAX_EDGE_THUMBNAIL)
                            .centerInside()
                            .into(mImageView);
                    Utils.getInstance().setTextToTextView(mFileNameTextView, file.getName());
                    Utils.getInstance().setTextToTextView(mFileSizeTextView,
                            Utils.getInstance().humanReadableByteCount(file.length(), true));
                    Utils.getInstance().setTextToTextView(mFileTimeStampTextView,
                            Utils.getInstance().toHumanReadableDateAndTime(file.lastModified()));
                }
            } else {
                Picasso.with(mActivity).load(R.drawable.ic_broken_image).into(mImageView);
            }
            mPlankView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DebugLog.d("plankClick, position : " + position);
                    FullScreenImageActivity.startMe(mActivity, filePath);
                }
            });
        }
    }
}
