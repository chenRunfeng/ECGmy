package com.example.mrchenrunfeng.myecg.classes;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Chen RunFENG on 2016/8/10.
 */
public class HeartRateThread implements Runnable {
    List<heightdifference> arrayList = new ArrayList<>() ;
    private volatile boolean done = false;
    private  boolean isgetR0;
    short R0;//首个R波高度差
    int ecgtimes;//记录poll出来的心电个数
    int Rnumber;//记录r波个数
    int count;//记录经过n个R波的采样点数
    Handler handler;

    public HeartRateThread(Handler handler) {
        this.handler = handler;
    }

    //计算心率，用差分方程
    public void run() {
        isgetR0=true;
        R0=0;
        //ecgtimes=0;
        Rnumber=0;
        count=0;
        byte section=0;
        while (!done) {
            short[] shorts = new short[50];
            int i = 0;
            while (i < shorts.length) {
                if (done){break;}
                if (!Command.mAboutheartratedataListQueue.isEmpty()) {
                    shorts[i] = Command.mAboutheartratedataListQueue.poll();
                    i++;
                    //ecgtimes++;
                }
            }
            short maxecg = max(shorts);
            short minecg = min(shorts);
            short difference = (short)(maxecg - minecg) ;
            heightdifference heightdifference = new heightdifference(maxecg, minecg, difference);
            arrayList.add(heightdifference);
            if (arrayList.size() > 11 && isgetR0 == true) {
                MAXR0(shorts);
            }
            if (R0>0){
                if (arrayList.size()>ecgtimes) {
                    heightdifference heightdifference1=arrayList.get(ecgtimes);
                    short data=heightdifference1.getDifference();
                    if (section>=5){R0=(short) (R0*0.8);}
                    if (data >=0.9*R0) {
                       // Log.v("r0:", "" + R0);
                        Rnumber++;
                        //long secondtime = ecgtimes;
                        int index=Index(shorts,heightdifference1.getMax());
                        count=(ecgtimes-1)*shorts.length+index;
                        int heartrate = 60 * Command.SAMPLE_RATE /(count/ Rnumber) ;
                        Command.mHeartRateQueue.offer(heartrate);
                        handler.obtainMessage(Command.HEART_RATE).sendToTarget();
                        Log.v("b:", "" + heartrate + "__" + count+"__"+Rnumber+"__"+index+"__"+R0);
                        R0=data;
                        section=0;
                        //firsttime = secondtime;
                    }
                    else {section++;}
                    ecgtimes++;
                }
            }
        }
        arrayList.clear();
    }

    //求数组元素中的最大值
    public short max(short[] anArray) {
        short MaxValue = anArray[0];
        for (int i = 0; i < anArray.length; i++) {
            if (anArray[i] > MaxValue) MaxValue = anArray[i];
        }
        //System.out.println("数组元素的最大值为" + MaxValue);
        return (MaxValue);

    }

    //求数组元素中的最小值
    public short min(short[] anArray) {
        short MinValue = anArray[0];
        for (int i = 0; i < anArray.length; i++) {
            if (anArray[i] < MinValue) MinValue = anArray[i];
        }
        //System.out.println("数组元素的最小值为" + MinValue);
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
    public void MAXR0(short[] shorts) {
        short[] ss = new short[12];
        for (int j = 6; j < 12; j++) {
            ss[j] = arrayList.get(j).getDifference();
        }
        R0 = max(ss);
        Log.v("R0:",""+R0);
//        heightdifference heightdifference=getRmax(R0);
//        int arrindex=arrayList.indexOf(heightdifference)-6;
//        int index=Index(shorts,heightdifference.getMax());
//        if (index!=-1) {
//            count=(arrindex-1)*50+index;
//            Log.v("maxr0:",""+count+"__"+index+"__"+arrindex);
//        }
        //arrayList.clear();
        //int rindex = arrayList.indexOf(R0);
        ecgtimes=12;
        Rnumber++;
        isgetR0 = false;
    }
    public heightdifference getRmax(short arg){
        for (heightdifference h:arrayList
                ) {
            if (h.getDifference()==arg)return h;
        }
        return null;
    }

    private class heightdifference {
        private short max;
        private short min;
        private short difference;

        public heightdifference(short difference, short max, short min) {
            this.difference = difference;
            this.max = max;
            this.min = min;
        }

        public short getMax() {
            return max;
        }

        public short getDifference() {
            return difference;
        }
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

