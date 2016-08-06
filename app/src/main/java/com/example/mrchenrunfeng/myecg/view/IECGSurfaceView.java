package com.example.mrchenrunfeng.myecg.view;

import java.io.PipedOutputStream;

/**
 * Created by Mr.Chen RunFENG on 2016/8/3.
 */
public interface IECGSurfaceView {
    void DrawBack();
    void StartDraw();
    void StopDraw();
    void SaveECG(String filename);
//    void ReadECG(String filneame);
}
