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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE;


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
    private ProgressBar progressBar;
    private TextView textViewWait;
    private com.wang.avi.AVLoadingIndicatorView indicator2;



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
        progressBar = findViewById(R.id.progressBar);
        textViewWait = findViewById(R.id.textViewWait);
        indicator2 = findViewById(R.id.indicator2);

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



        }
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                 photoFile = createImageFile(imageFileName + "al");
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
     params[0] = photoFile.getAbsolutePath().toString();
     //TO-DO Scaling the photo
        textViewWait.setVisibility(View.VISIBLE);
        indicator2.setVisibility(View.VISIBLE);
        indicator2.setIndicatorColor(Color.RED);

        uploader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
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

    class MyUploader extends AsyncTask<String, Void, Void> {


        private final Context mContext;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


             Toast.makeText(getApplicationContext(), "Image will be uploaded...", Toast.LENGTH_LONG).show();

            //  mContext.startActivity(new Intent(mContext, MainActivity.class));


        }

        public MyUploader(final Context context) {
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
                            Log.wtf("mytag", "callback started");
                        }
                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            // example code starts here
                            Double progress = (double) bytes/totalBytes;
                            // post progress to app UI (e.g. progress bar, notification)
                            Log.wtf("mytag", "callback progress" + progress);

                            progressBar.setProgress(progress.intValue()*100);
                            progressBar.setSecondaryProgress(progress.intValue()*100 + 10);
                            // example code ends here
                        }
                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            // your code here
                            Log.wtf("mytag", "callback success");
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
            Log.wtf("mytag", "try success" + requestId);


            return null;
        }



    }

}
