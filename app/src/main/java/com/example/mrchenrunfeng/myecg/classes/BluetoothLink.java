package com.example.mrchenrunfeng.myecg.classes;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class BluetoothLink implements IBluetoothLink {
	
	private ConnectThread mConnectThread;
	private final Handler mHandler;
	
	public static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private BluetoothAdapter mAdapter=BluetoothAdapter.getDefaultAdapter();
	private BluetoothDevice device;
	private BluetoothSocket socket;
	
	private int mState;
	private final int NONE=1;
	private final int CONNECTING=2;
	boolean hasConnected=false;
	
	
	//���캯��
	public BluetoothLink(Handler handler, String address){
		this.mHandler=handler;
		device = mAdapter.getRemoteDevice(address);
		setState(NONE);
	}
	
	//����״̬
	private synchronized void setState(int state){
		mState = state;
	}
	
	//�õ���ǰ״̬
	public synchronized int getState(){
		return mState;
	}

	//����
	public void connect() {
		// TODO �Զ���ɵķ������
		if(getState() == CONNECTING){
			if(mConnectThread != null){
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		setState(CONNECTING);
	}

	//�Ͽ�
	public void disconnect() {
		// TODO �Զ���ɵķ������
		if(mConnectThread != null){
			mConnectThread.cancel();
			mConnectThread = null;
		}
		setState(NONE);
	}
	
	//�����߳�
	private class ConnectThread extends Thread{
		
		private BluetoothDevice mDevice;
		
		public ConnectThread(BluetoothDevice device){
			mDevice = device;
		}
		
		public void run(){
			
			BluetoothSocket tempSocket = null;
			
			try {
				tempSocket = mDevice.createRfcommSocketToServiceRecord(uuid);
			} catch (IOException e) {
				// TODO �Զ���ɵ� catch ��
				Log.v("ConnectThread", "create socket fail");
				e.printStackTrace();
			}
			try {
				if(mDevice.getBondState()==BluetoothDevice.BOND_NONE){
					Method creMethod = mDevice.getClass().getMethod("createBond");  	                        
					Log.e("TAG", "��ʼ���");  
					creMethod.invoke(mDevice);  
							
				}else{
					
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		   mAdapter.cancelDiscovery();
			try {
				tempSocket.connect();
				hasConnected=true;
			} catch (IOException e) {
				// TODO �Զ���ɵ� catch ��
				e.printStackTrace();
				try {
					tempSocket.close();
					Log.v("ConnectThread", "tempSocket closed");
					mHandler.obtainMessage(Command.SOCKET_NOTCONNCTED, -1, -1).sendToTarget();
					hasConnected = false;
				} catch (IOException e1) {
					// TODO �Զ���ɵ� catch ��
					e1.printStackTrace();
				}
			}
			if(hasConnected){
				socket = tempSocket;
				mHandler.obtainMessage(Command.SOCKET_CONNECTED, -1, -1).sendToTarget();
			}
		}

		private void cancel() {
			// TODO �Զ���ɵķ������
			try {
				if(socket!=null){
				   socket.close();
				}
			} catch (IOException e) {
				// TODO �Զ���ɵ� catch ��
				e.printStackTrace();
			}
		}
	}
		
	//�����׽���
	public BluetoothSocket getSocket(){
		return socket;
	}
	
}
