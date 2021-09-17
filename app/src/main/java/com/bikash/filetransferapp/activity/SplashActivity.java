package com.bikash.filetransferapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.bikash.filetransferapp.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SplashActivity extends Activity
{
    String SecretPin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       // setSkipPermissionRequest(true);
      //  setWelcomePageDisallowed(true);

        new Handler().postDelayed(this::gotoMainActivity, 2000);

    }

    private void gotoMainActivity(){
        getSecretPin();

        if(SecretPin.equals("")){
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            Intent intent = new Intent(this, PinCheckerActivity.class);
            intent.putExtra("activity", "Splash");
            startActivity(intent);
        }
        finish();

    }

    //    gets the encrypted pin if available
    private void getSecretPin() {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            SharedPreferences encryptedPreferences = EncryptedSharedPreferences.create(
                    "secret_pin",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            SecretPin = encryptedPreferences.getString("secretPin", "");

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

}
