package com.example.mrchenrunfeng.myecg.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.mrchenrunfeng.myecg.classes.Command;

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


public class ECGSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback,IECGSurfaceView {
	DrawThread drawThread=null;
	 List<Integer> mSaveData = new ArrayList<Integer>();
	int lStartX;
	int centerY;
    int paintflag=1;//绘图是否暂停标志位，0为暂停
	TimerTask task=null;
	Timer timer =new Timer();
	//控制对象
	private SurfaceHolder holder = null;
	/**
	 * 创建画笔
	 */
	private Paint linePaint = new Paint();
	private Canvas mCanvas;
	private int canvaswidth;
	private int canvasheigth;
//	/**
//	 * 右边部分平均每一行所占高度
//	 */

	public ECGSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder = getHolder();
		holder.addCallback(this);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("surfaceCreated!!");
		DrawBack();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		System.out.println("surfaceChanged!!");
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		System.out.println("surfaceDestroyed!!");
		//cachedThreadpool.shutdown();
	}
    public void StartDraw(){
				drawThread=new DrawThread();
				drawThread.start();
			paintflag=1;
	}
	public  void StopDraw(){
		paintflag = 0;
		Command.mShowData.clear();
	}


	/**
	 * 画图的方法
	 *
	 */
    public synchronized void DrawBack(){
		try {
			//timer.cancel();
			paintflag=0;
			mCanvas = holder.lockCanvas();
			mCanvas.drawColor(Color.BLACK);
			canvaswidth = mCanvas.getWidth();
			canvasheigth = mCanvas.getHeight();
			int height=canvasheigth;
			int width=canvaswidth;
			//画Y轴
//		linePaint.setStyle(Paint.Style.STROKE);
//		linePaint.setStrokeWidth((float)5.0);
//		linePaint.setColor(Color.RED);
//		linePaint.setAntiAlias(true);// 锯齿不显示
			lStartX = width/38;
			centerY = height/2;
			//临时坐标
			int temp = lStartX;
			//中线位置
			int ltempy=centerY;
			//刻度
			double scale=0.25;
			//起始刻度
			double sstart=0.25;
			// 画背景格子
			for (int i=0;i<height;i++ )
			{
				ltempy+=15;
				linePaint.setStrokeWidth(1);
				linePaint.setColor(Color.GRAY);
				mCanvas.drawLine(lStartX, ltempy, width, ltempy, linePaint);
				linePaint.setTextSize(8);
				linePaint.setColor(Color.CYAN);
				mCanvas.drawText("-" + Double.toString(sstart), 2, ltempy, linePaint);
				sstart+=0.25;
				//lStartY += 15;
			}
			//画竖线
			ltempy=height/2;
			sstart=0;
			for (int i = 0; i < height; i++) {
				if (i==centerY)
				{
					linePaint.setColor(Color.RED);
					linePaint.setStrokeWidth(2);
					mCanvas.drawLine(lStartX, centerY, width, centerY, linePaint);
					linePaint.setTextSize(10);
					//linePaint.setColor(Color.CYAN);
					mCanvas.drawText("0", 20,height/2, linePaint);
				}

				linePaint.setStrokeWidth(1);
				linePaint.setColor(Color.GRAY);
				mCanvas.drawLine(temp, 0, temp, height, linePaint);
				ltempy-=15;
//			lStartY=height/2;
				mCanvas.drawLine(lStartX, ltempy, width, ltempy, linePaint);
				linePaint.setTextSize(8);
				linePaint.setColor(Color.CYAN);
				mCanvas.drawText(Double.toString(sstart), 2, ltempy, linePaint);
//			}
				sstart+=0.25;
//			canvas.drawLine(lStartX, lStartY, height/7+1, lStartY, linePaint);
//			lStartY += 15;
//			canvas.drawLine(temp, 0, temp, height, linePaint);
				temp+=15;
			}
			int simpleHeight=height/6;
			//心率
			linePaint.setTextSize(width*3/100);
			linePaint.setColor(Color.CYAN);
			mCanvas.drawText("心率(HR)", width*9/11, simpleHeight/4, linePaint);
			linePaint.setTextSize(width*2/100);
			mCanvas.drawText("bpm", width*16/17, simpleHeight/4, linePaint);
			linePaint.setColor(Color.MAGENTA);
			linePaint.setTextSize(width*5/100);
			mCanvas.drawText("0", width*12/13, simpleHeight-(simpleHeight/5), linePaint);
			//mCanvas.drawPath(mPath, mPaint);
		} catch (Exception e) {
		} finally {
			if (mCanvas != null)
				holder.unlockCanvasAndPost(mCanvas);
			paintflag=1;
			    //holder.lockCanvas(new Rect(0,0,0,0)); //锁定局部区域，其余地方不做改变
                //holder.unlockCanvasAndPost(mCanvas);
			//timer.
		}
	}
	private class DrawThread extends Thread {
        int cx=lStartX;
		int bx;
		float by=centerY;
		public void run() {
			DrawBack();
			while (paintflag == 1) {
				updateECG();
			}
			//while (!done) {
				//final Object obj = new Object();//申请一个对象
				// TODO Auto-generated method stub
				//drawBack(holder);    //画出背景和坐标轴
//				if (task != null) {
//					task.cancel();
//				}
//				task = new TimerTask() { //新建任务
//					@Override
//					public void run() {
//						if (paintflag == 1) {
//							updateECG();
//						}
//					}
//				};
//				timer.schedule(task, 0, 1); //隔1ms被执行一次该循环任务画出图形
				//简单一点就是1ms画出一个点，然后依次下去
			//}
		}

		private void updateECG() {
			if (Command.mShowData.isEmpty() == false) {
                int data=Command.mShowData.poll();
                float cy = centerY - (float) (finalecgdata(data)/2);
                mSaveData.add(data);
                //实时获取的temp数值，因为对于画布来说
                bx = cx;
                cx+=2;                               //cx 自增， 就类似于随时间轴的图形
                //最左上角是原点，所以我要到y值，需要从画布中间开始计数
                Canvas canvas = holder.lockCanvas(new Rect(bx, 0, cx, canvasheigth));
                //锁定画布，只对其中Rect(cx,cy-2,cx+2,cy+2)这块区域做改变，减小工程量
                linePaint.setColor(Color.GREEN);//设置波形颜色
                canvas.drawLine(bx, by, cx, cy, linePaint); //画线
                holder.unlockCanvasAndPost(canvas);  //解锁画布
                by = cy;
                if (cx >= canvaswidth) {
                    cx = lStartX;
                    DrawBack();
                    //画满之后，清除原来的图像，从新开始
                }
            }
			//paintflag=0;
		}

		private int finalecgdata(int arg){
			int ecgdata=arg;
			if (arg==Command.ESCAPE_CHAR){
				if (mSaveData.isEmpty()==false) {
					int next=Command.mShowData.poll();
					if (next==Command.SPECIAL_CHAR){
                        ecgdata= Command.ESCAPE_CHAR;
                    }
                    else if (next==Command.SPECIAL_CHAR1){
                        ecgdata= Command.intFirstFrame;
                    }
				}
			}
			return ecgdata;
		}
	}
	/**
	 * 保存成二进制文件
	 *
	 */
	@Override
	public void SaveECG(String fileName){
		System.out.println("开始保存："+mSaveData.size());

		// 先判断是否有SDCard
		if ((Environment.getExternalStorageState() != null) && (fileName != null)) {

			if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
				// 创建一个文件夹对象，赋值为外部存储器的目录
				File sdcardDir =Environment.getExternalStorageDirectory();
				//得到一个路径，内容是sdcard的文件夹路径和名字
				String path = sdcardDir.getPath();
				File path1 = new File(path);
				if (!path1.exists()) {
					//若不存在，创建目录，可以在应用启动的时候创建
					path1.mkdirs();
				}

				File file = new File(path1, fileName+".txt");
				FileOutputStream fOut = null;
				BufferedOutputStream bos = null;
				try {
					fOut = new FileOutputStream(file,true);
					bos = new BufferedOutputStream(fOut);

				} catch (FileNotFoundException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();

				}
				DataOutputStream dos = new DataOutputStream(bos);

				System.out.println(Command.mShowData.isEmpty());

				if (!mSaveData.isEmpty()) {
					try {

						for (int i = 0; i <mSaveData.size(); i++) {
						double Data = mSaveData.get(i).doubleValue();
							//short Data=1;
							try {
								dos.writeDouble(Data);

							} catch (Exception e) {
								// TODO: handle exception
								System.out.println("save here");

							}

						}

					}
					finally{
						try {

							dos.close();
//							Toast.makeText(
//									Application., path + "/"+fileName, Toast.LENGTH_LONG).show();
							Log.v("保存 成功！",""+path + "/"+fileName);
							Toast.makeText(getContext(), "已经保存到："+""+path + "/"+fileName, Toast.LENGTH_LONG).show();
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();

						}
					}


				}
			}

		}

	}
}
