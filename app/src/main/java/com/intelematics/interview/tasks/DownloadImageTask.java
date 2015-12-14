package com.intelematics.interview.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.intelematics.interview.SongListActivity;
import com.intelematics.interview.db.DBManager;
import com.intelematics.interview.db.SongManager;
import com.intelematics.interview.models.Song;
import com.intelematics.interview.net.ConnectionManager;
import com.intelematics.interview.util.CustomException;

import java.io.ByteArrayInputStream;

/**
 * Created by Jorge on 13/dic/2015.
 */
public class DownloadImageTask extends AsyncTask<String, String, String> {
    SongListActivity activity;
    Song song;
    DBManager dbManager;
    public DownloadImageTask(SongListActivity activity, Song song, DBManager dbManager) {
        this.activity = activity;
        this.song = song;
        this.dbManager = dbManager;
    }

    // Download Music File from Internet
    @Override
    protected String doInBackground(String... f_url) {
        ConnectionManager connectionManager;
        byte[] imageByteArray;
        Bitmap cover;
        SongManager songManager;

        try {
            connectionManager = new ConnectionManager(activity.getApplicationContext(), song.getCoverURL());
            imageByteArray = connectionManager.requestImage().buffer();
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray);
            cover = BitmapFactory.decodeStream(imageStream);
            song.setCover(cover);
            songManager = new SongManager(activity, dbManager);
            songManager.saveCover(song, imageByteArray);
        } catch (CustomException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Once Music File is downloaded
    @Override
    protected void onPostExecute(String file_url) {
        // Update the List when the image is downloaded, but we need to verify
        // that the activity is up & running to avoid invalid context exception
        if (activity == null || activity.isFinishing()) return;
        activity.getListAdapter().notifyDataSetChanged();
    }


}
