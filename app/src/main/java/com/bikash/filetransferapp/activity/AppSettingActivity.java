package com.bikash.filetransferapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.bikash.filetransferapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AppSettingActivity extends Activity {
    String SecretPin;
    LinearLayout linearLayout;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        dialog = new AlertDialog.Builder(this);

        getSecretPin();

        Button btn_pin_set = (Button) findViewById(R.id.btn_pin_set);
        btn_pin_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SecretPin.equals("")) {
                    startActivity(new Intent(getApplicationContext(), SetPinActivity.class));
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), PinCheckerActivity.class);
                    intent.putExtra("activity", "Setting");
                    startActivity(intent);
                }
            }
        });

        Button btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                alert msg
                dialog.setTitle("Warning");
                dialog.setMessage("Do you want to delete the current PIN number?");
                dialog.setNegativeButton(R.string.butn_close, null);
                dialog.setPositiveButton(R.string.butn_proceed, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                         checkPin();
                    }
                }).show();
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

//    check the pin is available or not. if yes then verify the current pin.
    private void checkPin() {
        if (SecretPin.equals("")) {
            Snackbar snackbar = Snackbar.make(
                    linearLayout,
                    "Don't have a pin.",
                    Snackbar.LENGTH_LONG
            );
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        } else {
            Intent intent = new Intent(getApplicationContext(), PinCheckerActivity.class);
            intent.putExtra("activity", "DeletePin");
            startActivity(intent);
        }
    }
}