package com.jotangi.NumberHealthy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {

    public static Bitmap loadAndRotateBitmap(String photoFilePath) {
        // Create and configure BitmapFactory
        try {
            FileInputStream fis = new FileInputStream(photoFilePath);
            int angle = getRotateAngleFromImageFile(fis);
            fis.close();

            fis = new FileInputStream(photoFilePath);
            BitmapFactory.Options bounds = getBitmapBounds(fis);
            fis.close();

            fis = new FileInputStream(photoFilePath);
            Bitmap bmp = loadBitmap(fis);
            fis.close();

            Bitmap rotatebmp = rotateBitmap(bmp, angle, bounds.outWidth, bounds.outHeight);
            //bmp.recycle();
            return rotatebmp;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Bitmap loadAndRotateBitmap(Context context, Uri photoFileUri) {
        // Create and configure BitmapFactory
        try {
            InputStream is = context.getContentResolver().openInputStream(photoFileUri);
            int angle = getRotateAngleFromImageFile(is);
            is.close();

            is = context.getContentResolver().openInputStream(photoFileUri);
            BitmapFactory.Options bounds = getBitmapBounds(is);
            is.close();

            is = context.getContentResolver().openInputStream(photoFileUri);
            Bitmap bmp = loadBitmap(is);
            is.close();

            Bitmap rotatebmp = rotateBitmap(bmp, angle, bounds.outWidth, bounds.outHeight);
            //bmp.recycle();
            return rotatebmp;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static BitmapFactory.Options getBitmapBounds(InputStream is) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, bounds);
        return bounds;
    }

    public static Bitmap loadBitmap(InputStream is) {
        Bitmap bmp = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            bmp = BitmapFactory.decodeStream(is, null, opts);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bmp;
    }

    public static int getRotateAngleFromImageFile(InputStream is) {
        int rotationAngle = 0;
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(is);
            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotationAngle;
    }

    public static Bitmap rotateBitmap(Bitmap bm, int angle, int outWidth, int outHeight) {
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(angle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        // Return result

        return Bitmap.createBitmap(bm, 0, 0, outWidth, outHeight, matrix, true);
    }

}
