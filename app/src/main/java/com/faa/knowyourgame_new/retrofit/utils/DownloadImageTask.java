package com.faa.knowyourgame_new.retrofit.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    String imgName;
    File storagePath;

    public DownloadImageTask(String _imgName, File _storagePath) {

        this.imgName = _imgName;
        this.storagePath = _storagePath;
    }

    protected Bitmap doInBackground(String... urls) {

        String urlDisplay = urls[0];
        String savePath = "";
        Bitmap loadedImage = null;
        boolean success = false;

        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            loadedImage = BitmapFactory.decodeStream(in);

            File savedImage = new File(storagePath, imgName);
            savePath = savedImage.getPath();

            // Encode the file as a PNG image.
            FileOutputStream outStream;

            outStream = new FileOutputStream(savedImage);
            loadedImage.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            /* 100 to keep full quality of the image */

            outStream.flush();
            outStream.close();
            success = true;
        }
        catch (IOException e) { e.printStackTrace(); }

        if (success) { Log.d("IMAGE", "Image saved to " + savePath); }
        else { Log.e("IMAGE", "Error during image saving!"); }

        return loadedImage;
    }

    protected void onPostExecute(Bitmap result) {
        Log.e("IMAGE", "Image downloaded!");
    }
}