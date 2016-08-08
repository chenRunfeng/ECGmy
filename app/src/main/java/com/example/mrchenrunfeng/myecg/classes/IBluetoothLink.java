package com.example.mrchenrunfeng.myecg.classes;

import android.bluetooth.BluetoothSocket;

/**
 * Created by Mr.Chen RunFENG on 2016/7/29.
 */
public interface IBluetoothLink {
    void connect();
    void disconnect();
    BluetoothSocket getSocket();
    int getState();
}
