package org.android.framework.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

import java.io.File;

/**
 * Created by shenyan on 13-7-18.
 */
public class AndroidUtil {
    public static int getDisplayWidth(Activity activity) {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getDisplayHeight(Activity activity) {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 获得状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStateBarHeight(Activity activity) {
        View v = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);

        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return v.getTop();
    }

    /**
     * 调用外部浏览器
     *
     * @param context
     * @param address
     */
    public static void viewWebSite(Context context, String address) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
        context.startActivity(intent);
    }

    /**
     * 调用系统界面拨打电话
     *
     * @param context
     * @param number
     */
    public static void callTelBySystemUI(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));// 调用拨打电话窗口
        context.startActivity(intent);
    }

    /**
     * 直接拨打电话
     *
     * @param context
     * @param number
     */
    public static void callTelByDirect(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));// 直接拨打电话
        context.startActivity(intent);
    }

    /**
     * 用系统UI发短信
     *
     * @param context
     * @param number  短信号码
     * @param smsBody 短信内容
     */
    public static void sendSMSBySystemUI(Context context, String number, String smsBody) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsBody);
        context.startActivity(it);
    }

    /**
     * 直接发送短信
     *
     * @param context
     * @param number  短信号码
     * @param smsBody 短信内容
     * @return
     */
    public static boolean sendSMSByDirect(Context context, String number, String smsBody) {
        try {
            SmsManager sms = SmsManager.getDefault();
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";
            PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);
            sms.sendTextMessage(number, null, smsBody, sentPI, deliveredPI);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 是否有SIM卡
     *
     * @param context
     * @return
     */
    public static boolean isSimExist(Context context) {
        boolean isExist = false;
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = mTelephonyManager.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                // mString = "无卡";
                isExist = false;
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                // mString = "需要NetworkPIN解锁";
                isExist = true;
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                // mString = "需要PIN解锁";
                isExist = true;
                break;

            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                // mString = "需要PUN解锁";
                isExist = true;
                break;

            case TelephonyManager.SIM_STATE_READY:
                // mString = "良好";
                isExist = true;
                break;

            case TelephonyManager.SIM_STATE_UNKNOWN:
                // mString = "未知状态";
                isExist = true;
                break;
        }
        return isExist;
    }

    /**
     * 是否飞行模式
     *
     * @param context
     * @return
     */
    public static boolean isAirplaneModeOn(Context context) {
        return android.provider.Settings.System.getInt(context.getContentResolver(),
                android.provider.Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    /**
     * 拍照
     *
     * @param act
     * @param dirName     图片目录
     * @param imgName     图片名称
     * @param requestCode 请求码
     */
    public static String takePhoto(Activity act, String dirName, String imgName, int requestCode) {
        String takePhotoFilePath = getDiskCacheDir(act, dirName).getAbsolutePath() + File.separator + imgName;
        try {
            File picture = new File(takePhotoFilePath);
            if (picture.exists()) {
                picture.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Uri uriPicture = Uri.fromFile(new File(takePhotoFilePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPicture);
//		intent.putExtra("crop","true");
        act.startActivityForResult(intent, requestCode);

        return takePhotoFilePath;
    }

    /**
     * 调用相册
     */
    public static void invokePhoto(Activity act, int requestCode) {
        Intent intent = new Intent();
        /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
        /* 取得相片后返回本画面 */
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 得到相册里的图片的路径
     *
     * @param act
     * @param uri 相册的URi
     * @return
     */
    public static String getPath(Activity act, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = act.managedQuery(uri, projection, null, null, null);
        int nIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(nIndex);
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();

        return new File(cachePath);
    }
    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     * otherwise.
     */
    @TargetApi(9)
    public static boolean isExternalStorageRemovable() {
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
    public static File getExternalCacheDir(Context context) {
        if (Utils.hasFroyo()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }
    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    @TargetApi(9)
    public static long getUsableSpace(File path) {
        if (Utils.hasGingerbread()) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }
}
