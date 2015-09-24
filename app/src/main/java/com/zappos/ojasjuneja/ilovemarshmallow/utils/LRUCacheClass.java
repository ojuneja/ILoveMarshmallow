package com.zappos.ojasjuneja.ilovemarshmallow.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


/**
 * Created by Ojas Juneja on 8/12/2015.
 * This class serves as cache class for storing images
 */
public class LRUCacheClass {

    private static LruCache lruCache;

    public void setLRUCache()
    {
        if(lruCache == null)
        {
            //proper cache memory is allotted to hold images
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory())/ 1024;
            final int cacheSize = maxMemory/8;
            lruCache = new LruCache<String,Bitmap>(cacheSize){
                @Override
                protected int sizeOf(String key,Bitmap bitmap)
                {
                    return bitmap.getByteCount()/1024;
                }
            };
        }
    }

    public static LruCache getCache()
    {
        return lruCache;
    }
}
