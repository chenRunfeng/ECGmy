package com.example.mrchenrunfeng.myecg.classes;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Chen RunFENG on 2016/8/10.
 */
public class HeartRateThread implements Runnable {
    List<Short> arrayList = new ArrayList<Short>() ;
    private volatile boolean done = false;
    private  boolean isgetR0;
    int R0;//首个R波高度差
    long ecgtimes;//记录poll出来的心电个数
    int Rnumber;//记录r波个数
    long count;//记录经过n个R波的采样点数
    Handler handler;

    public HeartRateThread(Handler handler) {
        this.handler = handler;
    }

    //计算心率，用差分方程
    public void run() {
        isgetR0=true;
        R0=0;
        ecgtimes=0;
        Rnumber=0;
        count=0;
        while (!done) {
            short[] shorts = new short[50];
            int i = 0;
            while (i < shorts.length) {
                if (!Command.mAboutheartratedataListQueue.isEmpty()) {
                    shorts[i] = Command.mAboutheartratedataListQueue.poll();
                    i++;
                    ecgtimes++;
                }
            }
            if (ecgtimes>250) {
                short maxecg=max(shorts);
                short minecg=min(shorts);
                int difference=maxecg-minecg;
                arrayList.add((short)difference);
                if (arrayList.size()>6&&isgetR0==true){
                    short[] ss=new short[7];
                    for (int j=0;i<7;j++){
                        ss[j]= arrayList.get(j);
                    }
                    R0=max(ss);
                    int rindex=arrayList.indexOf(R0);
                    Rnumber++;
                    isgetR0=false;
                }
            }
            if (R0>0){
                if (!arrayList.isEmpty()) {
                    short data = arrayList.poll();
                    if (data >= (float) (0.8 * R0)) {
                        Log.v("r0:", "" + R0);
                        long secondtime = ecgtimes;
                        long b = 60 * 1000 / 4 / (secondtime - firsttime);
                        //Command.mHeartRateQueue.offer(b);
                        handler.obtainMessage(Command.HEART_RATE, b).sendToTarget();
                        Log.v("b:", "" + b + "__" + (secondtime - firsttime));
                        //R0=data;
                        firsttime = secondtime;
                    }
                }
            }
        }
    }

    //求数组元素中的最大值
    public short max(short[] anArray) {
        short MaxValue = anArray[0];
        for (int i = 0; i < anArray.length; i++) {
            if (anArray[i] > MaxValue) MaxValue = anArray[i];
        }
        System.out.println("数组元素的最大值为" + MaxValue);
        return (MaxValue);

    }

    //求数组元素中的最小值
    public short min(short[] anArray) {
        short MinValue = anArray[0];
        for (int i = 0; i < anArray.length; i++) {
            if (anArray[i] < MinValue) MinValue = anArray[i];
        }
        System.out.println("数组元素的最小值为" + MinValue);
        return (MinValue);
    }
    public int Index(short[] array,short arg ){
        for (int i=0;i<array.length;i++){
            if (array[i]==arg)return i;
        }
        return -1;
    }

    public void setDone() {
        done = true;
    }

//    public short MAXR0() {
//        short r0;
//        int i = 0;
//        short[] ints = new short[5];
//        while (!arrayList.isEmpty()) {
//            short data = arrayList.poll();
//                if (i < ints.length) {
//                    ints[i] = data;
//                    i++;
//                    continue;
//                } else {
//                    R0 = max(ints);
//                    firsttime = ecgtimes;
//                    Log.v("R01:", "" + R0);
//                    break;
//                }
//            }
//    }

//    public Runnable UpdateHeartRate() {
//        return new Runnable() {
//            @Override
//            public void run() {
//                while (!done) {
////                    try {
////                        Thread.sleep(200);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
//                    if (R0 > 0) {
//                        if (!arrayList.isEmpty()) {
//                            short data = arrayList.poll();
//                            ecgtimes += 50;
//                            if (data >= (float) (0.95 * R0)) {
//                                Log.v("r0:", "" + R0);
//                                long secondtime = ecgtimes;
//                                long b = 60 * 1000 / 4 / (secondtime - firsttime);
//                                //Command.mHeartRateQueue.offer(b);
//                                handler.obtainMessage(Command.HEART_RATE, b).sendToTarget();
//                                Log.v("b:", "" + b + "__" + (secondtime - firsttime));
//                                //R0=data;
//                                firsttime = secondtime;
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
}

