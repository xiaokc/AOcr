package com.android.cong.aocr.ocr;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.cong.aocr.utils.FileUtil;
import com.android.cong.aocr.R;
import com.android.cong.aocr.utils.CommonUtil;
import com.android.cong.aocr.utils.Constant;
import com.android.cong.aocr.utils.HttpUtil;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OcrActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_CAMERA = 102;
    private TextView tvImgDetail;
    private ImageView ivImg;

    private Button btnBaidu;
    private Button btnGoogle;

    private String requstEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        tvImgDetail = (TextView) findViewById(R.id.image_details);
        ivImg = (ImageView) findViewById(R.id.main_image);

        btnBaidu = (Button) findViewById(R.id.btn_baidu);
        btnGoogle = (Button) findViewById(R.id.btn_google);

        btnBaidu.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
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

                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                ivImg.setImageBitmap(bitmap);

                recIdCard(filePath, bitmap);

            }
        }
    }

    private void recIdCard(String filePath, Bitmap bitmap) {
        if (!TextUtils.isEmpty(requstEngine)) {
            if (requstEngine.equals(Constant.REQEST_ENGINE_BAIDU_OCR)) {
                getOcrDataFromBaidu(filePath);
            } else if (requstEngine.equals(Constant.REQUEST_ENGINE_GOOGLE_OCR)) {
                getOcrDataFromGoogle(bitmap);
            }
        } else {
            tvImgDetail.setText("出错，请重试");
        }

    }

    private void getOcrDataFromBaidu(String filePath) {
        // 通用文字识别参数设置
        GeneralParams param = new GeneralParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));

        // 调用通用文字识别服务
        OCR.getInstance().recognizeGeneral(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                StringBuilder sb = new StringBuilder("识别成功:\n");
                // 调用成功，返回GeneralResult对象
                for (WordSimple wordSimple : result.getWordList()) {
                    // Word类包含位置信息
                    Word word = (Word) wordSimple;
                    sb.append(word.getWords());
                    sb.append("\n");
                }
                tvImgDetail.setText(sb.toString());
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
                tvImgDetail.setText("识别失败");
            }
        });
    }

    private void getOcrDataFromGoogle(final Bitmap bitmap) {
        new AsyncTask<Void, Void, String>() {
            String res = "";
            @Override
            protected String doInBackground(Void... params) {
                try {
                    JSONObject requestJson = new JSONObject();
                    JSONArray requests = new JSONArray();
                    JSONObject firstJson = new JSONObject();

                    JSONObject imageJson = new JSONObject();
                    imageJson.put("content", CommonUtil.enodeBitmap(bitmap));

                    JSONArray features = new JSONArray();
                    JSONObject typeJson = new JSONObject();
                    typeJson.put("type", "TEXT_DETECTION");
                    features.put(typeJson);

                    firstJson.put("image", imageJson);
                    firstJson.put("features", features);

                    requests.put(firstJson);
                    requestJson.put("requests", requests);
                    Log.i("===>xkc", "requestJson:" + requestJson);

                    return HttpUtil.submitJson(Constant.GOOGLE_OCR_URL, requestJson.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.i("===>xkc","result:"+s);
                String textDes = "";
                if (!TextUtils.isEmpty(s)) {
                    textDes = CommonUtil.getTextAnnotation(s);
                    if (!TextUtils.isEmpty(textDes)) {
                        tvImgDetail.setText(textDes);
                    } else {
                        tvImgDetail.setText("No Text Found.");
                    }
                }
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        // 生成intent对象
        Intent intent = new Intent(OcrActivity.this, CameraActivity.class);

        // 设置临时存储
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getApplication()).getAbsolutePath());

        // 调用拍摄普通图片activity
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);

        switch (v.getId()) {
            case R.id.btn_baidu:
                requstEngine = Constant.REQEST_ENGINE_BAIDU_OCR;
                break;
            case R.id.btn_google:
                requstEngine = Constant.REQUEST_ENGINE_GOOGLE_OCR;
                break;
        }
    }
}
