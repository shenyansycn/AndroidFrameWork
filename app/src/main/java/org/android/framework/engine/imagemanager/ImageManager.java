package org.android.framework.engine.imagemanager;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.Hashtable;

/**
 * Image管理类
 * <p/>
 * Created by ShenYan on 14-1-20.
 */
class ImageManager {
    public static ImageManager instance = null;

    private ImageManager() {
    }

    public static synchronized ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }

    //    private Hashtable<String, ImageTask> mImageTaskHash = new Hashtable<String, ImageTask>();
    private Hashtable<String, String> mImageTask = new Hashtable<String, String>();
    /**
     * add a new ImageTask to Hashtable
     *
     * @param imageTask
     */
    public void addImageTask(Context context, ImageTask imageTask) {
        //        mImageTaskHash.put(imageTask.getTag(), imageTask);
//        if (mImageTask.containsKey(imageTask.getUrl())){
//            LogInfo.out();
//        }
        Bitmap bitmap =  ImageMemoryCache.getInstance().getBitmapFromMemoryCache(imageTask.getUrl());
//        LogInfo.out("bitmap url = " + imageTask.getUrl());
        if (bitmap != null){
//            LogInfo.out("bitmap is in MemoryCache!");
//            imageTask.setImageView(bitmap);
            if (imageTask.getImageCallBackInterface() != null) {
                imageTask.getImageCallBackInterface().successCallBack(imageTask.getTag(), imageTask.getUrl());
            }
        } else {
//            LogInfo.out("bitmap is not in MemoryCache, Read it from DiskCache");
//            ImageDiskCache.getInstance(context).getBitmapFormDiskCache(context,imageTask);
        }

    }

    public void clearDiskCache(Context context){
//        ImageDiskCache.getInstance(context).clearCacheInternal();
    }
    public void clearMemoryCache(){
//        ImageMemoryCache.getInstance().clearMemoryCache();
    }
}
