package com.android.cong.aocr.utils;

/**
 * Created by xiaokecong on 13/07/2017.
 */

public class Constant {
    public static final String BAIDU_API_KEY = "okCxRDFlxUQG80rBAvo5QagL";
    public static final String BAIDU_SECRET_KEY = "SGNWXH97BqnRKt4u0F7Co20BGuyLbMzh";
    public static final String BAIDU_ACCESS_TOKEN =
            "24.f9ecd72fcddbdbffc8f093f0e08359ec.2592000.1502523019.282335-9874151";

    private static final String GOOGLE_OCR_BASE_URL = "https://vision.googleapis.com/v1/images:annotate";
    private static final String GOOGLE_OCR_API_KEY = "AIzaSyBxVs_-75AvAdVBc-hvkqRlDEf4I7-kyt4";

    public static final String GOOGLE_OCR_URL = GOOGLE_OCR_BASE_URL + "?key=" + GOOGLE_OCR_API_KEY;

    public static final String REQEST_ENGINE_BAIDU_OCR = "baidu_ocr";
    public static final String REQUEST_ENGINE_GOOGLE_OCR = "google_ocr";
    public static final String  REQUEST_ENGINE_OFFLINE_OCR = "offline_ocr";
}
