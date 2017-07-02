package rohitkhirid.com.galleryappagrostar.utils;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rohitkhirid.com.galleryappagrostar.constants.Constants;
import rohitkhirid.com.galleryappagrostar.constants.IntentConstants;

/**
 * Created by rohitkhirid on 7/1/17.
 */

public class Utils {
    private Context mContext;

    private volatile static Utils mUtils;

    public static void init(Context context) {
        if (mUtils == null) {
            synchronized (Utils.class) {
                if (mUtils == null) {
                    mUtils = new Utils(context);
                }
            }
        }
    }

    public Utils(Context context) {
        mContext = context;
    }

    public static Utils getInstance() {
        return mUtils;
    }

    public void showToast(String message) {
        if (Constants.IS_DEVELOPING) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
        DebugLog.d(message);
    }

    /**
     * reads file from our directory
     *
     * @return
     */
    public ArrayList<String> getImageListFromAppStorage() {
        ArrayList<String> filePaths = new ArrayList<>();
        try {
            File storageDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    Constants.CACHE_TEMP_IMAGES_SUBDIR);
            File[] files = storageDir.listFiles();
            for (File file : files) {
                if (file.length() == 0) {
                    continue;
                }
                filePaths.add(file.getAbsolutePath());
            }
            DebugLog.d("# of files : " + filePaths.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePaths;
    }

    /**
     * @param string
     * @return true if string is null or blank ("")
     */
    public boolean isEmpty(String string) {
        if (string == null || string.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * checks if text is non-empty if yes sets else make textview gone
     *
     * @param textView
     * @param text
     */
    public void setTextToTextView(@NonNull TextView textView, String text) {
        if (textView == null) {
            return;
        }
        if (!Utils.getInstance().isEmpty(text)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    /**
     * used to get parcaelabel array from intent data received from {@link Intent#ACTION_GET_CONTENT}
     *
     * @param data
     * @return
     */
    public Parcelable[] getParcelableUrisFromData(Intent data) {
        Parcelable[] parcelableUris = null;
        if (data == null) {
            // user gone to gallery but selected nothing
            return parcelableUris;
        }
        if (data.getData() != null) {
            DebugLog.d("single image");
            parcelableUris = new Parcelable[1];
            try {
                parcelableUris[0] = getParcelableFromFilePathUri(data.getData());
                return parcelableUris;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Build.VERSION.SDK_INT >= 18) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                parcelableUris = new Parcelable[clipData.getItemCount()];
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    if (item != null) {
                        try {
                            parcelableUris[i] = getParcelableFromFilePathUri(item.getUri());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        DebugLog.d(item.getUri().toString() + "--" + item.getUri().getPath());
                    }
                }
                DebugLog.d("added " + parcelableUris.length + " in string array");
                return parcelableUris;
            }
        }
        if (data.getExtras() == null) {
            // user opened camera but selected nothing
            return parcelableUris;
        } else {
            Uri uriCapturedImage = getImageUri((Bitmap) data.getExtras().get("data"));
            parcelableUris = new Parcelable[1];
            parcelableUris[0] = getParcelableFromFilePathUri(uriCapturedImage);
            return parcelableUris;
        }
    }

    public Parcelable getParcelableFromFilePathUri(Uri uri) {
        try {
            String filePath = getFilePathFromUri(uri);
            if (filePath != null) {
                DebugLog.d(Uri.parse((filePath)).toString());
                return Uri.parse(filePath);
            } else {
                DebugLog.e("file cannot be added : " + uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * used to return the absolute filepath from Uri
     *
     * @param uri
     * @return
     * @throws URISyntaxException
     */
    public String getFilePathFromUri(Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(mContext.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = mContext.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void copyFileToGalleryDirectory(ArrayList<Parcelable> filePaths) {
        try {
            for (Parcelable parcelable : filePaths) {
                String filePath = parcelable.toString();
                if (!isEmpty(filePath)) {
                    copyFileToGalleryDirectory(new File(filePath));
                }
            }
        } catch (Exception e) {
            DebugLog.e("Exception : " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        Intent sendImageAddedBroadcast = new Intent(IntentConstants.BROADCAST_UI_CHANGE_IMAGE_ADAPTER);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        localBroadcastManager.sendBroadcast(sendImageAddedBroadcast);
    }

    public void copyFileToGalleryDirectory(File file) {
        try {
            File newFile = new File(ImageUtils.createImageFile(mContext));
            copyFile(file, newFile);
        } catch (Exception e) {
            DebugLog.e("crashed while copying file");
            e.printStackTrace();
        }
    }

    private void copyFile(File inputFile, File outputFile) {
        if (inputFile.length() == 0) {
            DebugLog.d("input file is empty");
            return;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(inputFile);
            out = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;
        } catch (FileNotFoundException fnfe1) {
            DebugLog.e(fnfe1.getMessage());
        } catch (Exception e) {
            DebugLog.e(e.getMessage());
        }
    }

    /**
     * Returns the parameter date and time in human readable form.
     *
     * @param time
     * @return
     */
    public String toHumanReadableDateAndTime(long time) {
        return (new SimpleDateFormat("dd MMM yyyy hh:mm:ss a")).format(new Date(time));
    }

    public String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * Creates a confirmation dialog with Yes-No Button. By default the buttons just dismiss the
     * dialog.
     *
     * @param message
     *            Message to be shown in the dialog.
     * @param yesListener
     *            Yes click handler
     * @param noListener
     * @param yesLabel
     *            Label for yes button
     * @param noLabel
     *            Label for no button
     **/
    public void showConfirmDialog(Context context, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener, String yesLabel, String noLabel) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (yesListener == null) {
            yesListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }

        if (noListener == null) {
            noListener = new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }

        builder.setMessage(message).setPositiveButton(yesLabel, yesListener).setNegativeButton(noLabel, noListener).show();
    }
}