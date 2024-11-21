package com.example.lab5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.net.Uri;


import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etAddress;
    private Button buttonScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        buttonScan = findViewById(R.id.buttonScan);

        // Set up the button to start the QR code scanner
        buttonScan.setOnClickListener(view -> startQRScanner());

        // Open the website when clicked
        etAddress.setOnClickListener(view -> {
            String url = etAddress.getText().toString();
            if (!url.isEmpty() && url.startsWith("http")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to start the QR Code scanner
    private void startQRScanner() {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);  // Start CaptureActivity for ZXing scanner
        startActivityForResult(intent, 0);  // Start the QR code scanner activity
    }

    // Handle the result of the QR code scan
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Parse the result of the QR code scan
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra("SCAN_RESULT");

            if (result != null) {
                try {

                    JSONObject json = new JSONObject(result);
                    etName.setText(json.getString("title"));
                    etAddress.setText(json.getString("website"));
                } catch (Exception e) {
                    Toast.makeText(this, "Invalid QR Code Data", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Scan failed", Toast.LENGTH_SHORT).show();
        }
    }
}



