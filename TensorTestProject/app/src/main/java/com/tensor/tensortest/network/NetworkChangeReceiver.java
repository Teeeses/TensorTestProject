package com.tensor.tensortest.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tensor.tensortest.app.App;

/**
 * Created by develop on 03.04.2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityStatusString(context);
        if(status == NetworkUtil.TYPE_NOT_CONNECTED) {
            App.setNetworkStatus(false);
        } else {
            App.setNetworkStatus(true);
        }
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}