package com.android.cong.aocr.utils;

import java.io.ByteArrayOutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;

/**
 * Created by xiaokecong on 13/07/2017.
 */

public class CommonUtil {
    public static String enodeBitmap(Bitmap bitmap) {

        String base64 = null;
        try {
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
            byte[] bytes = byteArrayOS.toByteArray();
            int flags = Base64.NO_WRAP;
            base64 = Base64.encodeToString(bytes, flags);
            byteArrayOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return base64;
    }

    public static String getTextAnnotation(String jsonStr) {
        String res = "";
        if (TextUtils.isEmpty(jsonStr)) {
            return res;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (jsonObject.has("responses")) {
                JSONArray responses = jsonObject.getJSONArray("responses");
                if (responses.length() > 0) {
                    JSONObject annotations = responses.getJSONObject(0);
                    if (annotations.has("textAnnotations")) {
                        JSONArray textAnnotations = annotations.getJSONArray("textAnnotations");
                        if (textAnnotations.length() > 0) {
                            JSONObject detailObj = textAnnotations.getJSONObject(0);
                            if (detailObj.has("description")) {
                                res = detailObj.getString("description");
                            }

                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;

    }
}
