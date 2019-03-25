package com.example.backgroungapp;

import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static android.Manifest.permission.SEND_SMS;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MyDevicePolicyReceiver extends DeviceAdminReceiver {
//   public MyDevicePolicyReceiver(String num){
//        phoneNo = num;
//    }
    public static Camera camera;
    Handler mHandler;
    private final static String Tag  = "Device admin receiver";
    String phoneNo;
    String lat;
    String longi;
    String message;
    Geocoder geocoder;
    List<Address> addresses;
    String name;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.i(Tag,"ondisabled...........................");
        System.out.println("ondisabled................");

        Toast.makeText(context, " Device Admin Disabled",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.i(Tag,"onenabled...........................");
        System.out.println("onenabled....................");

        Toast.makeText(context, "Device Admin is now enabled",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        CharSequence disableRequestedSeq = "Requesting to disable Device Admin";
        return disableRequestedSeq;
    }

//    @TargetApi(Build.VERSION_CODES.O)
//    @Override
//    public void onPasswordChanged(Context context, Intent intent) {
//        Toast.makeText(context, "Device password is now changed",
//                Toast.LENGTH_SHORT).show();
//        DevicePolicyManager localDPM = (DevicePolicyManager) context
//                .getSystemService(Context.DEVICE_POLICY_SERVICE);
//        ComponentName localComponent = new ComponentName(context,
//                MyDevicePolicyReceiver.class);
//        localDPM.setPasswordExpirationTimeout(localComponent, 0L);
//    }

//    @TargetApi(Build.VERSION_CODES.O)
//    @Override
//    public void onPasswordExpiring(Context context, Intent intent) {
//        // This would require API 11 an above
//        Toast.makeText(
//                context,
//                "Truiton's Device password is going to expire, please change to a new password",
//                Toast.LENGTH_LONG).show();
//
//        DevicePolicyManager localDPM = (DevicePolicyManager) context
//                .getSystemService(Context.DEVICE_POLICY_SERVICE);
//        ComponentName localComponent = new ComponentName(context,
//                MyDevicePolicyReceiver.class);
//        long expr = localDPM.getPasswordExpiration(localComponent);
//        long delta = expr - System.currentTimeMillis();
//        boolean expired = delta < 0L;
//        if (expired) {
//            localDPM.setPasswordExpirationTimeout(localComponent, 10000L);
//            Intent passwordChangeIntent = new Intent(
//                    DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
//            passwordChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(passwordChangeIntent);
//        }
//    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
//        dispatchpictureteakenaaction(context,intent);

//        Intent serviceIntent = new Intent(context, CameraService.class);
//        ContextCompat.startForegroundService(context, serviceIntent);






        Log.i(Tag,"on password failed...........................");
        System.out.println("onpassword failed....................");

        Toast.makeText(context, "wrong password please try again", Toast.LENGTH_SHORT)
                .show();
//        Intent mainpage = new Intent(context, Main2Activity.class);
//        context.startActivity(mainpage);
       SharedPreferences sharedPreferences = context.getSharedPreferences("number",Context.MODE_PRIVATE);
       phoneNo =  sharedPreferences.getString("mobilenumber","0");
       lat = sharedPreferences.getString("lat","0");
       longi = sharedPreferences.getString("longi","0");
       name = sharedPreferences.getString("username","0");
       Log.d("Gotham", phoneNo);
        sendSMSMessage(context,intent);

    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        Log.i(Tag,"onpassword succeeded..........................");
        System.out.println("onpassword success..............");

        Toast.makeText(context, "Hurrayyy!!!!!", Toast.LENGTH_SHORT)
                .show();
//        Intent serviceIntent = new Intent(context, CameraService.class);
//        context.stopService(serviceIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        MainActivity mainActivity;
//
//        Log.i(Tag,"on receive...........................");
//        System.out.println("onreceive..........");

//        Log.i(LOG_TAG,
//                "MyDevicePolicyReciever Received: " + intent.getAction());
//        mainActivity = new MainActivity();
//        phoneNo= mainActivity.getnum();
//        System.out.println(phoneNo+"....................");
        super.onReceive(context, intent);

    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
////                    Toast.makeText(getApplicationContext(), "SMS sent.",
////                            Toast.LENGTH_LONG).show();
//                } else {
////                    Toast.makeText(getApplicationContext(),
////                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
//                    System.out.println("sms failed");
////                    Toast.makeText(context, "Hurrayyy!!!!!", Toast.LENGTH_SHORT)
////                            .show();
//                    return;
//                }
//            }
//        }
//
//    }

    protected void sendSMSMessage(Context context,Intent intent) {
        System.out.println("send sms message.............................");

//        intent = getIntent();
//        String input  = intent.getStringExtra("mobilenumber");
//        phoneNo = txtphoneNo.getText().toString();

        Log.i(Tag,"Sendsmsmessage..........................."+phoneNo);

        SmsManager bat = SmsManager.getDefault();
//        phoneNo = "9492515709";

//        phoneNo = sharedPreferences.getString("mobilenumber",0);
//        message = txtMessage.getText().toString();
//        message = "message recevied";
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(, Manifest.permission.SEND_SMS)) {
//            } else {
//                ActivityCompat.requestPermissions(context,
//                        new String[]{Manifest.permission.SEND_SMS},
//                        MY_PERMISSIONS_REQUEST_SEND_SMS);
//            }
//        }

        geocoder = new Geocoder(context, Locale.getDefault());

        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(longi);
        addresses = null;
//        String address = "";
        try {
            System.out.println("geocoder" + latitude + " " + longitude);
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            StringBuffer smsBody = new StringBuffer();
            smsBody.append("maps.google.com/maps?q=");
            smsBody.append(latitude);
            smsBody.append(",");
            smsBody.append(longitude);

            message = address +"\n"+smsBody.toString()+"\n" + "From---" +name;
            System.out.println(phoneNo+"..................................." + message);

//            PendingIntent pi=PendingIntent.getActivity(context, 0, intent,0);
//            bat.sendTextMessage(phoneNo,null,message,pi,null);
            ArrayList<String> parts =bat.divideMessage(message);
            int numParts = parts.size();

            ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
            ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();

            for (int i = 0; i < numParts; i++) {
                sentIntents.add(PendingIntent.getBroadcast(context, 0, intent, 0));
                deliveryIntents.add(PendingIntent.getBroadcast(context, 0, intent, 0));
            }

            bat.sendMultipartTextMessage(phoneNo,null, parts, sentIntents, deliveryIntents);
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


}
