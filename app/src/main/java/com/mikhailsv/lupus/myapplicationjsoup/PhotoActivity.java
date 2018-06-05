package com.mikhailsv.lupus.myapplicationjsoup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;



public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button photoBtn;
    File photoFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        photoBtn = findViewById(R.id.photoBtn);
        photoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
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


    private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        SharedPreferences sharedPref = getSharedPreferences("dishPref", MODE_PRIVATE);
        String imageFileName = sharedPref.getString("dish1","default");

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = new File( storageDir,
                imageFileName +  /* prefix */
                ".jpg" );
        Log.wtf("mytag", "FILE: " + image.toString());

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }
}
