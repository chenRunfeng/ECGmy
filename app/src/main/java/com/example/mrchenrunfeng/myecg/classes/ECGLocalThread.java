package com.example.mrchenrunfeng.myecg.classes;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Mr.Chen RunFENG on 2016/8/8.
 */
public class ECGLocalThread implements Runnable {
    private String filname;

    public ECGLocalThread(String filname) {
        this.filname = filname;
    }

    @Override
    public void run() {
        readfile();
    }

    private void readfile() {
        // 先判断是否有SDCard
        if ((Environment.getExternalStorageState() != null) && (filname != null)) {

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                // 创建一个文件夹对象，赋值为外部存储器的目录
                File sdcardDir = Environment.getExternalStorageDirectory();
                //得到一个路径，内容是sdcard的文件夹路径和名字
                String path = sdcardDir.getPath() + "//ECGDATA//";
                File path1 = new File(path);
                File file = new File(path1, filname);
                FileInputStream fin = null;
                BufferedInputStream bos = null;
                try {
                    fin = new FileInputStream(file);
                    bos = new BufferedInputStream(fin);

                } catch (FileNotFoundException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();

                }
                DataInputStream dos = new DataInputStream(bos);

                System.out.println(Command.mShowDataQueue.isEmpty());
//                if (!Command.mShowDataQueue.isEmpty()) {
                    Command.mShowDataQueue.clear();
                    try {
                        while (true){
                            double data=dos.readDouble();
                            Command.mShowDataQueue.offer((short)data);
                            Log.v("localecg:",""+data);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //while (true)
                //}
            }
        }

    }
}
