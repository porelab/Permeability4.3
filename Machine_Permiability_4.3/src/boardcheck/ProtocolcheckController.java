package boardcheck;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TooManyListenersException;

import communicationProtocol.Mycommand;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import toast.MyDialoug;
import toast.Openscreen;
import toast.Toast;
import application.DataStore;
import application.Main;
import application.SerialWriter;
import application.writeFormat;

public class ProtocolcheckController implements Initializable {
	@FXML
	Button btn1, btn2, btn3,btn31,btn311,btn3111,btn4,btn41;
	

	
	SerialReader in;
    writeFormat wrD ;
	
	@FXML
	Label lblstatus;
	
	@FXML
	TextArea txtbox;

	boolean isstart=false;
	int inc=1;
	

    @FXML
    private Button btnback;

    @FXML
    private Button setvalve;

    @FXML
    private ToggleButton onoffvalve;
	
   
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
		setvalve.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
			//	Mycommand.setLacthing("11111100011111",1000);
			}
		});
		
		onoffvalve.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				
				if(onoffvalve.isSelected())
				{
					Mycommand.valveOn(1, 0);
				}
				else
				{

					Mycommand.valveOff(1, 0);
				}
				
				
			}
		});
		
		
		btnback.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				Openscreen.open("/application/first.fxml");
				
			}
		});
		
		btn1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				sendAdcEnableBits();
				
				
			}
		});

		btn2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				sendAdcDelay();
			}
		});

		btn3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				
				if(!isstart)
				{
					sendAdcStart();
					isstart=true;
				}
				else
				{
					sendAdcStop();
					isstart=false;
				
				}
				
			}
		});

		btn31.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				//setDacValue('1', 10000,100);
				setDacValue('2', 16000,300);
				//setDacValue('3', 10000,500);
				//setDacValue('4', 10000,700);
				
			}
		});
		btn311.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				setDacIncrement(100, 200, 300, 400);	
				
			}
		});
		btn3111.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				setDacIncrement(0, 0, 0, 0);
				
			}
		});
		
		btn4.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
			
				getAdcSingleVal(2);
				//inc++;

				btn4.setText("Single Value :"+inc);
				
			}
		});
		btn41.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				systemReset();
				
			}
		});
		
		connectHardware();
	}
	
	
	void systemReset()
	{
    	writeFormat wr=new writeFormat();
		wr.addChar('C');
		wr.addChar('V');
		wr.addChar('D');
		wr.addChar('1');
		wr.addChar('1');
		wr.addChar('1');
		wr.addChar('1');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addData1(getValueList(2000));
		wr.addLast();
		sendData(wr, 200);
	}
	
	 void getAdcSingleVal(int adc)
	    {
	    	writeFormat wr=new writeFormat();
			wr.addChar('R');
			wr.addChar('S');
			wr.addChar('A');
			wr.addInt(adc);
			wr.addBlank(1);
			wr.addLast();
			sendData(wr, 200);
	    }

    void connectHardware() {
		in = new SerialReader(DataStore.in);

		try {
			DataStore.serialPort.removeEventListener();
			DataStore.serialPort.addEventListener(in);
			DataStore.serialPort.notifyOnDataAvailable(true);

			lblstatus.setText("Hardware Connected");

		} catch (TooManyListenersException e) {
			Toast.makeText(Main.mainstage, "Hardware Problem", 1000, 100, 100);

			lblstatus.setText("Hardware Problem");
		} catch (Exception e) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					lblstatus.setText("Hardware Problem");
					Toast.makeText(Main.mainstage, "Hardware Problem", 1000,
							100, 100);

				}
			});

		}

	}
    
    
    void setDacIncrement(int v1,int v2,int v3,int v4)
    {
    	writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('D');
		wr.addChar('I');
		wr.addData1(getValueList(v1));
		wr.addData1(getValueList(v2));
		wr.addData1(getValueList(v3));
		wr.addData1(getValueList(v4));
		
		wr.addLast();
		sendData(wr, 100);
    }
    
    
    void setDacValue(char dac,int val,int delay)
    {
    	writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('D');
		wr.addChar('S');
		wr.addChar(dac);
		wr.addData1(getValueList(val));
		wr.addLast();
		sendData(wr, delay);
    }
    
	void sendData(writeFormat w, int slp) {
		SystemPrintln("Sending Data......  "+w.showDataGet());
		
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
    	
    		Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
			    	txtbox.appendText(s+"\n");
				}
			});
    	
    }
    void SystemPrint(String s)
    {
    	
    		Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
			    	txtbox.appendText(s);
				}
			});
    	
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
					SystemPrint(prev+"");
					System.out.print(prev);
					// SystemPrint(new String(buffer,0,len));
				}

				for (int i = 1; i < readData.size(); i++) {

					if(readData.get(i)=='F' && readData.get(i+1)==(int)'M'&& readData.get(i+2)==(int)'A')
        			{
        			
				
						SystemPrintln(""+getAdcData(readData));
						
						
        			}
					else if(readData.get(i)=='S' && readData.get(i+1)=='A')
					{
						int ch=readData.get(i+2);
						SystemPrintln("SINGLE : "+(char)ch+" : "+getIntFromBit(readData.get(i+3), readData.get(i+4), readData.get(i+5)) );
					}
					readData.clear();
					break;
				}

			} catch (IOException e) {
				e.printStackTrace();

			}

		}

	}
	
	List<Integer> getAdcData(List<Integer> data)
	{
		List<Integer> d=new ArrayList<Integer>();
		
		System.out.println("READ .... ");
		for(int i=4;i<49;i=i+3)
		{
		d.add(getIntFromBit(data.get(i), data.get(i+1), data.get(i+2)));
	
		}
		System.out.println("READ DONE ..."+d.size());
		System.out.println("Adc Data :"+d);
		return d;
	}
	
	void sendAdcStart()
	{
		writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('S');
		wr.addBlank(2);
		wr.addLast();
		sendData(wr, 100);
		
		
	}
	void sendAdcStop()
	{
		writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('X');
		wr.addBlank(2);
		wr.addLast();
		sendData(wr, 100);
	}
	void sendAdcDelay()
	{
		writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('L');
		wr.addData1(getValueList(2000));
		wr.addLast();
		sendData(wr, 100);
		
		
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

	void sendAdcEnableBits()
	{
		writeFormat wr=new writeFormat();
		wr.addChar('F');
		wr.addChar('M');
		wr.addChar('A');
		wr.addChar('E');
		wr.addChar('1');
		wr.addChar('1');
		wr.addChar('1');
		wr.addChar('1');
		wr.addChar('1');
		wr.addChar('1');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addChar('0');
		wr.addLast();
		
		sendData(wr, 0);
	}
	
	int getIntFromBit(int a1,int a2,int a3)
	{
		System.out.println(a1 + " : "+a2+ ": "+a3);
		int a=0;
		
		a=a1<<16;
		   a2=a2<<8;
		   a = a|a2;
		   a = a|a3;
		   
		   return a;
	}
}
