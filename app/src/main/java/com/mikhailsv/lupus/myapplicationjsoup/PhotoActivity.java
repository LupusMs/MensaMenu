package com.mikhailsv.lupus.myapplicationjsoup;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@SuppressWarnings("FieldCanBeLocal")
public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int ACTIVITY_SELECT_IMAGE = 2;
    private Button photoBtn1;
    private Button photoBtn2;
    private Button photoBtn3;
    private Button photoBtn4;
    private Button photoBtn5;
    private Button btnUploadFrom1;
    private Button btnUploadFrom2;
    private Button btnUploadFrom3;
    private Button btnUploadFrom4;
    private Button btnUploadFrom5;
    private File photoFile;

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private String params[];
    private String imageFileName;
    private ProgressBar progressBar;
    private TextView textViewWait;
    private com.wang.avi.AVLoadingIndicatorView indicator2;
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 77;
    private final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 78;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        //Check read permissions
        if (ContextCompat.checkSelfPermission(this
                , Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_STORAGE);
        }

        //Check write permission
        if (ContextCompat.checkSelfPermission(this
                , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }



        photoBtn1 = findViewById(R.id.photoBtn1);
        photoBtn2 = findViewById(R.id.photoBtn2);
        photoBtn3 = findViewById(R.id.photoBtn3);
        photoBtn4 = findViewById(R.id.photoBtn4);
        photoBtn5 = findViewById(R.id.photoBtn5);
        btnUploadFrom1 = findViewById(R.id.btnUploadFrom1);
        btnUploadFrom2 = findViewById(R.id.btnUploadFrom2);
        btnUploadFrom3 = findViewById(R.id.btnUploadFrom3);
        btnUploadFrom4 = findViewById(R.id.btnUploadFrom4);
        btnUploadFrom5 = findViewById(R.id.btnUploadFrom5);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        textViewWait = findViewById(R.id.textViewWait);
        indicator2 = findViewById(R.id.indicator2);

        photoBtn1.setOnClickListener(this);
        photoBtn2.setOnClickListener(this);
        photoBtn3.setOnClickListener(this);
        photoBtn4.setOnClickListener(this);
        photoBtn5.setOnClickListener(this);
        btnUploadFrom1.setOnClickListener(this);
        btnUploadFrom2.setOnClickListener(this);
        btnUploadFrom3.setOnClickListener(this);
        btnUploadFrom4.setOnClickListener(this);
        btnUploadFrom5.setOnClickListener(this);
        params = new String[2];


        SharedPreferences sharedPref = getSharedPreferences("dishPref", MODE_PRIVATE);
        textView1.setText(sharedPref.getString("dish11",""));
        textView2.setText(sharedPref.getString("dish22",""));
        textView3.setText(sharedPref.getString("dish33",""));
        textView4.setText(sharedPref.getString("dish44",""));
        textView5.setText(sharedPref.getString("dish55",""));

        //Hiding photo buttons if there is no text in dish name
        if (textView2.getText().equals(""))
        {
            photoBtn2.setVisibility(View.GONE);
            btnUploadFrom2.setVisibility(View.GONE);
        }
        if (textView3.getText().equals(""))
        {
            photoBtn3.setVisibility(View.GONE);
            btnUploadFrom3.setVisibility(View.GONE);
        }
        if (textView4.getText().equals(""))
        {
            photoBtn4.setVisibility(View.GONE);
            btnUploadFrom4.setVisibility(View.GONE);
        }
        if (textView5.getText().equals(""))
        {
            photoBtn5.setVisibility(View.GONE);
            btnUploadFrom5.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View view) {

        SharedPreferences sharedPref = getSharedPreferences("dishPref", MODE_PRIVATE);

        switch (view.getId()) {
            case R.id.photoBtn1:
                params[1] = textView1.getText().toString();
                imageFileName = sharedPref.getString("dish1","default");
                textView1.setTextColor(Color.GREEN);
                createPhotoFile(imageFileName);
             break;
            case R.id.photoBtn2:
                params[1] = textView2.getText().toString();
                imageFileName = sharedPref.getString("dish2","default");
                textView2.setTextColor(Color.GREEN);
                createPhotoFile(imageFileName);
            break;

            case R.id.photoBtn3:
                params[1] = textView3.getText().toString();
                imageFileName = sharedPref.getString("dish3","default");
                textView3.setTextColor(Color.GREEN);
                createPhotoFile(imageFileName);
            break;
            case R.id.photoBtn4:
                params[1] = textView4.getText().toString();
                imageFileName = sharedPref.getString("dish4","default");
                textView4.setTextColor(Color.GREEN);
                createPhotoFile(imageFileName);
             break;
            case R.id.photoBtn5:
                params[1] = textView5.getText().toString();
                imageFileName = sharedPref.getString("dish5","default");
                textView5.setTextColor(Color.GREEN);
                createPhotoFile(imageFileName);
            break;
            case R.id.btnUploadFrom1:
                imageFileName = sharedPref.getString("dish1","default");
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            break;
            case R.id.btnUploadFrom2:
                imageFileName = sharedPref.getString("dish2","default");
                i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
                break;
            case R.id.btnUploadFrom3:
                imageFileName = sharedPref.getString("dish3","default");
                i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
                break;
            case R.id.btnUploadFrom4:
                imageFileName = sharedPref.getString("dish4","default");
                i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
                break;
        }


    }

   private void createPhotoFile(String imageFileName)
   {
       Intent takePhotoIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);


       if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {

           // Create the File where the photo should go
           photoFile = null;
           photoFile = createImageFile(imageFileName);
           if (photoFile != null) {
               takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                       FileProvider.getUriForFile(getApplicationContext(),  getApplicationContext().getPackageName() + ".provider",photoFile));
               takePhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
               startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
           }
       }

   }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case ACTIVITY_SELECT_IMAGE:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    Log.wtf("mytag", "URI _ " + selectedImage.toString());
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(Objects.requireNonNull(selectedImage), filePathColumn, null, null, null);
                    Objects.requireNonNull(cursor).moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();



                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    params[0] = filePath;
                }
        }


                //File to upload to cloudinary
    // MyUploader uploader = new MyUploader(getApplicationContext());
     if (photoFile!=null)
         params[0] = photoFile.getAbsolutePath();
     //TO-DO Scaling the photo
        textViewWait.setVisibility(View.VISIBLE);
        indicator2.setVisibility(View.VISIBLE);
        indicator2.setIndicatorColor(Color.RED);

        LoadingThread loadingThread = new LoadingThread();
        loadingThread.run();
        //uploader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
      }




    private File createImageFile(String imageStoreName) {
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


    public class LoadingThread extends Thread{

        public void run(){
            Map config = new HashMap();
            config.put("cloud_name", "hawmenu");
            MediaManager.init(getApplicationContext(), config);
            //String timeStamp = new SimpleDateFormat("ss").format(new Date());
            //Log.wtf("mytag", "stamp "+ timeStamp);
            SharedPreferences sharedPref = getSharedPreferences("dishPref", MODE_PRIVATE);
            //String newFileName = file.getName().replaceAll(".jpg", "");
            String newFileName = imageFileName;

            Log.wtf("mytag", "PARAMS 0  " + params[0]);



            String requestId = MediaManager.get().upload(params[0])
                    .unsigned("oafdysu0").option("public_id", newFileName).callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            // your code here
                            Log.wtf("mytag", "STARTED");

                        }
                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            // example code starts here
                        }
                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            // your code here
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("photo", true);
                            startActivity(intent);
                        }
                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            // your code here
                            Log.wtf("mytag", "ERROR");
                        }
                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            // your code here
                        }})
                    .dispatch();
        }
    }

}
