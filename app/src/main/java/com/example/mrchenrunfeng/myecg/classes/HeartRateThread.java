package com.example.mrchenrunfeng.myecg.classes;

import java.util.List;

/**
 * Created by Mr.Chen RunFENG on 2016/8/10.
 *
 */
public class HeartRateThread implements Runnable{
    private List<Integer> listecgrate;//用于计算心率的暂存心电数据

    public HeartRateThread(List<Integer> listecgrate) {
        this.listecgrate = listecgrate;
    }
     //计算心率，用差分方程
    public void run(){
        for (int ecg:listecgrate
             ) {

        }
    }
}

