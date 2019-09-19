package application;

import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import application.SerialCommunicator.SerialReader;
import toast.Toast;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

//this class is for data flow and data storing while page changes
public class DataStore 
{
	

	public static SimpleBooleanProperty isconfigure=new SimpleBooleanProperty(false);
	public static SerialPort serialPort;

	public static InputStream in=null;
	public static SerialReader sr;
	
	
	
	//for scada
	public static SimpleBooleanProperty sv1=new SimpleBooleanProperty(false);
	public static SimpleBooleanProperty sv2=new SimpleBooleanProperty(false);
	public static SimpleBooleanProperty sv3=new SimpleBooleanProperty(false);
	public static SimpleBooleanProperty sv4=new SimpleBooleanProperty(false);
	
	public static DoubleProperty spr=new SimpleDoubleProperty(0);
	public static DoubleProperty sfc=new SimpleDoubleProperty(0);
	public static DoubleProperty sfm1=new SimpleDoubleProperty(0);
	public static DoubleProperty sfm2=new SimpleDoubleProperty(0);
	public static DoubleProperty spg1=new SimpleDoubleProperty(0);
	public static DoubleProperty spg2=new SimpleDoubleProperty(0);
	
	public static SimpleStringProperty ssfm1=new SimpleStringProperty("0.0");
	public static SimpleStringProperty ssfm2=new SimpleStringProperty("0.0");
	public static SimpleStringProperty sspg1=new SimpleStringProperty("0.0");
	public static SimpleStringProperty sspg2=new SimpleStringProperty("0.0");
	
	
	
	
	public static int leakinterval,leakpress;
	
	public static SimpleBooleanProperty porometer_summary=new SimpleBooleanProperty(false);	
	
	static double maxflowofmachine=55000;
	public static SimpleBooleanProperty isDevicefatch=new SimpleBooleanProperty(false);
	public static String software_id;
	public static String firebase_url="https://pushnotification-305f7.firebaseio.com/";
	
	public static Stage gtg;

	public static SimpleBooleanProperty startTest=new SimpleBooleanProperty(false);
	
	// for userinput screen
	
	public static SimpleBooleanProperty testComplete=new SimpleBooleanProperty(false);
	
	
	public static SimpleBooleanProperty user_li=new SimpleBooleanProperty(true);
	public static SimpleBooleanProperty live_li=new SimpleBooleanProperty(false);
	

	public static SimpleDoubleProperty livepressure=new SimpleDoubleProperty(0);
	public static SimpleDoubleProperty liveflow=new SimpleDoubleProperty(0);
	
	
	//for online upload

	public static SimpleStringProperty ttime=new SimpleStringProperty("now");
	public static SimpleStringProperty tper=new SimpleStringProperty("0%");
	public static SimpleStringProperty tstatus=new SimpleStringProperty("loading");
	
	//new string

	public static ObservableList<String> leakpressure=FXCollections.observableArrayList();
	public static ObservableList<String> leaktime=FXCollections.observableArrayList();
	
	   //for csv generation...

	public static ObservableList<String> flow=FXCollections.observableArrayList();
	public static ObservableList<String> dpressure=FXCollections.observableArrayList();
	public static ObservableList<String> dflow=FXCollections.observableArrayList();

		
		
		// for user input screen...
	public static String dtempdata="",dtimedata="",dwdata="",sample_id="",id;
	
	public static long starttime;
	public static String thresoldvalue;

	public static  ObservableList<String> tempdata=FXCollections.observableArrayList();
	
	
	public static String surface,diameter,sampleid,thresold,thikness,starttime1,completetime,operator,customerid,comments,testdur;
	

		
		// for live graph...
	public static int pressure_min=0,pressure_max=0;
		
	    
		
	//configure Page 
	public static String pg,fm,pr,fc,thfirtbp,thmoderat,thcontinous;
	
	public static SimpleStringProperty sysinfolab=new SimpleStringProperty("");
	//for reminder
	
	public static List<List<String>> todayeventlist=new ArrayList<List<String>>();
	
   //for csv generation...
	public static ObservableList<String> pressure=FXCollections.observableArrayList();
	public static ObservableList<String> temp=FXCollections.observableArrayList();

	
	// for user input screen...
	public static String path_csv="";
	public static SerialCommunicator sc;
	
	
	// for live graph...
	public static int xdiff=0,flow_min=0;
	
	public static int no_sample=0;
	public static  int xSeriesData = 0;
	public static ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<Number>();   
	
	
	
    // for live graph listenrt
	public static int refresh=0;
	
     //for new test screen
	public static TabPane test_tab1;
	public static Tab user1,liveprocess1,livegraphs1,reports1;
 	
    // for connection is redy or not
 	
	public static SimpleBooleanProperty connect_hardware=new SimpleBooleanProperty(false);
	public static SimpleBooleanProperty connect_internet=new SimpleBooleanProperty(false);
 	
 	
    //for serial commnunicatior

	public static Set<Character> listOfHeads = new HashSet<Character>();// Contains all Heads eg. to read Pressure P, to read Temperature T  
	public static List<List<Integer>> allDataRead = new ArrayList<List<Integer>>();// List of all the data read 
 //	static HashMap<String,ObservableList<Integer>> intList = new HashMap<String,ObservableList<Integer>>(); // map for value received for P, T, H,F etc.
	public static HashMap<String,ObservableList<Double>> intList = new HashMap<String,ObservableList<Double>>(); // map for value received for P, T, H,F etc.
 	
	public static OutputStream out;
   
    static String type,pg1,pg2,fm1,fm2,chemb;
    
    public static boolean isCurveFit=false;
    public static  void setLoadListener()
    {
    	DataStore.isconfigure.addListener(new ChangeListener<Boolean>() {

    		@Override
    		public void changed(
    				ObservableValue<? extends Boolean> arg0,
    				Boolean arg1, Boolean arg2) {
    		
    			System.out.println("Chaangedd ------->"+arg2);
    			if(arg2==true)
    			{
    				Main.splashstage.hide();
    				Main.mainstage.show();
    				
    			}
    			
    		}
    	});
    }
	public static void Refresh()
	{

		
		System.out.println(" Calling refresh start");
		refresh=1;
		sc=null;
		

		listOfHeads = new HashSet<Character>();// Contains all Heads eg. to read Pressure P, to read Temperature T  
	 	allDataRead = new ArrayList<List<Integer>>();// List of all the data read 
	    intList = new HashMap<String,ObservableList<Double>>(); 
		
		
		
		pressure=null;
		pressure=FXCollections.observableArrayList();
		
		temp=null;
		temp=FXCollections.observableArrayList();
		

		 xdiff=0;
		 no_sample=0;
		 xSeriesData=0;
		 
		// dataQ=null;
		// dataQ=new ConcurrentLinkedQueue<Number>();
		 
		 System.out.println(" Calling refresh end");
		
	}
	
	public static void getconfigdata()
	{
		Database db=new Database();		
		List<List<String>> ll=db.getData("select * from configdata");
		type =(ll.get(0).get(0));
		pg1 =(ll.get(0).get(1));
		pg2 =(ll.get(0).get(2));
		fm1 =(ll.get(0).get(3));
		fm2 =(ll.get(0).get(4));
		chemb=(ll.get(0).get(7));
		pr=(ll.get(0).get(5));
		fc=(ll.get(0).get(6));
		
		String curvefittype =(ll.get(0).get(16));
		
		if(curvefittype.equals("on"))
		{
			isCurveFit=true;
		}
		else
		{
			isCurveFit=false;
		}
		
		System.out.println("type"+DataStore.getType());
		System.out.println("pg1"+DataStore.getPg1());
		System.out.println("pg2"+DataStore.getPg2());
		System.out.println("fm1"+DataStore.getFm1());
		System.out.println("fm2"+DataStore.getFm2());
		System.out.println("Pr : "+DataStore.getPr());
		System.out.println("fc : "+DataStore.getFc());
		
		System.out.println("chemb"+DataStore.getChemb());
		System.out.println("Curve FIt : "+isCurveFit);
	
	
	}
	public boolean isCurveFit()
	{
		return isCurveFit;
	}
	public static String getPr() {
		return pr;
	}

	public static void setPr(String pr) {
		DataStore.pr = pr;
	}

	public static String getFc() {
		return fc;
	}

	public static void setFc(String fc) {
		DataStore.fc = fc;
	}

	public static String getPg1() {
	System.out.println();
		return pg1;
	}

	public static String getPg2() {
	
		return pg2;
	}

	
	public static String getFm1() {
	
		return fm1;
	}

	public static String getFm2() {
	
		return fm2;
	}
	
	public static String getChemb() {
	
		return chemb;
	}

	public static String getType() {
		return type;
	}

	public static String getCom()
	{
		String cm="";
		Database db=new Database();		
		List<List<String>> ll=db.getData("select comport from configdata");
		cm =(ll.get(0).get(0));
			
		
		return cm;
	}

	public static boolean hardReset()
	{
		try{

			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub

					
					DataStore.serialPort.setDTR(true);
					try{Thread.sleep(200);}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					DataStore.serialPort.setDTR(false);
					
					writeFormat	wrd = new writeFormat();
					List<String> dd=getAdmin_screen1();
					wrd.setLatching(dd.get(0).charAt(0),dd.get(1).charAt(0),dd.get(2).charAt(0),dd.get(3).charAt(0),dd.get(4).charAt(0),dd.get(5).charAt(0),dd.get(6).charAt(0),dd.get(7).charAt(0));
					
					//wrd.setLatching('1', '0', '0', '1', '0', '1', '1', '1');
					wrd.addLast();
					sendData(wrd,500);
					
					
					
					writeFormat	wrd1 = new writeFormat();
					wrd1.addChar('C');
					wrd1.addChar('O');
					wrd1.addChar('P');
					wrd1.addChar('1');
					wrd1.addChar(getPressureCrossover().charAt(0));
					wrd1.addLast();
					sendData(wrd1,700);
					
					
					writeFormat	wrd2 = new writeFormat();
					wrd2.addChar('C');
					wrd2.addChar('O');
					wrd2.addChar('F');
					wrd2.addChar('1');
					wrd2.addChar(getFlowCrossover().charAt(0));
					wrd2.addLast();
					sendData(wrd2,900);
					
					
				}
			}).start();
			
			
			return true;
		}
		catch(Exception e)
		{
			return false;
		
		}
		
		
	}
	
	
	
	static void sendData(writeFormat w, int slp) {
		System.out.println("Sending Data......");
		w.showData();
		Thread t = new Thread(new SerialWriter(DataStore.out, w, slp));
		try {

			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static List<String> getAdmin_screen1()
	{
	
		List<String> data=new ArrayList<String>();
		Database db=new Database();		
		List<List<String>> ll=db.getData("select * from admin_screen1");
		
		String va =(ll.get(0).get(1));
		String vb =(ll.get(0).get(2));
		String vc =(ll.get(0).get(3));
		String vd =(ll.get(0).get(4));
		String ve =(ll.get(0).get(5));
		String vf =(ll.get(0).get(6));
		String vg =(ll.get(0).get(7));
		String vh =(ll.get(0).get(8));
		
		String pc =(ll.get(0).get(9));
		String fc =(ll.get(0).get(10));
		
		data.add(va);
		data.add(vb);
		data.add(vc);
		data.add(vd);
		data.add(ve);
		data.add(vf);
		data.add(vg);
		data.add(vh);
		
		data.add(pc);
		data.add(fc);
		
		return data;
	}

	public static String getchambertype()
	{
		String ch="";
		Database db=new Database();		
		List<List<String>> ll=db.getData("select chambertype from configdata");
		ch =(ll.get(0).get(0));
			
		
		return ch;
	}
	
	public static String getthfirstbp()
	{
		String bp="";
		Database db=new Database();		
		List<List<String>> ll=db.getData("select thfirst from configdata");
		bp =(ll.get(0).get(0));

		return bp;
	}
	public static String getthmoderat()
	{
		String bp="";
		Database db=new Database();		
		List<List<String>> ll=db.getData("select thmoderate from configdata");
		bp =(ll.get(0).get(0));

		return bp;
	}
	public static String getthcontinous()
	{
		String bp="";
		Database db=new Database();		
		List<List<String>> ll=db.getData("select thcontinous from configdata");
		bp =(ll.get(0).get(0));

		return bp;
	}
	
	public static void defaultsetting()
	{
		Database db=new Database();
		
		String conficupdate = "update configdata set  pg1='5',pg2='100',fm1='10000',fm2='200000',pr='130',chambertype='Manual',curvefittgb='on',fc='30',thfirst='1500',thmoderate='2500',thcontinous='3000'"; 
		
		String admindata = "update admin_screen1 set  pc='1',fc='1',va='0',vb='1',vc='0',vd='1',ve='1',vf='0',vg='0',vh='0'";
		
		if(db.Insert(conficupdate) && db.Insert(admindata))
		{
			 Toast.makeText(Main.mainstage, "Successfully Set Default Setting..", 1000, 200, 200);

		}
		else {
//			System.out.println("Configration Data save d Eroorr.....");
		}
	}
	
	public static String getPressureCrossover()
	{
		int bp;
		Database db=new Database();		
		List<List<String>> ll=db.getData("select pc from admin_screen1");
		bp =Integer.parseInt((ll.get(0).get(0)));
		return bp+"";
	}
	
	public static String getFlowCrossover()
	{
		int bp;
		Database db=new Database();		
		List<List<String>> ll=db.getData("select fc from admin_screen1");
		bp =Integer.parseInt((ll.get(0).get(0)));
		return bp+"";
	}
	
	
}
