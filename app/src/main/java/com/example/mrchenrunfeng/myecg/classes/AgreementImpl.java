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
        ecgServerThread.sendCommand(Command.intFirstFrame,Command.intStartConnectOrder,0x00,0x00);
        //ecgServerThread.sendCommand(Command.intFirstFrame,Command.intStartConnectOrder,0x00,0x00);
      singleThreadExecutor.execute(ecgServerThread);
        //ecgServerThread.start();
    }
    public void ATest(){
        handlerThread=new HandlerThread("Test handler");
        handlerThread.start();
        threadsHandler=new ThreadsHandler(handlerThread.getLooper());
        cachedThreadPool.execute(new TestThread(threadsHandler,ecgServerThread,bluetoothLink));
    }
}
