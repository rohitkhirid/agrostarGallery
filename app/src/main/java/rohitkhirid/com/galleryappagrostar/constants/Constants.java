package rohitkhirid.com.galleryappagrostar.constants;

/**
 * Created by rohitkhirid on 7/1/17.
 */
public class Constants {
    /**
     * mark this boolean false before releasing
     * else user will be irritated by the toasts !
     */
    public static final boolean IS_DEVELOPING = true;

    /**
     * logs user out everytime when app is up, strictly for development purpose
     * <p>
     * has to be false whenever app leaves emulator or test devices
     */
    public static final boolean LOG_OUT_ON_APP_UP = false;

    /**
     * image captured by camera will be saved to this directory
     */
    public static String CACHE_TEMP_IMAGES_SUBDIR = "temp_images";

    public static final String GALLERY_FILE_PROVIDER = "rohitkhirid.com.galleryappagrostar.fileprovider";

    public static final int MAX_EDGE_THUMBNAIL = 400;

    public static final String CLOUD_NAME = "agrostargallery";

    public static final String API_KEY = "878111434762122";

    public static final String API_SECRET_KEY = "gkITCW8WT_uPr9WNiaKo7CSHfSU";
}
