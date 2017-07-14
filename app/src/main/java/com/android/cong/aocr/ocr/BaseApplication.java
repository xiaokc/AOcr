package com.android.cong.aocr.ocr;

import com.android.cong.aocr.utils.Constant;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;

import android.app.Application;

/**
 * Created by xiaokecong on 11/07/2017.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
//            @Override
//            public void onResult(AccessToken result) {
//                // 调用成功，返回AccessToken对象
//                String token = result.getAccessToken();
//            }
//            @Override
//            public void onError(OCRError error) {
//                // 调用失败，返回OCRError子类SDKError对象
//            }
//        }, getApplicationContext(), BAIDU_API_KEY, BAIDU_SECRET_KEY);

        // 使用access token 进行ocr初始化
        OCR.getInstance().initWithToken(getApplicationContext(), Constant.BAIDU_ACCESS_TOKEN);
    }
}
