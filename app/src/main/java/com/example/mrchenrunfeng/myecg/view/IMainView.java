package com.example.mrchenrunfeng.myecg.view;

import android.view.View;

/**
 * Created by Mr.Chen RunFENG on 2016/7/29.
 */
public interface IMainView {
  void DisConnected();
  void OpenBluetoothView();
    void Connected();
  void Connecting();
  void TestCommunication();
  void NotConnecting();
  void LostConnect();
  void StopSampling();
  void Stopsampled();
  void StopConnecte();
  void TimeOut();
  void showLoginDialog();
//    void Play();
//    void Close();
//    void Save();
//    void OpenSaveView();
//    void Exit();
}
