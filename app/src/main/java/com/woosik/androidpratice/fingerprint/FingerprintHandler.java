package com.woosik.androidpratice.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.Toast;

import java.util.logging.Logger;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    CancellationSignal cancellationSignal;
    private Context context;

    private static Logger logger = Logger.getLogger("FingerprintManager");

    public FingerprintHandler(Context context) {
        this.context = context;
    }

    //메소드들 정의
    public void startAutho(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        logger.info("startAutho() cryptoObject=" + cryptoObject);
        cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        //this.update("인증 에러 발생" + errString, false);
        logger.info("onAuthenticationError() code=" + errorCode);
        Toast.makeText(context, "onAuthenticationError()", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
        //this.update("인증 실패", false);
        logger.info("onAuthenticationFailed()");
        Toast.makeText(context, "onAuthenticationFailed()", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        //this.update("Error: " + helpString, false);
        logger.info("onAuthenticationHelp() helpCode=" + helpCode);
        Toast.makeText(context, "onAuthenticationHelp()", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        //this.update("앱 접근이 허용되었습니다.", true);
        logger.info("onAuthenticationSucceeded() ");
        logger.info("onAuthenticationSucceeded() cryptoObject=" + result.getCryptoObject());
        Toast.makeText(context, "onAuthenticationSucceeded()", Toast.LENGTH_LONG).show();
    }

    public void stopFingerAuth() {
        if (cancellationSignal != null && !cancellationSignal.isCanceled()) {
            cancellationSignal.cancel();
        }
    }

}
