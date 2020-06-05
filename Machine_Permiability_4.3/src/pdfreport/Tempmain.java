package pdfreport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data_read_write.DatareadN;

public class Tempmain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
					File f=new File("5umnew_1_1221.csv");
			DatareadN d=new DatareadN();
			d.fileRead(f);
			
			Singlepororeport sp=new Singlepororeport();
			List<String> dd=new ArrayList<String>();
			dd.add("1");
			dd.add("1");
			dd.add("1");
			dd.add("1");
			dd.add("1");
			
			sp.Report("C:/Users/Jayesh/Desktop/111.pdf",d,"dd","Sun Pharmaceutical","",dd,true,true,"", true , true ,true);
			
		/*
			
			
			
			
			
			DatareadN d1=new DatareadN();
		    DatareadN d2=new DatareadN();
		    DatareadN d3=new DatareadN();
		    DatareadN d4=new DatareadN();
		    DatareadN d5=new DatareadN();
		    DatareadN d6=new DatareadN();
		    
		    d1.fileRead(new File("test_sample0221.csv"));
		    d2.fileRead(new File("test_sample0222.csv"));
		    d3.fileRead(new File("test_sample0223.csv"));
		    d4.fileRead(new File("3M900GV_0_4552.csv"));
		    d5.fileRead(new File("3M900GV_1_0010.csv"));
		    d6.fileRead(new File("test_sample5_15psi0.csv"));
			
		    List<DatareadN> ds=new ArrayList<DatareadN>();
		    ds.add(d1);
		    ds.add(d2);
		    ds.add(d3);
		    ds.add(d4);
		    ds.add(d5);
		    ds.add(d6);
		  
		    
			Multiplepororeport mp=new Multiplepororeport();
			mp.Report("C:/Users/Jayesh/Desktop/m1.pdf", ds,"Sun Pharmaceutical");
		*/
				
			File ff=new File("C:/Users/Jayesh/Desktop/111.pdf");
		 	try {
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + ff.getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (Exception e) {

		}
		
	}

}
