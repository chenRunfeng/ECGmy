package com.example.mrchenrunfeng.myecg.presenter;

/**
 * Created by Mr.Chen RunFENG on 2016/7/29.
 */
public interface IMainPresenter {
    void Bluetoothsocketconnet();
    void Connected();
    //MainPresenterImpl getUniqueInstance();
    void Test();
    void StartSample();
    void StopSample();
}
