package boardcheck;

import eu.hansolo.medusa.Gauge;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TooManyListenersException;

import toast.Openscreen;
import toast.Toast;
import userinput.manualcontroller;
import userinput.NLivetestController.SerialReader;
import application.DataStore;
import application.Main;
import application.Myapp;
import application.SerialWriter;
import application.writeFormat;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class BoardcheckController implements Initializable 
{

    @FXML
    private TextField pr;

    @FXML
    private TextField fc;

	
	@FXML
	Button clear,back;
	
	@FXML
	ToggleButton record,record1;
	
	SerialReader in;
    @FXML
    private TextField no;

    @FXML
    private Button btnstart;

    @FXML
    private Button btnstop;

    @FXML
    private Label lblstatus;

    writeFormat wrD ;
    
    @FXML
    private Label lbla;

    @FXML
    private Label lblb;

    @FXML
    private Label lblc;

    @FXML
    private Label lbld;

    @FXML
    private Label lble;
    
    int testno=0;
    
    boolean isStop=false;
    
    @FXML
    TextArea txtarea;
	
    void connectHardware() {
		in = new SerialReader(DataStore.in);

		try {
			DataStore.serialPort.removeEventListener();
			DataStore.serialPort.addEventListener(in);
			DataStore.serialPort.notifyOnDataAvailable(true);

			lblstatus.setText("Hardware Connected");

		} catch (TooManyListenersException e) {
			Toast.makeText(Main.mainstage, "Hardware Problem", 1000, 100, 100);

			 SystemPrintln("Error 6: "+e.getMessage());
			lblstatus.setText("Hardware Problem");
		} catch (Exception e) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					 SystemPrintln("Error 4: "+e.getMessage());
					lblstatus.setText("Hardware Problem");
					Toast.makeText(Main.mainstage, "Hardware Problem", 1000,
							100, 100);

				}
			});

		}

	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		
		connectHardware();
		btnstart.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(!no.getText().trim().isEmpty())
				{
					try{
						testno=Integer.parseInt(no.getText());
						isStop=false;
						
						wrD = new writeFormat();
						wrD.addChar('S');
						wrD.addChar('M');
						wrD.addBlank(3);
						wrD.addLast();
						sendData(wrD);
						
						
						setFC();
						setPR();
						
						no.setDisable(true);
						btnstart.setDisable(true);
						pr.setDisable(true);
						fc.setDisable(true);
					
						Thread t=new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub

								startTest(1);	
							}
						});
					
						t.setDaemon(false);
						t.start();
					
					}
					catch(Exception e)
					{

						 SystemPrintln("Error 4: "+e.getMessage());
					}
				}
			
			}
		});
		
		back.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				DataStore.hardReset();
				Openscreen.open("/application/first.fxml");

			}
		});
		
		clear.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
		
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						
						txtarea.setText("");
						
					}
				});
				
			}
		});
		
		btnstop.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
		stopTest();
				
			}
		});
	
	}

	void stopTest()
	{
		isStop=true;
		no.setDisable(false);
		btnstart.setDisable(false);
		pr.setDisable(false);
		fc.setDisable(false);
		SystemPrintln("test has been stop");
		
		setFC0();
		
		setPR0();
		
		wrD = new writeFormat();
		wrD.addChar('X');
		wrD.addChar('M');
		wrD.addBlank(3);
		wrD.addLast();
		sendData(wrD,500);
		
		
		
	}
	
	public static List<Integer> getValueList(int val) {
		String pad = "000000";
		String st = "" + Integer.toHexString(val);
		String st1 = (pad + st).substring(st.length());
		List<Integer> ls = new ArrayList<Integer>();

		int n = (int) Long.parseLong(st1.substring(0, 2), 16);
		int n1 = (int) Long.parseLong(st1.substring(2, 4), 16);
		int n2 = (int) Long.parseLong(st1.substring(4, 6), 16);
		ls.add(n);
		ls.add(n1);
		ls.add(n2);

		return ls;
	}

	void setPR0() {

		try {
			
			List<Integer> ss = getValueList((int) 0);
			wrD = new writeFormat();
			wrD.addChar('P');
			wrD.addChar('R');
			wrD.addData1(ss);
			wrD.addLast();
			sendData(wrD,200);


		} catch (Exception e) {

			 SystemPrintln("Error 3: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	void setPR() {

		try {
			double d1 = Double.parseDouble(pr.getText());
			double d=(double)65535*d1 / Integer.parseInt(DataStore.getPr());
			SystemPrintln("Sending  : "+(int)d);
			List<Integer> ss = getValueList((int) d);
			wrD = new writeFormat();
			wrD.addChar('P');
			wrD.addChar('R');
			wrD.addData1(ss);
			wrD.addLast();
			sendData(wrD,200);


		} catch (Exception e) {

			 SystemPrintln("Error 3: "+e.getMessage());
			e.printStackTrace();
		}
	}

	
	void setFC0() {
		try {


			List<Integer> ss = getValueList(0);
			wrD = new writeFormat();
			wrD.addChar('F');
			wrD.addChar('C');
			wrD.addData1(ss);
			wrD.addChkSm();
			wrD.addLast();
			sendData(wrD,100);


		} catch (Exception e) {
		 SystemPrintln("Error 1: "+e.getMessage());
			e.printStackTrace();
		}
	}

	
	void setFC() {
		try {

			double d1 = Double.parseDouble(fc.getText());
			// double d=Double.parseDouble(pr.getText());

			double d=(double)65535*d1 / Integer.parseInt(DataStore.getFc());
			SystemPrintln("Sending  : "+(int)d);

			List<Integer> ss = getValueList((int) d);
			wrD = new writeFormat();
			wrD.addChar('F');
			wrD.addChar('C');
			wrD.addData1(ss);
			wrD.addChkSm();
			wrD.addLast();
			sendData(wrD,100);


		} catch (Exception e) {
		 SystemPrintln("Error 1: "+e.getMessage());
			e.printStackTrace();
		}
	}


	void sendData(writeFormat w, int slp) {
		System.out.println("Sending Data......");
		w.showData();
		Thread t = new Thread(new SerialWriter(DataStore.out, w, slp));
		try {

			t.start();
		} catch (Exception e) {

			 SystemPrintln("Error 2: "+e.getMessage());
			e.printStackTrace();
		}

	}
	
    void SystemPrintln(String s)
    {
    	if(record.isSelected())
    	{
    		Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub

			    	txtarea.appendText(s+"\n");
				}
			});
    	}
    }
    

    void SystemPrint(String s)
    {
    	if(record.isSelected())
    	{
    		Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub

			    	txtarea.appendText(s);
				}
			});
    	}
    }
    
	
	void startTest(int n)
	{
		if(n>testno || isStop)
		{
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(isStop==true)
					{

						
						lblstatus.setText("Stop ...");
					}else
					{

						stopTest();
						lblstatus.setText("Test Completed "+testno+" times.");
					}
				}
			});
		}
		else
		{
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					

					lblstatus.setText("Test running "+n+"/"+testno);
					
					
					
				}
			});
			
			valveOn('6', lbla);
			try{
				Thread.sleep(3000);
			}
			catch(Exception e)
			{
				
			}
			valveOff('6', lbla);
			try{
				Thread.sleep(3000);
			}
			catch(Exception e)
			{
				
			}
			
			valveOn('2', lblb);
			try{
				Thread.sleep(3000);
			}
			catch(Exception e)
			{
				
			}
			valveOff('2', lblb);
			try{
				Thread.sleep(3000);
			}
			catch(Exception e)
			{
				
			}
			
			
			valveOn('3', lblc);
			try{
				Thread.sleep(3000);
			}
			catch(Exception e)
			{
				
			}
			valveOff('3', lblc);
			try{
				Thread.sleep(3000);
			}
			catch(Exception e)
			{
				
			}
			
			valveOn('4', lbld);
			try{
				Thread.sleep(3000);
			}
			catch(Exception e)
			{
				
			}
			valveOff('4', lbld);
			try{
				Thread.sleep(3000);
			}
			catch(Exception e)
			{
				
			}
			
			valveOn('5', lble);
			try{
				Thread.sleep(3000);
			}
			catch(Exception e)
			{
				
			}
			valveOff('5', lble);
			try{
				Thread.sleep(3000);
			}
			catch(Exception e)
			{
				
			}
			
			startTest(n+1);
		}
	}

	
	void valveOn(char no,Label l)
	{

		
		
		writeFormat wrD = new writeFormat();
		wrD.addChar('V');
		wrD.addChar(no);
		wrD.addChar('1');
		wrD.addBlank(2);
		wrD.addLast();
		sendData(wrD);
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				l.setText("On");
			}
		});
	}
	void valveOff(char no,Label l)
	{

		writeFormat wrD = new writeFormat();
		wrD.addChar('V');
		wrD.addChar(no);
		wrD.addChar('0');
		wrD.addBlank(2);
		wrD.addLast();
		sendData(wrD);
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				l.setText("Off");
			}
		});
	}
	public void sendData(writeFormat w) {
		SystemPrintln("Sending Data......");
		SystemPrintln(w.showDataGet());
		Thread t = new Thread(new SerialWriter(DataStore.out, w));
		t.start();
		// start.setDisable(true);
	}
	public class SerialReader implements SerialPortEventListener {

		InputStream in;
		int ind = 0;
		List<Integer> readData = new ArrayList<Integer>();

		public SerialReader(InputStream in) {
			this.in = in;
			DataStore.getconfigdata();
		}

		public void serialEvent(SerialPortEvent arg0) {
			int data;
			try {
				int len = 0;
				char prev = '\0';
				// SystemPrintln("Reading Started:");

				while ((data = in.read()) > -1) {

					if (data == '\n' && prev == 'E') {
						break;
					}
					if (len > 0 || (data == '\r' && prev == '\n')) {
						readData.add(data);

						len++;
					}
					prev = (char) data;
					SystemPrint(""+prev);

					// System.out.print(new String(buffer,0,len));
				}

				for (int i = 1; i < readData.size(); i++) {

					if(readData.get(i)==83 && readData.get(i+1)==(int)'C')
        			{
        			//.. for fm1
        			SystemPrintln("In");
        			int a=0,a1,a2,a3;
    				a1=readData.get(i+2);
    				a2=readData.get(i+3);
    				a3=readData.get(i+4);
    				
    				if(record1.isSelected())
    				SystemPrintln("\nFlow Meter 1 - >  bits  : "+a1+" : "+a2+" : "+a3);
           			
    				a=a1<<16;
    				   a2=a2<<8;
    				   a = a|a2;
    				   a = a|a3;
    				 
    				 //  SystemPrintln("Flow Meter 1 :  ... :"+DataStore.getFm1());
    				 double b=(double)a*Integer.parseInt(DataStore.getFm1())/65535;

     				if(record1.isSelected())
    				 SystemPrintln("Flow Meter 1 :  ... :"+b);
   					 
    				
   					
   					
   					if(a>62200)
   					{
   						//SkadaController.valve1s.selectedProperty().bind(DataStore.sv1);

   	    				if(record1.isSelected())
   						SystemPrintln("Max flow reach to fm1");
   					}
        			//.... for fm2
   					
   					a=0;
   					a1=readData.get(i+5);
    				a2=readData.get(i+6);
    				a3=readData.get(i+7);
    				

    				if(record1.isSelected())
    				   SystemPrintln("\nFlow Meter 2 - >  bits  : "+a1+" : "+a2+" : "+a3);
              			
    				
    				a=a1<<16;
    				   a2=a2<<8;
    				   a = a|a2;
    				   a = a|a3;
    				
    				  b=(double)a*Integer.parseInt(DataStore.getFm2())/65535;

      				if(record1.isSelected())
    				  SystemPrintln("Flow Meter 2 :  ... :"+b);

   					//.... for pg1
   					
   					
   					a=0;
   					a1=readData.get(i+8);
    				a2=readData.get(i+9);
    				a3=readData.get(i+10);
    			

    				if(record1.isSelected())
    				SystemPrintln("\nPressure G1  -> Bit : "+a1+" : "+a2+" : "+a3);
        			
    				a=a1<<16;
    				   a2=a2<<8;
    				   a = a|a2;
    				   a = a|a3;
    					
    				  b=(double)a*Integer.parseInt(DataStore.getPg1())/65535;


      				if(record1.isSelected())
    				  SystemPrintln("Pressure Gauge 1 :  ... :"+b);

   					
   					if(a>62200)
   					{
   						//SkadaController.valve3s.selectedProperty().bind(DataStore.sv3);

   	    				if(record1.isSelected())
   						SystemPrintln("Max pressure reach to PG1");
   					}
   					
   					//.... for pg2
   				
   					a=0;
   					a1=readData.get(i+11);
    				a2=readData.get(i+12);
    				a3=readData.get(i+13);

    				if(record1.isSelected())
    				SystemPrintln("\nPressure G2  -> Bit : "+a1+" : "+a2+" : "+a3);
        			
    				a=a1<<16;
    				   a2=a2<<8;
    				  a = a|a2;
    				   a = a|a3;
    				  b=(double)a*Integer.parseInt(DataStore.getPg2())/65535;
   					

      				if(record1.isSelected())
    				  SystemPrintln("Pressure Gauge 2 :  original   reading  : "+a+"... :"+b);
   					

        			i=i+16;
        			
        			}

					readData.clear();
					break;

				}

			} catch (IOException e) {
				e.printStackTrace();

				 SystemPrintln("Error 5: "+e.getMessage());
			}

		}

	}

}
