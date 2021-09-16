package com.bikash.filetransferapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.bikash.filetransferapp.R;
import com.hanks.passcodeview.PasscodeView;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SetPinActivity extends AppCompatActivity {
    PasscodeView passcodeView;

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

        passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {
                Toast.makeText(getApplicationContext(), "Pin is Wrong",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(String number) {
                saveUserPin(number);

                Toast.makeText(getApplicationContext(), "Successfully added Pin.",
                        Toast.LENGTH_LONG).show();
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
                    editor.putString("pin", pin);
                    editor.apply();
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
}