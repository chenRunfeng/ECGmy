package com.example.mrchenrunfeng.myecg.classes;

/**
 * Created by Mr.Chen RunFENG on 2016/7/29.
 */
public interface ECGServer {
     int sendCommand(int firstframe, int order, int arg1, int arg2);
}
