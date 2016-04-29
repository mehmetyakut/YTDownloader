package com.mhmtozcann.videodownloader;

/**
 * Created by razor on 29.04.2016.
 */
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by murat on 24.04.2016.
 */

//-----------MarsMallow Permission---------------------------------------------

public class Permissions extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    //    getPermissionCallPhone();
    //    getPermissionSendSms();
        getPermissionToInternet();
   //     getPermissionLocation();
   //     getPermissionBluetooth();
  //      getPermissionBluetoothAdmin();
        getPermissionWriteExternalStorage();
        getPermissionAccessNetworkState();
  //      getPermissionReadPhoneState();
  //      getPermissionRecordAudio();
        getPermissionWakeLock();
  //      getPermissionModifyAudioSetting();
 //       getPermissionAccesWifiState();
 //       getPermissionChangeWifiMultiCastState();
 //       getPermissionReceiveBootCompleted();

    }




/**
 <uses-permission android:name="android.permission.BLUETOOTH" />
 <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 <uses-permission android:name="android.permission.INTERNET" />
 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 <uses-permission android:name="android.permission.CALL_PHONE" />
 * */

    /**
     <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     <uses-permission android:name="android.permission.RECORD_AUDIO" />
     <uses-permission android:name="android.permission.WAKE_LOCK" />
     <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
     <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     <uses-permission android:name="android.permission.CHANGE_WIFI_MULTICAST_STATE" />
     <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
     <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />*/


    final static int PERMISSIONS_REQUEST_CODE = 1;

    public void getPermissionBluetooth() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.BLUETOOTH)) {
            }
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH},
                    PERMISSIONS_REQUEST_CODE);
        }
    }


    public void getPermissionBluetoothAdmin() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.BLUETOOTH_ADMIN)) {
            }
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN},
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

    public void getPermissionLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_CODE);
        }
    }


    public void getPermissionCallPhone() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.CALL_PHONE)) {
            }
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    public void getPermissionSendSms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.SEND_SMS)) {
            }
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    PERMISSIONS_REQUEST_CODE);
        }
    }
    public void getPermissionReadPhoneState() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_PHONE_STATE)) {
            }
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_CODE);
        }
    }
    public void getPermissionRecordAudio() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.RECORD_AUDIO)) {
            }
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
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


    public void getPermissionModifyAudioSetting() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
            }
            requestPermissions(new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS},
                    PERMISSIONS_REQUEST_CODE);
        }
    }
    public void getPermissionAccesWifiState() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_WIFI_STATE)) {
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                    PERMISSIONS_REQUEST_CODE);
        }
    }


    public void getPermissionChangeWifiMultiCastState() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_MULTICAST_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.CHANGE_WIFI_MULTICAST_STATE)) {
            }
            requestPermissions(new String[]{Manifest.permission.CHANGE_WIFI_MULTICAST_STATE},
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    public void getPermissionChangeWifiState() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.CHANGE_WIFI_STATE)) {
            }
            requestPermissions(new String[]{Manifest.permission.CHANGE_WIFI_STATE},
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    public void getPermissionReceiveBootCompleted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.RECEIVE_BOOT_COMPLETED)) {
            }
            requestPermissions(new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},
                    PERMISSIONS_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



}