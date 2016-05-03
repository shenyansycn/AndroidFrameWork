package org.android.framework.engine.imagemanager;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import org.android.BuildConfig;
import org.android.framework.log.LogInfo;

/**
 * Created by ShenYan on 14-1-21.
 */
class ImageMemoryCache {
    private static ImageMemoryCache instance = null;

    private ImageMemoryCache() {
        mCacheSize = setMemCacheSizePercent(memCacheSizePercent);
        init();
    }

    public static synchronized ImageMemoryCache getInstance() {
        if (instance == null) {
            instance = new ImageMemoryCache();
        }
        return instance;
    }

    /**
     * 缓存占用内存比例
     */
    private float memCacheSizePercent = 0.25f;
    /**
     * 内存尺寸
     */
    private int mCacheSize = 0;
    private android.support.v4.util.LruCache<String, Bitmap> mMemoryCache;

    private Object mMemoryCacheLock = new Object();

    private void init() {
        if (BuildConfig.DEBUG) {
            LogInfo.out("Memory cache created (size = " + mCacheSize + ")");
        }
        mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap value) {
                final int bitmapSize = getBitmapSize(value) / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;
            }
        };

    }

    /**
     * Get the size in bytes of a bitmap in a BitmapDrawable.
     *
     * @param value
     * @return size in bytes
     */
    @TargetApi(12)
    private int getBitmapSize(Bitmap value) {

        if (Utils.hasHoneycombMR1()) {
            return value.getByteCount();
        }
        // Pre HC-MR1
        return value.getRowBytes() * value.getHeight();
    }

    /**
     * Sets the memory cache size based on a percentage of the max available VM memory.
     * Eg. setting percent to 0.2 would set the memory cache to one fifth of the available
     * memory. Throws {@link IllegalArgumentException} if percent is < 0.05 or > .8.
     * memCacheSize is stored in kilobytes instead of bytes as this will eventually be passed
     * to construct a LruCache which takes an int in its constructor.
     * <p/>
     * This value should be chosen carefully based on a number of factors
     * Refer to the corresponding Android Training class for more discussion:
     * http://developer.android.com/training/displaying-bitmaps/
     *
     * @param percent Percent of available app memory to use to size memory cache
     */
    private int setMemCacheSizePercent(float percent) {
        if (percent < 0.05f || percent > 0.8f) {
            throw new IllegalArgumentException("setMemCacheSizePercent - percent must be " + "between 0.05 and 0.8 (inclusive)");
        }
        return Math.round(percent * Runtime.getRuntime().maxMemory() / 1024);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            synchronized (mMemoryCacheLock) {
                mMemoryCache.put(key, bitmap);
            }
        }
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        Bitmap bitmap = null;
        synchronized (mMemoryCacheLock) {
            bitmap = mMemoryCache.get(key);
        }
        return bitmap;
    }

    public void removeBitmapFromMemoryCache(String key) {
        if (getBitmapFromMemoryCache(key) != null) {
            synchronized (mMemoryCacheLock) {
                mMemoryCache.remove(key);
            }
        }
    }
    public void clearMemoryCache(){
        synchronized (mMemoryCacheLock){
            mMemoryCache.evictAll();
        }
    }
}
