package com.example.mrchenrunfeng.myecg.classes;

/**
 * Created by Mr.Chen RunFENG on 2016/7/29.
 */
public interface Agreement {
    void AConnect();
    void AStartTest();
    void AStopTest();
    void AStartSample();
    void ARecieveData();
    void AStopSample();
    void AStopConnect();
    void AStartTime();
    void AStopTime();
    void AStartHeartRate();
    void AStopHeartRate();
    void AReadLocalECG(String filepath);
    void AIniBluetoothLink(BluetoothLink b);
    void AStopLocalECG();
   // void BlueConnect();
}
