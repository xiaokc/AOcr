package com.android.cong.aocr.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiaokecong on 13/07/2017.
 */

public class HttpUtil {
    public static String submitJson(final String requireUrl, String jsonStr) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        OutputStream os = null;
        String response = null;

        byte[] buffer = null;

        try {
            URL url = new URL(requireUrl);
            connection = (HttpURLConnection) url.openConnection();

            // 允许向url流中读写数据
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // 启动post方法
            connection.setRequestMethod("POST");

            // 设置请求头内容
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");

            // 将请求json数据写入输出流
            os = connection.getOutputStream();
            os.write(jsonStr.getBytes("UTF-8"));

            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            response = builder.toString();

            if (connection.getResponseCode() != 200) {
                response = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            FileUtil.closeSilently(reader);

            if (connection != null) {
                connection.disconnect();
            }
        }

        return response;
    }
}
