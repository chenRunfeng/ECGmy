package com.example.mrchenrunfeng.myecg.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import  com.example.mrchenrunfeng.myecg.classes.*;

import com.example.mrchenrunfeng.myecg.R;
import com.example.mrchenrunfeng.myecg.presenter.IMainPresenter;
import com.example.mrchenrunfeng.myecg.presenter.MainPresenterImpl;
import com.example.mrchenrunfeng.myecg.view.ECGSurfaceView;
import com.example.mrchenrunfeng.myecg.view.IMainView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity implements IMainView,View.OnClickListener {
    private  IMainPresenter mainPresenter;
    private ProgressDialog pd;
    //启动blueactivity.
    private Intent lanyaIntent;
    private static boolean hasConnected = false;
    private int mstate;
    //启动扫描页面蓝牙请求连接代号
    private static final int REQUEST_CONNECT_DEVICE = 1;
    //蓝牙地址
    private String address = null;
    //private static ProcessData mProcessData;
    ImageButton imbtnbluetooth, imbtnleading, imbtnplay, imbtnlist, imbtnexit;
    private BluetoothAdapter blueadapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private boolean btnstatus = true;//记录播放按钮的状态
    private Handler handler;
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        ECGSurfaceView ecgSurfaceView = (ECGSurfaceView) findViewById(R.id.ECGV);
        switch (v.getId()) {
            case R.id.imbtnplay:
                if (btnstatus) {
                    ecgSurfaceView.startECG(btnstatus);
                    imbtnplay.setBackgroundResource(R.drawable.stop);
                    btnstatus = false;
                } else {
                    imbtnplay.setBackgroundResource(R.drawable.play);
                    ecgSurfaceView.stopECG(btnstatus);
                    btnstatus = true;
                }
                break;
            case R.id.imbtnbluetooth:
                if (!hasConnected) {
                    OpenBluetoothView();
                } else {

                }
                break;
            default:
                break;
        }
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            //textView.setText(intent.getExtras().getString("data"));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blueadapter= BluetoothAdapter.getDefaultAdapter();
        pd=new ProgressDialog(this);
        pd.setMessage("正在连接设备.......");
        if (blueadapter == null) {
            Toast.makeText(this, "蓝牙不可用！！",
                    Toast.LENGTH_SHORT).show();
            //finish();
            return;
        }
        imbtnplay=(ImageButton)findViewById(R.id.imbtnplay);
        imbtnplay.setOnClickListener(this);
        //
        imbtnbluetooth=(ImageButton)findViewById(R.id.imbtnbluetooth);
        imbtnbluetooth.setBackgroundResource(R.drawable.bluetooth1);
        imbtnbluetooth.setOnClickListener(this);
        IntentFilter filter = new IntentFilter(BluetoothActivity.action);
        registerReceiver(broadcastReceiver, filter);
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        enableBluetoothFunction();
        //checkExternalStorageMounted();
    }
    //开启蓝牙连接
    private void enableBluetoothFunction() {
        imbtnbluetooth = (ImageButton) findViewById(R.id.imbtnbluetooth);
        blueadapter = BluetoothAdapter.getDefaultAdapter();
    }
    //蓝牙搜索结果处理
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode==REQUEST_CONNECT_DEVICE && resultCode==Activity.RESULT_OK){
            address=data.getExtras().getString(BluetoothActivity.EXTRA_DEVICE_ADDRESS);
             mainPresenter=MainPresenterImpl.getUniqueInstance(this,address);
            mainPresenter.Bluetoothsocketconnet();
        }
    };
    @Override
    public void OpenBluetoothView(){
        lanyaIntent = new Intent();
        lanyaIntent.setClass(MainActivity.this, BluetoothActivity.class);
        startActivityForResult(lanyaIntent, REQUEST_CONNECT_DEVICE);
    }
    @Override
   public void Connect(){
            imbtnbluetooth.setBackgroundResource(R.drawable.bluetooth);
            pd.cancel();
    }
    @Override
    public void DisConnected(){
       pd.cancel();
        Toast.makeText(this,"建立连接失败！！",Toast.LENGTH_SHORT);
    }
    @Override
    public void Connecting(){
        pd.show();
    }
    @Override
    public void TestCommunication(){
        Toast.makeText(this,"通信正常！！",Toast.LENGTH_SHORT);
    }
}
