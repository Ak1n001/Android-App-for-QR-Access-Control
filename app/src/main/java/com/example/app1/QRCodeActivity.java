package com.example.app1;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeActivity extends AppCompatActivity {

    private ImageView qrCodeImage;
    private Button regenerateButton;
    private Handler handler;
    private Runnable hideQrRunnable;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        qrCodeImage = findViewById(R.id.qr_code_image);
        regenerateButton = findViewById(R.id.regenerate_button);
        handler = new Handler();

        ID = getIntent().getStringExtra("ID");
        generateAndDisplayQRCode();

        regenerateButton.setOnClickListener(v -> generateAndDisplayQRCode());
    }

    private void generateAndDisplayQRCode() {
        // Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();
        String qrContent = ID + " " + currentTimeMillis;

        try {
            Bitmap bitmap = generateQRCodeBitmap(qrContent);
            qrCodeImage.setImageBitmap(bitmap);
            qrCodeImage.setVisibility(View.VISIBLE);
            regenerateButton.setVisibility(View.GONE);

            // Schedule to show the regenerate button after 10 seconds
            if (hideQrRunnable != null) {
                handler.removeCallbacks(hideQrRunnable);
            }
            hideQrRunnable = () -> {
                qrCodeImage.setVisibility(View.GONE);
                regenerateButton.setVisibility(View.VISIBLE);
            };
            handler.postDelayed(hideQrRunnable, 10000);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private Bitmap generateQRCodeBitmap(String content) throws WriterException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hideQrRunnable != null) {
            handler.removeCallbacks(hideQrRunnable);
        }
    }
}
