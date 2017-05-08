package com.xing.fingerprintdemo;

import android.app.KeyguardManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/8.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private TextView textView;
    FingerprintManagerCompat fingerprintManager;
    KeyguardManager keyguardManager;
    FingerprintManagerCompat.AuthenticationCallback callback;
    CancellationSignal cancellationSignal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.submit);
        textView = (TextView) findViewById(R.id.result);
        button.setOnClickListener(this);

        fingerprintManager = FingerprintManagerCompat.from(this);
        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        cancellationSignal = new CancellationSignal();

        callback = new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
                textView.setText(errString);
                Log.i("sysout", "onAuthenticationError.." + errString);
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                super.onAuthenticationHelp(helpMsgId, helpString);
                textView.setText(helpString);
                Log.i("sysout", "onAuthenticationHelp.." + helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                textView.setText("指纹解锁  成功");
                Log.i("sysout", "onAuthenticationSucceeded...");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                textView.setText("指纹解锁  失败");
                Log.i("sysout", "onAuthenticationFailed");
            }
        };


    }


    @Override
    public void onClick(View view) {

        if (keyguardManager.isKeyguardSecure() && fingerprintManager.isHardwareDetected()) {
            Log.i("sysout", "ok");
            textView.setText("该设备支持指纹解锁");
            try {
                fingerprintManager.authenticate(new CryptoObjectHelper().buildCryptoObject(), 0, cancellationSignal, callback, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            textView.setText("该设备不支持指纹解锁");
        }
    }
}
