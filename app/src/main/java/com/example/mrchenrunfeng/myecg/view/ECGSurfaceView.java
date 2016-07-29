package com.example.mrchenrunfeng.myecg.view;

import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class ECGSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	//控制对象
	private SurfaceHolder holder = null;
	private Vector<Float> xs = new Vector<Float>();
	private Vector<Float> ys = new Vector<Float>();
	private boolean ecgstatus;
	/**
	 * 创建画笔
	 */
	private Paint linePaint = new Paint();
	/**
	 * �ĵ�ͼ���
	 */
//	private Integer[] dataY = {100, 200, 210, 200, 201, 205, 200, 199,50,230,200,215,216,222,225,227,400,230,220,222,226
//							  ,100, 200, 210, 200, 201, 205, 200, 199,50,230,200,215,216,222,225,227,400,230,220,222,226
//							  ,100, 200, 210, 200, 201, 205, 200, 199,50,230,200,215,216,222,225,227,400,230,220,222,226
//							  ,100, 200, 210, 200, 201, 205, 200, 199,50,230,200,215,216,222,225,227,400,230,220,222,226
//							  ,100, 200, 210, 200, 201, 205, 200, 199,50,230,200,215,216,222,225,227,400,230,220,222,226
//							  ,100, 200, 210, 200, 201, 205, 200, 199,50,230,200,215,216,222,225,227,400,230,220,222,226};
//	private Integer[] dataY = {230,230,228,227,226,225,205,185,165,145,125,105,103,101,100,99,100,101,103,105,125,145,165,185,205,225,226,228,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,235,260,285,310,335,336,337,336,335,285,265,245,240,235,230,228,227,226,225,226,227,228,229,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,228,230,232
//							  ,230,230,228,227,226,225,205,185,165,145,125,105,103,101,100,99,100,101,103,105,125,145,165,185,205,225,226,228,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,235,260,285,310,335,336,337,336,335,285,265,245,240,235,230,228,227,226,225,226,227,228,229,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,228,230,232
//							  ,230,230,228,227,226,225,205,185,165,145,125,105,103,101,100,99,100,101,103,105,125,145,165,185,205,225,226,228,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,235,260,285,310,335,336,337,336,335,285,265,245,240,235,230,228,227,226,225,226,227,228,229,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,228,230,232
//							  ,230,230,228,227,226,225,205,185,165,145,125,105,103,101,100,99,100,101,103,105,125,145,165,185,205,225,226,228,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,235,260,285,310,335,336,337,336,335,285,265,245,240,235,230,228,227,226,225,226,227,228,229,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,228,230,232
//							  ,230,230,228,227,226,225,205,185,165,145,125,105,103,101,100,99,100,101,103,105,125,145,165,185,205,225,226,228,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,235,260,285,310,335,336,337,336,335,285,265,245,240,235,230,228,227,226,225,226,227,228,229,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,228,230,232
//							  ,230,230,228,227,226,225,205,185,165,145,125,105,103,101,100,99,100,101,103,105,125,145,165,185,205,225,226,228,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,235,260,285,310,335,336,337,336,335,285,265,245,240,235,230,228,227,226,225,226,227,228,229,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,228,230,232
//							  ,230,230,228,227,226,225,205,185,165,145,125,105,103,101,100,99,100,101,103,105,125,145,165,185,205,225,226,228,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,235,260,285,310,335,336,337,336,335,285,265,245,240,235,230,228,227,226,225,226,227,228,229,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,228,230,232
//							  ,230,230,228,227,226,225,205,185,165,145,125,105,103,101,100,99,100,101,103,105,125,145,165,185,205,225,226,228,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,235,260,285,310,335,336,337,336,335,285,265,245,240,235,230,228,227,226,225,226,227,228,229,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,228,230,232
//							  ,230,230,228,227,226,225,205,185,165,145,125,105,103,101,100,99,100,101,103,105,125,145,165,185,205,225,226,228,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,235,260,285,310,335,336,337,336,335,285,265,245,240,235,230,228,227,226,225,226,227,228,229,230,232,234,236,237,238,237,236,234,232,222,212,213,212,212,222,232,235,234,232,235,236,235,231,226,225,226,231,235,236,235,225,215,205,204,203,204,205,215,225,228,230,232};
	private Integer[] dataY = {230,200,170,140,110,105,110,140,170,200,230,236,234,214,212,214,234,236,234,233,230,232,229,232,230,233,236,232,230,235,233,230,210,190,188,190,236,235,234,233,230,235,238,240,290,340,342,340,290,240,236,234,233,236,233,232
							  ,230,200,170,140,110,105,110,140,170,200,230,236,234,214,212,214,234,236,234,233,230,232,229,232,230,233,236,232,230,235,233,230,210,190,188,190,236,235,234,233,230,235,238,240,290,340,342,340,290,240,236,234,233,236,233,232
							  ,230,200,170,140,110,105,110,140,170,200,230,236,234,214,212,214,234,236,234,233,230,232,229,232,230,233,236,232,230,235,233,230,210,190,188,190,236,235,234,233,230,235,238,240,290,340,342,340,290,240,236,234,233,236,233,232
							  ,230,200,170,140,110,105,110,140,170,200,230,236,234,214,212,214,234,236,234,233,230,232,229,232,230,233,236,232,230,235,233,230,210,190,188,190,236,235,234,233,230,235,238,240,290,340,342,340,290,240,236,234,233,236,233,232
							  ,230,200,170,140,110,105,110,140,170,200,230,236,234,214,212,214,234,236,234,233,230,232,229,232,230,233,236,232,230,235,233,230,210,190,188,190,236,235,234,233,230,235,238,240,290,340,342,340,290,240,236,234,233,236,233,232
							  ,230,200,170,140,110,105,110,140,170,200,230,236,234,214,212,214,234,236,234,233,230,232,229,232,230,233,236,232,230,235,233,230,210,190,188,190,236,235,234,233,230,235,238,240,290,340,342,340,290,240,236,234,233,236,233,232
							  ,230,200,170,140,110,105,110,140,170,200,230,236,234,214,212,214,234,236,234,233,230,232,229,232,230,233,236,232,230,235,233,230,210,190,188,190,236,235,234,233,230,235,238,240,290,340,342,340,290,240,236,234,233,236,233,232};
	/**
	 * 心电数据集合
	 */
	private ArrayList<Integer> dataListY = new ArrayList<Integer>();
	/**
	 * 标识是否是第一次画图
	 */
	private boolean isFirstDraw = true;
	/**
	 * 在画布上正在显示的数据集合
	 */
	private ArrayList<Integer> showedList = new ArrayList<Integer>();
	/**
	 * 已经显示的数据波形 最多能够显示的单位格数量（横坐标为15px为一格）
	 */
	private int maxNum = 0;

	/**
	 * 显示的波形每个单元格�?��的位�?X轴坐�?
	 */
	private int showedBeginX = 0;
	/**
	 * 显示的波形每个单元格结束的位�?X轴坐�?
	 */
	private int showedEndX = 0;
	/**
	 * 显示的波形每个单元格�?��的时候波形长度的�?Y轴坐�?
	 */
	private int showedBeginY = 0;
	/**
	 * 显示的波形每个单元格结束的时候波形长度的�?Y轴坐�?
	 */
	private int showedEndY = 0;

	/**
	 *心率、血氧�?无创�?�� 、中心静脉压
	 */
	private int[] otherInformations={58,96,120,80,12,78};
//	/**
//	 * 右边部分平均每一行所占高度
//	 */
//	private int simpleHeight=0;
	private Bitmap backgroundBitmap=null;

	public ECGSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder = getHolder();
		holder.addCallback(this);

		//backgroundBitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.ecg_background);

		//将心电数据数组转换成心电数据集合
		for (int i = 0; i < dataY.length; i++) {
			dataListY.add(dataY[i]);
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("surfaceCreated!!");
		//new Thread(new MyLoop()).start();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		System.out.println("surfaceChanged!!");
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		System.out.println("surfaceDestroyed!!");
	}
    public void startECG(boolean b){
		new Thread(new MyLoop()).start();
		ecgstatus=b;
	}
	public  void stopECG(boolean b){
		ecgstatus=b;
	}


	/**
	 * 画图的方法
	 *
	 * @param canvas
	 */
	private Canvas doDraw(Canvas canvas) {
		super.draw(canvas);
		int width = canvas.getWidth();
		int height = canvas.getHeight();
//		canvas.clipRect(0, 0, width/2, height);
		//判断是否是第�?��在屏幕上画波�?
		if (isFirstDraw) {
			maxNum = (width- 5) / 5;
//			simpleHeight=height/6;
		}
		//画Y轴
//		linePaint.setStyle(Paint.Style.STROKE);
//		linePaint.setStrokeWidth((float)5.0);
//		linePaint.setColor(Color.RED);
//		linePaint.setAntiAlias(true);// 锯齿不显示
		int lStartX = 30;
		int lStartY = 30;
		//临时坐标
		int temp = 30;
		//中线位置
		int ltempy=height/2;
		//刻度
		double scale=0.25;
		//起始刻度
		double sstart=0.25;
		// 画背景格子
		for (int i=0;i<height;i++ )
		{
//			if (lStartY==width/2)
//			{


             ltempy+=15;
//			} else {
				linePaint.setStrokeWidth(1);
			linePaint.setColor(Color.GRAY);
			canvas.drawLine(lStartX, ltempy, width, ltempy, linePaint);
			linePaint.setTextSize(8);
			linePaint.setColor(Color.CYAN);
			canvas.drawText("-" + Double.toString(sstart), 2, ltempy, linePaint);
//			}
			sstart+=0.25;
			//lStartY += 15;
		}
		//画竖线
		ltempy=height/2;
		sstart=0;
		for (int i = 0; i < height; i++) {
			if (i==height/2)
			{
				linePaint.setColor(Color.RED);
				linePaint.setStrokeWidth(2);
				canvas.drawLine(lStartX, height / 2, width, height / 2, linePaint);
				linePaint.setTextSize(10);
				//linePaint.setColor(Color.CYAN);
				canvas.drawText("0", 20,height/2, linePaint);
			}

			linePaint.setStrokeWidth(1);
			linePaint.setColor(Color.GRAY);
			canvas.drawLine(temp, 0, temp, height, linePaint);
			ltempy-=15;
//			lStartY=height/2;
			canvas.drawLine(lStartX, ltempy, width, ltempy, linePaint);
			linePaint.setTextSize(8);
			linePaint.setColor(Color.CYAN);
			canvas.drawText(Double.toString(sstart), 2, ltempy, linePaint);
//			}
			sstart+=0.25;
//			canvas.drawLine(lStartX, lStartY, height/7+1, lStartY, linePaint);
//			lStartY += 15;
//			canvas.drawLine(temp, 0, temp, height, linePaint);
			temp+=15;
		}

		//判断心电数据集合中是否有数据
		if(dataListY.size()==1||dataListY.size()==0){
			dataListY.add(height/2);
		}

		//如果心电数据集合中有数据
		if (dataListY.size() != 0 && dataListY != null) {
			linePaint.setColor(Color.GREEN);
			// 设置画笔的线条粗�?+(5 * showedList.size()+5)
			linePaint.setStrokeWidth(2);
			if (showedList.size() != 0 && showedList != null) {
				showedBeginX = 30;
				showedEndX = showedBeginX + 5;
				showedBeginY = height / 2;
				for (int i = 0; i < showedList.size(); i++) {
					if (ecgstatus) {
						showedEndY = showedList.get(i);
						canvas.drawLine(showedBeginX, showedBeginY, showedEndX, showedEndY, linePaint);
						showedBeginX = showedEndX;
						showedEndX = showedBeginX + 5;
						showedBeginY = showedEndY;
						if (showedList.size() != 1 && showedList.size() != (i + 1)) {
							try {
								showedEndY = showedList.get(i + 1);
							} catch (Exception e) {
								e.printStackTrace();
								break;
							}
						}
					}
					else {break;}
				}
			}
			// 如果能完全显示已经显示过的波形数据就就直接添加进显示过波形数据的集合中否则去掉第一个再添加进去
			if (showedList.size() < maxNum) {
				showedList.add(dataListY.get(0));
				dataListY.remove(0);
			} else {
				showedList.remove(0);
				showedList.add(dataListY.get(0));
				dataListY.remove(0);
				// 此时已经没有了心电数�?
				if (dataListY.size() == 0) {
					// 则将心电数据设置为没有的状�?�?显示为一条直�?
					dataListY.add(height / 2);
				}
			}
			isFirstDraw = false;
			int simpleHeight=height/6;
			//心率
			linePaint.setTextSize(width*3/100);
			linePaint.setColor(Color.CYAN);
			canvas.drawText("心率(HR)", width*9/11, simpleHeight/4, linePaint);
			linePaint.setTextSize(width*2/100);
			canvas.drawText("bpm", width*16/17, simpleHeight/4, linePaint);
			linePaint.setColor(Color.MAGENTA);
			linePaint.setTextSize(width*5/100);
			canvas.drawText(String.valueOf(otherInformations[0]), width*12/13, simpleHeight-(simpleHeight/5), linePaint);
		}
		return canvas;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			xs.add(event.getX());
			ys.add(event.getY());
		}
		return true;
	}

	class MyLoop implements Runnable {
		Canvas mCanvas = null;

		public void run() {
			while (true) {
				try {
					System.out.println("MyLoop Thread start!!!");
					mCanvas = holder.lockCanvas();
					mCanvas.drawColor(Color.BLACK);
//					mCanvas.drawBitmap(backgroundBitmap, 0, 0, linePaint);
					doDraw(mCanvas);
					System.out.println("Myloop Thread will end!!!");
//					Thread.sleep(200);
				} catch (Exception e) {
					System.out.println("MyLoop Thread exception!!!");
				} finally {
					if (mCanvas != null) {
						System.out.println("MyLoop Thread finally unlockCanvasAndPost()!!!!");
						holder.unlockCanvasAndPost(mCanvas);
					}
				}
			}

		}

	}
}
