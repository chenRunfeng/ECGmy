package com.example.mrchenrunfeng.myecg.classes;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Mr.Chen RunFENG on 2016/7/31.
 */
public class ThreadsHandelr extends Handler {
    /**
     * 使用默认的构造函数，会将handler绑定当前UI线程的looper。
     * 如果想使用多线程这里是不能使用默认的构造方法的。
     */
    public ThreadsHandelr() {
        super();
    }

    public ThreadsHandelr(Looper looper) {
        super(looper);
    }


}
