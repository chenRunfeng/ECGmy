package com.example.mrchenrunfeng.myecg.activity;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.mrchenrunfeng.myecg.R;
import com.example.mrchenrunfeng.myecg.classes.FileAdapter;

public class SaveListActivity extends Activity implements OnClickListener,OnItemClickListener{
    /** Called when the activity is first created. */
    public static final String FILEPATH="file";
    private ListView List_View;
    private FileAdapter Adter;
    FileAdapter ad;
    File f;
    String Path;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savelist);
        List_View=(ListView) findViewById(R.id.List_View);
        Adter=new FileAdapter(this);
        List_View.setAdapter(Adter);
        ad=(FileAdapter) List_View.getAdapter();
        List_View.setOnItemClickListener(this);
        List_View.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                FileAdapter da = (FileAdapter) List_View.getAdapter();
                final File f = da.list.get(position);
                if(f.isFile()) {
                    DeleteDialog(f,da.list);
                }
                return false;
            }
        });
        Path= Environment.getExternalStorageDirectory().getPath()+"//ECGDATA//";//输入路径
        Adter.scanFiles(Path);
        ad=(FileAdapter) List_View.getAdapter();
        f=new File(ad.currPath);
    }
    public void onClick(View v) {
// TODO Auto-generated method stub
        if(ad.currPath.equals("/")) return;
        ad.scanFiles(f.getParent());
    }
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
// TODO Auto-generated method stub
        //获得选中项的HashMap对象
        FileAdapter da = (FileAdapter) List_View.getAdapter();
        final File f = da.list.get(position);
        String filename=f.getName();
            Intent intent = new Intent();
            intent.putExtra(FILEPATH,filename);
            //�������������Activity��Ҫ���ص���ݷ��ص���Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
       // }

    }
    private void DeleteDialog(final File f,final List<File> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SaveListActivity.this);

        builder.setMessage("确定删除文件？");
        builder.setTitle("提示");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里File构造方法参数就是从你list读取的文件路径

                f.delete();
                //通知adapter 更新
                list.remove(f);
                Adter.notifyDataSetChanged();
                List_View.setAdapter(Adter);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }
    public String getPath(){
        return ad.currPath;
    };
}

