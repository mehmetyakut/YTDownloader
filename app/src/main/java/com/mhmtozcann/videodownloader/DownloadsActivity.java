package com.mhmtozcann.videodownloader;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by razor on 1.05.2016.
 */
public class DownloadsActivity extends AppCompatActivity {
    private static final String TAG = "DownloadsActivity";
    public Context context;
    private Tracker mTracker;
    private ListView listView;
    private Toolbar toolbar;
    private TextView no_video,video_dir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloads);
        context = DownloadsActivity.this;
        listView =(ListView) findViewById(R.id.list);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        no_video = (TextView)findViewById(R.id.errmsg);
        video_dir = (TextView)findViewById(R.id.video_dir);
        listView.setLongClickable(true);

        setSupportActionBar(toolbar);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("DownloadsActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        Log.d(TAG, "onCreate: sended Tracker Info ");

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemLongClick: #"+position);
                TextView name = (TextView)view.findViewById(R.id.video_title);
                String filename = Environment.getExternalStorageDirectory()+"/"+getString(R.string.app_name)+"/"+name.getText().toString();
                final File file = new File(filename);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog.Builder deletebuilder = new AlertDialog.Builder(context);

                builder.setTitle(getString(R.string.options_video));
                String[] items = {getString(R.string.delete)};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    AlertDialog dialog1;
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletebuilder.setTitle(getString(R.string.are_you_sure));
                        deletebuilder.setMessage(getString(R.string.will_deleted));
                        deletebuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "DeleteBuilder onClick: YES");
                                file.delete();
                                recreate();
                            }
                        });
                        deletebuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "DeleteBuilder onClick: NO");
                                dialog1.dismiss();
                            }
                        });
                        dialog1 = deletebuilder.create();
                        dialog1.show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
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
