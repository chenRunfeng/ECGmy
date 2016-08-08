package com.example.mrchenrunfeng.myecg.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.mrchenrunfeng.myecg.classes.Agreement;
import com.example.mrchenrunfeng.myecg.classes.AgreementImpl;
import com.example.mrchenrunfeng.myecg.classes.BluetoothLink;
import com.example.mrchenrunfeng.myecg.classes.Command;
import com.example.mrchenrunfeng.myecg.classes.IBluetoothLink;
import com.example.mrchenrunfeng.myecg.view.IMainView;

/**
 * Created by Mr.Chen RunFENG on 2016/7/29.
 */
public class MainPresenterImpl implements IMainPresenter {
    private static MainPresenterImpl uniqueInstance = null;
    private Agreement agreement;
    private IMainView mainView;
    private IBluetoothLink iBluetoothLink;
    private static int mstate = 0;
    private static int timernumber=0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //mProcessData=new ProcessData(mHandler,mBluetoothLink);
            switch (msg.what) {
                case Command.SOCKET_CONNECTED:
                    mstate = Command.SOCKET_CONNECTED;
//                    handlerThread=new HandlerThread("posttask1");
//                    handlerThread.start();
//                    handler=new Handler(handlerThread.getLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //agreement.AStopTime();
                            Connected();
                        }
                    });
                    break;
                case Command.SOCKET_COMUNICATIONCONNECTED:
                    mstate = Command.SOCKET_COMUNICATIONCONNECTED;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            timernumberreturn0();
                            mainView.Connected();
                            Test();
                            agreement.AStopTime();
                        }
                    });
                    break;
                case Command.SOCKET_NOTCONNCTED:
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //agreement.AStartTime();
                            Bluetoothsocketconnet();
                        }
                    });
                    break;
                case Command.SOCKET_NOTCOMUNICATIONCONNECTED:
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mstate++;
                            agreement.AConnect();
                            Log.v("mstate:", "" + mstate);
                            if (mstate > 2) {
                                mainView.DisConnected();
                                iBluetoothLink.disconnect();
                                mstate = 0;
                            }
                        }
                    });
                    break;
                case Command.SOCKET_ISNOMALC:
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mainView.TestCommunication();
                        }
                    });
                    break;
                case Command.SOCKET_NOTNOMALC:
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mainView.NotConnecting();
                            agreement.AStopTest();
                            agreement.AConnect();
                        }
                    });
                    break;
                case Command.SOCKET_CLOSED:
                    mainView.LostConnect();
                    break;
                case Command.SAMPLING:
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            agreement.AStopTest();
                            agreement.ARecieveData();
                            mainView.Connected();
                        }
                    });
                    break;
                case Command.NOTSAMPLEING:
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mainView.Stopsampled();
                            agreement.AStopTime();
                            //agreement.AStartTest();
                        }
                    });
                    break;
                case Command.SOCKET_STOPCONNECTE:
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mainView.StopConnecte();
                            iBluetoothLink.disconnect();
                        }
                    });
                    break;
                case Command.TIME_OUT:
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            timernumberreturn0();
                           mainView.TimeOut();
                            iBluetoothLink.disconnect();
                        }
                    });
                default:
                    break;
            }
        }
    };

    private void timernumberreturn0() {
        if (timernumber!=0) {
            timernumber=0;
        }
    }

    private MainPresenterImpl(IMainView iMainView, String adress) {
        this.mainView = iMainView;
        BluetoothLink bluetoothLink = new BluetoothLink(mHandler, adress);
        this.iBluetoothLink = bluetoothLink;
        this.agreement = new AgreementImpl(mHandler, bluetoothLink);
    }

    public static synchronized MainPresenterImpl getUniqueInstance(IMainView iMainView, String adress) {
        if (uniqueInstance == null) {
            uniqueInstance = new MainPresenterImpl(iMainView, adress);
        }
        return uniqueInstance;
    }

    @Override
    public void Connected() {
        agreement.AConnect();
        //agreement.AStartTime();
    }

    @Override
    public void Bluetoothsocketconnet() {
        if (timernumber<1) {
            timernumber++;
            agreement.AStartTime();
        }
        mainView.Connecting();
        iBluetoothLink.connect();
    }

    public void Test() {
        //agreement.AStartTest();
    }

    @Override
    public void StartSample() {
        agreement.AStartSample();
    }

    @Override
    public void StopSample() {
        if (iBluetoothLink.getState()!=Command.SOCKET_NOTCONNCTED) {
            mainView.StopSampling();
            agreement.AStopSample();
        }
    }

    @Override
    public void StopConnect() {
       agreement.AStopConnect();
    }
}
