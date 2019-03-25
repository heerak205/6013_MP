package com.example.backgroungapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import static android.Manifest.permission.SEND_SMS;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private final static String LOG_TAG = "DevicePolicyAdmin";
    DevicePolicyManager devicePolicyManager;
    ComponentName devicePolicyAdmin;
    private CheckBox AdminEnabledCheckbox;
    protected static final int REQUEST_ENABLE = 1;
    protected static final int SET_PASSWORD = 2;
    public  EditText edit;
    public String num;
    Button  number;
    SharedPreferences sharedPreferences;
    // gps
    public static final String TAG = "Tag";
    private static final  int REQUEST_CODE = 1000;
    private GoogleApiClient googleApiClient;
    private Location location;
    TextView username;
    String namee;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("oncreate........................");
        setContentView(R.layout.activity_main);
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        devicePolicyAdmin = new ComponentName(this,
                MyDevicePolicyReceiver.class);

        AdminEnabledCheckbox = (CheckBox) findViewById(R.id.checkBox1);
        edit = (EditText) findViewById(R.id.editTextt);
        number = (Button) findViewById(R.id.button);
        username = (TextView) findViewById(R.id.textView);
        namee = "Hello!!!  "+ getIntent().getStringExtra("uname");


        sharedPreferences = getSharedPreferences("number",Context.MODE_PRIVATE);
        SharedPreferences.Editor editt = sharedPreferences.edit();
        editt.putString("username",namee);
        editt.apply();
        username.setText(sharedPreferences.getString("username"," Hello Buddy!"));
        number.setOnClickListener(new View.OnClickListener() {
           public void  onClick(View v)  {
               SharedPreferences.Editor editer = sharedPreferences.edit();
               num = edit.getText().toString();
               edit.setText("");
               Toast.makeText(MainActivity.this, "Number Saved", Toast.LENGTH_SHORT).show();

               SharedPreferences.Editor edit = sharedPreferences.edit();
               editer.putString("mobilenumber",num);
               editer.apply();
            }

        });

        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .addApi(LocationServices.API).build();
//        checkPermissions();
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(MainActivity.this, "SMS Permission Granted", Toast.LENGTH_SHORT).show();

        } else {
            requestsmspermission();
        }


    }
    private void requestsmspermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)) {
            new AlertDialog.Builder(this)
                    .setTitle("Need sms permission")
                    .setMessage("this permission is need for sending sms")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.SEND_SMS} , 23);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS} , 23);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 23) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "SMS Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //    private void checkPermissions() {
////        int permissionState = ActivityCompat.checkSelfPermission(this,
////                Manifest.permission.SEND_SMS);
////        return permissionState == PackageManager.PERMISSION_GRANTED;
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.SEND_SMS)) {
//            } else {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.SEND_SMS},
//                        0);
//            }
//        }
//    }

    // function related to get gps services
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG,"onconnected gps ");
        showuserlocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"on connection suspendeed");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"on connection failed");
        if(connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(MainActivity.this,REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
//            Toast.makeText(activity, "on connection falied", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
    //custom methods
    private void showuserlocation(){
        int permissioncheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissioncheck == PackageManager.PERMISSION_GRANTED){
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
//            location = fusedLocationProviderClient.getLastLocation(googleApiClient);

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location locationn) {
                            // Got last known location. In some rare situations this can be null.
                            location = locationn;
                            if(location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

//                                Toast.makeText(activity, "lat long", Toast.LENGTH_SHORT).show();
                                System.out.println(latitude + "................." + longitude);
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.putString("lat",latitude + "");
                                edit.putString("longi",longitude+"");
                                edit.apply();
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        } else {
            System.out.println("permission denied");
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},10);
        }

    }





    public void savenumber(View v) {
        num = edit+"";
    }
    @Override
    protected void onResume() {
        System.out.println("onresume.........................."+ sharedPreferences.getString("mobilenumber","0"));
        super.onResume();
        if (isMyDevicePolicyReceiverActive()) {
            AdminEnabledCheckbox.setChecked(true);
            System.out.println("check box true condition.......................");

        } else {
            AdminEnabledCheckbox.setChecked(false);
            System.out.println("check box on condition....................");

        }
        AdminEnabledCheckbox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        System.out.println("on check box listener..........................");

                        if (isChecked) {
                            Intent intent = new Intent(
                                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(
                                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                                    devicePolicyAdmin);
                            intent.putExtra(
                                    DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                    getString(R.string.admin_explanation));
//                            intent.putExtra("mobilenumber","9393343275");
                            startActivityForResult(intent, REQUEST_ENABLE);
                        } else {
                            devicePolicyManager
                                    .removeActiveAdmin(devicePolicyAdmin);
                        }
                    }
                });
        System.out.println(num + "number ..................");


    }
//   @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       System.out.println("onactivityresult..............................");

       super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            System.out.println(resultCode+"...............resultcode in onactivityresult");

            switch (requestCode) {
                case REQUEST_ENABLE:
                    System.out.println("switch case.............");

                    Log.v(LOG_TAG, "Enabling Policies Now");
                    devicePolicyManager.setMaximumTimeToLock(
                            devicePolicyAdmin, 60000L);
//                    devicePolicyManager.setMaximumFailedPasswordsForWipe(
//                            devicePolicyAdmin, 100);
//                    devicePolicyManager.setPasswordQuality(
//                            devicePolicyAdmin,
//                            DevicePolicyManager.PASSWORD_QUALITY_COMPLEX);
//                    devicePolicyManager.setCameraDisabled(
//                            devicePolicyAdmin, true);
                    boolean isSufficient = devicePolicyManager
                            .isActivePasswordSufficient();
                    if (isSufficient) {
                        devicePolicyManager.lockNow();
                    } else {
                        Intent setPasswordIntent = new Intent(
                                DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                        startActivityForResult(setPasswordIntent, SET_PASSWORD);
                        devicePolicyManager.setPasswordExpirationTimeout(
                                devicePolicyAdmin, 10000L);
                    }

                    break;
            }
        }
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    private boolean isMyDevicePolicyReceiverActive() {
        System.out.println("check dpr active ........................");

        return devicePolicyManager
                .isAdminActive(devicePolicyAdmin);
    }
}


