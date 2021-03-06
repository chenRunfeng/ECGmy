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
import com.example.mrchenrunfeng.myecg.classes.ECGLocalThread;
import com.example.mrchenrunfeng.myecg.classes.HeartRateThread;
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
    //final int ID_LED=19871103;
    private  IMainPresenter mainPresenter;
    private ProgressDialog pd;
    IECGSurfaceView iecgSurfaceView;
    //启动blueactivity.
    private Intent bluetooth;
    private Intent save;
    //启动扫描页面蓝牙请求连接代号
    private static final int REQUEST_CONNECT_DEVICE = 1;
    //启动保存文件列表启动代号
    private static final int SAVE_LIST=41;
    //播放按钮三种状态
    private  final byte SANPLING=1;
    private final byte NOTSAMPLING=2;
    private final byte DEFAULTSTART=0;
    //蓝牙地址
    private String address = null;
    ImageButton imbtnbluetooth, imbtnsave, imbtnplay, imbtnlist, imbtnexit;
    private BluetoothAdapter blueadapter;
    private byte btnstatus =DEFAULTSTART;//记录播放按钮的状态
    private boolean btnblue;//记录连接状态
    private TextView textView;
    TimerTask task = null;
    Timer timer = new Timer();
    Handler handler=new Handler();
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.imbtnplay:
                if (btnstatus==DEFAULTSTART) {
                    mainPresenter.StartSample();
                    iecgSurfaceView.StartDraw();
                    imbtnplay.setBackgroundResource(R.drawable.stop);
                    //drawheartrate();
                    btnstatus =SANPLING;
                } else if(btnstatus==SANPLING) {
                    mainPresenter.StopSample();
                    imbtnplay.setBackgroundResource(R.drawable.play);
                    iecgSurfaceView.StopDraw();
                    //task.cancel();
                    btnstatus = DEFAULTSTART;
                }
                else {
                    imbtnplay.setBackgroundResource(R.drawable.play);
                    iecgSurfaceView.StopDraw();
                    btnstatus=DEFAULTSTART;
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
            case R.id.imbtnlist:
                OpenSaveListview();
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
        iecgSurfaceView = (ECGSurfaceView) findViewById(R.id.ECGV);
        iecgSurfaceView.SetImainview(this);
//        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        notification = new Notification();
//        notification.ledARGB = 0xFFFFFF;  //这里是颜色，我们可以尝试改变，理论上0xFF0000是红色，0x00FF00是绿色
//        notification.ledOnMS = 100;
//        notification.ledOffMS = 100;
//        notification.flags = Notification.FLAG_SHOW_LIGHTS;
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
        imbtnlist=(ImageButton)findViewById(R.id.imbtnlist);
        imbtnlist.setOnClickListener(this);
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        //checkExternalStorageMounted();
    }
    //蓝牙搜索结果处理
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode==Activity.RESULT_OK) {
            mainPresenter=MainPresenterImpl.getUniqueInstance(this);
            if(requestCode==REQUEST_CONNECT_DEVICE ){
                address=data.getExtras().getString(BluetoothActivity.EXTRA_DEVICE_ADDRESS);
                mainPresenter.IniBlutoothLink(address);
                mainPresenter.Bluetoothsocketconnet();
            }
            else if (requestCode==SAVE_LIST){
                String filename=data.getExtras().getString(SaveListActivity.FILEPATH);
                mainPresenter.ReadLocalECG(filename);
                iecgSurfaceView.StartDrawL();
                imbtnplay.setBackgroundResource(R.drawable.stop);
                btnstatus=NOTSAMPLING;
                //HeartRateThread heartRateThread=new HeartRateThread()
            }
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

    @Override
    public void UpdateTxtheartrate() {
        if (!Command.mHeartRateQueue.isEmpty()) {
            int heartrate=Command.mHeartRateQueue.poll();
            textView.setText(String.valueOf(heartrate));
        }
    }

    public void OpenSaveListview(){
         save= new Intent();
        save.setClass(MainActivity.this, SaveListActivity.class);
        startActivityForResult(save, SAVE_LIST);
    }

    @Override
    public void StopHeartrate() {
        if (mainPresenter!=null) {
            mainPresenter.StopHeartrate();
        }
    }

    @Override
    public void StopLocalECG() {
        if (mainPresenter!=null) {
            mainPresenter.Stopreadlocalecg();
        }
    }

    protected void quit(){
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
