package com.android.cong.aocr;

import java.io.File;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.sdk.model.WordSimple;
import com.baidu.ocr.ui.camera.CameraActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CAMERA = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 生成intent对象
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);

                // 设置临时存储
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());

                // 调用拍摄普通图片activity
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取调用参数
        String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
        // 通过临时文件获取拍摄的图片
        String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
        // 判断拍摄类型（通用，身份证，银行卡）
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            // 判断是否是身份证正面
            if (CameraActivity.CONTENT_TYPE_GENERAL.equals(contentType)) {
                // 获取图片文件调用sdk数据接口，见数据接口说明
                recIdCard(filePath);
            }
        }
    }

    private void recIdCard(String filePath) {
        // 通用文字识别参数设置
        GeneralParams param = new GeneralParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));

        // 调用通用文字识别服务
        OCR.getInstance().recognizeGeneral(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                StringBuilder sb = new StringBuilder();
                // 调用成功，返回GeneralResult对象
                for (WordSimple wordSimple : result.getWordList()) {
                    // Word类包含位置信息
                    Word word = (Word) wordSimple;
                    sb.append(word.getWords());
                    sb.append("\n");
                }
                Toast.makeText(MainActivity.this, "识别成功：" + sb.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                Toast.makeText(MainActivity.this, "识别失败：" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
