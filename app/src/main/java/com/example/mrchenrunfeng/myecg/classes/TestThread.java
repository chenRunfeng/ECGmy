package com.example.mrchenrunfeng.myecg.classes;

import com.example.mrchenrunfeng.myecg.activity.MainActivity;
import android.os.Handler;
import android.util.Log;
import java.io.IOException;

/**
 * Created by Mr.Chen RunFENG on 2016/7/19.
 */
public class TestThread extends Thread  implements Runnable {
   // Handler handler;
   private volatile boolean done = false;
    BluetoothLink bluetoothLink;
    ECGServer ecgServer=null;
    Handler handler;
    public TestThread(Handler handler,ECGServer ecgServer,BluetoothLink bluetoothLink){
        this.ecgServer=ecgServer;
        this.bluetoothLink=bluetoothLink;
        this.handler=handler;
    }
    @Override
    public void run(){
        while (!done) {
            try {
                Thread.sleep(1000);
                //Log.v("Test", "" + Command.intTest);
                ecgServer = new ECGServerThread(bluetoothLink);
                ecgServer.sendCommand(Command.intFirstFrame, Command.intTest, 0x00, 0x00);
                handler.obtainMessage(Command.intTest).sendToTarget();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void setDone(){
        done = true;
    }
}
