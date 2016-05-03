package org.android.framework.utils;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SystemFileManager {
    private Context context = null;
    private String fileName = "";

    public SystemFileManager(Context context, String fileName) {
        super();
        this.context = context;
        this.fileName = fileName;
    }

    public boolean isExistsSystemFile() {
        File file = new File(context.getFilesDir(), fileName);
        try {
            if (!file.exists()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public String readSystemFile() {
        String str = "";
        File file = new File(context.getFilesDir(), fileName);
        try {
            RandomAccessFile f = new RandomAccessFile(file, "r");
            byte[] bytes = new byte[(int) f.length()];
            f.readFully(bytes);
            f.close();
            str = new String(bytes);
        } catch (FileNotFoundException e) {
//			e.printStackTrace();
            str = "";
        } catch (IOException e) {
//			e.printStackTrace();
            str = "";
        }
        return str;
    }

    public boolean writeSystemFile(String str) {
        File file = new File(context.getFilesDir(), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(str.getBytes());
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
//			e.printStackTrace();
            return false;
        } catch (IOException e) {
//			e.printStackTrace();
            return false;
        }
    }
}
