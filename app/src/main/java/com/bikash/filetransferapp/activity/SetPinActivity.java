package com.bikash.filetransferapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bikash.filetransferapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.hanks.passcodeview.PasscodeView;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SetPinActivity extends AppCompatActivity {
    PasscodeView passcodeView;
    String SecretPin;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        passcodeView = findViewById(R.id.passcodeView);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        getSecretPin();
        saveSecretPin();
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

//  save the pin
    private void saveSecretPin() {
        passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {
                Snackbar snackbar = Snackbar.make(
                        linearLayout,
                        "Pin mismatch. Enter the same pin.",
                        Snackbar.LENGTH_LONG
                );
                snackbar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }

            @Override
            public void onSuccess(String number) {
                saveUserPin(number);
                startActivity(new Intent(getApplicationContext(), AppSettingActivity.class));
                finish();
            }

            //            saving pin on SharedPreferences
            private void saveUserPin(String pin) {
                try {
                    String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

                    // Storing data into EncryptedSharedPreferences
                    SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                            "secret_pin",
                            masterKeyAlias,
                            getApplicationContext(),
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    );
                    // Creating an Editor object to edit(write to the file)
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Storing the key and its value as the data fetched from edittext
                    editor.putString("secretPin", pin);
                    editor.apply();
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
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