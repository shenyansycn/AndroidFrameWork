package org.android.framework.engine.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by shenyan on 13-7-17.
 */
 class ImageEngine {
    private ImageFetcher mImageFetcher;
    private static final String IMAGE_CACHE_DIR = "cache";
    private ImageCache.ImageCacheParams cacheParams;

    public ImageEngine(Context context, int imageWidth, int imageHeight) {
        mImageFetcher = new ImageFetcher(context, imageWidth, imageHeight);
        init(context);
    }

    public ImageEngine(Context context, int imageSize) {
        mImageFetcher = new ImageFetcher(context, imageSize);
        init(context);
    }

    private void init(Context context) {
        cacheParams = new ImageCache.ImageCacheParams(context, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f);// Set memory cache to 25% of app memory
        mImageFetcher.addImageCache(cacheParams);
    }

    /**
     * Set placeholder bitmap that shows when the the background thread is running.
     * 设置加载过程中显示的图片
     *
     * @param bitmap
     */
    public ImageEngine setLoadingImage(Bitmap bitmap) {
        mImageFetcher.setLoadingImage(bitmap);
        return this;
    }

    /**
     * Set placeholder bitmap that shows when the the background thread is running.
     * 设置加载过程中显示的图片
     *
     * @param resId
     */
    public ImageEngine setLoadingImage(int resId) {
        mImageFetcher.setLoadingImage(resId);
        return this;
    }
    public ImageEngine setLoadingProgress(ProgressBar progressBar){
        mImageFetcher.setProgressBar(progressBar);
        return this;
    }

    /**
     * 提交请求
     *
     * @param url
     * @param imageView
     */
    public void submit(String url, ImageView imageView) {

        mImageFetcher.loadImage(url, imageView);
    }

    /**
     * Pause any ongoing background work. This can be used as a temporary
     * measure to improve performance. For example background work could
     * be paused when a ListView or GridView is being scrolled using a
     * {@link android.widget.AbsListView.OnScrollListener} to keep
     * scrolling smooth.
     * 暂停所有后台运行的工作。通常用来暂时提高性能。比如，当ListView或者GridView使用{@link android.widget.AbsListView.OnScrollListener}保持平滑移动时暂停后台运行的工作。
     * <p/>
     * If work is paused, be sure setPauseWork(false) is called again
     * before your fragment or activity is destroyed (for example during
     * {@link android.app.Activity#onPause()}), or there is a risk the
     * background thread will never finish.
     * 如果工作暂停了，在fragment或者activity销毁（例如：在{@link android.app.Activity#onPause()}期间），或者有一个后台线程永远不会完成的风险的时候，确保setPauseWork(false)被再次调用。
     */
    public ImageEngine setPauseWork(boolean pauseWork) {
        mImageFetcher.setPauseWork(pauseWork);
        return this;
    }

    /**
     * If set to true, the image will fade-in once it has been loaded by the background thread.
     */
    public ImageEngine setImageFadeIn(boolean fadeIn) {
        mImageFetcher.setImageFadeIn(fadeIn);
        return this;
    }

    public ImageEngine setExitTasksEarly(boolean exitTasksEarly) {
        mImageFetcher.setExitTasksEarly(exitTasksEarly);
        return this;
    }

    /**
     * Set the target image width and height.
     *
     * @param width
     * @param height
     */
    public ImageEngine setImageSize(int width, int height) {
        mImageFetcher.setImageSize(width, height);
        return this;
    }

    /**
     * Set the target image size (width and height will be the same).
     *
     * @param size
     */
    public ImageEngine setImageSize(int size) {
        mImageFetcher.setImageSize(size);
        return this;
    }

    /**
     * Clears both the memory and disk cache associated with this ImageCache object.
     * 清楚所有的内存和磁盘缓存
     */
    public ImageEngine clearCache() {
        mImageFetcher.clearCache();
        return this;
    }

    /**
     * Flushes the disk cache associated with this ImageCache object.
     * 刷新磁盘缓存
     */
    public ImageEngine flushCache() {
        mImageFetcher.flushCache();
        return this;
    }

    /**
     * Closes the disk cache associated with this ImageCache object.
     * 关闭磁盘缓存
     */
    public ImageEngine closeCache() {
        mImageFetcher.closeCache();
        return this;
    }

    /**
     * Cancels any pending work attached to the provided ImageView.
     * 取消任何还没有运行关于传入的ImageView的工作
     *
     * @param imageView
     */
    public void cancelWork(ImageView imageView) {
        mImageFetcher.cancelWork(imageView);
    }

    /**
     * Returns true if the current work has been canceled or if there was no work in
     * progress on this image view.
     * Returns false if the work in progress deals with the same data. The work is not
     * stopped in that case.
     * 如果当前的工作被取消了，或者没有关于传入的ImageView的工作在执行，返回true
     * 如果当前的工作处理同样的数据，返回false。
     */
    public boolean cancelPotentialWork(Object data, ImageView imageView) {
        return mImageFetcher.cancelPotentialWork(data, imageView);
    }
}
