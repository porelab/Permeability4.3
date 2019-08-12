package userinput;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import toast.MyDialoug;
import application.Myapp;
import extrafont.Myfont;

public class NteststartpopupController implements Initializable {
	
	@FXML
	    private Button btnstart,btncancel;

	    @FXML
	    private Label lbltestcount,lblsname;
	    
	    
	    Myfont f=new Myfont(25);

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		/*Test start Popup*/
		
		
		lblsname.setText(Myapp.sampleid);
		
		//setLabelFont();
		
		
		btncancel.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				NLivetestController.stpropcan.set(true);
				

				MyDialoug.closeDialoug();

			}
		});
		
		btnstart.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				NLivetestController.stprop.set(true);
				

				MyDialoug.closeDialoug();
				
			}
		});
				
	}

	void setLabelFont()
	{
		btncancel.setFont(f.getM_M());
		btnstart.setFont(f.getM_M());

	}
	
	
	
	
}
