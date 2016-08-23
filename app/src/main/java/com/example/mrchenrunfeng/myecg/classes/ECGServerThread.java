package com.example.mrchenrunfeng.myecg.classes;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Mr.Chen RunFENG on 2016/7/19.
 */
public class ECGServerThread extends Thread implements ECGServer {
    private  boolean isread = false;
    protected static BluetoothSocket mSocket;
    protected BluetoothLink mBluetoothLink;
    protected static OutputStream mmOutputStream;
    protected static InputStream mmInputStream;
    private BufferedInputStream bufferedInputStream;
    //private BufferedReader
//    protected int mcommand;
//    protected int marg;//记录命令帧的返回参数。
    //int mData;
    protected Handler mHandler;

    public ECGServerThread(Handler handler, BluetoothLink bluetoothLink) {
        mHandler = handler;
        mBluetoothLink = bluetoothLink;
        getStream();
    }

    public ECGServerThread(BluetoothLink bluetoothLink) {
        mBluetoothLink = bluetoothLink;
        getStream();
    }

    @Override
    public void run() {
//        while (!isread) {
//
        isread=false;
        handleStream();
        //dataStream();
        // }
    }

//    public synchronized void dataStream() {
//        int mdata;
//        isread = false;
//        while (true) {
//            try {
//                if (isread==true) {
//                    Log.v("DONE:",""+isread);
//                    break;
//                }
//                mdata = mmInputStream.read();
//                if (mdata == Command.intFirstFrame) {
//                    Log.v("Firstframe:", "" + mdata);
//                    mdata = mmInputStream.read();
//                    Log.v("INTDATA:", "" + mdata);
//                    if (mdata == Command.intData) {
//                        int len;
//                        len = mmInputStream.read();
//                        byte[] buffer=new byte[len];
//                        mmInputStream.read(buffer);
//                        //while ()
//                        for (int i=0;i<buffer.length;i+=2){
//                            byte hght = buffer[i];
//                            byte low = buffer[i+1];
//                            Log.v("ECGDATA:", hght+"_"+low+"_"+len + "_" + byteToShort(low, hght));
//                            Command.mShowDataQueue.offer((int) byteToShort(low, hght));
//                        }
//                    }
//                } /*else {
//                    break;
//                }*/
//            } catch (IOException e) {
//                // TODO �Զ���ɵ� catch ��
//                Log.v(e.getMessage(), "   read data fail  ");
//                e.printStackTrace();
//                break;
//            }
//            // }
//        }
//    }

    public boolean getStream() {
        // while (mBluetoothLink.getSocket()!=null) {
        mSocket = mBluetoothLink.getSocket();
        //}
        try {
            mmInputStream = mSocket.getInputStream();
            mmOutputStream = mSocket.getOutputStream();
            bufferedInputStream=new BufferedInputStream(mSocket.getInputStream());
            return true;
        } catch (IOException e) {
            // TODO �Զ���ɵ� catch ��
            Log.v("stream error:", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int sendCommand(int firstframe, int order, int arg1, int arg2) {
        if (mSocket.isConnected() == true) {
            try {
                mmOutputStream.write(firstframe);
                mmOutputStream.write(order);
                mmOutputStream.write(arg1);
                mmOutputStream.write(arg2);
                return Command.SOCKET_ISNOMALC;
            } catch (IOException e) {
                // TODO �Զ���ɵ� catch ��
                e.printStackTrace();
                return Command.SOCKET_NOTNOMALC;
            }
        } else {
            return Command.SOCKET_CLOSED;
        }
    }
   public synchronized void Read(){
       while (true){
           byte[] bufferds=new byte[1024];
           try {
              int bytes= bufferedInputStream.read(bufferds);
              // Log.v("len:",""+bytes);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }
    public synchronized void handleStream() {
        int mdata;
        while (true) {
            try {
                try {
//                    if(bufferedInputStream.available()>0 == false){
//                        continue;
//                    }else{
                        Thread.sleep(2);
                   // }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Log.v("handleStreamThreadid:",""+Thread.currentThread().getId());
                if (mSocket.isConnected()==false){break;}
//                if(bufferedInputStream.markSupported()==true)
//                    bufferedInputStream.mark(53);
                mdata=bufferedInputStream.read();
                if (mdata == Command.intFirstFrame) {
                    mdata = bufferedInputStream.read();
                    if (mdata == Command.intCType) {
                        commandhandle();
                        break;
                    }
                    if (mdata == Command.intData) {
                        datahandle();
                        continue;
                    }
                }
                else {
                    //bufferedInputStream.reset();
                    bufferedInputStream.skip(1);
                   // Log.v("mdata:",""+mdata);
                    continue;
                }
//                if (bufferedInputStream.available()>0 == false){
//                    break;
//                }
                int i= bufferedInputStream.available();
                  // Log.v("idata:",""+i);
                    break;
            } catch (IOException e) {
                // TODO �Զ���ɵ� catch ��
                Log.v(e.getMessage(), "   read data fail  ");
                e.printStackTrace();
            }
        }// }
    }

    private synchronized void datahandle() throws IOException {
        int len;
        len = bufferedInputStream.read();
        byte[] buffer=new byte[len];
        bufferedInputStream.read(buffer);
//        int readCount = 0; // 已经成功读取的字节的个数
//        //while (readCount < len) {
//        bufferedInputStream.read(buffer, readCount, len - readCount);
       // }
//        for(int i=0;i<len;i+=2){
//            try {
//                Thread.sleep(4);
//                byte hght =(byte) bufferedInputStream.read();
//                //Thread.sleep(20);
//                byte low = (byte) bufferedInputStream.read();
//                Log.v("ECGDATA:", hght+"_"+low+"_"+len + "_" + byteToShort(low, hght));
//                Command.mShowDataQueue.offer((int) byteToShort(low, hght));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        //while ()
        //Log.v("dataThreadid:",""+Thread.currentThread().getId());
        if (isread ==false&&len%2==0) {
            for (int i = 0; i < buffer.length; i += 2) {
                byte hght = buffer[i];
                byte low = buffer[i + 1];
                short ecgdata=byteToShort(low, hght);
                //Log.v("ECGDATA:", hght + "_" + low + "_" + len + "_" + byteToShort(low, hght));
                Command.mShowDataQueue.offer(ecgdata);
                //Command.temp=ecgdata;
                Log.v("recievetime:",""+System.currentTimeMillis());
            }
        }
        else {
            bufferedInputStream.skip(bufferedInputStream.available()-4);
            return;
        }
    }

    private synchronized void commandhandle() throws IOException {
        int mcommand,marg;
//        byte[] bs=new byte[5];
//        marg=bufferedInputStream.read(bs);
        mcommand = bufferedInputStream.read();
        marg = bufferedInputStream.read();
        //Log.v("commandThreadid:",""+Thread.currentThread().getId());
        //Log.v("Command and Arg:", "" + mcommand + "" + marg);
        mHandler.obtainMessage(SetState(mcommand), marg).sendToTarget();
    }

    //设置状态
    public int SetState(int state) {
        switch (state) {
            case Command.intTest:
                return Command.SOCKET_ISNOMALC;
            case Command.intStartConnectOrder:
                return Command.SOCKET_COMUNICATIONCONNECTED;
            case Command.intStopSample:
                return Command.NOTSAMPLEING;
            case Command.intStartSample:
                return Command.SAMPLING;
            case Command.intStopConnect:
                return Command.SOCKET_STOPCONNECTE;
            default:
                return Command.SOCKET_NOTCOMUNICATIONCONNECTED;
        }
    }

    // 把byte 转化为两位十六进制数
    public String toHex(byte b) {
        String result = Integer.toHexString(b & 0xFF);
        if (result.length() == 1) {
            result = '0' + result;
        }
        return result;
    }

//    public Runnable GetThread() {
//        return new recieveThread();
//    }

    public void StopRead() {
        isread = true;
    }


    //    private void inputbreak(){
//        try {
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    //转换数据
    private short byteToShort(byte b, byte a) {
        short s = 0;
        short s0 = (short) (b & 0xff);// 最低位
        short s1 = (short) (a & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

//    private class recieveThread implements Runnable {
//        public void run() {
//            dataStream();
//        }
//    }
}
