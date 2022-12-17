package com.example.appflycontrol;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;




public class UdpClient {


    private DatagramSocket udpSocket;
    private InetAddress serverAddr;


    public static final String TAG = "test";//TcpClient.class.getSimpleName();
    //public static final String SERVER_IP = "192.168.100.1"; //server IP address
    public static final String SERVER_IP = "192.168.12.221"; //server IP address
    //public static final int SERVER_PORT = 2000;
    public static final int SERVER_PORT_RX = 14550;
    public static final int SERVER_PORT_TX = 14551;
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
    public static int buffer_len = 0;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public UdpClient(OnMessageReceived listener) {
        buffer = new byte[1600];
        mMessageListener = listener;

        try {
            udpSocket = new DatagramSocket(SERVER_PORT_RX);
            //udpSocket.setSoTimeout(timeoutMs);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            serverAddr = InetAddress.getByName(SERVER_IP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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
                byte[] data = message.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, serverAddr, SERVER_PORT_TX);
                try {
                    udpSocket.send(packet);
                    //Log.d(TAG, "sended data!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        //Log.d(TAG, "to send...!");
    }
    public void sendMessageByte(final byte[]  message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DatagramPacket packet = new DatagramPacket(message, message.length, serverAddr, SERVER_PORT_TX);
                try {
                    udpSocket.send(packet);
                    //Log.d(TAG, "sended data!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        //Log.d(TAG, "to send...!");
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        mRun = false;

//        if (mBufferOut != null) {
//            try{
//                mBufferOut.flush();
//                mBufferOut.close();
//            }
//            catch (Exception e) {//catch(IOException e) {
//                Status=-1;
//                e.printStackTrace();
//            }
//        }
//
//        mMessageListener = null;
//        mBufferIn = null;
//        mBufferOut = null;
//        mServerMessage = null;
    }

    public void run() {

        mRun = true;
        Status=0;

        try {

            while (mRun) {
                Status=1;

                //byte [] data = new byte[1600];

                //DatagramPacket packet = new DatagramPacket(data,data.length);
                DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
                //buffer
                try {
                    udpSocket.receive(packet);
                    buffer_len = packet.getLength();
                    //buffer = data;
                    mServerMessage = "rx data";
                    //mServerMessage = new String(data);;
                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                        //Log.d(TAG, "S: Received Message: '" + mServerMessage + "'");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }


            }

        } catch (Exception e) {
            Status=-1;
            Log.e(TAG, "S: Error", e);
        } finally {
            //the socket must be closed. It is not possible to reconnect to this socket
            // after it is closed, which means a new socket instance has to be created.
            udpSocket.close();
        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the Activity
    //class at on AsyncTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}



//
//
//public class UdpClient  extends Thread{
//
//
//    private String host;
//    private int port;
//    private DatagramSocket udpSocket;
//    private InetAddress serverAddr;
//
//    /**
//     * UDP constructor.
//     * @param host the ip address of remote host.
//     * @param port the port of remote host.
//     * @param timeoutMs the timeout for the socket in milliseconds.
//     */
////    public UdpClient(String host, int port, int timeoutMs){
////        this.host = host;
////        this.port = port;
////        try {
////            udpSocket = new DatagramSocket(port);
////            //udpSocket.setSoTimeout(timeoutMs);
////        } catch (SocketException e) {
////            e.printStackTrace();
////        }
////        try {
////            byte[] ipAddr = new byte[]{127, 0, 0, 1};
////            serverAddr = InetAddress.getByAddress(ipAddr);//getByName(host);
////        } catch (UnknownHostException e) {
////            e.printStackTrace();
////        }
////    }
//
//    public void run() {
//
//        UdpClient udpclient = new UdpClient();
//        try {
//            udpclient.startUDPClient();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
//
//    public void startUDPClient() throws IOException {
//
//        boolean complete = false;
//
//        udpSocket = new DatagramSocket(6069);
//
//        //while (!complete) {
//
//        String msg = "123123";
//
//        byte[] packet = msg.getBytes();
//
////            Scanner input = new Scanner(System.in);
////            String message = new String(input.next());
////            byte[] packet = message.getBytes();
//
//        DatagramPacket client_packet = new DatagramPacket(packet, packet.length, InetAddress.getLocalHost(), 6067);
//
//        udpSocket.send(client_packet);
//        System.out.println("Sent!");
//
////            if (message.compareTo("exit") == 0) {
////                complete = true;
////            }
//
//        //}
//
//        udpSocket.close();
//        System.out.println("Client socket closed");
//    }
//    /**
//     * Converts input Object to array of byte.
//     * @param object the input object.
//     * @return the array of byte.
//     */
//    public byte[] objectToBytes(Object object){
//        try {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(bos);
//            oos.writeObject(object);
//            oos.flush();
//            return bos.toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * Converts the input array of byte in Object.
//     * @param data the input array of byte.
//     * @return the Object.
//     */
//    public Object bytesToObject(byte[] data){
//        ByteArrayInputStream in = new ByteArrayInputStream(data);
//        try {
//            ObjectInputStream is = new ObjectInputStream(in);
//            return is.readObject();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * Send Object.
//     * @param obj the Object to send.
//     * @return true if sent, false otherwise.
//     */
//    public boolean send(Object obj){
//        byte[] data = objectToBytes(obj);
//        if (data == null){
//            return false;
//        }
//        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddr, port);
//        try {
//            udpSocket.send(packet);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * Send data as byte array.
//     * @param data the byte array.
//     * @return true if sent, false otherwise.
//     */
//    public boolean send(byte[] data){
//        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddr, port);
//        try {
//            udpSocket.send(packet);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * Receive an Object.
//     * @return the object received.
//     */
//    public Object receive(){
//        byte[] data = new byte[8000];
//        DatagramPacket packet = new DatagramPacket(data,data.length);
//        try {
//            udpSocket.receive(packet);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return bytesToObject(data);
//    }
//
//    /**
//     * Receive data as byte array.
//     * @param data the buffer where received data will be written.
//     * @return the received data.
//     */
//    public byte[] receive(byte[] data){
//        DatagramPacket packet = new DatagramPacket(data,data.length);
//        try {
//            udpSocket.receive(packet);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return data;
//    }
//
//    /**
//     * Close the socket.
//     */
//    public void close(){
//        udpSocket.close();
//    }
//
//}