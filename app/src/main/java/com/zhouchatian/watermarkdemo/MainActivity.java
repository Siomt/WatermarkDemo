package com.zhouchatian.watermarkdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private TextView watermark_text;
    private Button btnAdd;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    try {
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String text = watermark_text.getText().toString();
                                Point point = new Point(2.0,2.0);
                                Scalar scalar = new Scalar(0,0);
                                DFTUtil.getInstance().transformImageWithText(img2Mat(),text,point,3.0,scalar);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        watermark_text = (TextView) findViewById(R.id.watermark_text);
        btnAdd = (Button) findViewById(R.id.btn_add);
        findViewById(R.id.btn_extract).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    /**
     * 图片转Mat
     * @return
     */
    private Mat img2Mat(){
        Mat mat = new Mat();
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lena);
        Utils.bitmapToMat(srcBitmap, mat);
        return mat;
    }
}
