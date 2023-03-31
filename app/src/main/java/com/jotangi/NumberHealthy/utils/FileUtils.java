package com.jotangi.NumberHealthy.utils;

import static androidx.core.content.FileProvider.getUriForFile;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FileUtils {
    public static final String LOG_FOLDER = "log";
    public static final String USER_HEAD_PHOTO_FILE = "head.jpg";
    public static final String TEMPERATURE_PHOTO_FILE = "temperature";

    public static void saveBitmapToImageFile(Bitmap bitmap, File file) {
        OutputStream fOut = null;

        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File createSubFolder(File parent, String childname) {
        File folder = new File(parent, childname);
        if (!folder.exists()) {
            if (!folder.mkdirs()) return null;
        }
        return folder;
    }

    public static File getProjLogFolder(Context context) {
        File folder = context.getFilesDir();
        File logfolder = new File(folder, LOG_FOLDER);
        if (!logfolder.exists()) {
            if (!logfolder.mkdirs()) return null;
        }
        return logfolder;
    }

    public static File getImageFile(Context context, String fileName) {
        File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!path.exists()) {
            if (!path.mkdirs()) return null;
        }
        return new File(path, fileName);
    }

    public static File getCacheImageFile(Context context, String fileName) {
        File path = new File(context.getExternalCacheDir(), "camera");
        if (!path.exists()) {
            if (!path.mkdirs()) return null;
        }
        return new File(path, fileName);
    }

    public static Uri getCacheImageUri(Context context, String fileName, String appid) {
        File image = getCacheImageFile(context, fileName);
        if (image == null) return null;
        return getUriForFile(context, appid + ".provider", image);
    }

    public static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }


}
