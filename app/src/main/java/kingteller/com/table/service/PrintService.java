package kingteller.com.table.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;

import kingteller.com.table.printsdk.BluetoothUtil;

import static kingteller.com.table.printsdk.PrintConfig.REQUEST_ENABLE_BT;

/**
 * Created by Administrator on 2017/4/19.
 */

public class PrintService extends Service {
    // Name of the connected device
    protected String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    protected BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the services
    protected BluetoothUtil mService = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        // If Bluetooth is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
     //   if (mService == null)
     //       mService = new BluetoothUtil(this, mHandler);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth services
        if (mService != null)
            mService.stop();
    }
}
