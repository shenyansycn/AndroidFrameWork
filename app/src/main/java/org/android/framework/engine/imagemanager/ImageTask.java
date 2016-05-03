package org.android.framework.engine.imagemanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ShenYan on 14-1-20.
 */
 class ImageTask {


    private Object tag;

    Object getTag() {
        return tag;
    }

    void setTag(Object tag) {
        this.tag = tag;
    }

    private String url;

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    private String key;

    String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

    private int imageWidth = 0;
    private int imageHeight = 0;

    int getImageWidth() {
        return imageWidth;
    }

    void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    int getImageHeight() {
        return imageHeight;
    }

    void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    private Context context;

    public ImageTask(Context context, int imageWidth, int imageHeight) {
        this.context = context;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public ImageTask(Context context, int imageSize) {
        this.context = context;
        this.imageWidth = imageSize;
        this.imageHeight = imageSize;
    }


    private View view;

    public void submit(Object tag, String url, View imageView) {
        this.view = imageView;
        this.url = url;
        this.tag = tag;
        this.key = hashKeyForDisk(url);
        ImageTaskManager.getInstance().addImageTask(context, this);
    }

    void setView(Bitmap bitmap) {
        if (view != null) {
            if (view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(bitmap);
            }
        }
    }

    View getView() {
        return view;
    }

    public interface ImageCallBackInterface {
        void successCallBack(Object tag, String url);

        void failCallBack(Object tag, String url);

        void processUpdate(int percent);
    }

    private ImageCallBackInterface imageCallBackInterface = null;

    public ImageCallBackInterface getImageCallBackInterface() {
        return imageCallBackInterface;
    }

    public void setImageCallBackInterface(ImageCallBackInterface imageCallBackInterface) {
        this.imageCallBackInterface = imageCallBackInterface;
    }

    /**
     * A hashing method that changes a string (like a URL) into a hash suitable for using as a
     * disk filename.
     */
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
}
