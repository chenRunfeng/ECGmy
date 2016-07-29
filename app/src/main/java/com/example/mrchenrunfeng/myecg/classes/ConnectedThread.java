package com.example.mrchenrunfeng.myecg.classes;

import com.example.mrchenrunfeng.myecg.activity.MainActivity;
import android.os.Handler;
import android.util.Log;
import java.io.IOException;

/**
 * Created by Mr.Chen RunFENG on 2016/7/19.
 */
public class ConnectedThread extends Thread  implements Runnable {
   // Handler handler;
    BluetoothLink bluetoothLink;
    ECGServerThread ecgServerThread=null;
    public ConnectedThread(BluetoothLink bluetoothLink){
        this.bluetoothLink=bluetoothLink;
    }
    @Override
    public void run(){
        ecgServerThread=new ECGServerThread(bluetoothLink);
       // Log.v("connectedthreadsuccess",""+Command.intStartConnectOrder);
       ecgServerThread.sendCommand(Command.intFirstFrame,Command.intStartConnectOrder,0x00,0x00);
        Log.v("connectedthreadsuccess",""+Command.intStartConnectOrder);
    }
}
