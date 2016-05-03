package org.android.framework.log;

import android.util.Log;

/**
 * 日志类
 *
 * @author shenyan
 */
public class LogInfo {
    private static final boolean ENABLE_DEBUG = true;
    private static final String TAG = "Engine";

    /**
     * 打印调试信息
     *
     * @param object 调试信息内容
     */
    public static void out(Object object) {
        if (ENABLE_DEBUG) {
            Log.i(TAG, object.toString());
        }
    }

    public static void out(String Tag, Object object) {
        if (ENABLE_DEBUG) {
            Log.i(Tag, Tag + " " + object.toString());
        }
    }

    public static void outEmptyLine() {
        if (ENABLE_DEBUG) {
            Log.i("", "");
        }
    }
}
