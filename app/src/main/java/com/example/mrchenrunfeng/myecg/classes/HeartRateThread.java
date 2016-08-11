package com.example.mrchenrunfeng.myecg.classes;

import android.util.Log;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Mr.Chen RunFENG on 2016/8/10.
 *
 */
public class HeartRateThread implements Runnable{
    Queue<Short> queueheightdiference=new LinkedBlockingQueue<>();
    private volatile boolean done = false;
    int R0=0;//R波高度差
     long firsttime;
     //计算心率，用差分方程
    public void run(){
        while (!done) {
            short[] shorts=new short[250];
            int i=0;
            while (i<shorts.length){
                if (!Command.mAboutheartratedataListQueue.isEmpty()){
                    shorts[i]=Command.mAboutheartratedataListQueue.poll();
                    i++;
                }
            }
            queueheightdiference.offer((short)(max(shorts)-min(shorts)));
        }
    }
    //求数组元素中的最大值
    public short max(short[] anArray)
    {
        short MaxValue=anArray[0];
        for(int i=0;i<anArray.length;i++)
        {
            if(anArray[i]>MaxValue)MaxValue=anArray[i];
        }
        System.out.println("数组元素的最大值为"+MaxValue);
        return(MaxValue);

    }

    //求数组元素中的最小值
    public short min(short[] anArray)
    {
        short MinValue=anArray[0];
        for(int i=0;i<anArray.length;i++)
        {
            if(anArray[i]<MinValue)MinValue=anArray[i];
        }
        System.out.println("数组元素的最小值为"+MinValue);
        return(MinValue);
    }
    public void setDone(){
        done = true;
    }
    public Runnable MAXR0(){
        return new Runnable() {
            @Override
            public void run() {
                int i=0;
                short[] ints=new short[5];
                while (true){
                    if (!queueheightdiference.isEmpty()) {
                        short data=queueheightdiference.poll();
                        if (i<ints.length){
                            ints[i]=data;
                            i++;
                            continue;
                        }
                        else {
                            R0=max(ints);
                            firsttime=System.currentTimeMillis();
                            Log.v("R0:",""+R0);
                            break;
                        }
                    }
                    else {continue;}
                }
            }
        };
    }
    public Runnable UpdateHeartRate(){
        return new Runnable() {
            @Override
            public void run() {
                while (!done){
                    if (R0>0){
                       if (!queueheightdiference.isEmpty()){
                            short data=queueheightdiference.poll();
                           if (R0>900)
                               R0=R0/3;
                           if (data>=(float)0.7*R0){
                               long secondtime=System.currentTimeMillis();
                               byte b=(byte) (60*1000/(secondtime-firsttime));
                               Command.mHeartRateQueue.offer(b);
                               Log.v("b:",""+b);
                               R0=data;
                               firsttime=secondtime;
                           }
                       }
                    }
                }
            }
        };
    }
}

