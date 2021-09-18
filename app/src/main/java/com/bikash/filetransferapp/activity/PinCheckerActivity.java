package com.bikash.filetransferapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bikash.filetransferapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.hanks.passcodeview.PasscodeView;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Set;

public class PinCheckerActivity extends AppCompatActivity {
    PasscodeView passcodeView;
    String SecretPin;
    String activity;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_checker);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        passcodeView = findViewById(R.id.passcodeView);
        linearLayout = findViewById(R.id.linearLayout);

        Intent intent = getIntent();

        activity = intent.getStringExtra("activity");

//        passcodeView.getListener(new PasscodeView());

        getSecretPin();

        checkSecretPin();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home)
            finish();
        else
            return super.onOptionsItemSelected(item);

        return true;
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

    //    check the pin
    private void checkSecretPin() {
        passcodeView
                .setLocalPasscode(SecretPin)
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {
                        Toast.makeText(getApplicationContext(), "Wrong pin number. Please enter correct pin.",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(String number) {
                        switch (activity) {
                            case "Splash":
                                startActivity(new Intent(PinCheckerActivity.this, MainActivity.class));
                                finish();
                                break;
                            case "Setting":
                                startActivity(new Intent(PinCheckerActivity.this, SetPinActivity.class));
                                finish();
                                break;
                            case "DeletePin":
                                clearSecretPin();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();

//                                        finish();
//                                        startActivity(getIntent()); // to get same intent or activity
                                    }
                                }, 1500);

                                break;
                        }
                    }
                });
    }

    //    delete secret pin
    private void clearSecretPin() {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            SharedPreferences encryptedPreferences = EncryptedSharedPreferences.create(
                    "secret_pin",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            encryptedPreferences.edit().remove("secretPin").apply();

            Snackbar snackbar = Snackbar.make(
                    linearLayout,
                    "Pin remove successfully.",
                    Snackbar.LENGTH_LONG
            );
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}