package net.iamavailable.app;

/**
 * Created by Arshad on 7/14/2016.
 */
import java.io.File;
import android.graphics.Bitmap;
import java.io.FileOutputStream;
import android.graphics.BitmapFactory;
import android.util.Log;

import net.iamavailable.app.data.DataService;

public class ImageStorage {
    protected static final String TAG = DataService.TAG;

    public static String saveToSdCard(Bitmap bitmap, String filename) {
        String gallerypath=android.os.Environment.getExternalStorageDirectory() + "/" + (android.os.Environment.DIRECTORY_DCIM).toLowerCase() + "/IamAvailable/";
        String stored = null;

        File sdcard = android.os.Environment.getExternalStorageDirectory() ;

        File file = new File(gallerypath+filename) ;
        if (file.exists())
            return stored ;

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            stored = "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stored;
    }

    public static File getImage(String imagename) {
        String gallerypath=android.os.Environment.getExternalStorageDirectory() + "/" + (android.os.Environment.DIRECTORY_DCIM).toLowerCase() + "/IamAvailable/";
        File mediaImage = null;
        try {
            String root = android.os.Environment.getExternalStorageDirectory() + "/" + (android.os.Environment.DIRECTORY_DCIM).toLowerCase() + "/IamAvailable";
            File myDir = new File(root);
            if (!myDir.exists())
                return null;

            mediaImage = new File(gallerypath+imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mediaImage;
    }
    public static boolean checkifImageExists(String imagename)
    {
        String gallerypath=android.os.Environment.getExternalStorageDirectory() + "/" + (android.os.Environment.DIRECTORY_DCIM).toLowerCase() + "/IamAvailable/";
        Bitmap b = null ;
        String path = gallerypath+imagename;
        Log.i(TAG, "Path: "+path);
        if (path != null) {
            try {
                b = BitmapFactory.decodeFile(path);
            } catch (Exception epf) {
            }
        }

        if(b == null ||  b.equals(""))
        {
            return false ;
        }
        return true ;
    }
}
