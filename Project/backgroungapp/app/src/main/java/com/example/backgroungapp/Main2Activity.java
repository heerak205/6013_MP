package com.example.backgroungapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    EditText name ;
    Button userbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        name = (EditText) findViewById(R.id.name);
        userbutton = (Button) findViewById(R.id.button2);

        userbutton.setOnClickListener(new View.OnClickListener() {
            public void  onClick(View v)  {

//                num = edit.getText().toString();
//                edit.setText("");
                Toast.makeText(Main2Activity.this, "Username Saved", Toast.LENGTH_SHORT).show();

//                SharedPreferences.Editor edit = sharedPreferences.edit();
//                edit.putString("mobilenumber",num);
//                edit.apply();
                String usrnamee = name.getText().toString();
                Intent mainactivity = new Intent(getApplicationContext(), MainActivity.class);
                mainactivity.putExtra("uname",usrnamee);
                startActivity(mainactivity);
            }

        });
    }
//    private void username(View v) {
//        String usrnamee = name.getText().toString();
//        Intent mainactivity = new Intent(this, MainActivity.class);
//        mainactivity.putExtra("uname",usrnamee);
//        startActivity(mainactivity);
//
//    }


}
