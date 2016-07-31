package com.example.mrchenrunfeng.myecg.classes;

import android.os.Handler;

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
    private  ExecutorService cachedThreadPool;
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
       ECGServerThread ecgServerThread=new ECGServerThread(handler,bluetoothLink);
        ecgServerThread.sendCommand(Command.intFirstFrame,Command.intStartConnectOrder,0x00,0x00);
        //ecgServerThread.sendCommand(Command.intFirstFrame,Command.intStartConnectOrder,0x00,0x00);
      //singleThreadExecutor.submit(ecgServerThread);
        ecgServerThread.start();
    }
}
