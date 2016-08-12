package com.example.mrchenrunfeng.myecg.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrchenrunfeng.myecg.R;
import com.example.mrchenrunfeng.myecg.classes.Command;
import com.example.mrchenrunfeng.myecg.presenter.IMainPresenter;
import com.example.mrchenrunfeng.myecg.presenter.MainPresenterImpl;
import com.example.mrchenrunfeng.myecg.view.ECGSurfaceView;
import com.example.mrchenrunfeng.myecg.view.IECGSurfaceView;
import com.example.mrchenrunfeng.myecg.view.IMainView;
import com.example.mrchenrunfeng.myecg.view.SaveDialogFragmemt;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements IMainView,View.OnClickListener,SaveDialogFragmemt.SaveInputListener {
    NotificationManager nm;
    Notification notification;
    final int ID_LED=19871103;
    private  IMainPresenter mainPresenter;
    private ProgressDialog pd;
    IECGSurfaceView iecgSurfaceView;
    //启动blueactivity.
    private Intent bluetooth;
    //启动扫描页面蓝牙请求连接代号
    private static final int REQUEST_CONNECT_DEVICE = 1;
    //蓝牙地址
    private String address = null;
    ImageButton imbtnbluetooth, imbtnsave, imbtnplay, imbtnlist, imbtnexit;
    private BluetoothAdapter blueadapter;
    private boolean btnstatus = true;//记录播放按钮的状态
    private boolean btnblue;//记录连接状态
    private TextView textView;
    TimerTask task = null;
    Timer timer = new Timer();
    Handler handler=new Handler();
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        iecgSurfaceView = (ECGSurfaceView) findViewById(R.id.ECGV);
        switch (v.getId()) {
            case R.id.imbtnplay:
                if (btnstatus) {
                    mainPresenter.StartSample();
                    iecgSurfaceView.StartDraw();
                    imbtnplay.setBackgroundResource(R.drawable.stop);
                    drawheartrate();
                    btnstatus = false;
                } else {
                    mainPresenter.StopSample();
                    imbtnplay.setBackgroundResource(R.drawable.play);
                    iecgSurfaceView.StopDraw();
                    task.cancel();
                    btnstatus = true;
                }
                break;
            case R.id.imbtnbluetooth:
                if(btnblue==false){
                    OpenBluetoothView();
                }
                else {
                    mainPresenter.StopConnect();
                }
                break;
            case R.id.imbtnexit:
                quit();
                break;
            case R.id.imbtnsave:
                showLoginDialog();
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
        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.ledARGB = 0xFFFFFF;  //这里是颜色，我们可以尝试改变，理论上0xFF0000是红色，0x00FF00是绿色
        notification.ledOnMS = 100;
        notification.ledOffMS = 100;
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
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
        btnblue=false;
        imbtnbluetooth.setOnClickListener(this);
        imbtnexit=(ImageButton)findViewById(R.id.imbtnexit);
        imbtnexit.setOnClickListener(this);
        imbtnsave =(ImageButton)findViewById(R.id.imbtnsave);
        //imbtnsave=(ImageButton)findViewById(R.id.imbtnsave);
        imbtnsave.setOnClickListener(this);
        IntentFilter filter = new IntentFilter(BluetoothActivity.action);
        registerReceiver(broadcastReceiver, filter);
        textView=(TextView)findViewById(R.id.txtheartratecontent);
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        //checkExternalStorageMounted();
    }
    //蓝牙搜索结果处理
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode==REQUEST_CONNECT_DEVICE && resultCode==Activity.RESULT_OK){
            address=data.getExtras().getString(BluetoothActivity.EXTRA_DEVICE_ADDRESS);
             mainPresenter=MainPresenterImpl.getUniqueInstance(this,address);
            mainPresenter.Bluetoothsocketconnet();
        }
    }
    //保存数据
    public void showLoginDialog() {
        SaveDialogFragmemt dialog = new SaveDialogFragmemt();
        dialog.show(getFragmentManager(), "保存数据");

    }
    @Override
    public void onSaveInputComplete(String filename, int cmd)
    {
        if (cmd== Command.intSaveData){
            iecgSurfaceView.SaveECG(filename);
        }
    }
    @Override
    public void OpenBluetoothView(){
        bluetooth = new Intent();
        bluetooth.setClass(MainActivity.this, BluetoothActivity.class);
        startActivityForResult(bluetooth, REQUEST_CONNECT_DEVICE);
    }
    @Override
   public void Connected(){
            btnblue=true;
            imbtnbluetooth.setBackgroundResource(R.drawable.bluetooth);
            imbtnbluetooth.setAlpha((float)1);
            pd.dismiss();
    }
    @Override
    public void DisConnected(){
       pd.dismiss();
        Toast.makeText(getApplicationContext(),"建立连接失败！！",Toast.LENGTH_LONG).show();
    }
    @Override
    public void Connecting(){
        pd.setMessage("正在连接设备.......");
        pd.show();
    }
    @Override
    public void TestCommunication(){
        if(imbtnbluetooth.getAlpha()==(float) 0){
            imbtnbluetooth.setAlpha((float)1);
        }
        else {
            imbtnbluetooth.setAlpha((float)0);
        }
    }

    @Override
    public void NotConnecting() {
        imbtnbluetooth.setAlpha((float)1);
        imbtnbluetooth.setBackgroundResource(R.drawable.bluetooth1);
        btnblue=false;
        pd.setMessage("正在重新建立连接......");
        //Toast.makeText(getApplication(),"正在重新建立连接！！",Toast.LENGTH_SHORT);
        //Connecting();
        pd.show();
    }

    @Override
    public void LostConnect() {
        Toast.makeText(getApplicationContext(),"连接失败,检查设备，重新连接蓝牙！！",Toast.LENGTH_LONG).show();
    }

    @Override
    public void StopSampling() {
        pd.setMessage("正在停止采样......");
        pd.show();
    }

    @Override
    public void Stopsampled() {
        pd.dismiss();
    }

    @Override
    public void StopConnecte() {
        imbtnbluetooth.setAlpha((float)1);
        imbtnbluetooth.setBackgroundResource(R.drawable.bluetooth1);
        btnblue=false;
    }

    @Override
    public void TimeOut() {
        pd.dismiss();
        Toast.makeText(getApplicationContext(),"连接超时，请重新连接！！",Toast.LENGTH_LONG).show();
        StopConnecte();
    }

    protected void quit(){
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
    private void drawheartrate() {
        if (task != null) {
            task.cancel();
        }
        task = new TimerTask() { //新建任务
            //                int bx=canvaswidth * 12 / 13-canvaswidth * 5 / 100;
//                int ex=canvaswidth * 12 / 13+canvaswidth * 5 / 100;
            @Override
            public void run() {
                if (!Command.mHeartRateQueue.isEmpty()) {
                    long data=Command.mHeartRateQueue.poll();
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            textView.setText((int)Command.mHeartRateQueue.poll());
//                        }
//                    });
                }
                //Canvas c=holder.lockCanvas(new Rect(,))
            }
        };
        timer.schedule(task, 0, 1000); //隔1ms被执行一次该循环任务画出图形
    }
}
