package net.iamavailable.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BitmapUtil {

    private static final String TAG ="PostScreen1";
    public static File getAlbumStorageDir(String albumName) {
        String CAMERA_DIR = "/dcim/";
        return new File (
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }

    public static File getAlbumDir(String albumName) {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = getAlbumStorageDir(albumName);

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d(TAG, "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(TAG, "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }
    public static File createImageFile(String albumName) throws IOException {
        // Create an image file name
        String JPEG_FILE_PREFIX="IaA_", JPEG_FILE_SUFFIX=".jpg";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir(albumName);
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        Log.i(TAG, "createImageFile::"+imageFileName );
        return imageF;
    }

    public static Bitmap getThumb(String path, int thumbWidth, int thumbHeight) {
        Log.i(TAG,"Thumbnail With:" + thumbWidth + " Height: " + thumbHeight);
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), thumbWidth, thumbHeight);
    }
}
