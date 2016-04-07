package com.mhmtozcann.videodownloader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.Permission;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.fenjuly.library.ArrowDownloadButton;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static final String TAG = "MainActivity";

    private ProgressDialog dialog;
    private EditText etURL;
    private TextInputLayout textLayout;
    private ArrowDownloadButton downloadButton;

    private static final int REQUEST_WRITE = 111;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //reload my activity with permission granted or use the features what required the permission
                    MainActivity.this.recreate();
                } else
                {
                    Toast.makeText(getParent(), "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        boolean hasPermission =(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermission) {
            ActivityCompat.requestPermissions(getParent(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE);
        }

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        downloadButton = (ArrowDownloadButton)findViewById(R.id.arrow_download_button);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Video Downloader");

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(true);

      //  this.getWindow().setStatusBarColor(Color.parseColor("#b21212"));
      //  this.getWindow().setNavigationBarColor(Color.parseColor("#f1f1f1"));

        etURL = (EditText)findViewById(R.id.video_url);
      textLayout = (TextInputLayout)findViewById(R.id.txtLayout);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadButton.reset();
                if(etURL.getText().toString().contains("youtu.be")){
                    String url = etURL.getText().toString().split("be/")[1];
                    Log.d(TAG,"Submitted ID: "+ url);
                    String parse = "http://46.101.143.104/services/youtube/get.php?vformat=video/mp4&vid="+url;
                    if(!new CheckConnection().getState(MainActivity.this)){
                        Toast.makeText(MainActivity.this,getResources().getString(R.string.network_error),Toast.LENGTH_LONG).show();
                    }else{
                        new DownloadWebPageTask(MainActivity.this).execute(parse.toString());
                        downloadButton.startAnimating();
                    }

                }else if(etURL.getText().toString().contains("youtube.com")){
                    String url = etURL.getText().toString().split("v=")[1].split("&")[0];
                    Log.d(TAG,"Submitted ID: "+ url);
                    String parse = "http://46.101.143.104/services/youtube/get.php?vformat=video/mp4&vid="+url;
                    if(!new CheckConnection().getState(MainActivity.this)){
                        Toast.makeText(MainActivity.this,getResources().getString(R.string.network_error),Toast.LENGTH_LONG).show();
                    }else{
                        new DownloadWebPageTask(MainActivity.this).execute(parse.toString());
                        downloadButton.startAnimating();
                    }
                }else etURL.setError(getResources().getString(R.string.only_yt_links));
            }
        });




        Intent receivedIntent = getIntent();
        String receivedAction = receivedIntent.getAction();

        if(receivedAction.equals(Intent.ACTION_SEND)){
            Bundle extras = receivedIntent.getExtras();
            etURL.setText(extras.getString(Intent.EXTRA_TEXT));
            String parse = "http://46.101.143.104/services/youtube/get.php?vformat=video/mp4&vid="+etURL.getText().toString().split("be/")[1];
            if(!new CheckConnection().getState(MainActivity.this)){
                Toast.makeText(MainActivity.this,getResources().getString(R.string.network_error),Toast.LENGTH_LONG).show();
            }else
            new DownloadWebPageTask(MainActivity.this).execute(parse.toString());
           }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this,getResources().getString(R.string.developer),Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadYT extends AsyncTask<String,Integer,String>{
        private int size;
        private File f;
        private PowerManager.WakeLock mWakeLock;
        private String result;
        private Context context;
        private boolean anyException = false;
        private boolean noSpace = false;
        public DownloadYT(Context context){
            this.context = context;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
           // Log.d(TAG, "Downloading Progress: " + String.valueOf(values[0]));
            try{
                downloadButton.setProgress(values[0]);
            }catch(Exception e){
                Log.d(TAG,"Exception: "+e.toString());
            }

            downloadButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DownloadYT.this.cancel(true);
                    Toast.makeText(context, getResources().getString(R.string.download_cancelled), Toast.LENGTH_SHORT).show();
                    try{
                        f.delete();
                    }catch(Exception e){
                        Log.d(TAG,"Exception: "+ e.toString());
                    }
                    downloadButton.reset();
                    return true;
                }
            });
            if(size != 0) textLayout.setError(getResources().getString(R.string.video_size) + " " + (size / 1024 / 1024) + " MB");

        }

        @Override
        protected void onPostExecute(String s) {
           if(!anyException){
               Toast.makeText(context,getResources().getString(R.string.download_complete),Toast.LENGTH_LONG).show();
               etURL.setText("");
               textLayout.setError("");
           }else{
               Toast.makeText(context,getResources().getString(R.string.download_failed),Toast.LENGTH_LONG).show();
              try{
                  f.delete();
              }catch(Exception e){
                Log.d(TAG,"Exception: "+ e.toString());
              }

           }
           // dialog.dismiss();

            if(noSpace){
                Toast.makeText(context,getResources().getString(R.string.no_space),Toast.LENGTH_LONG).show();
                f.delete();
            }
            downloadButton.reset();
            super.onPostExecute(s);
        }

        protected void onPreExecute() {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
          //  dialog.setMessage(getResources().getString(R.string.downloading) + size / 1024 / 1024 + " MB ");
          //  dialog.show();
            downloadButton.startAnimating();
            Toast.makeText(context,getResources().getString(R.string.download_started),Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            URL u = null;
            InputStream is = null;

            try {
                u = new URL(params[0].toString());
                is = u.openStream();
                HttpURLConnection huc = (HttpURLConnection)u.openConnection();
                size = huc.getContentLength();

                Log.d(TAG,"Video Size: "+ size/1024/1024 +" MB");
                if(huc != null) {
                    Calendar c = Calendar.getInstance();

                    String fileName = "video-"+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE)+"-"+c.get((Calendar.HOUR_OF_DAY))+"-"+c.get(Calendar.MINUTE)+".mp4";
                    String storagePath = Environment.getExternalStorageDirectory().toString();
                    f = new File(storagePath,fileName);
                    int i = 1;
                    while(f.exists()){
                        fileName = "video-"+c.get(Calendar.YEAR)
                                +"-"+(c.get(Calendar.MONTH)+1)
                                +"-"+c.get(Calendar.DATE)+"-"+c.get((Calendar.HOUR_OF_DAY))
                                +"-"+c.get(Calendar.MINUTE)+"("+i+").mp4";
                        i++;
                        f = new File(storagePath,fileName);
                    }

                    FileOutputStream fos = new FileOutputStream(f);
                    byte[] buffer = new byte[1024];
                    long total = 0;
                    int len1 = 0;
                    if(is != null) {
                        while ((len1 = is.read(buffer)) > 0) {
                            fos.write(buffer, 0, len1);
                            total += len1;
                            publishProgress((int)(total * 100 / size));
                        }
                    }
                    if(fos != null) {
                        fos.close();
                    }
                }else{
                    //When Server Connection cant established
                }
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
                anyException = true;

            } catch (IOException ioe) {
                ioe.printStackTrace();
                if(ioe.toString().contains("ENOSPC")){
                   noSpace = true;
                }
                anyException = true;
            } catch (Exception e){
                anyException = true;
            }finally {
                try {
                    if(is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {

                }
            }
            return null;
        }

    }





    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        Context context;
        public DownloadWebPageTask(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";

            for (String url : urls) {
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();
                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(content, Charset.forName("UTF-8")),8);
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
        try{
            Log.d(TAG,"Result: "+ result);
            new DownloadYT(context).execute(result);
        }catch (Exception e){
            Log.d(TAG,"Error Result: "+ e.toString());
        }
            pDialog.dismiss();
        }
    }

    
}
