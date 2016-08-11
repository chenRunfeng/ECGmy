package com.example.mrchenrunfeng.myecg.classes;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mr.Chen RunFENG on 2016/7/13.
 */
public class Command {
    public volatile static int intState = 0xffff;//记录通信状态,初始化为无效状态。
    //帧首
    public static final int intFirstFrame = 0xaa;
    //发送给下位机的命令帧
    public static final int intTest = 0x01;
    public static final int intStartConnectOrder = 0x0a;
    public static final int intQueryVersion = 0x02;
    public static final int intQueryID = 0x03;
    public static final int intQueryDataType = 0x04;

    public static final int intSetRate = 0x05;
    public static final int intStartSample = 0x06;
    public static final int intStopSample = 0x07;
    public static final int intSaveData = 0x08;
    public static final int intStopSaveData = 0x09;
    public static final int intStopConnect = 0x0b;
    //下位机响应帧类型
    public static final int intCType = 0x00;
    public static final int intData = 0x01;
    //public final int intIDRe=0xaa000300;

    //通信状态
    public final static int SOCKET_CONNECTED = 21;
    public final static int SOCKET_NOTCONNCTED = 20;
    public final static int SOCKET_ISNOMALC = 22;//通信是否正常
    public final static int SOCKET_COMUNICATIONCONNECTED = 23;
    public final static int SOCKET_NOTCOMUNICATIONCONNECTED = 24;
    public final static int SAMPLING = 25;
    public final static int NOTSAMPLEING = 26;
    public final static int SOCKET_NOTNOMALC = 27;
    public final static int SOCKET_CLOSED = 28;
    public final static int SOCKET_STOPCONNECTE=29;
    //超时
    public final static int TIME_OUT = 31;
    //临时储存心电数据队列
    //数据暂存队列 阻塞队列
    public static LinkedBlockingQueue<Short> mShowDataQueue=new LinkedBlockingQueue<Short>() ;
    public static Queue<Short> mAboutheartratedataListQueue=new LinkedBlockingQueue<Short>();
    public static Queue<Byte> mHeartRateQueue=new LinkedBlockingQueue<Byte>();
    public static final short ESCAPE_CHAR = 0xdb;
    public static final short SPECIAL_CHAR = 0xdd;
    public static final short SPECIAL_CHAR1 = 0xda;
}
