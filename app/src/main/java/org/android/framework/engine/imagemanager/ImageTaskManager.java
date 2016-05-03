package org.android.framework.engine.imagemanager;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;

import org.android.framework.log.LogInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Image管理类
 * <p/>
 * Created by ShenYan on 14-1-20.
 */
class ImageTaskManager implements Runnable {
    public static ImageTaskManager instance = null;

    private ImageTaskManager() {
    }

    public static synchronized ImageTaskManager getInstance() {
        if (instance == null) {
            instance = new ImageTaskManager();
            Thread thread = new Thread(new ImageTaskManager());
            thread.start();
        }
        return instance;
    }

    private int maxRunningService = 3;


    private ArrayList<ImageTask> mReadyTasks = new ArrayList<ImageTask>();
    private Object mReadyTasksLock = new Object();

    private ArrayList<ImageTask> mRunningTasks = new ArrayList<ImageTask>();
    private Object mRunningTasksLock = new Object();

    private ArrayList<ImageTask> mWaitingTasks = new ArrayList<ImageTask>();
    private Object mWaitingTasksLock = new Object();


    //    MySyncList<ImageTask> mySyncList = new MySyncList<ImageTask>(3);


    private Context context;
    private String cacheDirName = "cache";

    /**
     * add a new ImageTask to Hashtable
     *
     * @param imageTask
     */
    public void addImageTask(Context context, ImageTask imageTask) {

        this.context = context;
        Bitmap bitmap = null;

        bitmap = ImageMemoryCache.getInstance().getBitmapFromMemoryCache(imageTask.getUrl());
        if (bitmap != null) {
            imageTask.setView(bitmap);
            if (imageTask.getImageCallBackInterface() != null) {
                imageTask.getImageCallBackInterface().successCallBack(imageTask.getTag(), imageTask.getUrl());
            }
        }
        //        bitmap = ImageDiskCache.getInstance(context).getBitmapFormDiskCache(context, imageTask);


        if (bitmap == null) {
            if (isRunningTask(imageTask)) {
                synchronized (mWaitingTasksLock) {
                    mWaitingTasks.add(imageTask);
                }
                synchronized (mReadyTasksLock) {
                    mReadyTasks.remove(imageTask);
                }
            } else {
                synchronized (mReadyTasksLock) {
                    mReadyTasks.add(imageTask);
                }
                mImageManagerLock.notifyAll();
            }

        }
    }

    //    public void clearDiskCache(Context context) {
    //        ImageDiskCache.getInstance(context).clearCacheInternal();
    //    }
    //
    //    public void clearMemoryCache() {
    //        ImageMemoryCache.getInstance().clearMemoryCache();
    //    }
    private Object mImageManagerLock = new Object();

    @Override
    public void run() {
        while (true) {
            LogInfo.out("running...");
            if (mReadyTasks.size() <= 0) {
                try {
                    mImageManagerLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ImageTask task;
            synchronized (mReadyTasksLock) {
                int size = mReadyTasks.size();
                task = mReadyTasks.get(size - 1);
            }
            if (task != null) {
                loadBitmapsByExecutorService(task);
            }
        }
    }

    private ExecutorService executorService = Executors.newFixedThreadPool(maxRunningService);
    private Handler mainHandler = new Handler();

    private void loadBitmapsByExecutorService(final ImageTask imageTask) {
        executorService.submit(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                String threadName = "当前线程：" + Thread.currentThread().getName();
                Bitmap bitmap = ImageMemoryCache.getInstance().getBitmapFromMemoryCache(imageTask.getKey());
                if (bitmap == null) {
                    synchronized (mRunningTasksLock) {
                        mRunningTasks.add(imageTask);
                    }
                    synchronized (mReadyTasksLock) {
                        mReadyTasks.remove(imageTask);
                    }
                    bitmap = downloadBitmapToDiskCache(imageTask, threadName);
                }
                try {
                    final Bitmap finalBitmap = bitmap;
                    mainHandler.post(new Runnable() {

                        @Override
                        public void run() {//这将在主线程运行
                            if (finalBitmap != null) {
                                synchronized (mRunningTasksLock) {
                                    mRunningTasks.remove(imageTask);
                                }
                                checkWaitingTaskList(imageTask, finalBitmap);
                                callBackImageTask(imageTask, finalBitmap);
                            } else {
                                if (imageTask.getImageCallBackInterface() != null){
                                    imageTask.getImageCallBackInterface().failCallBack(imageTask.getTag(),imageTask.getUrl());
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    private void callBackImageTask(ImageTask imageTask, Bitmap finalBitmap) {
        if (imageTask.getView() != null) {
            imageTask.setView(finalBitmap);
        }
        if (imageTask.getImageCallBackInterface() != null) {
            imageTask.getImageCallBackInterface().successCallBack(imageTask.getTag(), imageTask.getUrl());
        }
    }

    private void checkWaitingTaskList(ImageTask imageTask, Bitmap finalBitmap) {
        synchronized (mWaitingTasksLock) {
            int size = mWaitingTasks.size();
            String url = imageTask.getUrl();
            ArrayList<Integer> idList = new ArrayList<Integer>();
            for (int i = 0; i < size; i++) {
                ImageTask waitTask = mWaitingTasks.get(i);
                if (url.equals(waitTask.getUrl())) {
                    idList.add(i);
                    callBackImageTask(imageTask, finalBitmap);
                }
            }
            size = idList.size();
            for (int i = 0; i < size; i++) {
                mWaitingTasks.remove(i);
            }
        }
    }

    private boolean isRunningTask(ImageTask imageTask) {
        synchronized (mRunningTasksLock) {
            int size = mRunningTasks.size();
            for (int i = 0; i < size; i++) {
                ImageTask task = mRunningTasks.get(i);
                if (task.getUrl().equals(imageTask.getUrl())) {
                    return true;
                }
            }
            return false;
        }
    }

    private Bitmap downloadBitmapToDiskCache(ImageTask imageTask, String threadName) {
        File file = getDiskCacheDir(context, imageTask.getKey());
        try {
            boolean success = downloadUrlToStream(imageTask.getUrl(), new FileOutputStream(file), threadName);
            if (success) {
                return decodeSampledBitmapFromFile(file.getParent(), imageTask.getImageWidth(), imageTask.getImageHeight());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decode and sample down a bitmap from a file to the requested width and height.
     *
     * @param filename  The full path of the file to decode
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
     * that are equal to or greater than the requested width and height
     */
    private Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);
    }

    /**
     * Calculate an inSampleSize for use in a {@link android.graphics.BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link android.graphics.BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static final int IO_BUFFER_SIZE = 8 * 1024;

    /**
     * Download a bitmap from a URL and write the content to an output stream.
     * 从一个URL下载一个bitmap到一个output stream中
     *
     * @param urlString The URL to fetch
     * @return true if successful, false otherwise
     */

    public boolean downloadUrlToStream(String urlString, OutputStream outputStream, String threadName) {
        disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            //            LogInfo.out(threadName + " - " + "downloading... write to Disk Cache");
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            //            LogInfo.out(threadName + " - " + "downloading... write over");
            return true;
        } catch (final IOException e) {
            //            Log.e(TAG, "Error in downloadBitmap - " + e);
            LogInfo.out("Error in downloadBitmap - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
            }
        }
        return false;
    }

    /**
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     * 在Froyo之前的一个bug，详情请看上边的URL
     */
    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }


    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    private File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     * otherwise.
     */
    @TargetApi(9)
    private boolean isExternalStorageRemovable() {
        if (Utils.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(8)
    private File getExternalCacheDir(Context context) {
        if (Utils.hasFroyo()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + File.separator + cacheDirName + File.separator;
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }
}
