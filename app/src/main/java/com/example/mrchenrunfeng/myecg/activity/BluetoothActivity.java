package com.example.mrchenrunfeng.myecg.activity;

import java.util.Set;

import com.example.mrchenrunfeng.myecg.*;
import com.example.mrchenrunfeng.myecg.view.IBlueToothView;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import  com.example.mrchenrunfeng.myecg.view.switchbutton.*;

public class BluetoothActivity extends Activity implements IBlueToothView{
	//SysApplication sysApplication = new SysApplication();
	public static final String action = "broadcast.action";
	private SwitchButton openBluetoothButton;
	private Button scanButton;
	public static String EXTRA_DEVICE_NAME="device_name", EXTRA_DEVICE_ADDRESS="device_address";
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;//������豸
	private ArrayAdapter<String> mNewDevicesArrayAdapter;//�·����豸
	private ListView pairedListView;//����listView�ؼ�
	private ListView newDevicesListView;
	//private  SwitchButton switchButton;
	//�˵��ؼ�
	private static MenuItem mI;
		
	private static MenuItem mI1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ���ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth);
		setResult(RESULT_CANCELED);
		openBluetoothButton =(SwitchButton) findViewById(R.id.openBluetooth);
		scanButton = (Button)findViewById(R.id.scan);

		// 开关按钮事件监听
		openBluetoothButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//mListenerFinish.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
				if(isChecked)
				{
				  OpenB();
				}
				else
				{
                  CloseB();
				}

			}
		});

		scanButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
		      Scan(v);
			}
		});

		//���listviewʵ��
		pairedListView = (ListView) findViewById(R.id.listView1);
		//����android�Դ�������
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);
		//�����Ѿ���Ե������豸����ӡ����Ϣ
		Set<BluetoothDevice> paireDevices = mBluetoothAdapter.getBondedDevices();
		if (paireDevices.size()>0) {

			for(BluetoothDevice device : paireDevices){
				mPairedDevicesArrayAdapter.add(device.getName()+"\n"+device.getAddress());				
			}			
		}
		else {
			mPairedDevicesArrayAdapter.add("No Device");
		}
		//���listviewʵ��
		newDevicesListView = (ListView) findViewById(R.id.listView2);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);	
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);
		
		//����һ��InterFliter���󣬽���action ָ��ΪBluetoothDevice.ACTION_FOUND
		//IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		//bluetoothReciver = new BluetoothReciver();
		//ע��㲥������
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); 
		this.registerReceiver(mReceiver, filter);

	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (mBluetoothAdapter.isEnabled()){
			openBluetoothButton.setChecked(true);
		}
		else {
			openBluetoothButton.setChecked(false);
		}
	}
	//onCreate��������
	@Override
	protected void onDestroy() {
		// TODO �Զ���ɵķ������
		super.onDestroy();
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.cancelDiscovery();
		}
		this.unregisterReceiver(mReceiver);
	}
	
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO �Զ���ɵķ������
			mBluetoothAdapter.cancelDiscovery();
			String info = ((TextView) view).getText().toString();
			if(!info.equalsIgnoreCase("No Device")){
				String name=info.substring(0,info.length()-17);
				String address = info.substring(info.length() - 17);
				Intent intent = new Intent();
				intent.putExtra(EXTRA_DEVICE_NAME,name);
				intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
				//�������������Activity��Ҫ���ص���ݷ��ص���Activity
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		}
	};
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO �Զ���ɵķ������
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getBondState()!= BluetoothDevice.BOND_BONDED) {
					mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				}
				else {
					mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				}
			}else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
				scanButton.setVisibility(View.VISIBLE);
				if (mNewDevicesArrayAdapter.getCount() == 0) {
					mNewDevicesArrayAdapter.add("No Device");
				}
			}
		}

	};
	@Override
	public void OpenB(){
		Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivity(enableIntent);//����δ���������һ����Ϣ��ʹ���߿���
	}
	@Override
	public void CloseB(){
		mBluetoothAdapter.disable();
		Intent inten=new Intent();
		inten.setAction("closeTestThead");
		inten.putExtra("close",0);
		BluetoothActivity.this.sendBroadcast(inten);
	}
	@Override
	public void Scan(View v){
		// TODO �Զ���ɵķ������
		if(!mBluetoothAdapter.isEnabled()) {
			Toast.makeText( getApplicationContext(),"请先打开蓝牙！",
					Toast.LENGTH_SHORT).show();
		}
		else if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}
		else {
			Toast.makeText( getApplicationContext(),"扫描中....",
					Toast.LENGTH_SHORT).show();
			mBluetoothAdapter.startDiscovery();
			mPairedDevicesArrayAdapter.clear();
			mNewDevicesArrayAdapter.clear();
			v.setVisibility(View.GONE);
//					Toast.makeText( getApplicationContext(),"扫描完成！！",
//							Toast.LENGTH_SHORT).show();
		}
	}
}
