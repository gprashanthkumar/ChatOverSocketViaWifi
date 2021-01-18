package com.example.android3dpw;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

public class MyResolveListener implements NsdManager.ResolveListener {
    @Override
    public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
        //your code
    }

    @Override
    public void onServiceResolved(NsdServiceInfo serviceInfo) {
        //your code
        Log.i("test", "Discovery onServiceResolved Service Name: " + serviceInfo.getServiceName());
        Log.i("test", "Discovery onServiceResolved host: " + serviceInfo.getHost());
        Log.i("test", "Discovery onServiceResolved Port: " + serviceInfo.getPort());
    }
}



