/*
package org.android.framework.engine.imagemanager;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.widget.Toast;

import org.android.BuildConfig;
import org.android.framework.engine.image.util.DiskLruCache;
import org.android.framework.log.LogInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

*/
/**
 * Created by ShenYan on 14-1-21.
 *//*

class ImageDiskCache {
    private static ImageDiskCache instance = null;

    private ImageDiskCache(Context context) {
        init(context);
    }

    public static synchronized ImageDiskCache getInstance(Context context) {
        if (instance == null) {
            instance = new ImageDiskCache(context);
        }
        return instance;
    }

    public  getBitmapFormDiskCache(Context context, ImageTask imageTask) {
        checkConnection(context);
        loadBitmapsByExecutorService(imageTask);
    }


    private void init(Context context) {

        //获得缓存目录
        initDiskCache();
    }

    */
/**
     * 初始化磁盘缓存目录
     *//*

    private void initDiskCache() {
    }

    */
/**
     * 清空磁盘缓存
     *//*

    protected void clearCacheInternal() {
    }

    */
/**
     * 刷新磁盘缓存
     *//*

    protected void flushCacheInternal() {
    }

    */
/**
     * 关闭磁盘缓存
     *//*

    protected void closeCacheInternal() {
    }

    */
/**
     * Simple network connection check.
     * 检测网络连接
     *
     * @param context
     *//*

    private void checkConnection(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            Toast.makeText(context, "没有发现网络，请设置！", Toast.LENGTH_LONG).show();
            //            Log.e(TAG, "checkConnection - no connection found");
            LogInfo.out("checkConnection - no connection found");
        }
    }

    */
/**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     *//*

    private File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

    */
/**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     * otherwise.
     *//*

    @TargetApi(9)
    private boolean isExternalStorageRemovable() {
        if (Utils.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    */
/**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     *//*

    @TargetApi(8)
    private File getExternalCacheDir(Context context) {
        if (Utils.hasFroyo()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    */
/**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     *//*

    @TargetApi(9)
    private long getUsableSpace(File path) {
        if (Utils.hasGingerbread()) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }


    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private Handler mainHandler = new Handler();

    private void loadBitmapsByExecutorService(final ImageTask imageTask) {
        executorService.submit(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
//                LogInfo.out("当前线程：" + Thread.currentThread().getName());
                String threadName = "当前线程：" + Thread.currentThread().getName();
                final Bitmap bitmap = downloadBitmapToDiskCache(imageTask, threadName);
                if (bitmap != null) {
//                    LogInfo.out("downloaded success and save to Memory Cache");
                    ImageMemoryCache.getInstance().addBitmapToMemoryCache(imageTask.getUrl(), bitmap);
                } else {
//                    LogInfo.out(threadName + " - " + "downloaded fail not to save to Memory Cache");
                }
                try {
                    mainHandler.POST(new Runnable() {

                        @Override
                        public void run() {//这将在主线程运行
                            if (bitmap != null) {
                                imageTask.setView(bitmap);
                                if (imageTask.getImageCallBackInterface() != null) {
                                    imageTask.getImageCallBackInterface().successCallBack(imageTask.getTag(), imageTask.getUrl());
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

    */
/**
     * The demo_template_picker process method, which will be called by the ImageWorker in the AsyncTask background
     * thread.
     * 图片下载方法，在一个线程中被调用
     *
     * @return The downloaded and resized bitmap
     *//*

    private Bitmap downloadBitmapToDiskCache(ImageTask imageTask, String threadName) {
        if (BuildConfig.DEBUG) {
            //            Log.d(TAG, "downloadBitmapToDiskCache - " + url);
            //            LogInfo.out("downloadBitmapToDiskCache - " + imageTask.getUrl());
        }

        final String key = hashKeyForDisk(imageTask.getUrl());
        //        final String key = imageTask.getUrl();
        FileDescriptor fileDescriptor = null;
        FileInputStream fileInputStream = null;
        DiskLruCache.Snapshot snapshot;
        synchronized (mDiskCacheLock) {
            // Wait for disk cache to initialize
            //            while (mDiskCacheStarting) {
            //                try {
            //                    mDiskCacheLock.wait();
            //                } catch (InterruptedException e) {
            //                }
            //            }

            if (mDiskCache != null) {
                try {
                    snapshot = mDiskCache.GET(key);
                    if (snapshot == null) {
                        if (BuildConfig.DEBUG) {
//                            LogInfo.out(threadName + " - " + "not found in disk cache, downloading...");
                        }
                        DiskLruCache.Editor editor = mDiskCache.edit(key);
                        if (editor != null) {
                            if (downloadUrlToStream(imageTask.getUrl(), editor.newOutputStream(DISK_CACHE_INDEX), threadName)) {
//                                LogInfo.out(threadName + " - " + "downloaded success");
                                editor.commit();
                            } else {
//                                LogInfo.out(threadName + " - " + "downloaded fail");
                                editor.abort();
                            }
                        }
                        snapshot = mDiskCache.GET(key);
                    } else {
//                        LogInfo.out(threadName + " - " + "found in disk cache, reading...");
                    }
                    if (snapshot != null) {
//                        LogInfo.out(threadName + " - " + "beging reading...");
                        fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
                        fileDescriptor = fileInputStream.getFD();
                    } else {
//                        LogInfo.out(threadName + " - " + "snapshot is null");
                    }
                } catch (IOException e) {
                    //                    Log.e(TAG, "downloadBitmapToDiskCache - " + e);
//                    LogInfo.out(threadName + " - " + "downloadBitmapToDiskCache1 - " + e);
                } catch (IllegalStateException e) {
                    //                    Log.e(TAG, "downloadBitmapToDiskCache - " + e);
//                    LogInfo.out(threadName + " - " + "downloadBitmapToDiskCache2 - " + e);
                } finally {
                    if (fileDescriptor == null && fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }

        Bitmap bitmap = null;
        if (fileDescriptor != null) {
//            LogInfo.out(threadName + " - " + "creatint bitmap");
            bitmap = decodeSampledBitmapFromDescriptor(fileDescriptor, imageTask.getImageWidth(), imageTask.getImageHeight());
        } else {
//            LogInfo.out(threadName + " - " + "fileDescriptor is null");
        }
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {

            }
        }
        return bitmap;
    }

    */
/**
     * Download a bitmap from a URL and write the content to an output stream.
     * 从一个URL下载一个bitmap到一个output stream中
     *
     * @param urlString The URL to fetch
     * @return true if successful, false otherwise
     *//*

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

    */
/**
     * A hashing method that changes a string (like a URL) into a hash suitable for using as a
     * disk filename.
     *//*

    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    */
/**
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     * 在Froyo之前的一个bug，详情请看上边的URL
     *//*

    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    */
/**
     * Decode and sample down a bitmap from a file input stream to the requested width and height.
     *
     * @param fileDescriptor The file descriptor to read from
     * @param reqWidth       The requested width of the resulting bitmap
     * @param reqHeight      The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
     * that are equal to or greater than the requested width and height
     *//*

    public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        // If we're running on Honeycomb or newer, try to use inBitmap
        //        if (Utils.hasHoneycomb()) {
        //            addInBitmapOptions(options);
        //        }

        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    //    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    //    private static void addInBitmapOptions(BitmapFactory.Options options) {
    //        // inBitmap only works with mutable bitmaps so force the decoder to
    //        // return mutable bitmaps.
    //        options.inMutable = true;
    //
    //        if (cache != null) {
    //            // Try and find a bitmap to use for inBitmap
    //            Bitmap inBitmap = cache.getBitmapFromReusableSet(options);
    //
    //            if (inBitmap != null) {
    //                if (BuildConfig.DEBUG) {
    ////                    Log.d(TAG, "Found bitmap to use for inBitmap");
    //                    LogInfo.out("Found bitmap to use for inBitmap");
    //                }
    //                options.inBitmap = inBitmap;
    //            }
    //        }
    //    }

    */
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
     *//*

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
}
*/
