package kingteller.com.table;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SignPrintPreviewActivity extends AppCompatActivity {

    private ImageView ticket;
    private ImageView kffw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_print_preview);
        ticket = (ImageView) findViewById(R.id.ticket);
        kffw = (ImageView) findViewById(R.id.kffw);
        try {
            FileInputStream fis_ticket = new FileInputStream("/sdcard/KingTellerReport.jpg");
         //   FileInputStream fis_kffw = new FileInputStream("/sdcard/Kffw.png");
            Bitmap ticket_bitmap= BitmapFactory.decodeStream(fis_ticket);
      //      Bitmap kffw_bitmap= BitmapFactory.decodeStream(fis_kffw);
            ticket.setImageBitmap(ticket_bitmap);
       //     kffw.setImageBitmap(kffw_bitmap);
            fis_ticket.close();
       //     fis_kffw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
