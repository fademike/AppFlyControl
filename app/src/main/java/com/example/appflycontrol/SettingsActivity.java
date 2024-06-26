package com.example.appflycontrol;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class SettingsActivity extends Activity {

    public static final String TAG = "test";

    EditText edit_ipadress;
    EditText edit_ipport;
    Button btn_setip;
    Button btn_params;
    Button btn_calib_acc;
    Button btn_calib_gyro;
    Button btn_reboot;
    Button btn_back;
    Switch sw_viewlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        edit_ipadress = (EditText) findViewById(R.id.et_ipaddress);
        edit_ipadress.setText("" + MainActivity.IP_Adress);
        edit_ipport = (EditText) findViewById(R.id.et_ipport);
        edit_ipport.setText("" + MainActivity.IP_Port);

        sw_viewlog = (Switch) findViewById(R.id.sw_viewlog);
        sw_viewlog.setChecked(MainActivity.View_log);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {    //Send
            @Override
            public void onClick(View v) {

                boolean sw_state = sw_viewlog.isChecked();

                if (MainActivity.View_log != sw_state) {
                    MainActivity.View_log = sw_state;
                    MainActivity.mSettings.edit().putBoolean("View_log", sw_state).commit();
                }
                onBackPressed();
            }
        });

        btn_setip = (Button) findViewById(R.id.btn_ipset);
        btn_setip.setOnClickListener(new View.OnClickListener() {    //Send
            @Override
            public void onClick(View v) {
                String ipa = edit_ipadress.getText().toString();
                int ipport = Integer.parseInt(edit_ipport.getText().toString());
                MainActivity.IP_Adress = ipa;
                MainActivity.IP_Port = ipport;
                MainActivity.mSettings.edit().putString("IP_Adress", ipa).commit();
                MainActivity.mSettings.edit().putInt("IP_Port", ipport).commit();
            }
        });
        btn_params = (Button) findViewById(R.id.btn_param);
        btn_params.setOnClickListener(new View.OnClickListener() {    //goto list params
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, params_activity.class);//params_activity.class);//MenuActivity.class);
                startActivity(intent);
            }
        });
        btn_calib_acc = (Button) findViewById(R.id.btn_calib_acc);
        btn_calib_acc.setOnClickListener(new View.OnClickListener() {    //Start calibrate acc
            @Override
            public void onClick(View v) {
                MainActivity.mav_start_calibrate(false, true);
            }
        });
        btn_calib_gyro = (Button) findViewById(R.id.btn_calib_gyro);
        btn_calib_gyro.setOnClickListener(new View.OnClickListener() {    //Start calibrate acc
            @Override
            public void onClick(View v) {
                MainActivity.mav_start_calibrate(true, false);
            }
        });
        btn_reboot = (Button) findViewById(R.id.btn_reboot);
        btn_reboot.setOnClickListener(new View.OnClickListener() {    //Start calibrate acc
            @Override
            public void onClick(View v) {
                MainActivity.mav_start_reboot();
            }
        });


    }

}
