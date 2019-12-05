package com.woosik.androidpratice.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.widget.Toast;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.logging.Logger;

public class FingerprintImpl {

    private static Logger logger = Logger.getLogger("FingerprintManager");
    private static FingerprintImpl instance;

    private static final String KEY_NAME = "myid_android_key";

    public synchronized static FingerprintImpl getInstance() {
        if (instance == null) {
            instance = new FingerprintImpl();
        }
        return instance;
    }

    private FingerprintImpl() {

    }

    public String getKeyStoreState() {
        StringBuffer ret = new StringBuffer("KeyStore : ");
        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            Enumeration<String> aliases = ks.aliases();

            logger.info("KeyStore Aliases ");
            while (aliases.hasMoreElements()) {
                String key = aliases.nextElement();
                logger.info("KeyStore aliase name=" + key);
                ret.append(" [" + key + "]");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret.toString();
    }

    public void authenticateFingerprint(Context context) {
        logger.info("authenticateFingerprint()");
        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            Enumeration<String> aliases = ks.aliases();

            logger.info("KeyStore Aliases ");
            while (aliases.hasMoreElements()) {
                logger.info("KeyStore aliase name=" + aliases.nextElement());
            }

            SecretKey oldkey = (SecretKey) ks.getKey(KEY_NAME, null);
            logger.info("keyStore oldKey = " + oldkey);
            Cipher cipherTemp = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipherTemp.init(Cipher.ENCRYPT_MODE, oldkey);

            FingerprintManager fpMgr = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipherTemp);
            FingerprintHandler fingerprintHandler = new FingerprintHandler(context);
            fingerprintHandler.startAutho(fpMgr, cryptoObject);

        } catch (Exception e) {
            Toast.makeText(context, "Exception : " + e.getClass(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    public void generateKey(Context context) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);

            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);

            // This is a workaround to avoid crashes on devices whose API level is < 24
            // because KeyGenParameterSpec.Builder#setInvalidatedByBiometricEnrollment is only
            // visible on API level +24.
            // Ideally there should be a compat library for KeyGenParameterSpec.Builder but
            // which isn't available yet.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(true);
            }
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();

            Toast.makeText(context, "New key is generated !!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
