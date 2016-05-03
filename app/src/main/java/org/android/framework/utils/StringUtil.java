package org.android.framework.utils;

import org.android.framework.log.LogInfo;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 去除字符串中的空格、回车、换行符、制表符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(str);
        String after = m.replaceAll("");
        return after;
    }

    /**
     * 判断字符个数
     *
     * @param str    要判断的字符串
     * @param isHalf 是否判断半角/全角
     * @return 字符个数
     */
    public static int judegStrLength(String str, boolean isHalf) throws UnsupportedEncodingException {
        int length = 0;
        if (isHalf) {
            String content = str;
            char[] chs = content.toCharArray();
            for (int i = 0; i < chs.length; i++) {

                char ch = chs[i];
                String ss = ch + "";
                byte[] bb = ss.getBytes("gbk");
                if (bb.length == 2) {
                    length = length + 2;
                } else {
                    length = length + 1;
                }
            }
            if (length % 2 == 1) {
                length = length / 2 + 1;
            } else {
                length = length / 2;
            }
        } else {
            length = str.length();
        }
        return length;
    }
}
