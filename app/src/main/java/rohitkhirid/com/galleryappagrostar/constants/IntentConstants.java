package rohitkhirid.com.galleryappagrostar.constants;

/**
 * Created by rohitkhirid on 7/1/17.
 */

public class IntentConstants {
    // start activity for google sign in
    public static final int INTENT_CODE_GOOGLE_SIGN_IN = 1;

    // request code for requesting camera and storage permission
    public static final int PERMISSION_REQUEST_CODE_CAMERA_STORAGE_CAMERA = 2;

    public static final int PERMISSION_REQUEST_CODE_CAMERA_STORAGE_GALLERY = 6;

    // request code for requesting image from camera (document picker)
    public static final int INTENT_CODE_CAMERA_IMAGE = 3;

    // request code for requesting image from gallery (document picker)
    public static final int INTENT_CODE_GALLERY_IMAGE = 4;

    public static final String INTENT_KEY_IMAGE_FILEPATH = "documentpicker.selectedimages";

    public static final int INTENT_CODE_DOCUMENT_PICKER = 5;

    public static final String INTENT_KEY_FILE_PATHS_UPLOAD = "uploaderservice.filepath";

    public static final String INTENT_KEY_FILE_PATH_FULLSCREEN = "fullScreenActivity.filepath";
}
