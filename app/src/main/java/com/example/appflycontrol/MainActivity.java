package com.example.appflycontrol;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Parser;
import com.MAVLink.common.msg_attitude;
import com.MAVLink.common.msg_attitude_target;
import com.MAVLink.common.msg_battery_status;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.common.msg_rc_channels_override;
import com.MAVLink.common.msg_sys_status;
import com.MAVLink.common.msg_system_time;
import com.MAVLink.enums.MAV_CMD;
import com.MAVLink.minimal.msg_heartbeat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
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
import android.graphics.drawable.Drawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
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

    public enum TypeConnection { VCP, WIFI, BT }
    public TypeConnection typeConnection = TypeConnection.VCP;

    public static SharedPreferences mSettings;


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

    TcpClient mTcpClient;
    UdpClient mUdpClient;
    static TextView tv1, tv2, tv3, tv4, tv5;
    Button btn1, btn2, btn3, btnSettings, btnLight, btnApplyDev, ConnectBT, btnExit, btnPowerOff, btnTemp;

    Switch sw1, sw2, switchTRIM;

    SeekBar sb_yaw;

    public int time_t=0;

    public static int typeManage=0;
    public static int StabRoll=0;
    public static int StabPitch=0;
    public static int StabYaw=0;
    public static int StabAngelPitch=0;
    public static int StabAngelRoll=0;
    public static int StabKoeffManage1=127;
    public static int StabKoeffManage2=127;
    public static int StabServo1=0;
    public static int StabServo2=0;
    public static int StabKoeffCh1=127;
    public static int StabKoeffCh2=127;
    public static int pos_rev=0;
    public static int pos_ang1=3;
    public static int pos_ang2=19;
    public static int StabServoMax1=127;
    public static int StabServoMax2=127;
    public static int CmdTimeout=20;
    public static int math_K_angle=1;
    public static int math_K_bias=3;
    public static int math_K_measure=30;
    public static int math_gyroRate=131;
    public static int manageExp=50;

    public static int ROLL_INVERSION=0;
    public static int ROLL_RANGEVALUE=254;
    public static int PITCH_INVERSION=0;
    public static int PITCH_RANGEVALUE=254;
    public static int SERVO1_TYPEMOVE=0;
    public static int SERVO2_TYPEMOVE=0;

    TextView tvVoltage, tvTemp, tvPress;

    static Element e1, e2;

    static ImageView iw1, iw2, iw3, iw4, iw5;
    Spinner spTypeDevice, spTypeConnection;
    TextView etMulti;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;

//class newData{
//    boolean update;
//    float pitch;
//    float roll;
//    float yaw;
//    float throttle;
//
//
//};

//    static newData rxNewData;

    char [][] rxMSG;

    int sb_yaw_pos = 0;

    MAVLinkPacket headerPacket;
    byte mavlink_cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();

        headerPacket = new MAVLinkPacket(0, true);
        headerPacket.sysid = 1;
        headerPacket.compid = 0;

        rxNewData = new newData();

        new ConnectUdpTask().execute("");

        mSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        if (mTimer != null) mTimer.cancel();
        // re-schedule timer here otherwise, IllegalStateException of "TimerTask is scheduled already"  will be thrown
        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 1000, 1000);// delay 1000ms, repeat in 5000ms

        e1 = (Element) findViewById(R.id.element1);
        e2 = (Element) findViewById(R.id.element2);

        e1.n_x=e1.x_Width/2;
        e1.n_y=e1.y_Height-e1.sqrSize;
        e1.y_ret = false;
        e1.y = e1.y_Height-e1.sqrSize;


        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setText("Connect");
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setText("Disconnect");
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setText("Reset");
        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnLight = (Button) findViewById(R.id.btnLight);
        btnApplyDev = (Button) findViewById(R.id.btnApplyDev);
        ConnectBT = (Button) findViewById(R.id.ConnectBT);
        btnExit = (Button) findViewById(R.id.btnExit);
        btnPowerOff = (Button) findViewById(R.id.btnPowerOff);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setText("...");
        tv2 = (TextView) findViewById(R.id.tv2);
        tv2.setText("...");
        tv3 = (TextView) findViewById(R.id.tv3);
        tv3.setText("...");
        tv4 = (TextView) findViewById(R.id.textView4);
        tv4.setText("Status: Created");
        tv5 = (TextView) findViewById(R.id.tv5);
        tv5.setText("x=0,y=0,z=0");
        spTypeDevice = (Spinner) findViewById(R.id.spTypeDevice);
        spTypeConnection = (Spinner) findViewById(R.id.spTypeConnection);

        tvVoltage = (TextView) findViewById(R.id.textVoltage);
        tvTemp = (TextView) findViewById(R.id.textTemp);
        tvPress = (TextView) findViewById(R.id.textPress);

        tvVoltage.setText("tvVoltage");
        tvTemp.setText("tvTemp");
        tvPress.setText("tvPress");

        btn1.setOnClickListener(new View.OnClickListener() {    //Connect
            @Override
            public void onClick(View v) {
////int type = (int)TypeConnection.WIFI;
//                if (spTypeConnection.getSelectedItemPosition() == 0)typeConnection = TypeConnection.VCP;
//                else if (spTypeConnection.getSelectedItemPosition() == 1)typeConnection = TypeConnection.WIFI;
//                else if (spTypeConnection.getSelectedItemPosition() == 2)typeConnection = TypeConnection.BT;
//
//                if (typeConnection == TypeConnection.VCP) btn1.setText("VCP");
//                else if (typeConnection == TypeConnection.WIFI) btn1.setText("WIFI");
//                else if (typeConnection == TypeConnection.BT) btn1.setText("BT");
//
//                if (typeConnection == TypeConnection.WIFI)
//                {
//                    //tv1.setText("Connection...");
//                    Log.d("test", "Connection...");
//                    tv4.setText("Status: Connection...");
//                    //new ConnectTask().execute("");
//
//                    udpCliect = new UdpClient("192.168.12.221", 14550, 1000);
//
//                }
//                udpCliect = new UdpClient("192.168.12.221", 5020, 1000);
//                //String addr = "192.168.12.221";
//                //udpCliect = new UdpClient("127.0.0.1", 2000, 100);


                new ConnectUdpTask().execute("");
            }
        });
        btnTemp = (Button)findViewById(R.id.btnTemp);
        btnTemp.setOnClickListener(new View.OnClickListener() {    //Temp
            @Override
            public void onClick(View v) {

//                Thread client_thread = new Thread(new UdpClient(),"client_thread");
//                client_thread.start();


                msg_heartbeat init_HB = new msg_heartbeat((long)0, (short)0, (short)0, (short)0, (short)0, (short)2, (int)0, (short)0, true);
                //MAVLinkPacket packet_HB = new msg_heartbeat( init_HB.custom_mode, init_HB.type, init_HB.autopilot, init_HB.base_mode, init_HB.system_status, init_HB.mavlink_version, init_HB.sysid, init_HB.compid, init_HB.isMavlink2).pack();
                MAVLinkPacket packet_HB = init_HB.pack();
                packet_HB.seq = mavlink_cnt++;
                byte[] buffer = packet_HB.encodePacket();
                mUdpClient.sendMessageByte(buffer);

                //mUdpClient.sendMessage("Message");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {    //Disconnect
            @Override
            public void onClick(View v) {

                if (typeConnection == TypeConnection.WIFI) {
                    //tv1.setText("Disconnect...");

//                    if (udpCliect != null){
//                        udpCliect.close();
//                    }
                    if (mTcpClient != null) {
                        mTcpClient.stopClient();
                    }
                    if (mUdpClient != null) {
                        mUdpClient.stopClient();
                    }
                }

                tv4.setText("Status: Disconnect...");
                Log.d("test", "Disconnect...");
                btn1.setText("Connect");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {    //Send
            @Override
            public void onClick(View v) {
                //tv1.setText("Send...");
                Log.d("test", "Reset Joy...");
                //sends the message to the server

//                e1.y = e1.y_Height - e1.sqrSize;
//                e1.invalidate();
////                if (mTcpClient != null) {
////                    mTcpClient.sendMessage("123");
////                }
            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {    //Send
            @Override
            public void onClick(View v) {
                //tv1.setText("Send...");
                Log.d("test", "Go to Second Activity...");
                //sends the message to the server

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });


        btnLight.setOnClickListener(new View.OnClickListener() {    //Send
            @Override
            public void onClick(View v) {

            }
        });
        btnApplyDev.setOnClickListener(new View.OnClickListener() {    //Send
            @Override
            public void onClick(View v) {

//                if (spTypeDevice.getSelectedItemPosition() == 1)
//                {
//                    e1.n_x=e1.x_Width/2;
//                    e1.n_y=e1.y_Height/2;
//                    e1.y_ret = true;
//                    e1.y = e1.y_Height/2;
//                }
//                else {
//                    e1.n_x=e1.x_Width/2;
//                    e1.n_y=e1.y_Height-e1.sqrSize;
//                    e1.y_ret = false;
//                    e1.y = e1.y_Height-e1.sqrSize;
//                }


            }
        });
        ConnectBT.setOnClickListener(new View.OnClickListener() {    //Send
            @Override
            public void onClick(View v) {
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {    //Send
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        btnPowerOff.setOnClickListener(new View.OnClickListener() {    //PowerOff
            @Override
            public void onClick(View v) {


                if (mUdpClient != null) {
                    msg_command_long cmd_long = new msg_command_long(headerPacket);

                    cmd_long.command = MAV_CMD.MAV_CMD_COMPONENT_ARM_DISARM;
                    cmd_long.param1 = 1;

                    MAVLinkPacket packet_RC = cmd_long.pack();
                    packet_RC.seq = mavlink_cnt++;
                    byte[] buffer = packet_RC.encodePacket();
                    mUdpClient.sendMessageByte(buffer);
                }

//                //msg_command_long
//                if (mTcpClient != null) {
//                    char[] cmd = new char[1000];
//                    cmd[0] = '$';
//                    cmd[1] = 1;
//                    cmd[2] = 1;
//
//                    mTcpClient.sendBytes(cmd, 3);
//                }
            }
        });

        iw1 = (ImageView) findViewById(R.id.icon);
        iw2 = (ImageView) findViewById(R.id.icon2);
        iw3 = (ImageView) findViewById(R.id.icon3);
        iw4 = (ImageView) findViewById(R.id.icon4);
        iw5 = (ImageView) findViewById(R.id.icon5);

        iw2.setTop(-1040);
//
//
//        //iw1.setImageDrawable(Drawable.createFromPath("pitch.png"));
//        iw2.setImageResource(R.drawable.pitch);
//
//        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pitch);
//        mBitmap = Bitmap.createBitmap(mBitmap, 0, 1040, 220, 1040+220);
//        iw2.setImageBitmap(mBitmap);

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




//        btAdapter = BluetoothAdapter.getDefaultAdapter();
//        checkBTState();

    sw1 = (Switch) findViewById(R.id.switch1);
        sw2 = (Switch) findViewById(R.id.switch2);
        //sw1.setBackgroundColor(0x5500FF00);
        //sw1.setBackgroundColor(0x55FFFF00);

        switchTRIM = (Switch) findViewById(R.id.switchTRIM);

        etMulti = (TextView) findViewById(R.id.etmulti);
//        etMulti.setText("hello1");
//        etMulti.setText("hello2");
//        etMulti.setText("hello3");
//        etMulti.setText("hello4");
//        etMulti.setText("hello5");

        rxMSG = new char[5][200];

        sb_yaw = (SeekBar) findViewById(R.id.sb_yaw);
        sb_yaw.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sb_yaw_pos = 100-progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sb_yaw_pos = 0;
                seekBar.setProgress(100);
            }
        });




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
            });
            mUdpClient.run();

            return null;
        }



        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            //Log.d(TAG, "udp response " + values[0]);
            //process server response here....



            //byte [] rx_data = values[0].getBytes();

//            Log.d(TAG, "rx msg len  " + mUdpClient.buffer_len + ", data:"+ mUdpClient.buffer[0]+ ", "
//                    + mUdpClient.buffer[1]+ ", "+ mUdpClient.buffer[2]+ ", "+ mUdpClient.buffer[3]+ ", "
//                    + mUdpClient.buffer[4]+ ", "+ mUdpClient.buffer[5]+ ", "+ mUdpClient.buffer[6]+ ", "
//                    + mUdpClient.buffer[7]+ ", "+ mUdpClient.buffer[8]+ ", "+ mUdpClient.buffer[9]+ ", "
//                    + mUdpClient.buffer[10]+ ", "+ mUdpClient.buffer[11]+ ", "+ mUdpClient.buffer[12]+ ", "
//                    + mUdpClient.buffer[13]+ ", "+ mUdpClient.buffer[14]+ ", "+ mUdpClient.buffer[15]+ ", ");

            Parser mav_parser = new Parser();

            int cnt=0;
            for (cnt=0; cnt<mUdpClient.buffer_len;cnt++){

                MAVLinkPacket packet = mav_parser.mavlink_parse_char(mUdpClient.buffer[cnt]);
                if (packet != null) {

                    switch (packet.msgid) {
                        case msg_heartbeat.MAVLINK_MSG_ID_HEARTBEAT:
                            msg_heartbeat p_hb = new msg_heartbeat(packet);
                            Log.d(TAG, "rx hb id  " + packet.msgid);
                            break;
                        case msg_sys_status.MAVLINK_MSG_ID_SYS_STATUS:
                            msg_sys_status p_sys_status = new msg_sys_status(packet);
                            Log.d(TAG, "rx sys_status id  " + p_sys_status.msgid);
                            break;
                        case msg_battery_status.MAVLINK_MSG_ID_BATTERY_STATUS:
                            msg_battery_status rx_pack = new msg_battery_status(packet);
                            Log.d(TAG, "rx bat  " + rx_pack.battery_remaining);
                            int voltage = rx_pack.battery_remaining;
                            tvVoltage.setText("V= " + (int)(((float)((float)voltage*(11/1)*1.09))) + " mV");
                            break;
                        case msg_attitude.MAVLINK_MSG_ID_ATTITUDE:
                            Log.d(TAG, "rx msg MAVLINK_MSG_ID_ATTITUDE  " + packet.msgid);

                            msg_attitude att_pack = new msg_attitude(packet);
//                            tvTemp.setText("t1= "+(float)temp1/100+" t2= " + (float)temp2/100);
//                            tvPress.setText("Press = " + press + " (" + (int)(press/133.32) + " mmHg)");

                            float pitch = att_pack.pitch;
                            float roll = att_pack.roll;
                            float yaw = att_pack.yaw;

//                            rxNewData.pitch = pitch;
//                            rxNewData.roll = roll;
//                            rxNewData.yaw = yaw;
//                            rxNewData.update = true;


                            tv5.setText("x="+pitch+",y="+roll+",z="+yaw+" ");

                            Integer imgPitch = Math.round(pitch);

                            iw3.setRotation(roll/100);
                            iw5.setRotation(yaw/100);

                            iw2.setTop(-1040+((imgPitch/100)*160/45));
                            break;
                        default:
                            Log.d(TAG, "rx msg id  " + packet.msgid);
                            break;
                    }
                }
                //if (mav_parser.mavlink_parse_char(mUdpClient.buffer[cnt]) != null){Log.d(TAG, "rx msg id  " + mav_parser.m.msgid);}
            }
//            //MAVLinkPacket packet = mav_parser.m;
//
//            //Log.d(TAG, "rx msg id  " + packet.msgid);
//
//            Log.d(TAG, "rx msg id  " + mav_parser.m.msgid);
//
//
//            int cnt = 0;
//
//            MAVLinkPacket packet = new MAVLinkPacket(10);
//            //msg_heartbeat heartbeat = new msg_heartbeat();
//            //public msg_heartbeat( long custom_mode, short type, short autopilot, short base_mode, short system_status, short mavlink_version, int sysid, int compid, boolean isMavlink2) {
//            packet.seq = cnt++;
//            packet.msgid = msg_heartbeat.MAVLINK_MSG_ID_HEARTBEAT;
//
//            long custom_mode;
//            short type;
//            short autopilot;
//            short base_mode;
//            short system_status;
//            short mavlink_version;
//            int sysid;
//            int compid;
//            boolean isMavlink2;
//
//            msg_heartbeat init_HB = new msg_heartbeat(0, 0, 0, 0, 0, 2, 0, 0, 2);
//
//
//            MAVLinkPacket packet_HB = new msg_heartbeat( init_HB.custom_mode, init_HB.type, init_HB.autopilot, init_HB.base_mode, init_HB.system_status, init_HB.mavlink_version, init_HB.sysid, init_HB.compid, init_HB.isMavlink2).pack();
//
//            byte[] buffer = packet_HB.encodePacket();
//            MAVLinkPacket packet = msg_heartbeat.pack();

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


                int start = (int) TcpClient.buffer[105 + 0];
                //Log.d("test", "start = " + start);

                int indication = (int) TcpClient.buffer[105 + 3];
                //Log.d("test", "indication = " + indication);

            if (sw1.isChecked()) {
                if ((indication & 0x2) == 0x2) sw1.setBackgroundColor(0x55FFFFFF);
                else sw1.setBackgroundColor(0x55FFFF00);
            }
            else sw1.setBackgroundColor(0x55FFFFFF);

            int pitch = (TcpClient.buffer[105 + 17] & 0xFF) * 256;
            pitch += (int) (TcpClient.buffer[105 + 16] & 0xFF);
            if (pitch>32767) pitch = pitch-65535;

            int roll = (TcpClient.buffer[105 + 19] & 0xFF) * 256;
            roll += (int) (TcpClient.buffer[105 + 18] & 0xFF);
            if (roll>32767) roll = roll-65535;

            int yaw = (TcpClient.buffer[105 + 21] & 0xFF) * 256;
            yaw += (int) (TcpClient.buffer[105 + 20] & 0xFF);
            if (yaw>32767) yaw = yaw-65535;

            int temp1 = (TcpClient.buffer[105 + 23] & 0xFF) * 256;
            temp1 += (int) (TcpClient.buffer[105 + 22] & 0xFF);
            if (temp1>32767) temp1 = temp1-65535;

            int temp2 = (TcpClient.buffer[105 + 27] & 0xFF) * 256*256*256;
            temp2 += (int) ((TcpClient.buffer[105 + 26] & 0xFF)*256*256);
            temp2 += (int) ((TcpClient.buffer[105 + 25] & 0xFF)*256);
            temp2 += (int) (TcpClient.buffer[105 + 24] & 0xFF);
            if (temp2>32767) temp2 = temp2-65535;

            int press = (TcpClient.buffer[105 + 31] & 0xFF) * 256*256*256;
            press += (int) ((TcpClient.buffer[105 + 30] & 0xFF)*256*256);
            press += (int) ((TcpClient.buffer[105 + 29] & 0xFF)*256);
            press += (int) (TcpClient.buffer[105 + 28] & 0xFF);
// int  press=0;//, temp2=0;

                int voltage = (TcpClient.buffer[105 + 5] & 0xFF) * 256;
                voltage += (int) (TcpClient.buffer[105 + 4] & 0xFF);
                //Log.d("test", "pitch = " + pitch);
                int y = 0;
                String tStr = "rx data:";
                for (y = 100; y < 200; y++) {
                    if (TcpClient.buffer[y] != 0) {
                        tStr += "d";
                        tStr += y;
                        tStr += "=";
                        tStr += (TcpClient.buffer[y] & 0xFF);
                        tStr += "; ";
                    }
                }
                //Log.d(TAG, "rx" + tStr);

            int ccnt=0;
                //char t_buff[200];
            char [] t_buff = new char[200];

            //char [][] rxMSG = new char[3][200];


                while (TcpClient.buffer[5 + 200 + ccnt] != '\0'){t_buff[ccnt] = (char)TcpClient.buffer[5 + 200 + ccnt]; ccnt++;}


                if (ccnt > 0) {
                    t_buff[ccnt]='\0';

                    int u=0;
                    for (u=0;u<200;u++) rxMSG[0][u] = rxMSG[1][u];
                    for (u=0;u<200;u++) rxMSG[1][u] = rxMSG[2][u];
                    for (u=0;u<200;u++) rxMSG[2][u] = rxMSG[3][u];
                    for (u=0;u<200;u++) rxMSG[3][u] = rxMSG[4][u];
                    for (u=0;u<200;u++) rxMSG[4][u] = t_buff[u];

                    char [] out = new char[600];
                    u=0;
                    ccnt=0;
                    while(rxMSG[0][u]!='\0'){out[ccnt++] = rxMSG[0][u++];} u=0;
                    while(rxMSG[1][u]!='\0'){out[ccnt++] = rxMSG[1][u++];} u=0;
                    while(rxMSG[2][u]!='\0'){out[ccnt++] = rxMSG[2][u++];} u=0;
                    while(rxMSG[3][u]!='\0'){out[ccnt++] = rxMSG[3][u++];} u=0;
                    while(rxMSG[4][u]!='\0'){out[ccnt++] = rxMSG[4][u++];} u=0;

                    etMulti.setText(out, 0, ccnt);}



//if (TcpClient.buffer[5 + 200] != '\0') etMulti.setText(&TcpClient.buffer[5 + 200]);

            tvVoltage.setText("V= " + (int)(((float)((float)voltage*(11/1)*1.09))) + " mV");
            tvTemp.setText("t1= "+(float)temp1/100+" t2= " + (float)temp2/100);
            tvPress.setText("Press = " + press + " (" + (int)(press/133.32) + " mmHg)");

            tv5.setText("x="+pitch+",y="+roll+",z="+yaw+" ");


            iw3.setRotation(roll/100);
            iw5.setRotation(yaw/100);

            iw2.setTop(-1040+((pitch/100)*160/45));
//            int x_Pitch = 1040 - ((pitch/100)*160/45);
//            if (x_Pitch<0) x_Pitch = 0;
//            else if (x_Pitch>1700)x_Pitch = 1700;
//            int y_Pitch = x_Pitch+220;
//        try{
//            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pitch);
//            mBitmap = Bitmap.createBitmap(mBitmap, 0, x_Pitch, 220, y_Pitch);
//            iw2.setImageBitmap(mBitmap);
//
//        } catch (Exception e) {
//            Log.e(TAG, "Erorr BMP", e);
//
//            Log.e(TAG, "Erorr x= " + x_Pitch + " y = " + y_Pitch);
//
//        }
        }

    }


    char IntToChar(int i)
    {
        int outYaw = i;
        outYaw+=1;
        if (outYaw<0) outYaw=0;
        else if (outYaw>255)outYaw=255;


        if (outYaw>=128)outYaw-=128;
        else outYaw = 256-(127-outYaw);

        if (outYaw<0) outYaw=0;
        else if (outYaw>255)outYaw=255;
        return (char)outYaw;

    }


    private int uByte(byte b){
        return b&0xFF;
    }

    public int uint16_t_to_int (byte b1, byte b2)
    {
        int n= uByte(b1);
        n*=255;
        n+= uByte(b2);
        return n;
    }


    void receiveBlock(byte [] data){                                                                //receiveBlock


        if (data[1] == '\0'){     //str message
            if (data[2] == 0) return;   // if len == 0

            int ccnt=0;
            //char t_buff[200];
            char [] t_buff = new char[200];

            //char [][] rxMSG = new char[3][200];


            while (data[3 + ccnt] != '\0'){t_buff[ccnt] = (char)data[3 + ccnt]; ccnt++;}


            if (ccnt > 0) {
                t_buff[ccnt]='\0';

                int u=0;
                for (u=0;u<200;u++) rxMSG[0][u] = rxMSG[1][u];
                for (u=0;u<200;u++) rxMSG[1][u] = rxMSG[2][u];
                for (u=0;u<200;u++) rxMSG[2][u] = rxMSG[3][u];
                for (u=0;u<200;u++) rxMSG[3][u] = rxMSG[4][u];
                for (u=0;u<200;u++) rxMSG[4][u] = t_buff[u];

                char [] out = new char[600];
                u=0;
                ccnt=0;
                while(rxMSG[0][u]!='\0'){out[ccnt++] = rxMSG[0][u++];} u=0;
                while(rxMSG[1][u]!='\0'){out[ccnt++] = rxMSG[1][u++];} u=0;
                while(rxMSG[2][u]!='\0'){out[ccnt++] = rxMSG[2][u++];} u=0;
                while(rxMSG[3][u]!='\0'){out[ccnt++] = rxMSG[3][u++];} u=0;
                while(rxMSG[4][u]!='\0'){out[ccnt++] = rxMSG[4][u++];} u=0;

                etMulti.setText(out, 0, ccnt);}
        }

        if (data[1] != 0x6) {return; }       // rx only cmd 0x18


        //int start = (int) data[0];
        //Log.d("test", "start = " + start);

        int indication = (int) data[3];
        //Log.d("test", "indication = " + indication);

        if (sw1.isChecked()) {
            if ((indication & 0x2) == 0x2) sw1.setBackgroundColor(0x55FFFFFF);
            else sw1.setBackgroundColor(0x55FFFF00);
        }
        else sw1.setBackgroundColor(0x55FFFFFF);

        int pitch = (data[17] & 0xFF) * 256;
        pitch += (int) (data[16] & 0xFF);
        if (pitch>32767) pitch = pitch-65535;

        int roll = (data[19] & 0xFF) * 256;
        roll += (int) (data[18] & 0xFF);
        if (roll>32767) roll = roll-65535;

        int yaw = (data[21] & 0xFF) * 256;
        yaw += (int) (data[20] & 0xFF);
        if (yaw>32767) yaw = yaw-65535;

        int temp1 = (data[23] & 0xFF) * 256;
        temp1 += (int) (data[22] & 0xFF);
        if (temp1>32767) temp1 = temp1-65535;

        int temp2 = (data[27] & 0xFF) * 256*256*256;
        temp2 += (int) ((data[26] & 0xFF)*256*256);
        temp2 += (int) ((data[25] & 0xFF)*256);
        temp2 += (int) (data[24] & 0xFF);
        if (temp2>32767) temp2 = temp2-65535;

        int press = (data[31] & 0xFF) * 256*256*256;
        press += (int) ((data[30] & 0xFF)*256*256);
        press += (int) ((data[29] & 0xFF)*256);
        press += (int) (data[28] & 0xFF);
// int  press=0;//, temp2=0;

        int voltage = (data[5] & 0xFF) * 256;
        voltage += (int) (data[4] & 0xFF);
        //Log.d("test", "pitch = " + pitch);
        int y = 0;
//        String tStr = "rx data:";
//        for (y = 100; y < 200; y++) {
//            if (data[y] != 0) {
//                tStr += "d";
//                tStr += y;
//                tStr += "=";
//                tStr += (data[y] & 0xFF);
//                tStr += "; ";
//            }
//        }
        //Log.d(TAG, "rx" + tStr);





//if (TcpClient.buffer[5 + 200] != '\0') etMulti.setText(&TcpClient.buffer[5 + 200]);

        tvVoltage.setText("V= " + (int)(((float)((float)voltage*(11/1)*1.09))) + " mV");
        tvTemp.setText("t1= "+(float)temp1/100+" t2= " + (float)temp2/100);
        tvPress.setText("Press = " + press + " (" + (int)(press/133.32) + " mmHg)");

        tv5.setText("x="+pitch+",y="+roll+",z="+yaw+" ");


        iw3.setRotation(roll/100);
        iw5.setRotation(yaw/100);

        iw2.setTop(-1040+((pitch/100)*160/45));
//        int x_Pitch = 1040 - ((pitch/100)*160/45);
//        if (x_Pitch<0) x_Pitch = 0;
//        else if (x_Pitch>1700)x_Pitch = 1700;
//        int y_Pitch = x_Pitch+220;
//        try{
//            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pitch);
//            mBitmap = Bitmap.createBitmap(mBitmap, 0, x_Pitch, 220, y_Pitch);
//            iw2.setImageBitmap(mBitmap);
//
//        } catch (Exception e) {
//            Log.e(TAG, "Erorr BMP", e);
//
//            Log.e(TAG, "Erorr x= " + x_Pitch + " y = " + y_Pitch);
//
//        }
    }





    int index = 0;

    class MyTimerTask extends TimerTask {                                                           //MyTimerTask

        @Override
        public void run() {






            runOnUiThread(new Runnable() {

                @Override
                public void run() {


//
//if (index>=0)rot+=10;
//else rot-=10;
//
//if (rot>2000) index=-1;
//if (rot<-1000) index=0;
//
//
//                    iw3.setRotation(rot);
//                    iw5.setRotation(rot);
//
//                    int pitch=rot*100;
//
//                    int x_Pitch = 1040 - ((pitch/100)*160/45);
//                    if (x_Pitch<0) x_Pitch = 0;
//                    else if (x_Pitch>1700)x_Pitch = 1700;
//                    int y_Pitch = x_Pitch+220;
//                    try{
////                        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pitch);
////                        mBitmap = Bitmap.createBitmap(mBitmap, 0, x_Pitch, 220, y_Pitch);
////                        iw2.setImageBitmap(mBitmap);
//                        iw2.setTop(-1040+pitch/100);
//
//                    } catch (Exception e) {
//                        Log.e(TAG, "Erorr BMP", e);
//
//                        Log.e(TAG, "Erorr x= " + x_Pitch + " y = " + y_Pitch);
//
//                    }

//                    if (rxNewData.update) {
//                        rxNewData.update = false;
//
//                        tv5.setText("x="+rxNewData.pitch+",y="+rxNewData.roll+",z="+rxNewData.yaw+" ");
//
//                        Integer imgPitch = Math.round(rxNewData.pitch);
//
//                        iw3.setRotation(rxNewData.roll/100);
//                        iw5.setRotation(rxNewData.yaw/100);
//
//                        iw2.setTop(-1040+((imgPitch/100)*160/45));
//                    }




                    if (mUdpClient != null){
                        msg_rc_channels_override msg_rc = new msg_rc_channels_override(headerPacket);
                        Integer  tYaw = (int)(1500+e1.xPosition()*500/50);//*PITCH_RANGEVALUE/50);
                        Integer  tThrottle = (int)(1500+e1.xPosition()*500/50);//*PITCH_RANGEVALUE/50);
                        Integer  tRoll = (int)(1500+e2.xPosition()*500/50);//*PITCH_RANGEVALUE/50);
                        Integer tPitch = (int)(1500+e2.yPosition()*500/50);//*PITCH_RANGEVALUE/50);
                        Integer tMode = 1;
                        Log.d(TAG, "Send real x= " + tPitch + " y = " + tRoll);

                        msg_rc.chan1_raw = tThrottle.shortValue();
                        msg_rc.chan2_raw = tYaw.shortValue();
                        msg_rc.chan3_raw = tPitch.shortValue();
                        msg_rc.chan4_raw = tRoll.shortValue();
                        msg_rc.chan5_raw = tMode.shortValue();

                        MAVLinkPacket packet_RC = msg_rc.pack();
                        packet_RC.seq = mavlink_cnt++;
                        byte[] buffer = packet_RC.encodePacket();
                        mUdpClient.sendMessageByte(buffer);
                    }


                    if (mTcpClient != null) {

                        char[] cmd = new char[60];

                        PrepareBytesToSend(cmd);


                        mTcpClient.sendBytes(cmd, 34);
                    }







                    //if (mTcpClient != null)  tv4.setText("Connected");
                    //else tv4.setText("desConnected");

                    if (mTcpClient.Status >0) tv4.setText("Status: Connected");
                    //else if (fragment.connected == TerminalFragment.Connected.True) tv4.setText("Status: VCP Connected");
                    else tv4.setText("Status: disConnected");



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










    public void PrepareBytesToSend(char[] cmd){

        //fragment.send("123\n\r");
        //this.myapplication.TerminalFragment.sendText("123123\n\r");
        //getSupportFragmentManager().getFragment().send

//            if (spTypeDevice.getSelectedItemPosition() == 1)
//            {
//                e1.n_x=e1.x_Width/2;
//                e1.n_y=e1.y_Height/2;
//                e1.y_ret = true;
//                e1.y = e1.y_Height/2;
//            }
//            else {
//                e1.n_x=e1.x_Width/2;
//                e1.n_y=e1.y_Height-e1.sqrSize;
//                e1.y_ret = false;
//                e1.y = e1.y_Height-e1.sqrSize;
//            }


        //Log.d("test", "sel = " + spTypeDevice.getSelectedItemPosition());




//            Calendar calendar = Calendar.getInstance();
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
//                    "dd:MMMM:yyyy HH:mm:ss a", Locale.getDefault());
//            final String strDate = simpleDateFormat.format(calendar.getTime());


        final char TX_servo1Value, TX_servo2Value;

        int tPitch, tRoll;

        //tRoll = (int)(((float)(e2.xPosition()+50));//*PITCH_RANGEVALUE/50);
        //tPitch = (int)(((float)(e2.yPosition()+50));//*PITCH_RANGEVALUE/50);

        tRoll = (int)(e2.xPosition());//*PITCH_RANGEVALUE/50);
        tPitch = (int)(e2.yPosition());//*PITCH_RANGEVALUE/50);

        //tRoll = 100*(x*1/100)^(80*0.1+1)
        //if (tRoll<0)tRoll = (int)((-100)*Math.pow((tRoll/100), (80*0.1+1)));
        //else tRoll = (int)((100)*Math.pow((tRoll/100), (80*0.1+1)));

        float exp = manageExp;
        if (exp<0) exp=0;
        else if (exp>100) exp=100;



        int sig=1;
        if (tRoll<0){sig=-1; tRoll =-tRoll;}
        else sig=1;
        tRoll = (int)((sig*127)*Math.pow(((double)tRoll)/50, 1+((exp/10.0)*(3.0/5.0))));        // set expanenta. "4" - it is 50% from exp!!
        if (tPitch<0){sig=-1; tPitch =-tPitch;}
        else sig=1;
        tPitch = (int)((sig*127)*Math.pow(((double)tPitch)/50, 1+((exp/10.0)*(3.0/5.0))));


        //Log.d("test", "tRoll = " + tRoll);

        if (ROLL_INVERSION == 1) tRoll =-tRoll;
        if (PITCH_INVERSION == 1) tPitch =-tPitch;

        tRoll = (tRoll*ROLL_RANGEVALUE/50);       //-114...114
        tPitch = (tPitch*PITCH_RANGEVALUE/50);

//            TX_servo1Value = (char)tPitch;
//            TX_servo2Value = (char)tRoll;

        int Roll = tRoll+127;
        int mRoll = 127-tRoll;
        int Pitch = tPitch+127;
        int mPitch = 127-tPitch;

//            TX_servo1Value = (char)Pitch;
//            TX_servo2Value = (char)Roll;

        int out1 = 0;

        if (SERVO1_TYPEMOVE==0) out1 = tPitch+127;			// for manual manage servo motors
        else if (SERVO1_TYPEMOVE==1) out1 = 127-tPitch;
        else if (SERVO1_TYPEMOVE==2) out1 = tRoll+127;
        else if (SERVO1_TYPEMOVE==3) out1 = 127-tRoll;
        else if (SERVO1_TYPEMOVE==4 ) out1 = (tPitch + tRoll)+127;
        else if (SERVO1_TYPEMOVE==5) out1 = (tRoll-tPitch)+127;

        if (out1>=127)out1-=127;
        else out1 = 256-(127-out1);

        if (out1<0) out1=0;
        else if (out1>255)out1=255;

        TX_servo1Value = (char)out1;//(char)out1;

        int out2 = 0;

        if (SERVO2_TYPEMOVE==0) out2 = tPitch+127;			// for manual manage servo motors
        else if (SERVO2_TYPEMOVE==1) out2 = 127-tPitch;
        else if (SERVO2_TYPEMOVE==2) out2 = tRoll+127;
        else if (SERVO2_TYPEMOVE==3) out2 = 127-tRoll;
        else if (SERVO2_TYPEMOVE==4 ) out2 = (tPitch + tRoll)+127;
        else if (SERVO2_TYPEMOVE==5) out2 = (tRoll-tPitch)+127;

        if (out2>=127)out2-=127;
        else out2 = 256-(127-out2);

        if (out2<0) out2=0;
        else if (out2>255)out2=255;
        TX_servo2Value = (char)out2;


        ///////////////////////////////////////////////////////////////////////////////


        /// Convert int ti signed char:
        int outYaw = (int)(((int)(e1.xPosition()+50)*255/100)&0xFF);
        outYaw+=1;
        if (outYaw<0) outYaw=0;
        else if (outYaw>255)outYaw=255;

        if (outYaw>=128)outYaw-=128;
        else outYaw = 256-(127-outYaw);

        if (outYaw<0) outYaw=0;
        else if (outYaw>255)outYaw=255;
        //End convert


        //char[] cmd = new char[1000];
        cmd[0] = '$';
        cmd[1] = 18;
        cmd[2] = 44;
        //if ((int)e1.yPosition()!=0) cmd[3] = 1;
        //else cmd[3] = 0;
        //mValue = ((((int)e1.yPosition())&0xFF)&0xFF);
        cmd[3] = 1;//(char)mValue;//66;    // mainMotor
        if (sw2.isChecked()) cmd[4] = (char)SecondActivity.MotorValue;
        else cmd[4] = (char)(((int)(e1.yPosition()+50)*255/100)&0xFF);       //mainMotorValue

        if (cmd[4]>255) cmd[4] = 0;
        else if (cmd[4]<0) cmd[4]=0;
        cmd[5] = 0;       //secondMotor

        if (sb_yaw_pos != 0) cmd[6] = (char)IntToChar(sb_yaw_pos+127);//(char)sb_yaw_pos;
        else  cmd[6] = (char)outYaw;       //secondMotorValue



        tv2.setText("   Yaw="+ (int)cmd[4]);

        cmd[7] = (char)typeManage;       //typeManage
        if (sw1.isChecked()) cmd[7] = 100;
        else if (switchTRIM.isChecked()) cmd[7] = 101;
        cmd[8] = 1;       //servo1
        cmd[9] = TX_servo1Value;//(char)(((int)(e2.xPosition()+50))&0xFF);//(char)e2.xPosition();       //servo1Value
        cmd[10] = 1;       //servo2
        cmd[11] = TX_servo2Value;//(char)(e2.yPosition()+50);//(char)e2.yPosition();       //servo2Value
        cmd[12] = 0;       //indication
        cmd[13] = (char)StabRoll;       //StabRoll
        cmd[14] = (char)StabPitch;       //StabPitch
        cmd[15] = (char)StabYaw;       //StabYaw
        cmd[16] = (char)StabAngelPitch;       //StabAngelPitch
        cmd[17] = (char)StabAngelRoll;       //StabAngelRoll
        cmd[18] = (char)StabKoeffManage1;       //StabKoeffManage1
        cmd[19] = (char)StabKoeffManage2;       //StabKoeffManage2
        cmd[20] = (char)StabServo1;       //StabServo1
        cmd[21] = (char)StabServo2;       //StabServo2
        cmd[22] = (char)StabKoeffCh1;       //StabKoeffCh1
        cmd[23] = (char)StabKoeffCh2;       //StabKoeffCh2

        cmd[24] = (char)pos_rev;       //pos_rev
        cmd[25] = (char)pos_ang1;       //pos_ang1
        cmd[26] = (char)pos_ang2;       //pos_ang2
        cmd[27] = (char)StabServoMax1;       //StabServoMax1
        cmd[28] = (char)StabServoMax2;       //StabServoMax2

        cmd[29] = (char)CmdTimeout;       //CmdTimeout

        cmd[30] = (char)math_K_angle;       //math_K_angle
        cmd[31] = (char)math_K_bias;       //math_K_bias
        cmd[32] = (char)math_K_measure;       //math_K_measure
        cmd[33] = (char)math_gyroRate;       //math_gyroRate



        String strDate = "    date" + time_t;
        time_t+=1;
        //tv3.setText(strDate);
        //tv2.setText("   posy="+ (int)e1.yPosition());
        //tv1.setText("   posx="+ (int)e1.xPosition());
        //tv1.setText("   posx="+ (int)(((int)e1.yPosition())&0xFF));
        tv1.setText("   posx="+ (int)(char)(((int)(e1.yPosition()+50))&0xFF));
        //tv2.setText("   TX_servo1Value="+ (int)TX_servo1Value);
        tv3.setText("   TX_servo2Value="+ (int)TX_servo2Value);

    }

}

