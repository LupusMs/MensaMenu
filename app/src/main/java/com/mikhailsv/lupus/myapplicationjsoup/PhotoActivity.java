package com.mikhailsv.lupus.myapplicationjsoup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;


public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button photoBtn1;
    private Button photoBtn2;
    private Button photoBtn3;
    private Button photoBtn4;
    File photoFile;

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private String params[];
    private String imageFileName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        photoBtn1 = findViewById(R.id.photoBtn1);
        photoBtn2 = findViewById(R.id.photoBtn2);
        photoBtn3 = findViewById(R.id.photoBtn3);
        photoBtn4 = findViewById(R.id.photoBtn4);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);

        photoBtn1.setOnClickListener(this);
        photoBtn2.setOnClickListener(this);
        photoBtn3.setOnClickListener(this);
        photoBtn4.setOnClickListener(this);
        params = new String[2];


        SharedPreferences sharedPref = getSharedPreferences("dishPref", MODE_PRIVATE);
        textView1.setText(sharedPref.getString("dish11",""));
        textView2.setText(sharedPref.getString("dish22",""));
        textView3.setText(sharedPref.getString("dish33",""));
        textView4.setText(sharedPref.getString("dish44",""));


    }

    @Override
    public void onClick(View view) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SharedPreferences sharedPref = getSharedPreferences("dishPref", MODE_PRIVATE);

        switch (view.getId()) {
            case R.id.photoBtn1:
                params[1] = textView1.getText().toString();
                imageFileName = sharedPref.getString("dish1","default");
                textView1.setTextColor(Color.GREEN);
                textView1.setText("Image uploaded");
             break;
            case R.id.photoBtn2:
                params[1] = textView2.getText().toString();
                imageFileName = sharedPref.getString("dish2","default");
                textView2.setTextColor(Color.GREEN);
                textView2.setText("Image uploaded");
            break;

            case R.id.photoBtn3:
                params[1] = textView3.getText().toString();
                imageFileName = sharedPref.getString("dish3","default");
                textView3.setTextColor(Color.GREEN);
                textView3.setText("Image uploaded");
            break;
            case R.id.photoBtn4:
                params[1] = textView4.getText().toString();
                imageFileName = sharedPref.getString("dish4","default");
                textView4.setTextColor(Color.GREEN);
                textView4.setText("Image uploaded");
             break;



        }
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                 photoFile = createImageFile(imageFileName);
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.wtf("mytag", "onActivityResult");


                //File to upload to cloudinary
     MyUploader uploader = new MyUploader(getApplicationContext());
     Log.wtf("mytag", "path" + photoFile.getAbsolutePath().toString());
     params[0] = photoFile.getAbsolutePath().toString();

     uploader.execute(params);
      }




    private File createImageFile(String imageStoreName) throws IOException {
        // Create an image file name

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = new File( storageDir,
                imageStoreName +  /* prefix */".jpg"
                );
        Log.wtf("mytag", "FILE: " + image.toString());

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }
}
