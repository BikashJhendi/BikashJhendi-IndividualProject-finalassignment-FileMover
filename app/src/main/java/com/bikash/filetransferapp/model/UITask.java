package com.bikash.filetransferapp.model;

import com.genonbeta.android.framework.util.Interrupter;

public interface UITask
{
    void updateTaskStarted(final Interrupter interrupter);

    void updateTaskStopped();
}
