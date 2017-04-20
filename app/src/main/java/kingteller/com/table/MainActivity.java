package kingteller.com.table;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import kingteller.com.table.adapter.TableAdapter;
import kingteller.com.table.bean.WorkOrderBean;
import kingteller.com.table.paint.PaintDialogListener;
import kingteller.com.table.paint.WritePadDialog;
import kingteller.com.table.utils.BitmapUtil;

public class MainActivity extends Activity implements View.OnClickListener{
    // Debugging
    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = true;

    private Button btn_BMP = null;

    private Bitmap mBitmap;
    private Bitmap mHeaderBitmap;
    private Bitmap mFooterBitmap;
    private LinearLayout table;
    private ListView content;
    private LinearLayout footer_layout;
    private RelativeLayout header_layout;

    public int width;  //屏幕宽
    public int height; //屏幕高

    private LinearLayout khqm_ll;
    private ImageView khqm;
    private Bitmap mKhqmBitmap;
    private Bitmap mSignBitmap;
    private String signPath;  //签名图片保存路径
    private WritePadDialog writeTabletDialog;
    private View.OnClickListener signListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            writeTabletDialog = new WritePadDialog(
                    MainActivity.this, new PaintDialogListener() {
                @Override
                public void refreshActivity(Object object,boolean hasDraw) {

                    mSignBitmap = (Bitmap) object;
                    if (mSignBitmap != null && hasDraw) {
                        //      signPath = createFile();
                        khqm.setVisibility(View.VISIBLE);
                        //     mSignBitmap = BitmapUtil.zoomByScale(mSignBitmap, (float) width / 2);
                        khqm.setImageBitmap(mSignBitmap);
                        mHandler.sendEmptyMessage(0);
                        clickToSign.setVisibility(View.GONE);
                    } else {
                        clickToSign.setVisibility(View.VISIBLE);
                        khqm.setVisibility(View.GONE);
                    }
                }
            },width,height);
            writeTabletDialog.show();
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    writeTabletDialog.dismiss();
                    table.post(new Runnable() {
                        @Override
                        public void run() {
                            int width = table.getWidth();
                            int height = table.getHeight();
                            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(mBitmap);
                            table.layout(table.getLeft(), table.getTop(), table.getRight(), table.getBottom());
                            table.draw(canvas);
                            if (mBitmap == null) {
                                Toast.makeText(MainActivity.this, "没有获取图片！", Toast.LENGTH_LONG).show();
                            } else {
                                BitmapUtil.saveBitmap2Path(mBitmap,"KingTellerReport");
                            }
                        }
                    });
                    break;
                default:
                    break;
            }

        }
    };
    private TextView clickToSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;
        height = metric.heightPixels;
        if (DEBUG)
            Log.e(TAG, "+++ ON CREATE +++");
        setContentView(R.layout.layout_main);
        WorkOrderBean workOrderBean = new WorkOrderBean();
        workOrderBean.setOrderNo("WH/20161102/81834");
        content = (ListView) findViewById(R.id.content);
        TableAdapter tableAdapter = new TableAdapter(workOrderBean, MainActivity.this);
        content.setAdapter(tableAdapter);

        table = (LinearLayout) findViewById(R.id.table);
        table.post(new Runnable() {
            @Override
            public void run() {
                int width = table.getWidth();
                int height = table.getHeight();
                mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mBitmap);
                table.layout(table.getLeft(), table.getTop(), table.getRight(),
                        table.getBottom());
                table.draw(canvas);
                if (mBitmap == null) {
                    Toast.makeText(MainActivity.this, "没有获取图片！", Toast.LENGTH_LONG).show();
                } else {
                    BitmapUtil.saveBitmap2Path(mBitmap,"KingTellerReport");
                }
            }
        });

        clickToSign = (TextView) findViewById(R.id.clicksign);
        khqm = (ImageView) findViewById(R.id.khqm);
        clickToSign.setOnClickListener(signListener);
        khqm.setOnClickListener(signListener);

        footer_layout = (LinearLayout) findViewById(R.id.footer);
        footer_layout.requestLayout();
        footer_layout.post(new Runnable() {
            @Override
            public void run() {
                int footer_height = footer_layout.getHeight();
                if (footer_height == 0) {
                    footer_layout.setVisibility(View.GONE);
                } else {
                    footer_layout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        KeyListenerInit();
    }

    private void KeyListenerInit() {
        btn_BMP = (Button)findViewById(R.id.btn_prtbmp);
        btn_BMP.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_prtbmp:{
                Intent intent = new Intent(MainActivity.this, BlueToothActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }
    /**
     * 创建手写签名文件
     *
     * @return
     */
    private String createFile() {
        ByteArrayOutputStream baos = null;
        String _path = null;
        try {
            String sign_dir = Environment.getExternalStorageDirectory() + File.separator;
            _path = sign_dir + System.currentTimeMillis() + ".jpg";
            baos = new ByteArrayOutputStream();
            mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photoBytes = baos.toByteArray();
            if (photoBytes != null) {
                new FileOutputStream(new File(_path)).write(photoBytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _path;
    }
}
