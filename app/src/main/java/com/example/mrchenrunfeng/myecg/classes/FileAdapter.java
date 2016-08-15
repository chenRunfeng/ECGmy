package com.example.mrchenrunfeng.myecg.classes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrchenrunfeng.myecg.R;

public class FileAdapter extends BaseAdapter {
    public Activity activity; //创建View时必须要提供Context
    public List<File> list=new LinkedList<File>(); //数据源（文件列表）
    public String currPath;//当前路径
    private Bitmap bmp_folder,bmp_file;
    public int getCount() {
// TODO Auto-generated method stub
        return list.size();
    }
    public Object getItem(int arg0) {
// TODO Auto-generated method stub
        return null;
    }
    public long getItemId(int position) {
// TODO Auto-generated method stub
        return position;
    }
    public View getView(int position, View arg1, ViewGroup arg2) {
// TODO Auto-generated method stub
        View v=View.inflate(activity, R.layout.listitem,null);
        TextView Txt_Name=(TextView) v.findViewById(R.id.Txt_Name);
        TextView Txt_Size=(TextView) v.findViewById(R.id.Txt_Size);
        ImageView img=(ImageView) v.findViewById(R.id.image_ico);
        TextView Time=(TextView) v.findViewById(R.id.time);
        File f=list.get(position);
        Txt_Name.setText(f.getName());
        Txt_Size.setText(getFilesSize(f));
        Time.setText(getTime(f));
        img.setImageBitmap(bmp_file);
        return v;
    }
    public void scanFiles(String path)
    {
        list.clear();
        File dir=new File(path);
        File[] subFiles=dir.listFiles();
        if(subFiles!=null)
            for(File f:subFiles)
                list.add(f);
        this.notifyDataSetChanged();
        currPath=path;
    }
    public FileAdapter(Activity activity)
    {
        this.activity=activity;
        bmp_file=BitmapFactory.decodeResource(activity.getResources(),R.drawable.file);//文件
    }
    public static String getFilesSize(File f)
    {
        int sub_index=0;
        String show="";
        if(f.isFile())
        {
            long length=f.length();
            if(length>=1073741824)
            {
                sub_index=String.valueOf((float)length/1073741824).indexOf(".");
                show=((float)length/1073741824+"000").substring(0,sub_index+3)+"GB";
            }
            else if(length>=1048576)
            {
                sub_index=(String.valueOf((float)length/1048576)).indexOf(".");
                show=((float)length/1048576+"000").substring(0,sub_index+3)+"MB";
            }
            else if(length>=1024)
            {
                sub_index=(String.valueOf((float)length/1024)).indexOf(".");
                show=((float)length/1024+"000").substring(0,sub_index+3)+"KB";
            }
            else if(length<1024)
                show=String.valueOf(length)+"B";
        }
        return show;
    }

    public static String getTime(File f)
    {
        String time="未知";
        Date date;
        if(f.isFile()) {
            date = new Date(f.lastModified());//文件最后修改时间
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            time=sdf.format(date);
        }



        return time;
    }


}

