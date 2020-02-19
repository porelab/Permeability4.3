package userinput;

import java.net.URL;
import java.util.ResourceBundle;

import application.Myapp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import toast.MyDialoug;

public class TestinfopopupContoller implements Initializable {
	
	
	 @FXML
	    private AnchorPane root;

	    @FXML
	    private Button btnclose;

	    @FXML
	    private Label lblsname,lbltesttype,lblsplate,lblstability,lbltestacc;
	    
	  	@Override
	  	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		
		lblsname.setText(Myapp.sampleid);
		
		String ss=""+Myapp.testtype;
		
		if (ss.equals("1")) {
			lbltesttype.setText("Gas Permeability");
		}
		else if (ss.equals("2")) {
			lbltesttype.setText("Flow Step :" +Myapp.steppoints);
			}		
		else {
			lbltesttype.setText("Pressure DK : A).Interval - "+Myapp.dkdatainterval+" B).Hold Time - "+Myapp.dkholdtime+" C).Pressure - "+Myapp.dkpressure);

		}	

		if (Myapp.splate.equals("Small")) {
			lblsplate.setText(Myapp.splate +" : 1");
			
			} else if (Myapp.splate.equals("Large")) {

				lblsplate.setText(Myapp.splate +" : 7.2");
				} else {

					lblsplate.setText(Myapp.splate +" : 2.3");
			}
		
		
		
		if (Myapp.stabilitytype.equals("1")) {
			lblstability.setText("Delay :"+Myapp.accstability);
		}

		else if (Myapp.stabilitytype.equals("2")) {
			lblstability.setText("Aevrage :"+Myapp.accstability);
		}

		else {
			lblstability.setText("Method 3 :"+Myapp.accstability);
		}
		
		
		
			
		lbltestacc.setText(""+Myapp.accstep);
		
		
		
		
		
		
		
		
		btnclose.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
			
				MyDialoug.closeDialoug();
			}
		});
		
	}

}
