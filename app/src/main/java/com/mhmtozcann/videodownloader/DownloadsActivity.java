package com.mhmtozcann.videodownloader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by razor on 1.05.2016.
 */
public class DownloadsActivity extends AppCompatActivity {
    private static final String TAG = "DownloadsActivity";
    ListView listView;
    Toolbar toolbar;
    TextView no_video,video_dir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloads);
        listView =(ListView) findViewById(R.id.list);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        no_video = (TextView)findViewById(R.id.errmsg);
        video_dir = (TextView)findViewById(R.id.video_dir);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle( getResources().getString(R.string.downloads));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try{
            String[] items = listVideos();
            if(items.length >0) {
                no_video.setVisibility(View.GONE);
                video_dir.setVisibility(View.VISIBLE);
                video_dir.setText(getResources().getString(R.string.video_dir)+Environment.getExternalStorageDirectory()+"/"+getResources().getString(R.string.app_name));
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.video_title, items);
                listView.setAdapter(arrayAdapter);
            }else{
                no_video.setVisibility(View.VISIBLE);
                video_dir.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent vieww = new Intent();
                vieww.setAction(Intent.ACTION_VIEW);
                TextView text = (TextView)view.findViewById(R.id.video_title);
                String fname =  text.getText().toString();
                vieww.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory()+"/"+ getResources().getString(R.string.app_name)+"/"+fname),"video/mp4");
                Log.d(TAG, "onItemClick: "+vieww.getDataString());
                startActivity(vieww);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                //  Toast.makeText(MonitorSettingsActivity.this, "Settings Saved", Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask(this);

        }
        return super.onOptionsItemSelected(item);
    }

    public String[] listVideos (){
        ArrayList<String> lists = new ArrayList<>();

        String[] fileList;
        String[] last;
        File videoFiles = new File(Environment.getExternalStorageDirectory()+"/"+getString(R.string.app_name));
        if(videoFiles.exists()) {
            if (videoFiles.isDirectory()) {
                fileList = new String[videoFiles.list().length];
                fileList = videoFiles.list();
            } else fileList = new String[0];


            for (int i = 0; i < fileList.length; i++) {
                Log.e("Video:" + i + " File name", fileList[i]);
                if (fileList[i].endsWith(".mp4")) {
                    lists.add(fileList[i].toString());
                }
            }
        }
        last = new String[lists.size()];
        for (int i = 0; i < lists.size(); i++) {
            last[i] = lists.get(i).toString();
        }
        return last;
    }

}
