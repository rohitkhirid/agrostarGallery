package rohitkhirid.com.galleryappagrostar.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import rohitkhirid.com.galleryappagrostar.services.UploadService;

/**
 * Created by rohitkhirid on 7/3/17.
 *
 * called after network change.
 * 
 * we call {@link UploadService} on network change.
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        DebugLog.d("network change");
        if (intent.getExtras() != null) {
            NetworkInfo netInfo = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);

            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                if (!Utils.getInstance().isServiceAlreadyRunning("rohitkhirid.com.galleryappagrostar.utils.NetworkStateReceiver")) {
                    UploadService.startMe(context);
                }
            }
        }
    }
}