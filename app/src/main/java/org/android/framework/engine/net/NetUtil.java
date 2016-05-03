package org.android.framework.engine.net;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * NetUtil
 *
 * @author qinwei
 */
public class NetUtil {
    /**
     * 妫�祴缃戠粶
     */
    @SuppressWarnings("static-access")
    @SuppressLint("DefaultLocale")
    public static String getNetStyle(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
        if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
            return NetStyle.NETSTYLE_ERROR;
        } else if (netWrokInfo != null && netWrokInfo.isAvailable()) {
            String typename = netWrokInfo.getTypeName().toLowerCase();
            if ((typename.indexOf("mobile") != -1) && (netWrokInfo.getExtraInfo().indexOf("cmwap") != -1)) {
                return NetStyle.NETSTYLE_CMWAP;
            } else {
                return NetStyle.NETSTYLE_NORMAL;
            }
        }
        return NetStyle.NETSTYLE_NORMAL;
    }

    /**
     * 判断是wifi还是3g网络,用户的体现性在这里了，wifi就可以建议下载或者在线播放
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是3G网络
     *
     * @param context
     * @return
     */
    public static boolean is3rd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    public static boolean is3G(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            // NETWORK_TYPE_EVDO_A是电信3G
            // NETWORK_TYPE_EVDO_A是中国电信3G的getNetworkType
            // NETWORK_TYPE_CDMA电信2G是CDMA
            // 移动2G卡 + CMCC + 2//type = NETWORK_TYPE_EDGE
            // 联通的2G经过测试 China Unicom 1 NETWORK_TYPE_GPRS

            if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
                //				System.out.println("mobile connected");
                return false;
            } else {
                //				System.out.println("type:" + info.getSubtype());
                //				System.out.println("not mobile");
                return true;
            }
        } else {
            //			System.out.println("not mobile connected");
            return true;
        }
    }

    /**
     * 判断网络连接是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            // 如果仅仅是用来判断网络连接　　　　　　
            // 则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 下面的不仅可以判断，如果没有开启网络的话，就进入到网络开启那个界面 // 用的时候可以这样用： // if(!CheckNetwork())
     * return;
     *
     * @return
     */
    protected boolean CheckNetwork(final Context context) {
        // 用的时候可以这样用：
        // if(!CheckNetwork()) return;
        boolean flag = false;
        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cwjManager.getActiveNetworkInfo() != null)
            flag = cwjManager.getActiveNetworkInfo().isAvailable();
        if (!flag) {
            Builder b = new AlertDialog.Builder(context).setTitle("没有可用的网络").setMessage("请开启GPRS或WIFI网路连接");
            b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent mIntent = new Intent("/");
                    ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    mIntent.setComponent(comp);
                    mIntent.setAction("android.intent.action.VIEW");
                    context.startActivity(mIntent);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create();
            b.show();
        }
        return flag;
    }
}
