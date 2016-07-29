package com.example.mrchenrunfeng.myecg.classes;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mr.Chen RunFENG on 2016/7/19.
 */
public class ECGServerThread extends Thread implements ECGServer {
    private volatile boolean done = false;
    protected static BluetoothSocket mSocket;
    protected BluetoothLink mBluetoothLink;
    protected static OutputStream mmOutputStream;
    protected static InputStream mmInputStream;
    protected int mCommand;
    protected int mArg;//记录命令帧的返回参数。
    protected Handler mHandler;

    public ECGServerThread(Handler handler, BluetoothLink bluetoothLink) {
        mHandler = handler;
        mBluetoothLink = bluetoothLink;
        getStream();
    }
    public ECGServerThread(BluetoothLink bluetoothLink){
        mBluetoothLink = bluetoothLink;
        getStream();
    }
    @Override
    public void run(){
        while (!done) {
            handleStream();
            dataStream();
        }
    }
    public synchronized void dataStream() {
        int mdata;
        while (true) {
            try {
                if ((mdata = mmInputStream.read()) == Command.intFirstFrame) {
                    Log.v("Firstframe:",""+mdata);
                    mdata = mmInputStream.read();
                    Log.v("INTDATA:",""+mdata);
                    if (mdata == Command.intData) {
                        int len;
                        len = mmInputStream.read();
//                        if(len>50)
//                            len=50;
                        //  if(len>=255)
                        // len=50;
//                        Log.v("datalen:", "" + toHex(len));
//                    while (true) {
                        //int bytes;
                        if (len%2==0) {
                            byte[] buffer = new byte[len];
                            for (int i = 0; i < buffer.length; i = i + 2) {
                                byte hght = (byte) mmInputStream.read();
                                byte low = (byte) mmInputStream.read();
                                buffer[i] = hght;
                                buffer[i + 1] = low;
                                Log.v("I:", "" + i);
                            }
                            //bytes=mmInputStream.read(buffer);
                            //if (toHex(low)!="aa"||toHex(low)!=toHex(len));
                            mHandler.obtainMessage(Command.MESSAGE_ECG, -1, -1, buffer).sendToTarget();
                        }
                        //Log.v("dATA","len:"+toHex(len)+"  hght: "+toHex(hght)+"  low:"+toHex(low));
                        // }
//                    byte[] buffer=new byte[256];
//                    int bytes=mmInputStream.read(buffer);
//                    Log.v("len:",""+bytes);
                    }
                    else {return;}
                }
            } catch (IOException e) {
                // TODO �Զ���ɵ� catch ��
                Log.v(e.getMessage(), "   read data fail  ");
                e.printStackTrace();
            }
            // }
        }
    }
    public boolean getStream() {
        mSocket = mBluetoothLink.getSocket();
        try {
            mmInputStream = mSocket.getInputStream();
            mmOutputStream = mSocket.getOutputStream();
            return true;
        } catch (IOException e) {
            // TODO �Զ���ɵ� catch ��
            Log.v("stream error:", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public  boolean sendCommand(int firstframe, int order, int arg1, int arg2) {

        try {

            mmOutputStream.write(firstframe);
            mmOutputStream.write(order);
            mmOutputStream.write(arg1);
            mmOutputStream.write(arg2);
            return true;
        } catch (IOException e) {
            // TODO �Զ���ɵ� catch ��
            e.printStackTrace();
            return false;
        }
    }
    public synchronized void handleStream() {
        int mdata;
        //mmInputStream
        //while (true) {
        try {
            if ((mdata = mmInputStream.read()) == Command.intFirstFrame) {
                mdata = mmInputStream.read();
                if (mdata == Command.intCType) {
                    mCommand = mmInputStream.read();
                    mArg = mmInputStream.read();
                    Log.v("Command and Arg:", "" + mCommand + "" + mArg);
                }
            }
            return;
        } catch (IOException e) {
            // TODO �Զ���ɵ� catch ��
            Log.v(e.getMessage(), "   read data fail  ");
            e.printStackTrace();
        }
        // }
    }
    //设置状态
    public int SetState(int state) {
        switch (state) {
            case Command.intTest:
                return Command.SOCKET_ISNOMALC;
            case Command.intStartConnectOrder:
                return Command.SOCKET_COMUNICATIONCONNECTED;
            case Command.intStopConnect:
                return Command.NOTSAMPLEING;
            case Command.intStartSample:
                return Command.SAMPLING;
            default:
                return Command.SOCKET_NOTCOMUNICATIONCONNECTED;
        }
    }
    // 把byte 转化为两位十六进制数
    public  String toHex(byte b) {
        String result = Integer.toHexString(b & 0xFF);
        if (result.length() == 1) {
            result = '0' + result;
        }
        return result;
    }
     public void setDone(){
     done = true;
     }
}
