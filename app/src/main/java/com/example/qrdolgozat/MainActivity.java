package com.example.qrdolgozat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button btnScann,btnKiir;
    TextView tvErdemeny;
    String allapot;
    File file;
    String szovegesAdat;
    Date date= Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btnScann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("QR Code Scannelés");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

            }
        });
        btnKiir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formatedDate= dateFormat.format(date);
                szovegesAdat = tvErdemeny.getText()+","+formatedDate+","+"\r\n";

                allapot= Environment.getExternalStorageState();
                if ( allapot.equals(Environment.MEDIA_MOUNTED)){
                    file= new File(Environment.getExternalStorageState(),"scannedCodes.csv");
                    try {
                        BufferedWriter bw= new BufferedWriter(new FileWriter(file),1024);
                        bw.append(szovegesAdat);
                        bw.close();
                    }catch (IOException e){

                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(MainActivity.this,"Kilépés a scannelésből.",Toast.LENGTH_SHORT).show();
            }
            else {
                tvErdemeny.setText(result.getContents());

                try {
                    Uri uri = Uri.parse(result.getContents());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {

                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        btnScann = findViewById(R.id.BtnScan);
        btnKiir = findViewById(R.id.BtnKiir);
        tvErdemeny = findViewById(R.id.textView);
    }
}
