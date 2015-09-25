package com.zappos.ojasjuneja.ilovemarshmallow.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.zappos.ojasjuneja.ilovemarshmallow.variables.Tag;
import com.zappos.ojasjuneja.ilovemarshmallow.utils.NetworkUtility;

import java.lang.ref.WeakReference;

/**
 * Created by Ojas Juneja on 9/13/2015.
 * This java file uses AsyncTask to download images from URL.
 * The URL has a link to JSON file and
 * Both tasks eg.product listing page and product information page
 * uses this code to download image.
 */
public class MyAsyncTaskDownloadImage extends AsyncTask<String,Void,String> {

    private Bitmap bitmap;
    private WeakReference<ImageView> imageViewWeakReference,imageViewSquareWeakReference;
    private LruCache <String,Bitmap>lruCache;
    //uses weak reference of image view in order to make UI thread independent of async task
    public MyAsyncTaskDownloadImage(ImageView imageView,LruCache lruCache,ImageView imageViewSquare)
    {
       imageViewWeakReference = new WeakReference<>(imageView);
        imageViewSquareWeakReference = new WeakReference<>(imageViewSquare);
       this.lruCache = lruCache;
    }
    @Override
    protected String doInBackground(String... urlAndType) {

            bitmap = NetworkUtility.downloadImage(urlAndType[0]);
        //if the image is downloaded properly then store it into LRU cache
            if(bitmap!=null)
            {
                lruCache.put(urlAndType[0],bitmap);
            }
        return urlAndType[1];
    }

    @Override
    protected void onPostExecute(String typePage)
    {
        //imageView is updated only for PLP and PIP
       if(typePage.equals(Tag.PIP))
       {
           ImageView imageViewSquare = imageViewSquareWeakReference.get();
           imageViewSquare.setImageBitmap(bitmap);
       }
        ImageView imageView = imageViewWeakReference.get();
        imageView.setImageBitmap(bitmap);

     }
}
