package com.intelematics.interview.tasks;

import android.os.AsyncTask;

import com.intelematics.interview.SongListActivity;
import com.intelematics.interview.db.DBManager;
import com.intelematics.interview.db.SongManager;
import com.intelematics.interview.models.Song;
import com.intelematics.interview.util.CustomException;
import com.intelematics.interview.util.JsonParser;

import java.util.ArrayList;

/**
 *
 */
public class ReadDBSongListTask extends AsyncTask<Void, Void, Void> {
	private DBManager dbManager;
	private SongListActivity activity;
	private ArrayList<Song> songList;
	
	
	public ReadDBSongListTask(SongListActivity activity, DBManager dbManager) {
		this.activity = activity;
		this.dbManager = dbManager;
		songList = new ArrayList<Song>();
	}

	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			SongManager songManager = new SongManager(activity, dbManager);
			songList = songManager.getSongsList();
		}catch(Exception e){
			System.out.println(JsonParser.getStackTrace(e));
            try {
                throw new CustomException(JsonParser.getStackTrace(e));
            } catch (CustomException e1) {
                e1.printStackTrace();
            }
        }
		return null;
	}

    protected void onPostExecute(Void result) {
    	activity.updateSongList(songList);
    }



}
