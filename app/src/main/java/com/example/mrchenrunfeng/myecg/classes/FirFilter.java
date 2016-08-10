/****************************************************
 * written by ZhangWei,2016-2-1
 * ����ʵ�ִ���FIR�˲���ֱ���ͽṹ�����Խṹ
 * ʵ��ֱ�����˲��������˲�
 * *************************************************/
package com.example.mrchenrunfeng.myecg.classes;

//���ڱ�ʾFIRֱ���͵Ľṹ
class FIR_DF{
	private double[] h_n;   //h(n)����ϵ������b 
	private double[] x_n;   //x(n-m)������ʱ�������� 
	private int N;          //h(n)��x(n-m)�ĳ��� 
	public double[] getH_n() {
		return h_n;
	}
	public void setH_n(double[] h_n) {
		this.h_n = h_n;
	}
	public double[] getX_n() {
		return x_n;
	}
	public void setX_n(double[] x_n) {
		this.x_n = x_n;
	}
	public int getN() {
		return N;
	}
	public void setN(int n) {
		N = n;
	}
}
//���ڱ�ʾFIR������λ�͵Ľṹ 

class FIR_LPF{
	private double[] h_n;   //h(n)����ϵ������b 
	private double[] x_n;   //x(n-m)������ʱ�������� 
	private int hN;         //h(n)����Ч���ȣ�������λ��h(n)���жԳ��ԣ��������Ч���Ȳ�����h(n)�ĳ��� 
	private int N;          //x(n-m)�ĳ���
	private Boolean isEvenSymm;    //��ż�Գƣ�
	private Boolean isEvenNum;     //hN��ż��
	public double[] getH_n() {
		return h_n;
	}
	public void setH_n(double[] h_n) {
		this.h_n = h_n;
	}
	public double[] getX_n() {
		return x_n;
	}
	public void setX_n(double[] x_n) {
		this.x_n = x_n;
	}
	public int gethN() {
		return hN;
	}
	public void sethN(int hN) {
		this.hN = hN;
	}
	public int getN() {
		return N;
	}
	public void setN(int n) {
		N = n;
	}
	public Boolean getIsEvenSymm() {
		return isEvenSymm;
	}
	public void setIsEvenSymm(Boolean isEvenSymm) {
		this.isEvenSymm = isEvenSymm;
	}
	public Boolean getIsEvenNum() {
		return isEvenNum;
	}
	public void setIsEvenNum(Boolean isEvenNum) {
		this.isEvenNum = isEvenNum;
	}
}
public class FirFilter implements IFirFilter  {
	public static double[] LowPass_hn = {-0.000000000000000,0.000001024361573,-0.000000000000000,-0.000009457919927,0.000027551658342,-0.000043592885815,0.000039282702911,-0.000000000000000,
			-0.000071581185635,0.000148390302068,-0.000185441558979,0.000140367519453,0.000000000000000,-0.000200829939619,0.000381408156923,
			-0.000443106262266,0.000315325701976,-0.000000000000000,-0.000408707145179,0.000745650889588,-0.000836116371391,0.000576560865730,-0.000000000000000,
			-0.000708405960108,0.001263219015868,-0.001387414806401,0.000938834655745,-0.000000000000000,-0.001116067328164,0.001961547610263,
			-0.002125912427336,0.001421036308031,-0.000000000000000,-0.001653007578759,0.002877455852959,-0.003091043119777,0.002049353587522,-0.000000000000000,
			-0.002349760693985,0.004064552036651,-0.004341164909831,0.002863176657482,-0.000000000000000,-0.003253768808021,0.005607494055399,
			-0.005969951799442,0.003926780212525,-0.000000000000000,-0.004445079595187,0.007651599385422,-0.008141055659353,0.005354511127490,-0.000000000000000,
			-0.006071671064355,0.010471067204640,-0.011170004324747,0.007371926108421,-0.000000000000000,-0.008440488228122,0.014650770575994,
			-0.015750459284938,0.010491436808574,-0.000000000000000,-0.012303378205011,0.021687224052887,-0.023745710775911,0.016166690952169,-0.000000000000000,
			-0.020095728859863,0.036842382106962,-0.042371333057572,0.030718130808507,-0.000000000000000,-0.046463705720669,0.100532750666418,
			-0.151113517877557,0.187020005278217,0.800000000000000,0.187020005278217,-0.151113517877557,0.100532750666418,-0.046463705720669,
			-0.000000000000000,0.030718130808507,-0.042371333057572,0.036842382106962,-0.020095728859863,-0.000000000000000,0.016166690952169,
			-0.023745710775911,0.021687224052887,-0.012303378205011,-0.000000000000000,0.010491436808574,-0.015750459284938,0.014650770575994,-0.008440488228122,
			-0.000000000000000,0.007371926108421,-0.011170004324747,0.010471067204640,-0.006071671064355,-0.000000000000000,0.005354511127490,
			-0.008141055659353,0.007651599385422,-0.004445079595187,-0.000000000000000,0.003926780212525,-0.005969951799442,0.005607494055399,-0.003253768808021,
			-0.000000000000000,0.002863176657482,-0.004341164909831,0.004064552036651,-0.002349760693985,-0.000000000000000,0.002049353587522,
			-0.003091043119777,0.002877455852959,-0.001653007578759,-0.000000000000000,0.001421036308031,-0.002125912427336,0.001961547610263,-0.001116067328164,
			-0.000000000000000,0.000938834655745,-0.001387414806401,0.001263219015868,-0.000708405960108,-0.000000000000000,0.000576560865730,
			-0.000836116371391,0.000745650889588,-0.000408707145179,-0.000000000000000,0.000315325701976,-0.000443106262266,0.000381408156923,
			-0.000200829939619,0.000000000000000,0.000140367519453,-0.000185441558979,0.000148390302068,-0.000071581185635,-0.000000000000000,0.000039282702911,
			-0.000043592885815,0.000027551658342,-0.000009457919927,-0.000000000000000,0.000001024361573,-0.000000000000000};
	public static double[] x_n = new double[155];

	/**
	 * ����FIRֱ���͵Ľṹ
	 * @param h_n����λ�弤��Ӧh(n)������
	 * @param x_n���ṹ������Ļ���x(n-m)
	 * @param N��h(n)�ĳ��� 
	 * @return FIRֱ���͵Ľṹ
	 */
	public FIR_DF CreateFIRDF(double[] h_n, double[]  x_n, int N)
	{
	    FIR_DF fir = new FIR_DF();
	    fir.setH_n(h_n);
	    fir.setX_n(x_n);
	    fir.setN(N);
	    return fir;
	}
	
	/**
	 * ����FIR������λ�ͽṹ
	 * @param h_n����λ�弤��Ӧh(n)������
	 * @param x_n���ṹ������Ļ���x(n-m)
	 * @param N��h(n)�ĳ���
	 * @return FIR������λ�ͽṹ
	 */
	public FIR_LPF CreateFIRLPF(double[] h_n, double[] x_n, int N)
	{
	    FIR_LPF fir = new FIR_LPF();
	    if(!IsLinearPhase(h_n, N)) return fir;
	    
	    fir.setH_n(h_n);
	    fir.setX_n(x_n);
	    fir.setN(N);
	    fir.setIsEvenSymm(( (h_n[0] == h_n[N-1]) ? true : false )); 
	    fir.setIsEvenNum( ( N % 2 == 0 ) ? true : false );
	    fir.sethN(( ( N % 2 == 0 ) ? N/2 : (N+1)/2 ));
	    
	    return fir;
	}
	
	/**
	 * ʵ��FIRֱ�����˲�����
	 * @param pF��ֱ���ͽṹ
	 * @param x��������ź�ֵ
	 * @return����������ź�ֵ 
	 */
	public double FIRDF_Filter(FIR_DF  pF, double x)
	{
	    double y = 0.0;
	    int i = 0;
	    int N = pF.getN();
	    double[] x_n = pF.getX_n();
	    double[] h_n = pF.getH_n();
	    for(i = N-1; i > 0; i--)
	    {
	    	x_n[i] = x_n[i-1];
	    	y += (h_n[i] * x_n[i]);
	    }
	    y += (h_n[0] * x);
	    x_n[0] = x;
	    
	    return y;    
	}
	
	/**
	 * ʵ��FIR������λ���˲����� 
     * @param x��������ź�ֵ
	 * @return����������ź�ֵ
	 */
	public double FIRLPF_Filter( double x)
	{
		FIR_LPF  pF=CreateFIRLPF(LowPass_hn,x_n,155);
	    double y = 0.0;
	    int i = 0;
	    int N = pF.getN();
	    int hN = pF.gethN();
	    double[] x_n = pF.getX_n();
	    double[] h_n = pF.getH_n();
	    
	    for(i = N-1; i > 0; i--)
	        x_n[i] = x_n[i-1];
	    x_n[0] = x;    
	    
	    if(pF.getIsEvenSymm())  //h(n)ż�Գ� 
	    {
	        if(!pF.getIsEvenNum())  //h(n)����Ϊ��������1 
	        {
	            for(i = 0; i < hN-1; i++)
	                y += (h_n[i] * (x_n[i] + x_n[N-1-i]));
	            y += (h_n[hN-1] * x_n[hN-1] );  //�м�һ��Ϊ������ 
	        } 
	        else    //h(n)����Ϊż������2 
	        {
	            for(i = 0; i < hN; i++)
	                y += (h_n[i] * (x_n[i] + x_n[N-1-i]));            
	        }
	    }
	    else                //h(n)Ϊ��Գ� 
	    {
	        if(!pF.getIsEvenNum())  //h(n)����Ϊ��������3
	        {
	            for(i = 0; i < hN-1; i++)
	                y += (h_n[i] * (x_n[i] - x_n[N-1-i]));            
	        }
	        else            //h(n)����Ϊż������4
	        {
	            for(i = 0; i < hN; i++)
	                y += (h_n[i] * (x_n[i] - x_n[N-1-i]));              
	        } 
	    } 
	    
	    return y;    
	}
	/**
	 * �ж�h(n)�Ƿ�Ϊ������λ�� 
	 * @param h_n���˲�����ϵ������
	 * @param N���˲���ϵ���
	 * @return Boolean����ֵ
	 */
	static Boolean IsLinearPhase(double[]  h_n, int N)
	{
	    if(h_n == null || N <= 0) return false;
	    
	    int halfN = 0;
	    if( N % 2 == 0 ) halfN = N/2;
	    else halfN = (N-1)/2;
	    
	    int i = 0;
	    for(i = 0; i < halfN;)
	    {
	        if( h_n[i] == h_n[N-1-i] || h_n[i] == -h_n[N-1-i] ) return true;
	        else return false;
	    }
	    return false;
	}

}
