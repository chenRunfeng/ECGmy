package com.example.mrchenrunfeng.myecg.classes;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mr.Chen RunFENG on 2016/7/29.
 */
public class AgreementImpl implements Agreement {
    private Handler handler;
    public BluetoothLink bluetoothLink;
    private  ExecutorService singleThreadExecutor;
    private ECGServerThread ecgServerThread;
    private  ExecutorService cachedThreadPool;
    private HandlerThread handlerThread;
    private ThreadsHandler threadsHandler;
    private TestThread testThread;
    /**
     * Created by Mr.Chen RunFENG on 2016/7/31.
     */
    private class ThreadsHandler extends Handler {
        /**
         * 使用默认的构造函数，会将handler绑定当前UI线程的looper。
         * 如果想使用多线程这里是不能使用默认的构造方法的。
         */
        public ThreadsHandler() {
            super();
        }

        public ThreadsHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg){
            if (msg.what==Command.intTest){
                this.post(new Runnable() {
                    @Override
                    public void run() {
                        singleThreadExecutor.execute(ecgServerThread);
                    }
                });
            }
            else if (msg.what==Command.SOCKET_NOTNOMALC){
                this.post(new Runnable() {
                    @Override
                    public void run() {
                        handler.obtainMessage(Command.SOCKET_NOTNOMALC).sendToTarget();
                    }
                });
            }
        }
    }
    public AgreementImpl(Handler h,BluetoothLink b){
        handler=h;
        bluetoothLink=b;
        singleThreadExecutor= Executors.newSingleThreadExecutor();
        cachedThreadPool=Executors.newCachedThreadPool();
    }
    @Override
    public void AConnect(){
       // new Thread(new ConnectedThread(bluetoothLink)).start();
      //cachedThreadPool.execute(new ConnectedThread(bluetoothLink));
      ecgServerThread=new ECGServerThread(handler,bluetoothLink);
      int isrecive=ecgServerThread.sendCommand(Command.intFirstFrame,Command.intStartConnectOrder,0x00,0x00);
        if (isrecive==Command.SOCKET_ISNOMALC){
            //ecgServerThread.sendCommand(Command.intFirstFrame,Command.intStartConnectOrder,0x00,0x00);
            singleThreadExecutor.execute(ecgServerThread);
            //ecgServerThread.start();
        }
        else if (isrecive==Command.SOCKET_NOTNOMALC){
            handler.obtainMessage(Command.SOCKET_NOTCOMUNICATIONCONNECTED).sendToTarget();
        }
        else{
            handler.obtainMessage(Command.SOCKET_CLOSED).sendToTarget();
        }
    }
    @Override
    public void AStartTest(){
//        handlerThread=new HandlerThread("Test handler");
//        handlerThread.start();
//        threadsHandler=new ThreadsHandler(handlerThread.getLooper());
//        testThread=new TestThread(threadsHandler,ecgServerThread,bluetoothLink);
//        //new Thread(new TestThread(threadsHandler,ecgServerThread,bluetoothLink)).start();
//        cachedThreadPool.execute(testThread);
    }
    @Override
    public void AStopTest() {
        if (testThread!=null)
            testThread.setDone();
    }

    @Override
    public void AStartSample() {
        if (ecgServerThread.sendCommand(Command.intFirstFrame,Command.intStartSample,0,0)==Command.SOCKET_ISNOMALC){
        singleThreadExecutor.execute(ecgServerThread);
        }
    }

    @Override
    public void ARecieveData() {
       // singleThreadExecutor.execute(ecgServerThread);
    }

    @Override
    public void AStopSample() {
        if (ecgServerThread.sendCommand(Command.intFirstFrame,Command.intStopSample,0,0)==Command.SOCKET_ISNOMALC){
            //停止接收数据线程
           //ecgServerThread.setDone();
            //开启接收命令线程
            singleThreadExecutor.execute(ecgServerThread);
        }
    }

    @Override
    public void AStopConnect() {
        if (ecgServerThread.sendCommand(Command.intFirstFrame,Command.intStopConnect,0,0)==Command.SOCKET_ISNOMALC){
            singleThreadExecutor.execute(ecgServerThread);
        }
    }
}
