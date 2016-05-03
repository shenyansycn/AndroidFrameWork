package org.android.framework.engine.image.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.io.OutputStream;

/**
 * Created by ShenYan on 14-1-23.
 */
 class ImageTask {
    private ImageFetcher mImageFetcher;
    private FragmentManager fragmentManager;

    public ImageTask(Context context, FragmentManager fragmentManager, int imageWidth, int imageHeight) {
        mImageFetcher = new ImageFetcher(context, imageWidth, imageHeight);
        this.fragmentManager = fragmentManager;
        initImageCacheParams(context);
    }

    public ImageTask(Context context, FragmentManager fragmentManager, int imageSize) {
        mImageFetcher = new ImageFetcher(context, imageSize);
        this.fragmentManager = fragmentManager;
        initImageCacheParams(context);
    }

    private ImageCache.ImageCacheParams imageCacheParams = null;

    private void initImageCacheParams(Context context) {
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(context, "image_cache");
        cacheParams.setMemCacheSizePercent(0.25f);// Set memory cache to 25% of app memory
        mImageFetcher.addImageCache(fragmentManager, cacheParams);
    }

    /**
     * Load an image specified by the data parameter into an ImageView (override
     * {@link ImageWorker#processBitmap(Object)} to define the processing logic). A memory and
     * disk cache will be used if an {@link ImageCache} has been added using
     * {@link ImageWorker#addImageCache(FragmentManager, ImageCache.ImageCacheParams)}. If the
     * image is found in the memory cache, it is set immediately, otherwise an {@link AsyncTask}
     * will be created to asynchronously load the bitmap.
     *
     * @param data The URL of the image to download.
     * @param view The View to bind the downloaded image to.
     */
    public void loadImage(Object data, View view, ImageTask.ImageCallBackInterface imageCallBackInterface) {
        ImageViewObject imageViewObject = new ImageViewObject();
        imageViewObject.url = data;
        imageViewObject.view = view;
        imageViewObject.imageCallBackInterface = imageCallBackInterface;
        mImageFetcher.loadImage(imageViewObject);

//        mImageFetcher.loadImage(data, (ImageView)view);
    }

    /**
     * Pause any ongoing background work. This can be used as a temporary
     * measure to improve performance. For example background work could
     * be paused when a ListView or GridView is being scrolled using a
     * {@link android.widget.AbsListView.OnScrollListener} to keep
     * scrolling smooth.
     * <p/>
     * If work is paused, be sure setPauseWork(false) is called again
     * before your fragment or activity is destroyed (for example during
     * {@link android.app.Activity#onPause()}), or there is a risk the
     * background thread will never finish.
     */
    public ImageTask setPauseWork(Boolean pauseWork) {
        mImageFetcher.setPauseWork(pauseWork);
        return this;
    }

    public ImageTask setExitTasksEarly(Boolean exitTasksEarly) {
        mImageFetcher.setExitTasksEarly(exitTasksEarly);
        return this;
    }

    public ImageTask flushCache() {
        mImageFetcher.flushCache();
        return this;
    }

    public ImageTask closeCache() {
        mImageFetcher.closeCache();
        return this;
    }

    public ImageTask clearCache() {
        mImageFetcher.clearCache();
        return this;
    }


    /**
     * Set the target image width and height.
     *
     * @param width
     * @param height
     */
    public ImageTask setImageSize(int width, int height) {
        mImageFetcher.setImageSize(width, height);
        return this;
    }

    /**
     * Set the target image size (width and height will be the same).
     *
     * @param size
     */
    public ImageTask setImageSize(int size) {
        setImageSize(size, size);
        return this;
    }

    /**
     * Download a bitmap from a URL and write the content to an output stream.
     *
     * @param urlString The URL to fetch
     * @return true if successful, false otherwise
     */
    public boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        return mImageFetcher.downloadUrlToStream(urlString, outputStream);
    }

    /**
     * If set to true, the image will fade-in once it has been loaded by the background thread.
     */
    public ImageTask setImageFadeIn(boolean fadeIn) {
        mImageFetcher.setImageFadeIn(fadeIn);
        return this;
    }

    /**
     * Set placeholder bitmap that shows when the the background thread is running.
     *
     * @param bitmap
     */
    public ImageTask setLoadingImage(Bitmap bitmap) {
        mImageFetcher.setLoadingImage(bitmap);
        return this;
    }

    /**
     * Set placeholder bitmap that shows when the the background thread is running.
     *
     * @param resId
     */
    public ImageTask setLoadingImage(int resId) {
        mImageFetcher.setLoadingImage(resId);
        return this;
    }

    class ImageViewObject {
        View view;
        Object url;
        ImageCallBackInterface imageCallBackInterface;
    }

    public interface ImageCallBackInterface {
        void success(String url);

        void fail(String url);
    }
}
