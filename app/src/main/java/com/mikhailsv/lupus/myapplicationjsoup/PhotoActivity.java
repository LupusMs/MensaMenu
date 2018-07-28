package com.mikhailsv.lupus.myapplicationjsoup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE;


public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button photoBtn1;
    private Button photoBtn2;
    private Button photoBtn3;
    private Button photoBtn4;
    private Button photoBtn5;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        getSupportActionBar().setTitle("Photo Upload");

        photoBtn1 = findViewById(R.id.photoBtn1);
        photoBtn2 = findViewById(R.id.photoBtn2);
        photoBtn3 = findViewById(R.id.photoBtn3);
        photoBtn4 = findViewById(R.id.photoBtn4);
        photoBtn5 = findViewById(R.id.photoBtn5);
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
        params = new String[2];




        SharedPreferences sharedPref = getSharedPreferences("dishPref", MODE_PRIVATE);
        textView1.setText(sharedPref.getString("dish11",""));
        textView2.setText(sharedPref.getString("dish22",""));
        textView3.setText(sharedPref.getString("dish33",""));
        textView4.setText(sharedPref.getString("dish44",""));
        textView5.setText(sharedPref.getString("dish55",""));

        //Hiding photo buttons if there is no text in dish name
        if (textView2.getText().equals("")) photoBtn2.setVisibility(View.INVISIBLE);
        if (textView3.getText().equals("")) photoBtn3.setVisibility(View.INVISIBLE);
        if (textView4.getText().equals("")) photoBtn4.setVisibility(View.INVISIBLE);
        if (textView5.getText().equals("")) photoBtn5.setVisibility(View.INVISIBLE);


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
             break;
            case R.id.photoBtn2:
                params[1] = textView2.getText().toString();
                imageFileName = sharedPref.getString("dish2","default");
                textView2.setTextColor(Color.GREEN);
            break;

            case R.id.photoBtn3:
                params[1] = textView3.getText().toString();
                imageFileName = sharedPref.getString("dish3","default");
                textView3.setTextColor(Color.GREEN);
            break;
            case R.id.photoBtn4:
                params[1] = textView4.getText().toString();
                imageFileName = sharedPref.getString("dish4","default");
                textView4.setTextColor(Color.GREEN);
             break;
            case R.id.photoBtn5:
                params[1] = textView5.getText().toString();
                imageFileName = sharedPref.getString("dish5","default");
                textView5.setTextColor(Color.GREEN);
            break;
        }
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            photoFile = createImageFile(imageFileName + "al");
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


                //File to upload to cloudinary
     MyUploader uploader = new MyUploader(getApplicationContext());
     params[0] = photoFile.getAbsolutePath();
     //TO-DO Scaling the photo
        textViewWait.setVisibility(View.VISIBLE);
        indicator2.setVisibility(View.VISIBLE);
        indicator2.setIndicatorColor(Color.RED);

        uploader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
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

    class MyUploader extends AsyncTask<String, Void, Void> {


        private final Context mContext;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
             Toast.makeText(getApplicationContext(), "Image will be uploaded...", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.VISIBLE);

        }

        MyUploader(final Context context) {
            mContext = context;


        }
        @Override
        protected Void doInBackground(String... strings) {
            android.os.Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND + THREAD_PRIORITY_MORE_FAVORABLE);
            Map config = new HashMap();
            config.put("cloud_name", "hawmenu");
            MediaManager.init(mContext, config);
            //String timeStamp = new SimpleDateFormat("ss").format(new Date());
            //Log.wtf("mytag", "stamp "+ timeStamp);
            SharedPreferences sharedPref = getSharedPreferences("dishPref", MODE_PRIVATE);
            File file = new File(strings[0]);
            String newFileName = file.getName().replaceAll(".jpg", "");

            String requestId = MediaManager.get().upload(strings[0])
                    .unsigned("oafdysu0").option("public_id", newFileName).callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            // your code here


                        }
                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            // example code starts here
                            Double progress = (double) bytes/totalBytes;
                            // post progress to app UI (e.g. progress bar, notification)
                            progressBar.setProgress(progress.intValue()*100);
                            progressBar.setSecondaryProgress(progress.intValue()*100 + 10);
                            // example code ends here
                        }
                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            // your code here
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            // your code here
                        }
                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            // your code here
                        }})
                    .dispatch();

            return null;
        }
    }

}
