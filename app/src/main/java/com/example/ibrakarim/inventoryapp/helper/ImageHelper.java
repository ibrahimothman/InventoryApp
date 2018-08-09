package com.example.ibrakarim.inventoryapp.helper;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class ImageHelper {



    public static byte[] getImageByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
