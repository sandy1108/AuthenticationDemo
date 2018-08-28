package pro.wsgh.authenticationdemo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import pro.wsgh.fingerprintidentify.FingerprintIdentify;
import pro.wsgh.fingerprintidentify.base.BaseFingerprint;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button mInitFingerPrint;
    private Button mStartFingerPrint;
    private TextView mFingerPrintResult;
    private FingerprintIdentify mFingerprintIdentify;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initClickListener();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 初始化View
     */
    private void initView(){
        setContentView(R.layout.activity_main);
        mInitFingerPrint = findViewById(R.id.btn_init_fingerprint);
        mStartFingerPrint = findViewById(R.id.btn_fingerprint);
        mFingerPrintResult = findViewById(R.id.tv_auth_result);
    }

    /**
     * 初始化按钮动作
     */
    private void initClickListener(){
        //指纹初始化
        mInitFingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "mInitFingerPrint onClick");
                initFingerPrint();
            }
        });

        //启动指纹认证
        mStartFingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(TAG, "mStartFingerPrint onClick");
                mFingerPrintResult.setText("等待指纹验证结果");
                mAlertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(true)
                        .setMessage("正在等待验证指纹")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                mFingerPrintResult.setText("取消指纹验证");
                                mFingerprintIdentify.cancelIdentify();
                            }
                        })
                        .create();
                mAlertDialog.show();

                mFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
                    @Override
                    public void onSucceed() {
                        Log.i(TAG, "mFingerprintIdentify onSucceed");
                        mAlertDialog.dismiss();
                        mFingerPrintResult.setText("指纹验证通过");
                        Toast.makeText(MainActivity.this, "onSucceed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNotMatch(int availableTimes) {
                        Log.i(TAG, "mFingerprintIdentify onNotMatch, availableTimes: " + availableTimes);
                        mAlertDialog.dismiss();
                        mFingerPrintResult.setText("指纹验证不通过");
                        Toast.makeText(MainActivity.this, "onNotMatch", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {
                        Log.i(TAG, "mFingerprintIdentify onFailed");
                        mAlertDialog.dismiss();
                        mFingerPrintResult.setText("指纹验证错误次数超过上限");
                        Toast.makeText(MainActivity.this, "onFailed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 初始化指纹识别
     */
    private void initFingerPrint(){
        mFingerprintIdentify = new FingerprintIdentify(this, new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                Log.i(TAG, "initFingerPrint", exception);
                mFingerPrintResult.setText("指纹验证初始化异常：" + exception.getMessage());
            }
        });
        if(!mFingerprintIdentify.isHardwareEnable()){
            mFingerPrintResult.setText("指纹验证初始化失败：设备硬件不支持指纹识别");
            return;
        }

        if(!mFingerprintIdentify.isRegisteredFingerprint()){
            mFingerPrintResult.setText("指纹验证初始化失败：未注册指纹");
            return;
        }

        if(!mFingerprintIdentify.isFingerprintEnable()){
            mFingerPrintResult.setText("指纹验证初始化失败：指纹识别不可用");
            return;
        }
        mFingerPrintResult.setText("指纹验证初始化正常");
    }
}
