package com.mikhailsv.lupus.myapplicationjsoup;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cloudinary.android.MediaManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyUploader extends AsyncTask <String, Void, Void> {

    private final Context mContext;

    public MyUploader(final Context context) {
        mContext = context;
    }
    @Override
    protected Void doInBackground(String... strings) {
        Map config = new HashMap();
        config.put("cloud_name", "hawmenu");

        Log.wtf("mytag", "cloud accessed");
        Log.wtf("mytag", "strings0 " + strings[0]);
        Log.wtf("mytag", "strings1 "+ strings[1]);



        Log.wtf("mytag", "FILE ddd" + strings[0]);
        MediaManager.init(mContext, config);
        String timeStamp = new SimpleDateFormat("ss").format(new Date());
        Log.wtf("mytag", "stamp "+ timeStamp);
        String newFileName = new changeDisplayText().searchText(strings[1]);
        String requestId = MediaManager.get().upload(strings[0])
                .unsigned("oafdysu0").option("public_id", newFileName + timeStamp)
                .dispatch();
        Log.wtf("mytag", "try success" + requestId);
        Log.wtf("mytag", "newName" + newFileName + timeStamp);

        return null;
    }


}
