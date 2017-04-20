package kingteller.com.table;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mview.medittext.utils.QpadJudgeUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kingteller.com.table.printsdk.BluetoothUtil;
import kingteller.com.table.printsdk.Command;
import kingteller.com.table.printsdk.DeviceListActivity;
import kingteller.com.table.printsdk.PrintConfig;
import kingteller.com.table.printsdk.PrintPicture;
import kingteller.com.table.printsdk.PrinterCommand;
import kingteller.com.table.utils.BitmapUtil;
import kingteller.com.table.utils.KingTellerStaticConfig;
import kingteller.com.table.utils.SharedPreferencesUtils;

import static kingteller.com.table.printsdk.PrintConfig.MESSAGE_CONNECTION_LOST;
import static kingteller.com.table.printsdk.PrintConfig.MESSAGE_DEVICE_NAME;
import static kingteller.com.table.printsdk.PrintConfig.MESSAGE_READ;
import static kingteller.com.table.printsdk.PrintConfig.MESSAGE_STATE_CHANGE;
import static kingteller.com.table.printsdk.PrintConfig.MESSAGE_TOAST;
import static kingteller.com.table.printsdk.PrintConfig.MESSAGE_UNABLE_CONNECT;
import static kingteller.com.table.printsdk.PrintConfig.MESSAGE_WRITE;
import static kingteller.com.table.printsdk.PrintConfig.REQUEST_CONNECT_DEVICE;
import static kingteller.com.table.printsdk.PrintConfig.REQUEST_ENABLE_BT;
import static kingteller.com.table.printsdk.PrintConfig.TOAST;

public  class BlueToothActivity extends Activity implements View.OnClickListener{
    // Debugging
    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = true;

    public int width;  //屏幕宽
    public int height; //屏幕高

    // Name of the connected device
    protected String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    protected BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the services
    protected BluetoothUtil mService = null;


    private TextView mTitle;
    private Button btnScanButton = null;
    private Button btnClose = null;
    private Button btn_BMP = null;

    protected List<Bitmap> bitmaps;
    /****************************************************************************************************/
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (DEBUG)
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    MessageStateChange(msg.arg1);
                    break;
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(PrintConfig.DEVICE_NAME);
                    // Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "连接到" + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    if (msg.getData().getString(PrintConfig.TOAST).equals("无法连接设备")) {
                        int bondState = device.getBondState();
                        if (bondState == BluetoothDevice.BOND_NONE) {
                            //取消匹配
                            unpairDevice(device);
                            //重新匹配
                            if (!mBluetoothAdapter.isEnabled()) {
                                Intent enableIntent = new Intent(
                                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                                // Otherwise, setup the session
                            }
                        }
                    }
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    //Toast.makeText(getApplicationContext(), "Device connection was lost", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "蓝牙已断开连接", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    //Toast.makeText(getApplicationContext(), "Unable to connect device", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "无法连接设备", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    protected Bitmap mBitmap;
    private BluetoothDevice device;

    public void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG)
            Log.e(TAG, "+++ ON CREATE +++");
        setContentView(getLayout());
        // Set up the custom title
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;
        height = metric.heightPixels;
        initView();

        //drawBitmaps();

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            finish();
        }

       /* new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!mBluetoothAdapter.isEnabled()) {
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
                }
            }
        },1000,1000);*/
    }

    /**
     * 初始化界面
     */
    protected void initView() {
        mTitle = (TextView) findViewById(R.id.title_left_text);
        mTitle.setText(R.string.app_title);
        mTitle = (TextView) findViewById(R.id.title_right_text);
    }

    /**
     * 画Bitmaps
     */
    protected  void drawBitmaps(){

    }

    @Override
    public void onStart() {
        super.onStart();
        // If Bluetooth is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        } else {
            if (mService == null)
                mService = new BluetoothUtil(this, mHandler);
                KeyListenerInit();//监听
        }

    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (mService != null) {
            if (mService.getState() == BluetoothUtil.STATE_NONE) {
                // Start the Bluetooth services
                mService.start();
                String deviceAddress = SharedPreferencesUtils.getData(KingTellerApplication.getApplication(), "deviceAddress");
                device = mBluetoothAdapter
                        .getRemoteDevice(deviceAddress);
                if (!QpadJudgeUtils.isEmpty(deviceAddress)) {
                    connectToBlueTooth(deviceAddress);
                }
            }
        } else {
            mService = new BluetoothUtil(this, mHandler);
        }
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (DEBUG)
            Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (DEBUG)
            Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth services
        if (mService != null)
            mService.stop();
        if (DEBUG)
            Log.e(TAG, "--- ON DESTROY ---");
    }

    /**
     * 设置监听事件
     */
    protected void KeyListenerInit() {
        btnScanButton = (Button)findViewById(R.id.button_scan);
        btnScanButton.setOnClickListener(this);

        btnClose = (Button)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        btn_BMP = (Button)findViewById(R.id.btn_prtbmp);
        btn_BMP.setOnClickListener(this);


        btnClose.setEnabled(false);
        btn_BMP.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.button_scan:{
                Intent serverIntent = new Intent(getContext(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                break;
            }
            case R.id.btn_close:{
                mService.stop();
                btnClose.setEnabled(false);
                btn_BMP.setEnabled(false);
                btnScanButton.setEnabled(true);
                btnScanButton.setText(getText(R.string.connect));
                break;
            }
            case R.id.btn_prtbmp:{
                Print_BMP(mBitmap,true,true);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (DEBUG)
            Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:{
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    SharedPreferencesUtils.saveData(KingTellerApplication.getApplication(),"deviceAddress",address);
                    // Get the BLuetoothDevice object
                    connectToBlueTooth(address);
                }
                break;
            }
            case REQUEST_ENABLE_BT:{
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a session
                    KeyListenerInit();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }

    protected void connectToBlueTooth(String address) {
        if (BluetoothAdapter.checkBluetoothAddress(address)) {
            device = mBluetoothAdapter
                    .getRemoteDevice(address);
            // Attempt to connect to the device
            mService.connect(device);
        }
    }

    /*
           * 打印图片
           */
    protected void Print_BMP(Bitmap bitmap,boolean isFirst,boolean isEnd){
        //	byte[] buffer = PrinterCommand.POS_Set_PrtInit();
        // mBitmap = ((BitmapDrawable) imageViewPicture.getDrawable()).getBitmap();
        int nMode = 0;
        int nPaperWidth = 384;
        if (mBitmap != null ) {
            /**
             * Parameters:
             * mBitmap  要打印的图片
             * nWidth   打印宽度（58和80）
             * nMode    打印模式
             * Returns: byte[]
             */
            byte[] data = PrintPicture.POS_PrintBMP(bitmap, nPaperWidth, nMode);
            SendDataByte(Command.ESC_Init);
            if (isFirst) {
                SendDataByte(Command.LF);
                SendDataByte(Command.LF);
                SendDataByte(Command.LF);
            }
            SendDataByte(data);
            if (isEnd) {
                SendDataByte(Command.LF);
                SendDataByte(Command.LF);
                SendDataByte(Command.LF);
                SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
                SendDataByte(PrinterCommand.POS_Set_Cut(1));
                SendDataByte(PrinterCommand.POS_Set_PrtInit());
            }
        } else {
            Toast.makeText(BlueToothActivity.this, "没有图片可以打印！", Toast.LENGTH_SHORT).show();
        }
    }
    /*
        * 打印图片
        */
    protected void Print_BMP_By_Bitmaps(){
        if (bitmaps == null) {
            return;
        }
        if (bitmaps.size() == 0) {
            return;
        }
        int nMode = 0;
        int nPaperWidth = 384;
        if (bitmaps == null) {
            return;
        }
        if (bitmaps.size() == 0) {
            return;
        }
        byte[] data = PrintPicture.POS_PrintBMP(mBitmap, nPaperWidth, nMode);
        SendDataByte(Command.ESC_Init);
        SendDataByte(Command.LF);
        for (int i = 0; i < bitmaps.size(); i++) {
            if (bitmaps.get(i) != null ) {
                /**
                 * Parameters:
                 * mBitmap  要打印的图片
                 * nWidth   打印宽度（58和80）
                 * nMode    打印模式
                 * Returns: byte[]
                 */
                SendDataByte(data);
                bitmaps.get(i).recycle(); //回收
            }
        }
        SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
        SendDataByte(PrinterCommand.POS_Set_Cut(1));
        SendDataByte(PrinterCommand.POS_Set_PrtInit());
    }

    /*
        * 打印图片
        */
    protected void Print_BMP_By_Paths(List<String> paths){
        if (paths == null) {
            return;
        }
        if (paths.size() == 0) {
            return;
        }
        for (int i = 0; i < paths.size(); i++) {
            Bitmap bitmap = BitmapUtil.getBitmapByPath(paths.get(i));
            bitmaps.add(bitmap);
        }
        int nMode = 0;
        int nPaperWidth = 384;
        if (bitmaps == null) {
            return;
        }
        if (bitmaps.size() == 0) {
            return;
        }
        byte[] data = PrintPicture.POS_PrintBMP(mBitmap, nPaperWidth, nMode);
        SendDataByte(Command.ESC_Init);
        SendDataByte(Command.LF);
        for (int i = 0; i < bitmaps.size(); i++) {
            if (bitmaps.get(i) != null ) {
                /**
                 * Parameters:
                 * mBitmap  要打印的图片
                 * nWidth   打印宽度（58和80）
                 * nMode    打印模式
                 * Returns: byte[]
                 */
                SendDataByte(data);
                bitmaps.get(i).recycle(); //回收
            }
        }
        SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
        SendDataByte(PrinterCommand.POS_Set_Cut(1));
        SendDataByte(PrinterCommand.POS_Set_PrtInit());
    }

    protected int getLayout() {
        return R.layout.layout_print;
    }

    protected Context getContext(){
        return BlueToothActivity.this;
    }

    protected List<Bitmap> getBitmaps(){
        return bitmaps;
    }
    /**
     * 连接状态改变
     * @param state
     */
    protected void MessageStateChange(int state){
        switch (state) {
            case BluetoothUtil.STATE_CONNECTED:
                mTitle.setText(R.string.title_connected_to);
                mTitle.append(mConnectedDeviceName);
                btnScanButton.setEnabled(false);
                btnClose.setEnabled(true);
                btn_BMP.setEnabled(true);
                break;
            case BluetoothUtil.STATE_CONNECTING:
                mTitle.setText(R.string.title_connecting);
                break;
            case BluetoothUtil.STATE_LISTEN:
            case BluetoothUtil.STATE_NONE:
               // mTitle.setText(R.string.title_not_connected);
                openBlueTooth();
                break;
            default:
                break;
        }
    }
    public void openBlueTooth(){
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        }
    }
    /*
	 *SendDataByte
	 */
    private void SendDataByte(byte[] data) {

        if (mService.getState() != BluetoothUtil.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mService.write(data);
    }
}
