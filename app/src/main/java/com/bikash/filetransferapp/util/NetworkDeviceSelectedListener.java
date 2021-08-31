package com.bikash.filetransferapp.util;

import com.bikash.filetransferapp.model.NetworkDevice;

public interface NetworkDeviceSelectedListener
{
    boolean onNetworkDeviceSelected(NetworkDevice networkDevice, NetworkDevice.Connection connection);

    boolean isListenerEffective();
}
