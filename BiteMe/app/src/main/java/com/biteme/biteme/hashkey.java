package com.biteme.biteme;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by den_d on 27/11/2016.
 */

public class hashkey extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        printHashkey();
    }
    public void  printHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.biteme.biteme",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("keyhash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
}
