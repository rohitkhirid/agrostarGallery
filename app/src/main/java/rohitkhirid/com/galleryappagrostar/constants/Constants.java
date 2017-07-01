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
}