package org.android.framework.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SystemIdentifierUtil {


    public static String getDeviceID(Context context) {
        String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (deviceId == null) {
            deviceId = "";
        }
        return deviceId;
    }

    public static String getAndroidID(Context context) {
        String androidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (androidId == null) {
            androidId = "";
        }
        return androidId;
    }

    public static String getIMEI(Context context) {
        String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
        if (imei == null) {
            imei = "";
        }
        return imei;
    }

    public static String getIMSI(Context context) {
        String imsi = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        if (imsi == null) {
            imsi = "";
        }
        return imsi;
    }

    /**
     * 获得电话号码
     *
     * @return
     */
    public static String getTelNumber(Context context) {
        String mTel = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
        if (mTel == null) {
            mTel = "";
        }
        return mTel;
    }

    /**
     * 获得手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        String model = Build.MODEL;
        if (model == null) {
            model = "";
        }
        return model;
    }

    public static String getSystemVersion() {
        String version = String.valueOf(Build.VERSION.SDK_INT);
        if (version == null || version.equals("") || version.length() == 0) {
            version = String.valueOf(Build.VERSION.SDK);
            if (version == null || version.equals("") || version.length() == 0) {
                version = "";
            }
        }
        return version;
    }

    private String sID = null;
    private final String INSTALLATION = "INSTALLATION";

    public synchronized String getInstallID(Context context) {
        File installation = new File(context.getFilesDir(), INSTALLATION);
        try {
            if (!installation.exists()) {
                writeInstallationFile(installation);
            }
            sID = readInstallationFile(installation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sID;
    }

    public boolean isFirstRun(Context context) {
        File installation = new File(context.getFilesDir(), INSTALLATION);
        try {
            if (!installation.exists()) {
                writeInstallationFile(installation);
                return true;
            }
            return false;
        } catch (Exception e) {
            // throw new RuntimeException(e);
            return false;
        }
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        // String id = UUID.randomUUID().toString();
        String id = String.valueOf(System.currentTimeMillis());
        out.write(id.getBytes());
        out.close();
    }

}
