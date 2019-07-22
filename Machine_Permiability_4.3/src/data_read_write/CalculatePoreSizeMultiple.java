package data_read_write;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import application.Myapp;

public class CalculatePoreSizeMultiple {
	
	
	int no;
	double min,max;
	List<DatareadN> dr;
	public List<String> ranges;
	String name;
	
	int stpoint=10;
	int lstpoint=2;
	
	public CalculatePoreSizeMultiple(List<DatareadN> dr,int no,String name) {
		
		System.out.println("SIZE- >>>>>>>>>>>>>>>"+dr.size());
		this.name=name;
		ranges=new ArrayList<String>();
		this.dr=dr;
		List<Double> dd=new ArrayList<Double>();
		for(int i=0;i<dr.size();i++)
		{
			List<String> tempdata=dr.get(i).getValuesOf(dr.get(i).data.get(name)+"");
			for(int j=stpoint;j<tempdata.size()-lstpoint;j++)
			{
				dd.add(Double.parseDouble(tempdata.get(j)));
			}
		}
		min=Collections.min(dd);
		max=Collections.max(dd);
		
		System.out.println("min : "+min);
		System.out.println("max : "+max);
		
		
	
		
		double avg = (double) (max - min) / no;

		//avg=Double.parseDouble(Myapp.getRound(avg, 2));
		System.out.println("avg : "+avg);
		for (int j = 0; j < no; j++) {
			//List<Double> tempdata = new ArrayList<Double>();
			double start, end;
			double tempsum=0;
			if(j==0)
			{
				start=min;
				end=min+avg;
				
				ranges.add(0.0+"-"+Myapp.getRound(start, 2));
				
			}
			else
			{
			  start = Double.parseDouble(getRound(min + (avg * (j - 1)), 3));
			  end = Double.parseDouble(getRound((min + (avg * j)), 3));
			  
				ranges.add(Myapp.getRound(start, 2)+"-"+Myapp.getRound(end, 2));
				
			}
			
		}
		
		System.out.println(ranges);
		this.no=no;
		
	}
	
	public LinkedHashMap<String,LinkedHashMap<String,Double>> getDistribution()
	{
		LinkedHashMap<String, LinkedHashMap<String,Double>> dd=new LinkedHashMap<>();
		
		for(int i=0;i<dr.size();i++)
		{	
			dd.put(dr.get(i).filename,getDistributionChart(dr.get(0).getValuesOf(dr.get(i).data.get(name)+""), dr.get(i).getValuesOf(dr.get(i).data.get("psd")+""), min, max, no));
			
		}
		
		return dd;
		
	}
	
	public LinkedHashMap<String, Double> getDistributionChart(List<String> diameter,List<String> poresd,double min,double max, int no) {
		
		
		List<Double> dia=new ArrayList<Double>();
		List<Double> psd=new ArrayList<Double>();
		LinkedHashMap<String, Double> filterdata = new LinkedHashMap<String, Double>();
		double all=0;
		for (int i = stpoint; i < diameter.size()-lstpoint; i++) 
		{

			dia.add(Double.parseDouble(diameter.get(i)));
			psd.add(Double.parseDouble(poresd.get(i)));
			all=all+Double.parseDouble(poresd.get(i));	
	
		}
	
		double avg = (double) (max - min) / no;

		//System.out.println("Min :" + min + " Max :" + max + " avg:" + avg);

		for (int j = 0; j <no; j++) {
			//List<Double> tempdata = new ArrayList<Double>();
			double start, end;
			double tempsum=0;
			if(j==0)
			{
				start=0;
				end=min;
			}
			else
			{
			  start = Double.parseDouble(getRound(min + (avg * (j - 1)), 3));
			  end = Double.parseDouble(getRound((min + (avg * j)), 3));
			
			}

			for (int k = stpoint; k < psd.size()-lstpoint; k++) {
				if (dia.get(k) >= start && dia.get(k) < end) {
				//	tempdata.add(psd.get(k));
					tempsum=tempsum+psd.get(k);
					// data1.remove(k);
				}

			}

			
			//if(tempsum>0)
			//{
				filterdata.put(Myapp.getRound(start, 2)+"-"+Myapp.getRound(end, 2),tempsum*100/all);
			//}

		}
		
		return filterdata;
	}
	public String getRound(Double r, int round) {

		if (round == 2) {
			r = (double) Math.round(r * 100) / 100;
		} else if (round == 3) {
			r = (double) Math.round(r * 1000) / 1000;

		} else {
			r = (double) Math.round(r * 10000) / 10000;

		}

		return r + "";

	}
}