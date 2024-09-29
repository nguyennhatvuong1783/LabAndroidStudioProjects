package com.example.callactivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PERMISSION_CODE = 10;
    private Button btnQR;

    private void initViews() {
        btnQR = findViewById(R.id.btnQR);
        btnQR.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        if (id == R.id.btnQR) {
            ScanQRCode();
        }
    }

    public void ScanQRCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        intentIntegrator.setPrompt("Scan a QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                try {
                    String s[] = result.getContents().toString().split(";");
                    String i[] = s[0].split(":");
                    if (i[0].equalsIgnoreCase("MATMSG")) {
                        String sub[] = s[1].split(":");
                        String body[] = s[2].split(":");

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{i[2]});
                        try {
                            intent.putExtra(Intent.EXTRA_SUBJECT, sub[1]);
                        } catch (Exception e) {
                            intent.putExtra(Intent.EXTRA_SUBJECT, "");
                        }
                        try {
                            intent.putExtra(Intent.EXTRA_TEXT, body[1]);
                        } catch (Exception e) {
                            intent.putExtra(Intent.EXTRA_TEXT, "");
                        }
                        intent.setType("massage/rfc822");
                        startActivity(Intent.createChooser(intent, "Choose email client:"));
                    }
                } catch (Exception e) {
                }

                String str[] = result.getContents().toString().split(":");
                if (str[0].equalsIgnoreCase("http") || str[0].equalsIgnoreCase("https")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(result.getContents()));
                    startActivity(intent);
                } else if (str[0].equalsIgnoreCase("SMSTO")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SENDTO);
                    intent.putExtra("sms_body", str[2]);
                    intent.setData(Uri.parse("sms:" + str[1]));
                    startActivity(intent);
                } else if (str[0].equalsIgnoreCase("TEL")) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + str[1]));
                            startActivity(intent);
                        } else {
                            String[] permission = {android.Manifest.permission.CALL_PHONE};
                            requestPermissions(permission, REQUEST_PERMISSION_CODE);
                        }
                    }
                } else {
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}