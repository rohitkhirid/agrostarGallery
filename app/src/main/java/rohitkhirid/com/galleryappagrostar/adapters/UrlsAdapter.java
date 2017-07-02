package rohitkhirid.com.galleryappagrostar.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import rohitkhirid.com.galleryappagrostar.R;
import rohitkhirid.com.galleryappagrostar.activities.FullScreenImageActivity;

/**
 * Created by rohitkhirid on 7/2/17.
 */

public class UrlsAdapter extends RecyclerView.Adapter<UrlsAdapter.ViewHolder> {

    ArrayList<String> mUrls;
    Activity mActivity;

    public UrlsAdapter(Activity activity, ArrayList<String> urls) {
        mActivity = activity;
        mUrls = urls;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mPlankView;
        TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.url_textview);
            mPlankView = v;
        }
    }

    @Override
    public UrlsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.plank_url, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(mUrls.get(position));
        holder.mPlankView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenImageActivity.startMe(mActivity, mUrls.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUrls.size();
    }
}