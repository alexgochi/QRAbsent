package alexgochi.superb.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import alexgochi.superb.R;

public class QRActivity extends AppCompatActivity {
    Button btnGenerate, btnHome;
    ImageView imageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        imageCode = (ImageView) findViewById(R.id.qrViewOutput);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setVisibility(View.INVISIBLE);

        btnGenerate = (Button) findViewById(R.id.btnGenerate);
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(
                            getIntent().getExtras().getString("KEY_NAME") + "\n"
                                    + getIntent().getExtras().getString("KEY_SEMINAR"),
                            BarcodeFormat.QR_CODE, 300, 300);

                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imageCode.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }

                btnGenerate.setVisibility(View.GONE);
                btnHome.setVisibility(View.VISIBLE);
            }
        });
    }

    public static Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void downloadImage(View view) {
        AlertDialog dialog = new AlertDialog.Builder(QRActivity.this).create();
        dialog.setTitle("QR-Code");
        dialog.setMessage("Save QR-Code to Gallery ?");
        dialog.setButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startSave();
            }
        });
        dialog.setButton2("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void startSave() {
        FileOutputStream fileOutputStream;
        File file = getDisc();
        if (!file.exists() && !file.mkdirs()) {
            Toast.makeText(getApplicationContext(), "Can't create directory to save Image.", Toast.LENGTH_SHORT).show();
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = simpleDateFormat.format(new Date());
        String name = "QRAbsent " + date + ".png";
        String file_name = file.getAbsolutePath() + "/" + name;
        File new_file = new File(file_name);
        try {
            fileOutputStream = new FileOutputStream(new_file);
            Bitmap bitmap = viewToBitmap(imageCode, imageCode.getWidth(), imageCode.getHeight());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            Toast.makeText(QRActivity.this, "QRCode Saved to Gallery", Toast.LENGTH_SHORT).show();
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshGallery(new_file);
    }

    private void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "QRCode");
    }

    public void shareImage(View view) {
        Bitmap bitmap = viewToBitmap(imageCode, imageCode.getWidth(), imageCode.getHeight());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "QRCode.png");
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/Code.png"));
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    public void backToHome(View view) {
        Intent intent = new Intent(QRActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
