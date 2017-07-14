/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.android.cong.aocr.utils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import android.content.Context;

public class FileUtil {
    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }

    public static void closeSilently(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (Throwable e) {
                // ignore
            }
        }
    }

    public static void closeSilently(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (Throwable e) {
                // ignore
            }
        }
    }

    public static void closeSilently(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (Throwable e) {
                // ignore
            }
        }
    }

    public static void closeSilently(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (Throwable e) {
                // ignore
            }
        }
    }
}
