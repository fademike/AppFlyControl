package com.example.appflycontrol;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    public static final String TAG = "test";//TcpClient.class.getSimpleName();


    EditText typeManage, CmdTimeout, StabRoll, StabPitch, StabYaw, StabAngelPitch, StabAngelRoll, StabKoeffManage1, StabKoeffManage2;
    EditText StabServo1, StabServo2, StabKoeffCh1, StabKoeffCh2, pos_rev , pos_ang1,  pos_ang2,  StabServoMax1, StabServoMax2;
    EditText math_K_angle,  math_K_bias, math_K_measure, math_gyroRate;
    EditText manageExp;

    static int MotorValue = 0;

    int Click_TextView = 0;

    TextView texttypeManage, textCmdTimeout, textStabRoll, textStabPitch, textStabYaw, textStabAngelPitch, textStabAngelRoll, textStabKoeffManage1, textStabKoeffManage2;
    TextView textStabServo1, textStabServo2, textStabKoeffCh1, textStabKoeffCh2, textpos_rev , textpos_ang1,  textpos_ang2,  textStabServoMax1, textStabServoMax2;
    TextView textmath_K_angle,  textmath_K_bias, textmath_K_measure, textmath_gyroRate;
    //TextView textmanageExp;
    TextView text_toChange, text_toChangeIndic;


    EditText ROLL_INVERSION;
    EditText ROLL_RANGEVALUE;
    EditText PITCH_INVERSION;
    EditText PITCH_RANGEVALUE;
    Spinner SERVO1_TYPEMOVE;
    Spinner SERVO2_TYPEMOVE;


    SeekBar sb_toCnange, sb_Motor;

    Button btnReset;

//            textCmdTimeout.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) { TextView tw = (TextView)v; }
//    })
//

//    static public View.OnClickListener click_textView = new View.OnClickListener(){
//        @Override
//        public void OnClick(View v){
//
//        }
//    }
//
//
//abstract class CustomClickListener{
////        @Override
////        public void OnClick(View v){}
////    }

    int Downloaded_Id = 0;


    View.OnClickListener text_OnClick = new View.OnClickListener(){
    //    @Override
        public void onClick(View v)
        {
            int nch=0;
            TextView tw = (TextView)v;
            text_toChange.setText(tw.getText());
            if (v.getId() == R.id.texttypeManage) {text_toChangeIndic.setText(typeManage.getText()); sb_toCnange.setProgress(Integer.parseInt(typeManage.getText().toString()));}
            else if (v.getId() == R.id.textCmdTimeout) {text_toChangeIndic.setText(CmdTimeout.getText()); sb_toCnange.setProgress(Integer.parseInt(CmdTimeout.getText().toString()));}
            else if (v.getId() == R.id.textStabRoll) {text_toChangeIndic.setText(StabRoll.getText()); sb_toCnange.setProgress(Integer.parseInt(StabRoll.getText().toString()));}
            else if (v.getId() == R.id.textStabPitch) {text_toChangeIndic.setText(StabPitch.getText()); sb_toCnange.setProgress(Integer.parseInt(StabPitch.getText().toString()));}
            else if (v.getId() == R.id.textStabYaw) {text_toChangeIndic.setText(StabYaw.getText()); sb_toCnange.setProgress(Integer.parseInt(StabYaw.getText().toString()));}
            else if (v.getId() == R.id.textStabAngelPitch) {text_toChangeIndic.setText(StabAngelPitch.getText()); sb_toCnange.setProgress(Integer.parseInt(StabAngelPitch.getText().toString()));}
            else if (v.getId() == R.id.textStabAngelRoll) {text_toChangeIndic.setText(StabAngelRoll.getText()); sb_toCnange.setProgress(Integer.parseInt(StabAngelRoll.getText().toString()));}
            else if (v.getId() == R.id.textStabKoeffManage1) {text_toChangeIndic.setText(StabKoeffManage1.getText()); sb_toCnange.setProgress(Integer.parseInt(StabKoeffManage1.getText().toString()));}
            else if (v.getId() == R.id.textStabKoeffManage2) {text_toChangeIndic.setText(StabKoeffManage2.getText()); sb_toCnange.setProgress(Integer.parseInt(StabKoeffManage2.getText().toString()));}
            else if (v.getId() == R.id.textStabServo1) {text_toChangeIndic.setText(StabServo1.getText()); sb_toCnange.setProgress(Integer.parseInt(StabServo1.getText().toString()));}
            else if (v.getId() == R.id.textStabServo2) {text_toChangeIndic.setText(StabServo2.getText()); sb_toCnange.setProgress(Integer.parseInt(StabServo2.getText().toString()));}
            else if (v.getId() == R.id.textStabKoeffCh1) {text_toChangeIndic.setText(StabKoeffCh1.getText()); sb_toCnange.setProgress(Integer.parseInt(StabKoeffCh1.getText().toString()));}
            else if (v.getId() == R.id.textStabKoeffCh2) {text_toChangeIndic.setText(StabKoeffCh2.getText()); sb_toCnange.setProgress(Integer.parseInt(StabKoeffCh2.getText().toString()));}
            else if (v.getId() == R.id.textpos_rev) {text_toChangeIndic.setText(pos_rev.getText()); sb_toCnange.setProgress(Integer.parseInt(pos_rev.getText().toString()));}
            else if (v.getId() == R.id.textpos_ang1) {text_toChangeIndic.setText(pos_ang1.getText()); sb_toCnange.setProgress(Integer.parseInt(pos_ang1.getText().toString()));}
            else if (v.getId() == R.id.textpos_ang2) {text_toChangeIndic.setText(pos_ang2.getText()); sb_toCnange.setProgress(Integer.parseInt(pos_ang2.getText().toString()));}
            else if (v.getId() == R.id.textStabServoMax1) {text_toChangeIndic.setText(StabServoMax1.getText()); sb_toCnange.setProgress(Integer.parseInt(StabServoMax1.getText().toString()));}
            else if (v.getId() == R.id.textStabServoMax2) {text_toChangeIndic.setText(StabServoMax2.getText()); sb_toCnange.setProgress(Integer.parseInt(StabServoMax2.getText().toString()));}
            else if (v.getId() == R.id.textmath_K_angle) {text_toChangeIndic.setText(math_K_angle.getText()); sb_toCnange.setProgress(Integer.parseInt(math_K_angle.getText().toString()));}
            else if (v.getId() == R.id.textmath_K_bias) {text_toChangeIndic.setText(math_K_bias.getText()); sb_toCnange.setProgress(Integer.parseInt(math_K_bias.getText().toString()));}
            else if (v.getId() == R.id.textmath_K_measure) {text_toChangeIndic.setText(math_K_measure.getText()); sb_toCnange.setProgress(Integer.parseInt(math_K_measure.getText().toString()));}
            else if (v.getId() == R.id.textmath_gyroRate) {text_toChangeIndic.setText(math_gyroRate.getText()); sb_toCnange.setProgress(Integer.parseInt(math_gyroRate.getText().toString()));}
            else {nch = 1;}
            if (nch == 0)Downloaded_Id = v.getId();
        }
    };


    void UploadData(){

        MainActivity.typeManage = Integer.parseInt(typeManage.getText().toString());
        MainActivity.CmdTimeout = Integer.parseInt(CmdTimeout.getText().toString());
        MainActivity.StabRoll = Integer.parseInt(StabRoll.getText().toString());
        MainActivity.StabPitch = Integer.parseInt(StabPitch.getText().toString());
        MainActivity.StabYaw = Integer.parseInt(StabYaw.getText().toString());
        MainActivity.StabAngelPitch = Integer.parseInt(StabAngelPitch.getText().toString());
        MainActivity.StabAngelRoll = Integer.parseInt(StabAngelRoll.getText().toString());
        MainActivity.StabKoeffManage1 = Integer.parseInt(StabKoeffManage1.getText().toString());
        MainActivity.StabKoeffManage2 = Integer.parseInt(StabKoeffManage2.getText().toString());
        MainActivity.StabServo1 = Integer.parseInt(StabServo1.getText().toString());
        MainActivity.StabServo2 = Integer.parseInt(StabServo2.getText().toString());
        MainActivity.StabKoeffCh1 = Integer.parseInt(StabKoeffCh1.getText().toString());
        MainActivity.StabKoeffCh2 = Integer.parseInt(StabKoeffCh2.getText().toString());
        MainActivity.pos_rev = Integer.parseInt(pos_rev.getText().toString());
        MainActivity.pos_ang1 = Integer.parseInt(pos_ang1.getText().toString());
        MainActivity.pos_ang2 = Integer.parseInt(pos_ang2.getText().toString());
        MainActivity.StabServoMax1 = Integer.parseInt(StabServoMax1.getText().toString());
        MainActivity.StabServoMax2 = Integer.parseInt(StabServoMax2.getText().toString());
        MainActivity.math_K_angle = Integer.parseInt(math_K_angle.getText().toString());
        MainActivity.math_K_bias = Integer.parseInt(math_K_bias.getText().toString());
        MainActivity.math_K_measure = Integer.parseInt(math_K_measure.getText().toString());
        MainActivity.math_gyroRate = Integer.parseInt(math_gyroRate.getText().toString());
        MainActivity.manageExp = Integer.parseInt(manageExp.getText().toString());

        MainActivity.ROLL_INVERSION = Integer.parseInt(ROLL_INVERSION.getText().toString());
        MainActivity.ROLL_RANGEVALUE = Integer.parseInt(ROLL_RANGEVALUE.getText().toString());
        MainActivity.PITCH_INVERSION = Integer.parseInt(PITCH_INVERSION.getText().toString());
        MainActivity.PITCH_RANGEVALUE = Integer.parseInt(PITCH_RANGEVALUE.getText().toString());
        MainActivity.SERVO1_TYPEMOVE = SERVO1_TYPEMOVE.getSelectedItemPosition();
        MainActivity.SERVO2_TYPEMOVE = SERVO2_TYPEMOVE.getSelectedItemPosition();
    }

void RestoreData(){


    MainActivity.typeManage = MainActivity.mSettings.getInt("typeManage", 0);
    MainActivity.StabRoll = MainActivity.mSettings.getInt("StabRoll", 0);
    MainActivity.StabPitch = MainActivity.mSettings.getInt("StabPitch", 0);
    MainActivity.StabYaw = MainActivity.mSettings.getInt("StabYaw", 0);
    MainActivity.StabAngelPitch = MainActivity.mSettings.getInt("StabAngelPitch", 0);
    MainActivity.StabAngelRoll = MainActivity.mSettings.getInt("StabAngelRoll", 0);
    MainActivity.StabKoeffManage1 = MainActivity.mSettings.getInt("StabKoeffManage1", 127);
    MainActivity.StabKoeffManage2 = MainActivity.mSettings.getInt("StabKoeffManage2", 127);
    MainActivity.StabServo1 = MainActivity.mSettings.getInt("StabServo1", 0);
    MainActivity.StabServo2 = MainActivity.mSettings.getInt("StabServo2", 0);
    MainActivity.StabKoeffCh1 = MainActivity.mSettings.getInt("StabKoeffCh1", 127);
    MainActivity.StabKoeffCh2 = MainActivity.mSettings.getInt("StabKoeffCh2", 127);
    MainActivity.pos_rev = MainActivity.mSettings.getInt("pos_rev", 0);
    MainActivity.pos_ang1 = MainActivity.mSettings.getInt("pos_ang1", 3);
    MainActivity.pos_ang2 = MainActivity.mSettings.getInt("pos_ang2", 19);
    MainActivity.StabServoMax1 = MainActivity.mSettings.getInt("StabServoMax1", 127);
    MainActivity.StabServoMax2 = MainActivity.mSettings.getInt("StabServoMax2", 127);
    MainActivity.CmdTimeout = MainActivity.mSettings.getInt("CmdTimeout", 20);
    MainActivity.math_K_angle = MainActivity.mSettings.getInt("math_K_angle", 1);
    MainActivity.math_K_bias = MainActivity.mSettings.getInt("math_K_bias", 3);
    MainActivity.math_K_measure = MainActivity.mSettings.getInt("math_K_measure", 30);
    MainActivity.math_gyroRate = MainActivity.mSettings.getInt("math_gyroRate", 131);
    MainActivity.manageExp = MainActivity.mSettings.getInt("manageExp", 50);


}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();

        text_toChange = (TextView)findViewById(R.id.toChange);
        text_toChangeIndic = (TextView)findViewById(R.id.toChangeIndic);

        sb_toCnange = (SeekBar) findViewById(R.id.seekBar);
        sb_toCnange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (Downloaded_Id == R.id.texttypeManage){typeManage.setText(progress);}
//                else if (Downloaded_Id == R.id.textCmdTimeout){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textStabRoll){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textStabPitch){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textStabYaw){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textStabAngelPitch){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textStabAngelRoll){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textStabKoeffManage1){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textStabKoeffManage2){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textStabServo1){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textStabServo2){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textStabKoeffCh1){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textStabKoeffCh2){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textpos_rev){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textpos_ang1){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textpos_ang2){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textStabServoMax1){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textStabServoMax2){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textmath_K_angle){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textmath_K_bias){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textmath_K_measure){CmdTimeout.setText(progress);}
//                else if (Downloaded_Id == R.id.textmath_gyroRate){CmdTimeout.setText(progress);}

                text_toChangeIndic.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (Downloaded_Id == R.id.texttypeManage){typeManage.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textCmdTimeout){CmdTimeout.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textStabRoll){StabRoll.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textStabPitch){StabPitch.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textStabYaw){StabYaw.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textStabAngelPitch){StabAngelPitch.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textStabAngelRoll){StabAngelRoll.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textStabKoeffManage1){StabKoeffManage1.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textStabKoeffManage2){StabKoeffManage2.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textStabServo1){StabServo1.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textStabServo2){StabServo2.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textStabKoeffCh1){StabKoeffCh1.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textStabKoeffCh2){StabKoeffCh2.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textpos_rev){pos_rev.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textpos_ang1){pos_ang1.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textpos_ang2){pos_ang2.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textStabServoMax1){StabServoMax1.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textStabServoMax2){StabServoMax2.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textmath_K_angle){math_K_angle.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textmath_K_bias){math_K_bias.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textmath_K_measure){math_K_measure.setText(""+seekBar.getProgress());}
                else if (Downloaded_Id == R.id.textmath_gyroRate){math_gyroRate.setText(""+seekBar.getProgress());}


                UploadData();
            }
        });



        typeManage = (EditText) findViewById(R.id.typeManage);
        typeManage.setText("" + MainActivity.typeManage);

        CmdTimeout = (EditText) findViewById(R.id.CmdTimeout);
        CmdTimeout.setText("" + MainActivity.CmdTimeout);
        StabRoll = (EditText) findViewById(R.id.StabRoll);
        StabRoll.setText("" + MainActivity.StabRoll);
        StabPitch = (EditText) findViewById(R.id.StabPitch);
        StabPitch.setText("" + MainActivity.StabPitch);
        StabYaw = (EditText) findViewById(R.id.StabYaw);
        StabYaw.setText("" + MainActivity.StabYaw);
        StabAngelPitch = (EditText) findViewById(R.id.StabAngelPitch);
        StabAngelPitch.setText("" + MainActivity.StabAngelPitch);
        StabAngelRoll = (EditText) findViewById(R.id.StabAngelRoll);
        StabAngelRoll.setText("" + MainActivity.StabAngelRoll);
        StabKoeffManage1 = (EditText) findViewById(R.id.StabKoeffManage1);
        StabKoeffManage1.setText("" + MainActivity.StabKoeffManage1);
        StabKoeffManage2 = (EditText) findViewById(R.id.StabKoeffManage2);
        StabKoeffManage2.setText("" + MainActivity.StabKoeffManage2);
        StabServo1 = (EditText) findViewById(R.id.StabServo1);
        StabServo1.setText("" + MainActivity.StabServo1);
        StabServo2 = (EditText) findViewById(R.id.StabServo2);
        StabServo2.setText("" + MainActivity.StabServo2);
        StabKoeffCh1 = (EditText) findViewById(R.id.StabKoeffCh1);
        StabKoeffCh1.setText("" + MainActivity.StabKoeffCh1);
        StabKoeffCh2 = (EditText) findViewById(R.id.StabKoeffCh2);
        StabKoeffCh2.setText("" + MainActivity.StabKoeffCh2);
        pos_rev = (EditText) findViewById(R.id.pos_rev);
        pos_rev.setText("" + MainActivity.pos_rev);
        pos_ang1 = (EditText) findViewById(R.id.pos_ang1);
        pos_ang1.setText("" + MainActivity.pos_ang1);
        pos_ang2 = (EditText) findViewById(R.id.pos_ang2);
        pos_ang2.setText("" + MainActivity.pos_ang2);
        StabServoMax1 = (EditText) findViewById(R.id.StabServoMax1);
        StabServoMax1.setText("" + MainActivity.StabServoMax1);
        StabServoMax2 = (EditText) findViewById(R.id.StabServoMax2);
        StabServoMax2.setText("" + MainActivity.StabServoMax2);
        math_K_angle = (EditText) findViewById(R.id.math_K_angle);
        math_K_angle.setText("" + MainActivity.math_K_angle);
        math_K_bias = (EditText) findViewById(R.id.math_K_bias);
        math_K_bias.setText("" + MainActivity.math_K_bias);
        math_K_measure = (EditText) findViewById(R.id.math_K_measure);
        math_K_measure.setText("" + MainActivity.math_K_measure);
        math_gyroRate = (EditText) findViewById(R.id.math_gyroRate);
        math_gyroRate.setText("" + MainActivity.math_gyroRate);


        manageExp = (EditText) findViewById(R.id.manageExp);
        manageExp.setText("" + MainActivity.manageExp);




        texttypeManage = (TextView) findViewById(R.id.texttypeManage);              texttypeManage.setOnClickListener(text_OnClick);
        textCmdTimeout = (TextView) findViewById(R.id.textCmdTimeout);              textCmdTimeout.setOnClickListener(text_OnClick);
        textStabRoll = (TextView) findViewById(R.id.textStabRoll);                  textStabRoll.setOnClickListener(text_OnClick);
        textStabPitch = (TextView) findViewById(R.id.textStabPitch);                textStabPitch.setOnClickListener(text_OnClick);
        textStabYaw = (TextView) findViewById(R.id.textStabYaw);                    textStabYaw.setOnClickListener(text_OnClick);
        textStabAngelPitch = (TextView) findViewById(R.id.textStabAngelPitch);      textStabAngelPitch.setOnClickListener(text_OnClick);
        textStabAngelRoll = (TextView) findViewById(R.id.textStabAngelRoll);        textStabAngelRoll.setOnClickListener(text_OnClick);
        textStabKoeffManage1 = (TextView) findViewById(R.id.textStabKoeffManage1);  textStabKoeffManage1.setOnClickListener(text_OnClick);
        textStabKoeffManage2 = (TextView) findViewById(R.id.textStabKoeffManage2);  textStabKoeffManage2.setOnClickListener(text_OnClick);
        textStabServo1 = (TextView) findViewById(R.id.textStabServo1);              textStabServo1.setOnClickListener(text_OnClick);
        textStabServo2 = (TextView) findViewById(R.id.textStabServo2);              textStabServo2.setOnClickListener(text_OnClick);
        textStabKoeffCh1 = (TextView) findViewById(R.id.textStabKoeffCh1);          textStabKoeffCh1.setOnClickListener(text_OnClick);
        textStabKoeffCh2 = (TextView) findViewById(R.id.textStabKoeffCh2);          textStabKoeffCh2.setOnClickListener(text_OnClick);
        textpos_rev = (TextView) findViewById(R.id.textpos_rev);                    textpos_rev.setOnClickListener(text_OnClick);
        textpos_ang1 = (TextView) findViewById(R.id.textpos_ang1);                  textpos_ang1.setOnClickListener(text_OnClick);
        textpos_ang2 = (TextView) findViewById(R.id.textpos_ang2);                  textpos_ang2.setOnClickListener(text_OnClick);
        textStabServoMax1 = (TextView) findViewById(R.id.textStabServoMax1);        textStabServoMax1.setOnClickListener(text_OnClick);
        textStabServoMax2 = (TextView) findViewById(R.id.textStabServoMax2);        textStabServoMax2.setOnClickListener(text_OnClick);
        textmath_K_angle = (TextView) findViewById(R.id.textmath_K_angle);          textmath_K_angle.setOnClickListener(text_OnClick);
        textmath_K_bias = (TextView) findViewById(R.id.textmath_K_bias);            textmath_K_bias.setOnClickListener(text_OnClick);
        textmath_K_measure = (TextView) findViewById(R.id.textmath_K_measure);      textmath_K_measure.setOnClickListener(text_OnClick);
        textmath_gyroRate = (TextView) findViewById(R.id.textmath_gyroRate);        textmath_gyroRate.setOnClickListener(text_OnClick);





        ROLL_INVERSION = (EditText) findViewById(R.id.ROLL_INVERSION);
        ROLL_INVERSION.setText("" + MainActivity.ROLL_INVERSION);
        ROLL_RANGEVALUE = (EditText) findViewById(R.id.ROLL_RANGEVALUE);
        ROLL_RANGEVALUE.setText("" + MainActivity.ROLL_RANGEVALUE);
        PITCH_INVERSION = (EditText) findViewById(R.id.PITCH_INVERSION);
        PITCH_INVERSION.setText("" + MainActivity.PITCH_INVERSION);
        PITCH_RANGEVALUE = (EditText) findViewById(R.id.PITCH_RANGEVALUE);
        PITCH_RANGEVALUE.setText("" + MainActivity.PITCH_RANGEVALUE);

        SERVO1_TYPEMOVE = (Spinner) findViewById(R.id.SERVO1_TYPEMOVE);
        SERVO1_TYPEMOVE.setSelection(MainActivity.SERVO1_TYPEMOVE);
        SERVO2_TYPEMOVE = (Spinner) findViewById(R.id.SERVO2_TYPEMOVE);
        SERVO2_TYPEMOVE.setSelection(MainActivity.SERVO2_TYPEMOVE);




        Button btn1 = (Button) findViewById(R.id.btnApply);
        btn1.setOnClickListener(new View.OnClickListener() {    //Send
            @Override
            public void onClick(View v) {
                Log.d("test", "Reset Joy...");

                MainActivity.typeManage = Integer.parseInt(typeManage.getText().toString());
                MainActivity.CmdTimeout = Integer.parseInt(CmdTimeout.getText().toString());
                MainActivity.StabRoll = Integer.parseInt(StabRoll.getText().toString());
                MainActivity.StabPitch = Integer.parseInt(StabPitch.getText().toString());
                MainActivity.StabYaw = Integer.parseInt(StabYaw.getText().toString());
                MainActivity.StabAngelPitch = Integer.parseInt(StabAngelPitch.getText().toString());
                MainActivity.StabAngelRoll = Integer.parseInt(StabAngelRoll.getText().toString());
                MainActivity.StabKoeffManage1 = Integer.parseInt(StabKoeffManage1.getText().toString());
                MainActivity.StabKoeffManage2 = Integer.parseInt(StabKoeffManage2.getText().toString());
                MainActivity.StabServo1 = Integer.parseInt(StabServo1.getText().toString());
                MainActivity.StabServo2 = Integer.parseInt(StabServo2.getText().toString());
                MainActivity.StabKoeffCh1 = Integer.parseInt(StabKoeffCh1.getText().toString());
                MainActivity.StabKoeffCh2 = Integer.parseInt(StabKoeffCh2.getText().toString());
                MainActivity.pos_rev = Integer.parseInt(pos_rev.getText().toString());
                MainActivity.pos_ang1 = Integer.parseInt(pos_ang1.getText().toString());
                MainActivity.pos_ang2 = Integer.parseInt(pos_ang2.getText().toString());
                MainActivity.StabServoMax1 = Integer.parseInt(StabServoMax1.getText().toString());
                MainActivity.StabServoMax2 = Integer.parseInt(StabServoMax2.getText().toString());
                MainActivity.math_K_angle = Integer.parseInt(math_K_angle.getText().toString());
                MainActivity.math_K_bias = Integer.parseInt(math_K_bias.getText().toString());
                MainActivity.math_K_measure = Integer.parseInt(math_K_measure.getText().toString());
                MainActivity.math_gyroRate = Integer.parseInt(math_gyroRate.getText().toString());
                MainActivity.manageExp = Integer.parseInt(manageExp.getText().toString());

                MainActivity.ROLL_INVERSION = Integer.parseInt(ROLL_INVERSION.getText().toString());
                MainActivity.ROLL_RANGEVALUE = Integer.parseInt(ROLL_RANGEVALUE.getText().toString());
                MainActivity.PITCH_INVERSION = Integer.parseInt(PITCH_INVERSION.getText().toString());
                MainActivity.PITCH_RANGEVALUE = Integer.parseInt(PITCH_RANGEVALUE.getText().toString());
                MainActivity.SERVO1_TYPEMOVE = SERVO1_TYPEMOVE.getSelectedItemPosition();
                MainActivity.SERVO2_TYPEMOVE = SERVO2_TYPEMOVE.getSelectedItemPosition();




                //MainActivity.SaveData(1);

                MainActivity.mSettings.edit().putInt("typeManage", MainActivity.typeManage).commit();
                MainActivity.mSettings.edit().putInt("CmdTimeout", MainActivity.CmdTimeout).commit();
                MainActivity.mSettings.edit().putInt("StabRoll", MainActivity.StabRoll).commit();
                MainActivity.mSettings.edit().putInt("StabPitch", MainActivity.StabPitch).commit();
                MainActivity.mSettings.edit().putInt("StabYaw", MainActivity.StabYaw).commit();
                MainActivity.mSettings.edit().putInt("StabAngelPitch", MainActivity.StabAngelPitch).commit();
                MainActivity.mSettings.edit().putInt("StabAngelRoll", MainActivity.StabAngelRoll).commit();
                MainActivity.mSettings.edit().putInt("StabKoeffManage1", MainActivity.StabKoeffManage1).commit();
                MainActivity.mSettings.edit().putInt("StabKoeffManage2", MainActivity.StabKoeffManage2).commit();
                MainActivity.mSettings.edit().putInt("StabServo1", MainActivity.StabServo1).commit();
                MainActivity.mSettings.edit().putInt("StabServo2", MainActivity.StabServo2).commit();
                MainActivity.mSettings.edit().putInt("StabKoeffCh1", MainActivity.StabKoeffCh1).commit();
                MainActivity.mSettings.edit().putInt("StabKoeffCh2", MainActivity.StabKoeffCh2).commit();
                MainActivity.mSettings.edit().putInt("pos_rev", MainActivity.pos_rev).commit();
                MainActivity.mSettings.edit().putInt("pos_ang1", MainActivity.pos_ang1).commit();
                MainActivity.mSettings.edit().putInt("pos_ang2", MainActivity.pos_ang2).commit();
                MainActivity.mSettings.edit().putInt("StabServoMax1", MainActivity.StabServoMax1).commit();
                MainActivity.mSettings.edit().putInt("StabServoMax2", MainActivity.StabServoMax2).commit();
                MainActivity.mSettings.edit().putInt("math_K_angle", MainActivity.math_K_angle).commit();
                MainActivity.mSettings.edit().putInt("math_K_bias", MainActivity.math_K_bias).commit();
                MainActivity.mSettings.edit().putInt("math_K_measure", MainActivity.math_K_measure).commit();
                MainActivity.mSettings.edit().putInt("math_gyroRate", MainActivity.math_gyroRate).commit();
                MainActivity.mSettings.edit().putInt("manageExp", MainActivity.manageExp).commit();

                MainActivity.mSettings.edit().putInt("ROLL_INVERSION", MainActivity.ROLL_INVERSION).commit();
                MainActivity.mSettings.edit().putInt("ROLL_RANGEVALUE", MainActivity.ROLL_RANGEVALUE).commit();
                MainActivity.mSettings.edit().putInt("PITCH_INVERSION", MainActivity.PITCH_INVERSION).commit();
                MainActivity.mSettings.edit().putInt("PITCH_RANGEVALUE", MainActivity.PITCH_RANGEVALUE).commit();
                MainActivity.mSettings.edit().putInt("SERVO1_TYPEMOVE", MainActivity.SERVO1_TYPEMOVE).commit();
                MainActivity.mSettings.edit().putInt("SERVO2_TYPEMOVE", MainActivity.SERVO2_TYPEMOVE).commit();


            }
        });




        sb_Motor = (SeekBar) findViewById(R.id.sb_Motor);
        sb_Motor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MotorValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestoreData();
            }
        });

    }
}