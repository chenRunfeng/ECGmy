package com.example.mrchenrunfeng.myecg.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;

import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrchenrunfeng.myecg.R;
import com.example.mrchenrunfeng.myecg.activity.MainActivity;
import com.example.mrchenrunfeng.myecg.classes.Command;
import com.example.mrchenrunfeng.myecg.classes.FirFilter;
import com.example.mrchenrunfeng.myecg.classes.IFirFilter;
import com.example.mrchenrunfeng.myecg.classes.IirFilter;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ECGSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback, IECGSurfaceView {
    DrawThread drawThread = null;
    private DrawThread1 drawThread1;
    List<Double> mSaveData = new ArrayList<>();
    int lStartX;
    int centerY;
    int paintflag = 1;//绘图是否暂停标志位，0为暂停
    final int ECGTIMES = 2;
    final int ECG_1MV_DATA = 324;//1mv心电数据参考值
    float flmvwidth;
    private  ExecutorService singleThreadExecutor;
    //int simpleHeight;//记录心率的纵坐标
    //控制对象
    private SurfaceHolder holder = null;
    /**
     * 创建画笔
     */
    private Paint linePaint = new Paint();
    private Canvas mCanvas;
    private int canvaswidth;
    private int canvasheigth;
    private IMainView iMainView;
//	/**
//	 * 右边部分平均每一行所占高度
//	 */

    public ECGSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        singleThreadExecutor= Executors.newSingleThreadExecutor();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("surfaceCreated!!");
//        holder=getHolder();
//        this.holder = holder;
//        this.holder.addCallback(this);
        DrawBack();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("surfaceChanged!!");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("surfaceDestroyed!!");
      resetvalues();
        //cachedThreadpool.shutdown();
    }

    public void StartDraw() {
//        holder = getHolder();
//        holder.addCallback(this);
        mSaveData.clear();
        drawThread = new DrawThread();
//        drawThread.start();
        singleThreadExecutor.execute(drawThread);
        paintflag = 1;
    }

    public void StopDraw() {
        resetvalues();
    }

    @Override
    public void StartDrawL() {
        mSaveData.clear();
        drawThread1 = new DrawThread1();
//        drawThread.start();
        singleThreadExecutor.execute(drawThread1);
        paintflag = 1;
    }

    @Override
    public void StopDrawL() {

    }

    //重置相关变量
    private void resetvalues() {
        paintflag = 0;
        Command.mShowDataQueue.clear();
        Command.mHeartRateQueue.clear();
        Command.mAboutheartratedataListQueue.clear();
        iMainView.StopHeartrate();
        iMainView.StopLocalECG();
        Command.mShowDataQueue1.clear();
    }


    /**
     * 画图的方法
     */
    public  void DrawBack() {
        try {
            //timer.cancel();
            paintflag = 0;
            mCanvas = holder.lockCanvas();
//            Paint paint=new Paint();
            linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mCanvas.drawPaint(linePaint);
            linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            mCanvas.drawColor(Color.BLACK);
            canvaswidth = mCanvas.getWidth();
            canvasheigth = mCanvas.getHeight();
            int height = canvasheigth;
            int width = canvaswidth;
            //画Y轴
//		linePaint.setStyle(Paint.Style.STROKE);
//		linePaint.setStrokeWidth((float)5.0);
//		linePaint.setColor(Color.RED);
//		linePaint.setAntiAlias(true);// 锯齿不显示
            lStartX = width / 38;
            centerY = height / 2;
            float f1mv = (float) (ECG_1MV_DATA / ECGTIMES);
            flmvwidth = (float) height / 60;
//            //临时坐标
//            int temp = lStartX;
//            //中线位置
//            int ltempy = centerY;
//            //刻度
//            double scale = 0.25;
//            //起始刻度
//            double sstart = 0.25;
//			// 画背景格子
//			for (int i=0;i<height;i++ )
//			{
//				ltempy+=15;
//				linePaint.setStrokeWidth(1);
//				linePaint.setColor(Color.GRAY);
//				mCanvas.drawLine(lStartX, ltempy, width, ltempy, linePaint);
//				linePaint.setTextSize(8);
//				linePaint.setColor(Color.CYAN);
//				mCanvas.drawText("-" + Double.toString(sstart), 2, ltempy, linePaint);
//				sstart+=0.25;
//				//lStartY += 15;
//			}
            //画竖线
//			ltempy=height/2;
//			sstart=0;
//			for (int i = 0; i < height; i++) {
//				if (i==centerY)
//				{
            linePaint.setColor(Color.RED);
            linePaint.setStrokeWidth(width / 300);
            mCanvas.drawLine(lStartX, centerY, width, centerY, linePaint);
            linePaint.setTextSize(height / 32);
            //linePaint.setColor(Color.CYAN);
            linePaint.setColor(Color.WHITE);
            mCanvas.drawText("1mV", lStartX - flmvwidth * (float) 1.8, centerY - f1mv / 2, linePaint);
            linePaint.setStrokeWidth(flmvwidth);
            mCanvas.drawLine(lStartX, centerY + f1mv / 2, lStartX, centerY - f1mv / 2, linePaint);//画出参考线
//				}
//
//				linePaint.setStrokeWidth(1);
//				linePaint.setColor(Color.GRAY);
//				mCanvas.drawLine(temp, 0, temp, height, linePaint);
//				ltempy-=15;
////			lStartY=height/2;
//				mCanvas.drawLine(lStartX, ltempy, width, ltempy, linePaint);
//				linePaint.setTextSize(8);
//				linePaint.setColor(Color.CYAN);
//				mCanvas.drawText(Double.toString(sstart), 2, ltempy, linePaint);
////			}
//				sstart+=0.25;
////			canvas.drawLine(lStartX, lStartY, height/7+1, lStartY, linePaint);
////			lStartY += 15;
////			canvas.drawLine(temp, 0, temp, height, linePaint);
//				temp+=15;
//			}
             //simpleHeight= height / 6;
            //心率
//            linePaint.setTextSize(width * 3 / 100);
//            linePaint.setColor(Color.CYAN);
//            mCanvas.drawText("心率(HR)", width * 9 / 11, simpleHeight / 4, linePaint);
//            linePaint.setTextSize(width * 2 / 100);
//            mCanvas.drawText("bpm", width * 16 / 17, simpleHeight / 4, linePaint);
//            linePaint.setColor(Color.MAGENTA);
//            linePaint.setTextSize(width * 5 / 100);
//            mCanvas.drawText("0", width * 12 / 13, simpleHeight - (simpleHeight / 5), linePaint);
            //mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {
        } finally {
            if (mCanvas != null)
                holder.unlockCanvasAndPost(mCanvas);
            paintflag = 1;
            //holder.lockCanvas(new Rect(0,0,0,0)); //锁定局部区域，其余地方不做改变
            //holder.unlockCanvasAndPost(mCanvas);
            //timer.
        }
    }
    private class DrawThread extends Thread {
        float fbx = lStartX + flmvwidth / 2;
        int cx = (int) fbx;
        int bx;
        float by = centerY;
        IFirFilter iFirFilter=new FirFilter();
        IirFilter iirFilter=new IirFilter();
        public void run() {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            DrawBack();
//            DrawBack();
//            DrawBack();
            Log.d("drawthread:",""+Thread.currentThread().getId()+"___"+Command.mShowDataQueue.isEmpty());
            //drawheartrate();
            //testdraw();
            while (paintflag == 1) {
                if (Command.mShowDataQueue.isEmpty() == false) {
                    short ecg=Command.mShowDataQueue.poll();
                   double data = iFirFilter.FIRLPF_Filter(iirFilter.IIRDF2_Filter(finalecgdata(ecg)));
                    //int data=finalecgdata(Command.mShowDataQueue.poll());
                    //double data=0;
                    //Log.d("mshowdata:",Thread.currentThread().getId()+""+ecg);
                    float cy = centerY - (float) (data / ECGTIMES);
                    //float cy = centerY - (float) (ecg / ECGTIMES);
                    mSaveData.add(data);
                    Command.mAboutheartratedataListQueue.offer(ecg);
                    //实时获取的temp数值，因为对于画布来说
                    bx = cx;
                    cx++;//cx 自增， 就类似于随时间轴的图形
                    long start=System.currentTimeMillis();
//                    Log.d("startime:",""+)
                    //最左上角是原点，所以我要到y值，需要从画布中间开始计数
                    try {
                        Canvas canvas = holder.lockCanvas(new Rect(bx, 0, cx, canvasheigth));
                        //锁定画布，只对其中Rect(cx,cy-2,cx+2,cy+2)这块区域做改变，减小工程量
//                linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//                canvas.drawPaint(linePaint);
//                linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                        //canvas.drawColor(Color.TRANSPARENT);
                        linePaint.setColor(Color.GREEN);//设置波形颜色
                        canvas.drawLine(bx, by, cx, cy, linePaint); //画线
                       // canvas.drawPoint(cx,cy,linePaint);
//                        if (canvas!=null) {
                            holder.unlockCanvasAndPost(canvas);  //解锁画布
//                        }
                    } catch (Exception e) {
                        if (paintflag==0)break;
                        e.printStackTrace();

                    }
                    long end=System.currentTimeMillis();
                    //Log.d("showtime:",""+(end-start));
                    by = cy;
                    if (cx >= canvaswidth) {
                        cx = (int) fbx;
                        DrawBack();
                        DrawBack();
                        DrawBack();
                        //画满之后，清除原来的图像，从新开始
                    }
                }
            }
            //while (!done) {
            //final Object obj = new Object();//申请一个对象
            // TODO Auto-generated method stub
            //drawBack(holder);    //画出背景和坐标轴
            //drawheartrate();
            //简单一点就是1ms画出一个点，然后依次下去
            //}
        }

//        private void updateECG() {
//            if (Command.mShowDataQueue.isEmpty() == false) {
//                int data =finalecgdata(Command.mShowDataQueue.poll()) ;
//                float cy = centerY - (float)(data / ECGTIMES);
//                mSaveData.add(data);
//                //实时获取的temp数值，因为对于画布来说
//                bx = cx;
//                cx++;                               //cx 自增， 就类似于随时间轴的图形
//                //最左上角是原点，所以我要到y值，需要从画布中间开始计数
//                Canvas canvas = holder.lockCanvas(new Rect(bx, 0, cx, canvasheigth));
//                //锁定画布，只对其中Rect(cx,cy-2,cx+2,cy+2)这块区域做改变，减小工程量
////                linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
////                canvas.drawPaint(linePaint);
////                linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//                canvas.drawColor(Color.TRANSPARENT);
//                linePaint.setColor(Color.GREEN);//设置波形颜色
//                canvas.drawLine(bx, by, cx, cy, linePaint); //画线
//                holder.unlockCanvasAndPost(canvas);  //解锁画布
//                by = cy;
//                if (cx >= canvaswidth) {
//                    cx = (int) fbx;
//                    DrawBack();
//                    DrawBack();
//                    DrawBack();
//                    //画满之后，清除原来的图像，从新开始
//                }
//            }
//            //paintflag=0;
//        }

        private int finalecgdata(int arg) {
            int ecgdata = arg;
           // Log.v("argecg:",""+arg);
            if (arg == Command.ESCAPE_CHAR) {
                if (Command.mShowDataQueue.isEmpty() == false) {
                    int next = Command.mShowDataQueue.poll();
                    if (next == Command.SPECIAL_CHAR) {
                        ecgdata = Command.ESCAPE_CHAR;
                    } else if (next == Command.SPECIAL_CHAR1) {
                        ecgdata = Command.intFirstFrame;
                    }
                }
            }
           // Log.v("ecg:",""+ecgdata);
            return ecgdata;
        }
    }
    private class DrawThread1 extends Thread {
        float fbx = lStartX + flmvwidth / 2;
        int cx = (int) fbx;
        int bx;
        float by = centerY;
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            DrawBack();
            while (paintflag == 1) {
                if (Command.mShowDataQueue1.isEmpty() == false) {
                    double ecg=Command.mShowDataQueue1.poll();
                    //double data = iFirFilter.FIRLPF_Filter(iirFilter.IIRDF2_Filter(finalecgdata(ecg)));
                    //int data=finalecgdata(Command.mShowDataQueue.poll());
                    //double data=0;
                    Log.d("mshowdata:",Thread.currentThread().getId()+""+ecg);
                    float cy = centerY - (float) (ecg / ECGTIMES);
                    //float cy = centerY - (float) (ecg / ECGTIMES);
                    //mSaveData.add(ecg);
                    Command.mAboutheartratedataListQueue.offer((short)ecg);
                    //实时获取的temp数值，因为对于画布来说
                    bx = cx;
                    cx++;//cx 自增， 就类似于随时间轴的图形
//                    Log.d("startime:",""+)
                    //最左上角是原点，所以我要到y值，需要从画布中间开始计数
                    try {
                        Canvas canvas = holder.lockCanvas(new Rect(bx, 0, cx, canvasheigth));
                        //锁定画布，只对其中Rect(cx,cy-2,cx+2,cy+2)这块区域做改变，减小工程量
//                linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//                canvas.drawPaint(linePaint);
//                linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                        //canvas.drawColor(Color.TRANSPARENT);
                        linePaint.setColor(Color.GREEN);//设置波形颜色
                        canvas.drawLine(bx, by, cx, cy, linePaint); //画线
                        // canvas.drawPoint(cx,cy,linePaint);
//                        if (canvas!=null) {
                        holder.unlockCanvasAndPost(canvas);  //解锁画布
//                        }
                    } catch (Exception e) {
                        if (paintflag==0)break;
                        e.printStackTrace();

                    }
                    //Log.d("showtime:",""+(end-start));
                    by = cy;
                    if (cx >= canvaswidth) {
                        cx = (int) fbx;
                        DrawBack();
                        DrawBack();
                        DrawBack();
                        //画满之后，清除原来的图像，从新开始
                    }
                }
            }
            //while (!done) {
            //final Object obj = new Object();//申请一个对象
            // TODO Auto-generated method stub
            //drawBack(holder);    //画出背景和坐标轴
            //drawheartrate();
            //简单一点就是1ms画出一个点，然后依次下去
            //}
        }
    }
    /**
     * 保存成二进制文件
     */
    @Override
    public void SaveECG(String fileName) {
        //System.out.println("开始保存：" + mSaveData.size());

        // 先判断是否有SDCard
        if ((Environment.getExternalStorageState() != null) && (fileName != null)) {

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                // 创建一个文件夹对象，赋值为外部存储器的目录
                File sdcardDir = Environment.getExternalStorageDirectory();
                //得到一个路径，内容是sdcard的文件夹路径和名字
                String path = sdcardDir.getPath()+"//ECGDATA//";
                File path1 = new File(path);
                if (!path1.exists()) {
                    //若不存在，创建目录，可以在应用启动的时候创建
                    path1.mkdirs();
                }

                File file = new File(path1, fileName + ".txt");
                FileOutputStream fOut = null;
                BufferedOutputStream bos = null;
                try {
                    fOut = new FileOutputStream(file, true);
                    bos = new BufferedOutputStream(fOut);

                } catch (FileNotFoundException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();

                }
                DataOutputStream dos = new DataOutputStream(bos);

               // System.out.println(Command.mShowDataQueue.isEmpty());

                if (!mSaveData.isEmpty()) {
                    try {
                        try {
//                            dos.writeBytes("ecg singal\r\n");
//                            dos.writeBytes("sample_rate:");
//                            dos.writeInt(SAMPLE_RATE);
//                            dos.writeBytes("\r\n");
//                            mSaveData.remove();
                            for (int i = 2*Command.SAMPLE_RATE; i < mSaveData.size(); i++) {
                                double Data = mSaveData.get(i);
                                //short Data=1;
                                dos.writeDouble(Data);
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            System.out.println("save here");

                        }
                    } finally {
                        try {

                            dos.close();
//							Toast.makeText(
//									Application., path + "/"+fileName, Toast.LENGTH_LONG).show();
                            Log.v("保存 成功！", "" + path + "/" + fileName);
                            Toast.makeText(getContext(), "已经保存到：" + "" + path + "/" + fileName, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            // TODO 自动生成的 catch 块
                            e.printStackTrace();

                        }
                    }


                }
            }

        }

    }

    @Override
    public void SetImainview(IMainView iMainView) {
        this.iMainView=iMainView;
    }
}
