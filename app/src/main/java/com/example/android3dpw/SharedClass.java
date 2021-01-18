package com.example.android3dpw;

import android.app.Application;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Keep;

import org.json.JSONObject;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

@Keep
public  class SharedClass extends Application {
    public  static final String SERVICE_TYPE = "_http._tcp.";
    public  static final String _serviceName = "Kinefy";
    private static final String REQUEST_CONNECT_CLIENT = "request-connect-client";
    NsdManager.DiscoveryListener discoveryListener;
    NsdManager.ResolveListener resolveListener;
    public static TextView txtMessage;
    public static TextView txtMessageSec;
    NsdManager nsdManager;
    NsdServiceInfo mService;
    public int hostPort = 0;
    public InetAddress hostAddress;
    public static boolean RegisterStatus;
    public static boolean DiscoverStatus;
    NsdManager.RegistrationListener registrationListener;
    public String MyIPAddress = "";
    public String SecondaryIPAddress = "";
    public SharedClass()
    {

    }

    public  void initializeRegistrationListener() {
        registrationListener = new NsdManager.RegistrationListener() {

            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                // Save the service name. Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                String serviceName = NsdServiceInfo.getServiceName();
                Log.i("test","Registered Service: "+ serviceName);
                // nsdManager.resolveService(NsdServiceInfo, resolveListener);
            }


            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Registration failed! Put debugging code here to determine why.
            }


            public void onServiceUnregistered(NsdServiceInfo arg0) {
                // Service has been unregistered. This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
            }


            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Unregistration failed. Put debugging code here to determine why.
            }
        };
    }

    public void registerService(int port, String serviceName,Context context) {
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(serviceName);
        serviceInfo.setServiceType("_http._tcp.");
        serviceInfo.setPort(port);
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);

    }
    public void unRegisterService(){
        nsdManager.unregisterService(registrationListener);
    }

    public void initializeDiscoveryListener(final String serviceName, Context context) {

        // Instantiate a new DiscoveryListener
        discoveryListener = new NsdManager.DiscoveryListener() {

            // Called as soon as service discovery begins.

            public void onDiscoveryStarted(String regType) {
                Log.i("test", "Service discovery started");
            }

            public void onServiceFound(NsdServiceInfo service) {
                // A service was found! Do something with it.
                Log.i("test", "Service discovery success" + service);
                // nsdManager.resolveService(service, resolveListener);

                if (service.getServiceType().equals(SERVICE_TYPE)) {
                    if (resolveListener == null) {
                        // resolveListener = new MyResolveListener();
                        initializeResolveListener();
                        nsdManager.resolveService(service, resolveListener);
                    }
                    // nsdManager.resolveService(service, resolveListener);
                    // Service type is the string containing the protocol and
                    // transport layer for this service.
                    Log.i("test", "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(serviceName)) {
                    nsdManager.resolveService(service, new MyResolveListener());
                    // The name of the service tells the user what they'd be
                    // connecting to. It could be "Bob's Chat App".
                    Log.i("test", "Same machine: " + serviceName);
                } else if (service.getServiceName().contains("Honor9N")) {
                    Log.i("test", "Same machine: " + serviceName);
                    nsdManager.resolveService(service, new MyResolveListener());
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.i("test", "service lost: " + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i("test", "Discovery stopped: " + serviceType);
            }
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.i("test", "Discovery failed: Error code:" + errorCode);
                try {
                    nsdManager.stopServiceDiscovery(this);
                }catch (Exception ex){
                    Log.e("test", "Error in nsdManager.stopServiceDiscovery: " + ex.getLocalizedMessage());
                }

            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.i("test", "Discovery failed: Error code:" + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }
        };
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }


    public void initializeResolveListener() {


        resolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails. Use the error code to debug.
                Log.i("test", "Resolve failed: " + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {

                Log.i("test", "Resolve Succeeded. " + serviceInfo);


//                if (serviceInfo.getServiceName().equals(serviceName)) {
//                    Log.i("test", "Same IP.");
//                    return;
//                }
                mService = serviceInfo;
               // int port = mService.getPort();
              //  InetAddress host = mService.getHost();
                hostPort = mService.getPort();
                hostAddress = mService.getHost();


            }
        };
    }








}

