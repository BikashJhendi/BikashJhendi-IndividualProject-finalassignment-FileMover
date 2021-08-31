package com.bikash.filetransferapp.service;

import android.content.SharedPreferences;

import com.bikash.filetransferapp.util.AppUtils;
import com.bikash.filetransferapp.util.NotificationUtils;
import com.bikash.filetransferapp.db.AccessDatabase;

abstract public class Service extends android.app.Service
{
    private NotificationUtils mNotificationUtils;

    public AccessDatabase getDatabase()
    {
        return AppUtils.getDatabase(this);
    }

    public SharedPreferences getDefaultPreferences()
    {
        return AppUtils.getDefaultPreferences(getApplicationContext());
    }

    public NotificationUtils getNotificationUtils()
    {
        if (mNotificationUtils == null)
            mNotificationUtils = new NotificationUtils(getApplicationContext(), getDatabase(), getDefaultPreferences());

        return mNotificationUtils;
    }
}
