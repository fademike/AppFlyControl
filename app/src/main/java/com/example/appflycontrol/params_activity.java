package com.example.appflycontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;

import com.MAVLink.enums.MAV_PARAM_TYPE;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class params_activity  extends Activity {


    private static final String TAG = "test"; //Log.d("catalog",

    private ListView mLV1;

    private ArrayList<Param> fList = new ArrayList<Param>();
    params_activity.ParamAdapter adapter;

    private TextView textView3;

    public int wantToSetParam_cntWait = 0;  // cnt wait need to set patameter and it is flag
    public Param wantToSetParam = new Param(0,0,"",0,0);

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.params_frame);

        mLV1 = (ListView) findViewById(R.id.listParams);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setText("List (0/0):");

        int st = MainActivity.mParams.status;   // get status of load
        int cntPar = MainActivity.mParams.cntParam; // cnt all parameters

        if (st == 1) {  // if full load
            int i=0;
            for (i=0;i<cntPar;i++) {    // add all parameters to list
                Param param = new Param(0+0,cntPar,MainActivity.mParams.getParamName(i+0),MainActivity.mParams.getParamValue(i+0),MainActivity.mParams.getParamType(i+0));
                fList.add(param);
            }
        }

        mLV1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new params_activity.ParamAdapter(this, fList);
        mLV1.setAdapter(adapter);

        Button back = (Button)findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {    //Back btn
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mLV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Param param = fList.get(position);

                String param_name = param.getName();
                //float value = param.t_value;
                Log.d(TAG, "was clicked " + param_name);

                LayoutInflater li = LayoutInflater.from(params_activity.this);
                View promptsView = li.inflate(R.layout.param_edit_value, null);

                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(params_activity.this);

                mDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
                final TextView tv = (TextView) promptsView.findViewById(R.id.tv);
                tv.setText("set value param: " + param_name);

                if (param.t_type == MAV_PARAM_TYPE.MAV_PARAM_TYPE_INT8 ) userInput.setText(String.valueOf((int)param.t_value));
                else if (param.t_type == MAV_PARAM_TYPE.MAV_PARAM_TYPE_REAL32 ) userInput.setText(String.valueOf((float)param.t_value));
                else  userInput.setText(String.valueOf(param.t_value));

                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Send Value",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        try {
                                            wantToSetParam_cntWait = 10;
                                            wantToSetParam.t_name = param_name;//.substring(0, param_name.length());    //FIXME
                                            wantToSetParam.t_value = Float.valueOf(userInput.getText().toString());
                                            wantToSetParam.t_type = param.t_type;
                                            wantToSetParam.t_index = param.t_index;

                                            Log.d(TAG, "set new: " + param.t_index + " value: " + wantToSetParam.t_value);
                                            MainActivity.mav_param_set(wantToSetParam.t_name, wantToSetParam.t_value, (short)wantToSetParam.t_type);
                                            MainActivity.mParams.deleteParam(wantToSetParam.t_index);
//                                            MainActivity.mav_param_set(param_name, Float.valueOf(userInput.getText().toString()), (short) param.t_type);
//                                            MainActivity.mParams.deleteParam(param.t_index);

                                            //MainActivity.mav_param_request_read(param_name.getBytes(), (short)1);
                                        }
                                        catch (Exception e){
                                            dialog.cancel();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();
            }
        });


        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {    //Update btn
            @Override
            public void onClick(View v) {
                fList = new ArrayList<Param>();     //fixme
                mLV1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                adapter = new params_activity.ParamAdapter(params_activity.this, fList);
                mLV1.setAdapter(adapter);
                MainActivity.mav_param_request_list();
            }

        });


        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    while(true){
                        TimeUnit.MILLISECONDS.sleep(500);
                        runOnUiThread(runn1);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        //MainActivity.mav_param_request_list();
        Log.d(TAG, "started");
    }

    Runnable runn1 = new Runnable() {
        public void run() {
            update_list();
        }
    };

    static int timeToReload = 0;

    static int last_upCnt = -1;
    static float last_progress = -1;
    public void update_list(){
        int st = MainActivity.mParams.status;
        int cntPar = MainActivity.mParams.cntParam;
        float progress = MainActivity.mParams.getProgressParam();
        int upCnt = MainActivity.mParams.getUpdateCnt();

        //Log.d(TAG, "st " + st + " cntPar " + cntPar + " progress " + progress + " upCnt " + upCnt + ".");

        if (wantToSetParam_cntWait>0){
            wantToSetParam_cntWait--;
            if (MainActivity.mParams.getParamValue(wantToSetParam.t_index) != wantToSetParam.t_value) {
                if (wantToSetParam_cntWait == 0){
                    Toast toast = Toast.makeText(getApplicationContext(), "Failed set value", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    MainActivity.mav_param_set(wantToSetParam.t_name, wantToSetParam.t_value, (short) wantToSetParam.t_type);
                    MainActivity.mParams.deleteParam(wantToSetParam.t_index);
                }
            }
        }

        textView3.setText("List( load "+ Math.round(progress*100) +"%)");
        if ((st == 1) && (last_upCnt != upCnt) && (wantToSetParam_cntWait == 0)) {   // if full load and new update
            timeToReload=0;
            last_upCnt = upCnt;
            int i=0;
            Log.d(TAG, "Update List!!!");
            mLV1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);                                  //new
            adapter = new params_activity.ParamAdapter(params_activity.this, fList);    //new
            mLV1.setAdapter(adapter);                                                           //new
            fList = new ArrayList<Param>();
            for (i=0; i<cntPar; i++) {
                Param param = new Param(0+i,cntPar,MainActivity.mParams.getParamName(i+0),MainActivity.mParams.getParamValue(i+0),MainActivity.mParams.getParamType(i+0));
                fList.add(param);
            }
            mLV1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            adapter = new params_activity.ParamAdapter(this, fList);
            mLV1.setAdapter(adapter);

//            Intent intent = getIntent();
//            finish();
//            startActivity(intent);
        }
        else if (last_progress == progress) {
            int need2get = MainActivity.mParams.need2getParam();
            if (need2get>0) {
                MainActivity.mav_param_request_read("".getBytes(), (short) need2get);
                Log.d(TAG, "Send to get param " + need2get +  ".");
            }
        }
        if ((st == 0) && (cntPar == 0)){
            if (timeToReload++>=5){
                timeToReload=0;
                MainActivity.mav_param_request_list();
                Log.d(TAG, "mav_param_request_list");
            }
        }
        last_progress = progress;
    }

    protected void onResume() {
        super.onResume();
        //handler.postDelayed(task, 100);
    }

    private class ParamAdapter extends ArrayAdapter<Param> {

        private final Context context;

        public ParamAdapter(Context context, ArrayList<Param> tParam) {
            super(context, R.layout.param_item, tParam);
            this.context = context;
            //this.mFiles = tParam;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Param param = getItem(position);

            View rowView = inflater.inflate(R.layout.param_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.textView0);
            textView.setText(param.getName());

            TextView textView2 = (TextView) rowView.findViewById(R.id.textnum);
            textView2.setText(param.t_index + "/" + param.t_all);

            TextView textValue = (TextView) rowView.findViewById(R.id.textValue);
            if (param.t_type == MAV_PARAM_TYPE.MAV_PARAM_TYPE_INT8 ) textValue.setText(String.valueOf((int)param.t_value));
            else if (param.t_type == MAV_PARAM_TYPE.MAV_PARAM_TYPE_REAL32 ) textValue.setText(String.valueOf((float)param.t_value));
            else  textValue.setText(String.valueOf(param.t_value));

            TextView textType = (TextView) rowView.findViewById(R.id.textView5);
            textType.setText(String.valueOf(param.t_type));

            return rowView;
        }

    }






}
