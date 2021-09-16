package com.bikash.filetransferapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bikash.filetransferapp.R;
import com.google.android.material.appbar.AppBarLayout;

public class AppSettingActivity extends Activity {
    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        Toolbar toolbar = findViewById(R.id.toolbar);

        mAppBarLayout = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button btn_pin_set = (Button) findViewById(R.id.btn_pin_set);
        btn_pin_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SetPinActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home)
            finish();
        else
            return super.onOptionsItemSelected(item);

        return true;
    }
}