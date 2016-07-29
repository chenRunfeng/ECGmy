package com.example.mrchenrunfeng.myecg.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.mrchenrunfeng.myecg.classes.Agreement;
import com.example.mrchenrunfeng.myecg.classes.AgreementImpl;
import com.example.mrchenrunfeng.myecg.classes.BluetoothLink;
import com.example.mrchenrunfeng.myecg.classes.Command;
import com.example.mrchenrunfeng.myecg.classes.ConnectedThread;
import com.example.mrchenrunfeng.myecg.classes.IBluetoothLink;
import com.example.mrchenrunfeng.myecg.view.IMainView;

/**
 * Created by Mr.Chen RunFENG on 2016/7/29.
 */
public class MainPresenterImpl implements IMainPresenter {
    private Agreement agreement;
    private IMainView mainView;
    private IBluetoothLink iBluetoothLink;
    private int mstate;
    private Handler mHandler = new Handler();//{
//        @Override
//        public void handleMessage(Message msg){
//            //mProcessData=new ProcessData(mHandler,mBluetoothLink);
//            switch(msg.what){
//                case Command.SOCKET_CONNECTED:
//                    mstate=Command.SOCKET_CONNECTED;
//                    break;
//                case Command.SOCKET_COMUNICATIONCONNECTED:
//                    mstate=Command.SOCKET_COMUNICATIONCONNECTED;
//                    break;
//                case Command.SOCKET_NOTCONNCTED:
//                    break;
//                case Command.SOCKET_NOTCOMUNICATIONCONNECTED:
//                   break;
//                default:
//                    break;
//            }
//        }
//    };
    public MainPresenterImpl(IMainView iMainView, String adress){
        this.mainView=iMainView;
        BluetoothLink bluetoothLink=new BluetoothLink(mHandler,adress);
        this.iBluetoothLink=bluetoothLink;
        this.agreement=new AgreementImpl(mHandler,bluetoothLink);
    }
    @Override
    public void Connected(){
        mstate=Command.intState;
      for (int i=0;i<3;i++){
          agreement.AConnect();
          mHandler=new Handler(){
              @Override
              public void handleMessage(Message msg){
                  Log.v("mHandler",""+msg.what);
                  if (msg.what==Command.SOCKET_COMUNICATIONCONNECTED){
                      mHandler.post(new Runnable() {
                          @Override
                          public void run() {
                            mainView.Connect();
                          }
                      });
                      mstate=Command.intStartConnectOrder;
                  }
              }
          };
          if (mstate==Command.intStartConnectOrder){
              return;
          }
          else {iBluetoothLink.connect();continue;}
      }
     mainView.DisConnected();
     iBluetoothLink.disconnect();
    }
    @Override
   public void Bluetoothsocketconnet(){
        iBluetoothLink.connect();
    }
}
