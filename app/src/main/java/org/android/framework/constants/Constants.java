package org.android.framework.constants;

import android.os.Environment;

/**
 * 常量类
 *
 * @author shenyan
 */
public class Constants {
    /**
     * 屏幕的宽
     */
    public static int ScreenWidth = 0;
    /**
     * 屏幕的高
     */
    public static int ScreenHeight = 0;
    /**
     * 缩放比例
     */
    private static float scaled = 0;

    /**
     * 以320X480为基础
     *
     * @return
     */
    public static float ConstantCodition() {
        if (scaled != 0) {
            return scaled;
        }
        if (Constants.ScreenWidth == 240) {// 240*320
            scaled = 0.6f;
        } else if (Constants.ScreenWidth == 320) {// 320*480
            scaled = 1.0f;
        } else if (Constants.ScreenWidth == 480) {
            if (Constants.ScreenHeight == 800) {// 480*800
                scaled = 1.5f;// 图片的
                // scaled = 1.1f;//Canvas
            } else if (Constants.ScreenHeight == 854) {// 480*854
                scaled = 1.6f;// 图片的
                // scaled = 1.8f;//Canvas
            }
        }
        return scaled;
    }

}
