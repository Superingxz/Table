package kingteller.com.table;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import kingteller.com.table.paint.PaintDialogListener;
import kingteller.com.table.view.SignView;


public class SignPrintActivity extends Activity {

    boolean mHasDraw = false;
    Context context;
    WindowManager.LayoutParams p ;
    PaintDialogListener paintDialogListener;
    public int width;
    public int height;

    SignView mView;

    /** The index of the current color to use. */
    int mColorIndex;
    private Bitmap mBitmap;
    private Button mClear;
    private Button mSave;
    private Button mChangeColor;
    private Button mChangeWidth;
    private SignView mPathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.layout_sign_print);
        mClear = (Button) findViewById(R.id.clear);
        mSave = (Button) findViewById(R.id.save);
        mChangeWidth = (Button) findViewById(R.id.changewidth);

        mPathView = (SignView) findViewById(R.id.signView);

        //设置保存监听
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPathView.getTouched()) {
                    try {
                        mPathView.save("/sdcard/signName.png", true, 10);
                        Intent intent = new Intent();
                        intent.putExtra("hasDraw", mPathView.getTouched());
                        setResult(RESULT_OK,intent);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(SignPrintActivity.this, "您没有签名~", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPathView.clear();
            }
        });
        //修改笔的宽度
        mChangeWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mPathView.setPaintWidth(20);
                mPathView.clear();
            }
        });
    }

}
