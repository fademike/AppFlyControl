package com.example.appflycontrol;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpClient {

    public static final String TAG = "test";//TcpClient.class.getSimpleName();
    //public static final String SERVER_IP = "192.168.100.1"; //server IP address
    public static final String SERVER_IP = "192.168.12.221"; //server IP address
    public static final int SERVER_PORT = 2000;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    //private PrintWriter mBufferOut;
    private BufferedWriter mBufferOut;
    //private OutputStreamWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    public static int Status = 0;

    public static byte [] buffer;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener) {
        buffer = new byte[1000];
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(final String message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    if (mBufferOut != null) {
                        Log.d(TAG, "Sending: " + message);
                        //mBufferOut.println(message);
                        //mBufferOut.flush();
                        mBufferOut.flush();
                    }
                }
                catch (Exception e) {//catch(IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void sendBytes(final char [] message, final int num) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null) {
                    //Log.d(TAG, "Sending: " + message);
                    //mBufferOut.println(message);
                    //mBufferOut.write(message, 0, num);
                    try{
                        int c=0;
                        int cc=0;
                        for (cc=0;cc<num;cc++){
                            //c = (int)(message[cc]&0xFF);
                            c = (char)(message[cc]);

                            //if (c<0)c=22;
                            //if (c>127)c=180;

                            //mBufferOut.write(c);
                            mBufferOut.write(c);
                        }
                        //mBufferOut.write(message, 0, num);
                        //mBufferOut.
                        mBufferOut.flush();
                    }
                    catch (Exception e) {//catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        mRun = false;

        if (mBufferOut != null) {
            try{
                mBufferOut.flush();
                mBufferOut.close();
            }
            catch (Exception e) {//catch(IOException e) {
                Status=-1;
                e.printStackTrace();
            }
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run() {

        mRun = true;
        Status=0;
        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.d(TAG, "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVER_PORT);

            try {

                //sends the message to the server
                mBufferOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.ISO_8859_1)); //));//, StandardCharsets.UTF_8));
                //mBufferOut = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.ISO_8859_1); //);//, StandardCharsets.UTF_8));

                //receives the message which the server sends back
                //mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataInputStream  in = new DataInputStream(socket.getInputStream());


                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    Status=1;

                    //mServerMessage = mBufferIn.readLine();

                    int j = in.read(buffer);
                    if (j>0)mServerMessage = "rx data";//Log.d(TAG, "S: Rx data: '" + buffer + "'");
                    //int y=0;
                    //for (y=0;y<j;y++)Log.d(TAG, "y=" + buffer[y] + ";");



                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                        Log.d(TAG, "S: Received Message: '" + mServerMessage + "'");
                    }
//                    else if (mServerMessage != null) {
//                        //call the method messageReceived from MyActivity class
//                        Log.d(TAG, "S: Received Message: '" + mServerMessage + "'");
//                    }

                }

                //Log.d(TAG, "S: Received Message: '" + mServerMessage + "'");

            } catch (Exception e) {
                Status=-1;
                Log.e(TAG, "S: Error", e);
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {
            Status=-1;
            Log.e(TAG, "C: Error", e);
        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the Activity
    //class at on AsyncTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}