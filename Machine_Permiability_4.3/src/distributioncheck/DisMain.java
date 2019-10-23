package distributioncheck;

import java.util.ArrayList;
import java.util.List;

public class DisMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		double mean = 20;
		double st = 10.68869397;

		NormalDistribution nd = new NormalDistribution(mean, st);

		double ans = nd.cumulativeProbability(31.25);

		double ans1 = nd.density(15.59);

	//	System.out.println("Ans  : " + ans);

	//	System.out.println("Ans  : " + ans1);

		String s = "62.53\r\n" + "31.25\r\n" + "20.84\r\n" + "15.59\r\n" + "12.5\r\n" + "10.4\r\n" + "8.925\r\n"
				+ "7.808\r\n" + "6.939\r\n" + "6.247\r\n" + "5.685\r\n" + "5.209\r\n" + "4.81\r\n" + "4.463\r\n"
				+ "4.168\r\n" + "3.909\r\n" + "3.676\r\n" + "3.473\r\n" + "3.291\r\n" + "3.126\r\n" + "2.978\r\n"
				+ "2.841\r\n" + "2.719\r\n" + "2.606\r\n" + "2.5\r\n" + "2.405\r\n" + "2.315\r\n" + "2.233\r\n"
				+ "2.156\r\n" + "2.084\r\n" + "2.017\r\n" + "1.954\r\n" + "1.895\r\n" + "1.839\r\n" + "1.786\r\n"
				+ "1.737\r\n" + "1.69\r\n" + "1.646\r\n" + "1.603\r\n" + "1.563\r\n" + "1.525\r\n" + "";

		String sm=s.replace('\n', ',');
		System.out.println(sm);
		
		String[] stt=s.split("\r\n");
		List<Double> list=new ArrayList<Double>();
		for(int i=0;i<stt.length;i++)
		{
		//	System.out.println(stt[i]);
			list.add(Double.parseDouble(stt[i]));
		}
		
		s = s.replace('\n', ',');

		DisMain dm=new DisMain();
		dm.getPsd(list,6.55);
		//System.out.println(" - -  >  "+dans);
		
	//	System.out.println(s);

	}

	
	List<Double> getPsd(List<Double> dia,Double mean)
	{
		List<Double> ans=new ArrayList<Double>();
		
		List<Double> sq = new ArrayList<Double>();
		double sqmean = 0;
		for (int i = 0; i < dia.size(); i++) {
			double dd = dia.get(i) - mean;
			sq.add(dd * dd);
			sqmean = sqmean + sq.get(i);
		}

		sqmean = sqmean / sq.size();
		sqmean = Math.sqrt(sqmean);
		double std = Math.round(Double.parseDouble("" + sqmean) * 10000) / 10000D;
		
		System.out.println("Std : "+std);
		
		//mean=20.0;
		
		NormalDistribution nd = new NormalDistribution(mean, std);

		List<Double> dis=new ArrayList<Double>();

		double ans1,sum=0;
		for(int i=0;i<dia.size();i++)
		{
			ans1 = nd.density(dia.get(i));
			dis.add(ans1);
			
			sum=sum+ans1;
		}
		
		System.out.println("Sum :"+sum);
		System.out.println("Dis : "+dis);
		
		double per;
		for(int i=0;i<dia.size();i++)
		{
			per=dis.get(i)*100/sum;
			ans.add(per);
		}

		
		System.out.println(ans);
		
		
		return ans;
	}
	
	
	double getSd(List<Double> ls, Double mean) {

		List<Double> sq = new ArrayList<Double>();
		double sqmean = 0;
		for (int i = 0; i < ls.size(); i++) {
			double dd = ls.get(i) - mean;
			sq.add(dd * dd);
			sqmean = sqmean + sq.get(i);
		}

		sqmean = sqmean / sq.size();
		sqmean = Math.sqrt(sqmean);
		double sp3 = Math.round(Double.parseDouble("" + sqmean) * 10000) / 10000D;
		sqmean = sp3;

		return sqmean;
	}

}
