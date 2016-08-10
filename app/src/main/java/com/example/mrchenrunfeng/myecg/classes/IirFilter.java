/**********************************************
 * written by ZhangWei ,2016-1-29�賿
 * �������ڴ���IIRֱ��I�͡�II�ͽṹ
 * ʵ��IIRֱ��I�͡�II���˲�
 * ********************************************/
package com.example.mrchenrunfeng.myecg.classes;

/********************************
 * ���ڱ�ʾIIRֱ��I�͵Ľṹ
 *bm ��ϵͳ������Ӷ���ʽϵ������
 *ak ��ϵͳ�����ĸ����ʽϵ������ 
 *xm x(n-m)
 *yk y(n-k)  
 * M bm�ĳ���
 * N ak�ĳ��� 
 *******************************/
class IirDf1{
	private double[]  bm;    //��ϵͳ������Ӷ���ʽϵ������
	private double[]  ak;    //��ϵͳ�����ĸ����ʽϵ������ 
	private double[]  xm;    //x(n-m)
	private double[]  yk;    //y(n-k) 
	private int M;          //bm�ĳ���
	private int N;          //ak�ĳ��� 
	public double[] getBm() {
		return bm;
	}
	public void setBm(double[] bm) {
		this.bm = bm;
	}
	public double[] getAk() {
		return ak;
	}
	public void setAk(double[] ak) {
		this.ak = ak;
	}
	public double[] getXm() {
		return xm;
	}
	public void setXm(double[] xm) {
		this.xm = xm;
	}
	public double[] getYk() {
		return yk;
	}
	public void setYk(double[] yk) {
		this.yk = yk;
	}
	public int getM() {
		return M;
	}
	public void setM(int m) {
		M = m;
	}
	public int getN() {
		return N;
	}
	public void setN(int n) {
		N = n;
	}
	
}
/********************************
 * ���ڱ�ʾIIRֱ��II�͵Ľṹ
 *m��ϵͳ������Ӷ���ʽϵ������
 *ak��ϵͳ�����ĸ����ʽϵ������ 
 * wn  w(n) 
 * M bm�ĳ���
 * N ak�ĳ��� 
 *******************************/
class IirDf2{
	private double[]  bm;    //��ϵͳ������Ӷ���ʽϵ������
	private double[]  ak;    //��ϵͳ�����ĸ����ʽϵ������ 
	private double[]  wn;    //w(n) 
	private int M;          //bm�ĳ���
	private int N;          //ak�ĳ��� 
	public double[] getBm() {
		return bm;
	}
	public void setBm(double[] bm) {
		this.bm = bm;
	}
	public double[] getAk() {
		return ak;
	}
	public void setAk(double[] ak) {
		this.ak = ak;
	}
	public double[] getWn() {
		return wn;
	}
	public void setWn(double[] wn) {
		this.wn = wn;
	}
	public int getM() {
		return M;
	}
	public void setM(int m) {
		M = m;
	}
	public int getN() {
		return N;
	}
	public void setN(int n) {
		N = n;
	}
	
}
/**************************
 * ����IIR�Ľṹ
 * ֱ��I�͵Ľṹ
 * ֱ��II�͵Ľṹ
 * ***********************/

public class IirFilter{
	//50HZ陷波器系数
	//分母
	double ak50[] ={1,-0.6025011004,0.9503661722};
	//分子
	double bm50[] ={0.9753243284,-0.6027835850,0.9753243284};
	double[] wn50 = new double[3];
	/**
	 * ����IIRֱ��I�͵Ľṹ
	 * @param bm��ϵͳ������Ӷ���ʽϵ������
	 * @param ak��ϵͳ�����ĸ����ʽϵ������ 
	 * @param xm�����ڽṹ�л���x(n-m)�����飬��ĳ���Ӧ����bm��ͬ
	 * @param yk�����ڽṹ�л���y(n-k)�����飬��ĳ���Ӧ����ak��ͬ
	 * @param M��bm�ĳ���
	 * @param N��ak�ĳ��� 
	 * @return ֱ��I�ͽṹ
	 */
	public IirDf1 CreateIIRDF1(double[] bm,double[] ak,double[] xm,double[] yk,int M,int N){
		IirDf1 iirDf1 = new IirDf1();
		iirDf1.setBm(bm);
		iirDf1.setAk(ak);
		iirDf1.setXm(xm);
		iirDf1.setYk(yk);
		iirDf1.setM(M);
		iirDf1.setN(N);
		int i = 0;
	    if(ak[0] != 1.0)
	    {	
	        for(i = 0; i < M; i++)
	        {
	            iirDf1.getBm()[i] /= iirDf1.getAk()[0];
	        }
	        for(i = 0; i < N; i++)
	        {
	            iirDf1.getAk()[i] /= iirDf1.getAk()[0];
	        }
	       
	    }
	        
		return iirDf1;
	}
	/**
	 * ����IIRֱ��II�͵Ľṹ��ע������Ҫ��
	 * 1��M<=N�������Ӷ���ʽ����С�ڵ��ڷ�ĸ�ģ�
	 * 2������wn�ĳ���ҲΪN
	 * @param bm��ϵͳ������Ӷ���ʽϵ������
	 * @param ak��ϵͳ�����ĸ����ʽϵ������ 
	 * @param wn�����ڽṹ�л���w(n-k)����õ�14ҳ��������Ӧ����ak��ͬ 
	 * @param M: bm�ĳ���
	 * @param N��ak�ĳ���
	 * @return ֱ��II�ͽṹ
	 */
	public IirDf2 CreateIIRDF2(double[] bm,double[] ak,double[] wn,int M, int N){
		IirDf2 iirDf2 = new IirDf2();
		iirDf2.setBm(bm);
        iirDf2.setM(M);
        iirDf2.setAk(ak);
        iirDf2.setN(N);
        iirDf2.setWn(wn);
		int i = 0;
	    if(ak[0] != 1.0)
	    {
	        for(i = 0; i < M; i++)
	        {
	            iirDf2.getBm()[i] /= iirDf2.getAk()[0];
	        }
	        for(i = 0; i < N; i++)
	        {
	        	iirDf2.getAk()[i] /= iirDf2.getAk()[0];
	        }
	       
	    }
	  
		return iirDf2;	
	}

	/**
	 * ʵ��IIRֱ��I���˲�����
	 * @param pF��IIRֱ��I���˲����ṹ��
	 * @param x�������һ���ź�ֵ
	 * @return�����ص��˲���������ź�ֵ 
	 */
	public double IIRDF1_Filter(IirDf1 pF, double x){
		double[] bm = pF.getBm();
	    double[] ak = pF.getAk();
	    double[] xm = pF.getXm();
	    double[] yk = pF.getYk();
	  //�ȼ���ȫ��㲿�֣���õƵ�5ҳ 
	    double wn = 0.0;
	    int i = 0;
	    for(i = pF.getM()-1; i > 0; i--)
	    {
	        wn += (bm[i] * (xm[i] = xm[i-1]));        
	    }
	    wn += (bm[0] * (xm[0] = x));
	    
	    //�ټ���ȫ���㲿�֣���õƵ�6ҳ 
	    for(i = pF.getN()-1; i > 0; i--)
	    {
	        wn -= (ak[i] * (yk[i] = yk[i-1]));
	    }    
	    return (yk[0] = wn); 
	}
	
	/**
	 * ʵ��IIRֱ��II���˲�����
	 * @return�����ص��˲���������ź�ֵ 
	 */
	public double IIRDF2_Filter(double x){
		IirDf2 pF=CreateIIRDF2(bm50, ak50, wn50, 3, 3);
		double[] bm = pF.getBm();
	    double[] ak = pF.getAk();
	    double[] wn = pF.getWn();  
	    
	    int i = 0;
	    double wSum = 0.0;
	    double ySum = 0.0;
	    //��õƵ�13ҳ 
	    for(i = pF.getN()-1; i > 0; i--)
	    {
	        wSum -= (ak[i] * (wn[i] = wn[i-1]));
	        if(i < pF.getM()) ySum += (bm[i] * wn[i]);
	    } 
	    wSum += x;
	    return ( ySum += (bm[0] * (wn[0] = wSum)) );
	}
}
