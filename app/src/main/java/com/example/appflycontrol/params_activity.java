package com.example.appflycontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.params_frame);

        mLV1 = (ListView) findViewById(R.id.listParams);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setText("List (0/0):");

        int st = MainActivity.mParams.status;
        int cntPar = MainActivity.mParams.cntParam;

        if (st == 1) {
            int i=0;
            for (i=0;i<cntPar;i++) {
                Param param = new Param(1+i,cntPar,MainActivity.mParams.getParamName(i+1),MainActivity.mParams.getParamValue(i+1),MainActivity.mParams.getParamType(i+1));
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
                                            MainActivity.mav_param_set(param_name, Float.valueOf(userInput.getText().toString()), (short) param.t_type);
                                            MainActivity.mParams.deleteParam(param.t_index);
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
                MainActivity.mav_param_request_list();
            }

        });


        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    while(true){
                        TimeUnit.SECONDS.sleep(1);
                        runOnUiThread(runn1);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        MainActivity.mav_param_request_list();
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

        textView3.setText("List( load "+ progress*100 +"%)");
        if ((st == 1) && (last_upCnt != upCnt)) {
            timeToReload=0;
            last_upCnt = upCnt;
            int i=0;
            Log.d(TAG, "Update List!!!");
            fList = new ArrayList<Param>();
            for (i=0; i<cntPar; i++) {

                Param param = new Param(1+i,cntPar,MainActivity.mParams.getParamName(i+1),MainActivity.mParams.getParamValue(i+1),MainActivity.mParams.getParamType(i+1));
                fList.add(param);
            }

            mLV1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            adapter = new params_activity.ParamAdapter(this, fList);
            mLV1.setAdapter(adapter);

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
