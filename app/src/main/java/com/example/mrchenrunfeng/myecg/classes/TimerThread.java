package com.example.mrchenrunfeng.myecg.classes;

import android.os.Handler;

import java.util.Timer;

/**
 * Created by Mr.Chen RunFENG on 2016/8/3.
 */
public class TimerThread implements Runnable {
    private volatile boolean done=false;
    final int maxtime=15;
    int time;
    Handler handler;

    public TimerThread(Handler handler) {
        this.handler = handler;
    }
    //Timer timer=new Timer();
    public void run(){
        time=0;
        while (!done) {
            try {
                Thread.sleep(1000);
                time++;
                if (time>=maxtime){
                   handler.obtainMessage(Command.TIME_OUT).sendToTarget();
                    SetDone();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void SetDone(){
        done=true;
    }
}
