package com.mhmtozcann.videodownloader;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import com.fenjuly.library.ArrowDownloadButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static final String TAG = "MainActivity";
    public String urlss = null;
    private ProgressDialog dialog;
    private EditText etURL;
    private TextInputLayout textLayout;
    private ArrowDownloadButton downloadButton;
    final static int PERMISSIONS_REQUEST_CODE = 1;
    private Tracker mTracker;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        Log.d(TAG, "onCreate: sended Tracker Info "+mTracker.toString());



       getPermissionAccessNetworkState();
       getPermissionToInternet();
       getPermissionWakeLock();
       getPermissionWriteExternalStorage();


        downloadButton = (ArrowDownloadButton)findViewById(R.id.arrow_download_button);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("DCE0AE10FB4584072F669804913626A3")
                .build();
        mAdView.loadAd(adRequest);

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
               //.. Log.d(TAG,"URI: "+ Uri.parse(etURL.getText().toString()).getQueryParameter("v"));

                if(etURL.getText().toString().contains("youtu.be")){
                    String url = etURL.getText().toString().split("be/")[1];

                    Log.d(TAG,"Submitted ID: "+ url);
                    urlss = "https://www.youtube.com/watch?v="+url;
                    String parse = "http://46.101.143.104/services/youtube/get.php?vformat=video/mp4&vid="+url;
                    if(!new CheckConnection().getState(MainActivity.this)){
                        Toast.makeText(MainActivity.this,getResources().getString(R.string.network_error),Toast.LENGTH_LONG).show();
                    }else{
                        new DownloadWebPageTask(MainActivity.this).execute(parse.toString());
                     //   downloadButton.startAnimating();
                    }

                }else if(etURL.getText().toString().contains("youtube.com")){
                    String url = etURL.getText().toString().split("v=")[1].split("&")[0];
                    Log.d(TAG,"Submitted ID: "+ url);
                    urlss = etURL.getText().toString();

                    String parse = "http://46.101.143.104/services/youtube/get.php?vformat=video/mp4&vid="+url;
                    if(!new CheckConnection().getState(MainActivity.this)){
                        Toast.makeText(MainActivity.this,getResources().getString(R.string.network_error),Toast.LENGTH_LONG).show();
                    }else{
                        new DownloadWebPageTask(MainActivity.this).execute(parse.toString());
                    //    downloadButton.startAnimating();
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
            String url = etURL.getText().toString().split("be/")[1];
            Log.d(TAG,"Submitted ID: "+ url);
            urlss = "https://www.youtube.com/watch?v="+url;
            if(!new CheckConnection().getState(MainActivity.this)){
                Toast.makeText(MainActivity.this,getResources().getString(R.string.network_error),Toast.LENGTH_LONG).show();
            }else
            new DownloadWebPageTask(MainActivity.this).execute(parse.toString());
           }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void getPermissionAccessNetworkState() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_NETWORK_STATE)) {
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    public void getPermissionWriteExternalStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    public void getPermissionToInternet() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.INTERNET)) {
            }
            requestPermissions(new String[]{Manifest.permission.INTERNET},
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    public void getPermissionWakeLock() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.WAKE_LOCK)) {
            }
            requestPermissions(new String[]{Manifest.permission.WAKE_LOCK},
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this,getResources().getString(R.string.developer),Toast.LENGTH_LONG).show();
            return true;
        }else if( id == R.id.action_downloads){
            startActivity(new Intent(MainActivity.this,DownloadsActivity.class));
            return true;
        }else if(id == R.id.openyt){
            boolean isInstalled = appInstalledOrNot("com.google.android.youtube");
            if(isInstalled){
                Log.d(TAG, "onOptionsItemSelected: YouTube is installed on this phone");
                Intent launch = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                startActivity(launch);
            }else{
                Log.d(TAG, "onOptionsItemSelected: YouTube is NOT installed on this phone");
                final String appPackageName = "com.google.android.youtube"; // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendNotification(String message,String title) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public static String getTitleQuietly(String youtubeUrl) {
        try {
            if (youtubeUrl != null) {
                URL embededURL = new URL("http://www.youtube.com/oembed?url=" +
                        youtubeUrl + "&format=json"
                );
                BufferedReader reader = new BufferedReader(new InputStreamReader(embededURL.openStream()));
                String content="";
                String line;
                while ((line = reader.readLine())!= null){
                    content += line;
                }
                reader.close();
                JSONObject data = new JSONObject(content.toString());
                String title = data.getString("title");
                title.replaceAll("[^a-zA-Z0-9.-]", " ");

                return title;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
               Toast.makeText(context,getString(R.string.download_complete), Toast.LENGTH_LONG).show();
           }else{
               Toast.makeText(context,getResources().getString(R.string.download_failed),Toast.LENGTH_LONG).show();
              try{
                  f.delete();
              }catch(Exception e){
                Log.d(TAG,"Exception: "+ e.toString());
              }

           }

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

                    String fileName;
                    String storagePath = Environment.getExternalStorageDirectory().toString();
                    File dirs = new File(storagePath+"/"+getString(R.string.app_name).toString());
                    if(!dirs.exists()){
                        dirs.mkdirs();
                    }
                    f = new File(storagePath+"/"+getString(R.string.app_name).toString(),getTitleQuietly(urlss.toString())+".mp4");
                    int i = 1;
                    while(f.exists()){
                        fileName = getTitleQuietly(urlss.toString())+"("+i+").mp4";
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
                Log.d(TAG,"Video Title: "+ getTitleQuietly(urlss.toString()));

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
            downloadButton.startAnimating();
        }catch (Exception e){
            Log.d(TAG,"Error Result: "+ e.toString());
        }
            pDialog.dismiss();
        }
    }

}
