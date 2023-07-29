package com.example.appflycontrol;

import static com.MAVLink.enums.MAV_STATE.MAV_STATE_ACTIVE;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.Description;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.Units;
import com.MAVLink.Parser;
import com.MAVLink.common.msg_attitude;
import com.MAVLink.common.msg_attitude_target;
import com.MAVLink.common.msg_battery_status;
import com.MAVLink.common.msg_command_ack;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.common.msg_param_request_list;
import com.MAVLink.common.msg_param_request_read;
import com.MAVLink.common.msg_param_set;
import com.MAVLink.common.msg_param_value;
import com.MAVLink.common.msg_rc_channels_override;
import com.MAVLink.common.msg_sys_status;
import com.MAVLink.common.msg_system_time;
import com.MAVLink.enums.MAV_CMD;
import com.MAVLink.enums.MAV_PARAM_TYPE;
import com.MAVLink.minimal.msg_heartbeat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.io.OutputStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.widget.ArrayAdapter;

import android.bluetooth.BluetoothSocket;

//import com.hoho.android.usbserial.driver.UsbSerialDriver;
//import com.hoho.android.usbserial.driver.UsbSerialProber;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "test";//TcpClient.class.getSimpleName();

    public enum TypeConnection {VCP, WIFI, BT}

    public TypeConnection typeConnection = TypeConnection.VCP;

    public static SharedPreferences mSettings;

    public static ParamList mParams;

    UdpClient udpCliect;
///// BT
//
//    private static final int REQUEST_ENABLE_BT = 1;
//    private BluetoothAdapter btAdapter = null;
//    private BluetoothSocket btSocket = null;
//    private OutputStream outStream = null;
//
//
//    // SPP UUID сервиса
//    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//
//    // MAC-адрес Bluetooth модуля
//    private static String address = "98:D3:31:60:08:14";
//
//
//
//    int light_sw = 0;   // For Car light on/off
//    int NeedToSendBT_fl = 0;
//    /// BT end

    static TcpClient mTcpClient;
    static UdpClient mUdpClient;
    static TextView tv_position, tv_status, tv_voltage, tv_temperature, tv_press;
    static Button btnExit, btnState, btnTemp, btnSettings;


    public int time_t = 0;

    public static String IP_Adress = "127.0.0.1";
    public static int IP_Port = 14551;

    public static int typeManage = 0;
    public static int StabRoll = 0;
    public static int StabPitch = 0;
    public static int StabYaw = 0;
    public static int StabAngelPitch = 0;
    public static int StabAngelRoll = 0;
    public static int StabKoeffManage1 = 127;
    public static int StabKoeffManage2 = 127;
    public static int StabServo1 = 0;
    public static int StabServo2 = 0;
    public static int StabKoeffCh1 = 127;
    public static int StabKoeffCh2 = 127;
    public static int pos_rev = 0;
    public static int pos_ang1 = 3;
    public static int pos_ang2 = 19;
    public static int StabServoMax1 = 127;
    public static int StabServoMax2 = 127;
    public static int CmdTimeout = 20;
    public static int math_K_angle = 1;
    public static int math_K_bias = 3;
    public static int math_K_measure = 30;
    public static int math_gyroRate = 131;
    public static int manageExp = 50;

    public static int ROLL_INVERSION = 0;
    public static int ROLL_RANGEVALUE = 254;
    public static int PITCH_INVERSION = 0;
    public static int PITCH_RANGEVALUE = 254;
    public static int SERVO1_TYPEMOVE = 0;
    public static int SERVO2_TYPEMOVE = 0;

    static Joystick joystick_L, joystick_R;

    static ImageView iw1, iw2, iw3, iw4, iw5;
    static Spinner spTypeDevice, spTypeConnection;
    static TextView etMulti;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;


    char[][] rxMSG;

    int sb_yaw_pos = 0;
    static int arm_disarm = 1;

    static MAVLinkPacket headerPacket;
    static byte mavlink_cnt;
    static int timer_rx_HB_system = 0;
    static int timer_rx_HB_drone = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();

        mSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        IP_Adress = mSettings.getString("IP_Adress", "127.0.0.1");
        IP_Port = mSettings.getInt("IP_Port", 1234);

        headerPacket = new MAVLinkPacket(0, true);
        headerPacket.sysid = 1;
        headerPacket.compid = 0;

        mParams = new ParamList();


        new ConnectUdpTask().execute("");


        if (mTimer != null) mTimer.cancel();
        // re-schedule timer here otherwise, IllegalStateException of "TimerTask is scheduled already"  will be thrown
        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 1000, 100);// delay 1000ms, repeat in 5000ms

        joystick_L = (Joystick) findViewById(R.id.joystick_L);
        joystick_R = (Joystick) findViewById(R.id.joystick_R);

        joystick_L.n_x = joystick_L.x_Width / 2;
        joystick_L.n_y = joystick_L.y_Height - joystick_L.sqrSize;
        joystick_L.y_ret = false;
        joystick_L.y = joystick_L.y_Height - joystick_L.sqrSize;


        btnExit = (Button) findViewById(R.id.btnExit);
        btnState = (Button) findViewById(R.id.btnState);
        btnTemp = (Button) findViewById(R.id.btnTemp);
        btnSettings = (Button) findViewById(R.id.btnSettings);

        tv_position = (TextView) findViewById(R.id.tv_position);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_voltage = (TextView) findViewById(R.id.tv_voltage);
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_press = (TextView) findViewById(R.id.tv_press);

        tv_status.setText("Status: Created");
        tv_position.setText("x=0,y=0,z=0");

        btnState.setOnClickListener(new View.OnClickListener() {    //btn STATE
            @Override
            public void onClick(View v) {



                msg_command_long cmd_long = new msg_command_long(headerPacket);
                cmd_long.command = MAV_CMD.MAV_CMD_COMPONENT_ARM_DISARM;

                if (arm_disarm>1) arm_disarm=0;
                Log.d(TAG, "arm: " + arm_disarm +".");
                cmd_long.param1 = arm_disarm;
                mav_send_pack(cmd_long.pack());



            }
        });

        btnTemp.setOnClickListener(new View.OnClickListener() {    //Temp
            @Override
            public void onClick(View v) {


                msg_command_long cmd_long = new msg_command_long(headerPacket);
                cmd_long.command = MAV_CMD.MAV_CMD_COMPONENT_ARM_DISARM;
                cmd_long.param1 = 0;
                mav_send_pack(cmd_long.pack());

                //mav_start_calibrate(true, false);
                //mav_param_request_list();

            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {    //Settings
            @Override
            public void onClick(View v) {
                //tv1.setText("Send...");
                Log.d("test", "Go to Second Activity...");
                //sends the message to the server

                //Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);//params_activity.class);//MenuActivity.class);
                //Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {    //Send
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        iw1 = (ImageView) findViewById(R.id.icon);
        iw2 = (ImageView) findViewById(R.id.icon2);
        iw3 = (ImageView) findViewById(R.id.icon3);
        iw4 = (ImageView) findViewById(R.id.icon4);
        iw5 = (ImageView) findViewById(R.id.icon5);

        iw2.setTop(-1040);

        iw3.setRotation(0);

        typeManage = mSettings.getInt("typeManage", 0);
        StabRoll = mSettings.getInt("StabRoll", 0);
        StabPitch = mSettings.getInt("StabPitch", 0);
        StabYaw = mSettings.getInt("StabYaw", 0);
        StabAngelPitch = mSettings.getInt("StabAngelPitch", 0);
        StabAngelRoll = mSettings.getInt("StabAngelRoll", 0);
        StabKoeffManage1 = mSettings.getInt("StabKoeffManage1", 127);
        StabKoeffManage2 = mSettings.getInt("StabKoeffManage2", 127);
        StabServo1 = mSettings.getInt("StabServo1", 0);
        StabServo2 = mSettings.getInt("StabServo2", 0);
        StabKoeffCh1 = mSettings.getInt("StabKoeffCh1", 127);
        StabKoeffCh2 = mSettings.getInt("StabKoeffCh2", 127);
        pos_rev = mSettings.getInt("pos_rev", 0);
        pos_ang1 = mSettings.getInt("pos_ang1", 3);
        pos_ang2 = mSettings.getInt("pos_ang2", 19);
        StabServoMax1 = mSettings.getInt("StabServoMax1", 127);
        StabServoMax2 = mSettings.getInt("StabServoMax2", 127);
        CmdTimeout = mSettings.getInt("CmdTimeout", 20);
        math_K_angle = mSettings.getInt("math_K_angle", 1);
        math_K_bias = mSettings.getInt("math_K_bias", 3);
        math_K_measure = mSettings.getInt("math_K_measure", 30);
        math_gyroRate = mSettings.getInt("math_gyroRate", 131);
        manageExp = mSettings.getInt("manageExp", 50);

        ROLL_INVERSION = mSettings.getInt("ROLL_INVERSION", 0);
        ROLL_RANGEVALUE = mSettings.getInt("ROLL_RANGEVALUE", 0);
        PITCH_INVERSION = mSettings.getInt("PITCH_INVERSION", 0);
        PITCH_RANGEVALUE = mSettings.getInt("PITCH_RANGEVALUE", 0);
        SERVO1_TYPEMOVE = mSettings.getInt("SERVO1_TYPEMOVE", 0);
        SERVO2_TYPEMOVE = mSettings.getInt("SERVO2_TYPEMOVE", 0);





        rxMSG = new char[5][200];

//        sb_yaw = (SeekBar) findViewById(R.id.sb_yaw);
//        sb_yaw.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                sb_yaw_pos = 100 - progress;
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                sb_yaw_pos = 0;
//                seekBar.setProgress(100);
//            }
//        });


    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


    public class ConnectUdpTask extends AsyncTask<String, String, UdpClient> {

        @Override
        protected UdpClient doInBackground(String... message) {

            //we create a UdpClient object
            mUdpClient = new UdpClient(new UdpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                    //Log.d(TAG, "RxData!");
                }
            }, IP_Adress, IP_Port);
            //}, IP_Adress, IP_Port);
            mUdpClient.run();

            return null;
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            //Log.d(TAG, "udp response " + values[0]);
            //process server response here....


            Parser mav_parser = new Parser();

            int cnt = 0;
            for (cnt = 0; cnt < mUdpClient.buffer_len; cnt++) {

                MAVLinkPacket packet = mav_parser.mavlink_parse_char(mUdpClient.buffer[cnt]);
                if (packet != null) {

                    switch (packet.msgid) {
                        case msg_heartbeat.MAVLINK_MSG_ID_HEARTBEAT:
                            msg_heartbeat p_hb = new msg_heartbeat(packet);
                            //Log.d(TAG, "rx HB");
                            if (p_hb.sysid == 100) timer_rx_HB_system = 50;
                            else {
                                timer_rx_HB_drone = 50;
                                int hb_status = p_hb.system_status;
                                if (hb_status == MAV_STATE_ACTIVE) {
                                    btnState.setText("ARMed");
                                    arm_disarm = 0;
                                } else {
                                    btnState.setText("Relax");
                                    arm_disarm = 1;
                                }
                            }
                            //Log.d(TAG, "rx hb id  " + packet.msgid);
                            break;
                        case msg_command_ack.MAVLINK_MSG_ID_COMMAND_ACK:
                            msg_command_ack cmd_ack = new msg_command_ack(packet);
                            int ack_result = cmd_ack.result;
                            int ack_progress = cmd_ack.progress;
                            Log.d(TAG, "Ack ( res:" + ack_result + " progress: " + ack_progress + "%)");
                            if (ack_result == 0) {
                                if (arm_disarm == 1){btnState.setText("ARMed."); arm_disarm = 0;}
                                else {btnState.setText("Relax."); arm_disarm = 1;}
                                //arm_disarm++;
                            }
                            break;
                        case msg_sys_status.MAVLINK_MSG_ID_SYS_STATUS:
                            msg_sys_status p_sys_status = new msg_sys_status(packet);
                            int st_bat = p_sys_status.battery_remaining;
                            int st_voltage = p_sys_status.voltage_battery;
                            //Log.d(TAG, "bat remaining  " + st_bat);
                            tv_voltage.setText("V= " + st_voltage + " mV (" + st_bat + "%)");
                            //Log.d(TAG, "rx sys_status id  " + p_sys_status.msgid);
                            break;
                        case msg_battery_status.MAVLINK_MSG_ID_BATTERY_STATUS:
                            msg_battery_status rx_pack = new msg_battery_status(packet);
                            //Log.d(TAG, "rx bat  " + rx_pack.battery_remaining);
                            int percent = rx_pack.battery_remaining;
                            int voltage = rx_pack.voltages[0];
                            tv_voltage.setText("V= " + voltage + " mV (" + percent + "%)");
                            break;
                        case msg_param_value.MAVLINK_MSG_ID_PARAM_VALUE:
                            msg_param_value rx_param_value = new msg_param_value(packet);
                            //String rx_id = String.valueOf(rx_param_value.param_id);
                            String rx_id = new String(rx_param_value.param_id, StandardCharsets.UTF_8);
                            rx_id = rx_id.substring(0, rx_id.indexOf('\0'));
                            Log.d(TAG, "index  " + rx_param_value.param_index + " cnt  " + rx_param_value.param_count +
                                    " id " + rx_id + " value " + rx_param_value.param_value + " type " + rx_param_value.param_type);
                            ParamList.setParam(rx_param_value.param_index, rx_param_value.param_count, rx_id, rx_param_value.param_value, rx_param_value.param_type);
                            float progress = ParamList.getProgressParam();
                            Log.d(TAG, "progress  " + progress + " .  ");

                            break;
                        case msg_attitude.MAVLINK_MSG_ID_ATTITUDE:
                            //Log.d(TAG, "rx msg MAVLINK_MSG_ID_ATTITUDE  " + packet.msgid);

                            msg_attitude att_pack = new msg_attitude(packet);
//                            tvTemp.setText("t1= "+(float)temp1/100+" t2= " + (float)temp2/100);
//                            tvPress.setText("Press = " + press + " (" + (int)(press/133.32) + " mmHg)");

                            float pitch = (float) ((180 * att_pack.pitch) / Math.PI);//att_pack.pitch;
                            float roll = (float) ((180 * att_pack.roll) /  Math.PI);
                            float yaw = (float) ((180 * att_pack.yaw) /  Math.PI);//att_pack.yaw;

                            pitch = Math.round(pitch*10)/10;
                            roll = Math.round(roll*10)/10;
                            yaw = Math.round(yaw*10)/10;

                            tv_position.setText("x=" + pitch + ",y=" + roll + ",z=" + yaw + " ");

                            Integer imgPitch = Math.round(pitch);

                            iw3.setRotation(roll);
                            iw5.setRotation(yaw);

                            //iw2.setTop(-1570 + (((int) pitch) * 225 / 45));
                            iw2.setTop(-1000+(((int)pitch)*160/45));
                            //iw2.setTop(-1040+(((int)pitch)*160/45));
                            break;
                        default:
                            //Log.d(TAG, "rx msg id  " + packet.msgid);
                            break;
                    }
                }
                //if (mav_parser.mavlink_parse_char(mUdpClient.buffer[cnt]) != null){Log.d(TAG, "rx msg id  " + mav_parser.m.msgid);}
            }

        }

    }


    public class ConnectTcpTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                    //Log.d("test", "RxData!");
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            //Log.d("test", "response " + values[0]);
            //process server response here....

        }

    }

    static public void mav_send_pack(MAVLinkPacket pack) {
        pack.seq = mavlink_cnt++;
        //pack.isMavlink2 = false;
        byte[] buffer = pack.encodePacket();
        //Log.d(TAG, "len len " + pack.len + " payload size " + pack.payload.size() + " send " + buffer.length);
        mUdpClient.sendMessageByte(buffer);
    }

    public void mav_send_HB() {
        msg_heartbeat init_HB = new msg_heartbeat(headerPacket);
        init_HB.custom_mode = 0;
        init_HB.type = 0;
        init_HB.autopilot = 0;
        init_HB.base_mode = 0;
        init_HB.system_status = 0;
        init_HB.mavlink_version = 2;

        mav_send_pack(init_HB.pack());
    }

    public void mav_send_rc_channels(){
 //       if (mUdpClient != null) {
            msg_rc_channels_override msg_rc = new msg_rc_channels_override(headerPacket);
            Integer tYaw = (int) (1500 + joystick_L.xPosition() * 500 / 50);//*PITCH_RANGEVALUE/50);
            Integer tThrottle = (int) (1500 + joystick_L.yPosition() * 500 / 50);//*PITCH_RANGEVALUE/50);
            Integer tRoll = (int) (1500 + joystick_R.xPosition() * 500 / 50);//*PITCH_RANGEVALUE/50);
            Integer tPitch = (int) (1500 + joystick_R.yPosition() * 500 / 50);//*PITCH_RANGEVALUE/50);
            Integer tMode = 1;
            //Log.d(TAG, "Send real x= " + tPitch + " y = " + tRoll);
            //Log.d(TAG, "Send real x= " + tPitch + " y = " + tRoll + " tThrottle = " + tThrottle + " tYaw = " + tYaw);

            msg_rc.chan1_raw = tThrottle.shortValue();
            msg_rc.chan2_raw = tYaw.shortValue();
            msg_rc.chan3_raw = tPitch.shortValue();
            msg_rc.chan4_raw = tRoll.shortValue();
            msg_rc.chan5_raw = tMode.shortValue();

//            MAVLinkPacket packet_RC = msg_rc.pack();
//            packet_RC.seq = mavlink_cnt++;
//            byte[] buffer = packet_RC.encodePacket();
//            mUdpClient.sendMessageByte(buffer);

            mav_send_pack(msg_rc.pack());
 //       }
    }

    public int init_status = 0;

    public static void mav_param_set(String key, float value, short type) {
        msg_param_set set_param = new msg_param_set(headerPacket);
        set_param.param_id = key.getBytes();
        set_param.param_type = type;
        set_param.param_value = value;

        mav_send_pack(set_param.pack());
    }

    public static void mav_param_request_read(byte[] id, short index) {
        msg_param_request_read request_read = new msg_param_request_read(headerPacket);
        request_read.target_system = 1;
        request_read.target_component = 1;
        request_read.param_index = index;
        request_read.param_id = id;

        mav_send_pack(request_read.pack());
    }

    public static void mav_param_request_list() {
        msg_param_request_list request_list = new msg_param_request_list(headerPacket);
        request_list.target_system = 1;
        request_list.target_component = 1;

        ParamList.clearParam();
        mav_send_pack(request_list.pack());
    }

    public static int mav_calibrate_rx_responce = 0;
    public static void mav_start_calibrate(boolean gyro, boolean acc) {

        msg_command_long cmd_long = new msg_command_long(headerPacket);

//  1      | 1: gyro calibration, 3: gyro temperature calibration
//  2      | 1: magnetometer calibration
//  3      | 1: ground pressure calibration
//  4      | 1: radio RC calibration, 2: RC trim calibration
//  5      | 1: accelerometer calibration, 2: board level calibration, 3: accelerometer temperature calibration, 4: simple accelerometer calibration
//  6      | 1: APM: compass/motor interference calibration (PX4: airspeed calibration, deprecated), 2: airspeed calibration
//  7      | 1: ESC calibration, 3: barometer temperature calibration|

        cmd_long.command = MAV_CMD.MAV_CMD_PREFLIGHT_CALIBRATION;
        if (gyro) cmd_long.param1 = 1;    //gyro calibration
        if (acc) cmd_long.param5 = 1;    //accelerometer calibration

        mav_send_pack(cmd_long.pack());
        mav_calibrate_rx_responce = 0;
    }

    public void init_mav() {
        //param
        //msg_param_request_read
    }

static int timer_send_HB = 0;
    class MyTimerTask extends TimerTask {                                                           //MyTimerTask
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mav_send_rc_channels();
                    if (++timer_send_HB>=10){mav_send_HB();timer_send_HB=0;}

//                    if (mTcpClient.Status > 0) tv_status.setText("Status: Connected");
//                        //else if (fragment.connected == TerminalFragment.Connected.True) tv4.setText("Status: VCP Connected");
//                    else tv_status.setText("Status: disConnected");

                    if (timer_rx_HB_drone>0){timer_rx_HB_drone--; timer_rx_HB_system--; tv_status.setText("Status: Connected");}
                    else if (timer_rx_HB_system>0){timer_rx_HB_system--; tv_status.setText("Status: Connecting...");}
                    else {tv_status.setText("Status: DisConnected");}

                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "...In onPause()...");

    }


}

