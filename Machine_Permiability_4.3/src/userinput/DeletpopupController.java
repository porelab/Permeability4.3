package userinput;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import toast.MyDialoug;
import toast.Openscreen;
import toast.Toast;
import application.Main;

public class DeletpopupController implements Initializable {

	@FXML 
	Button btncancel,btnyes;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	
		btncancel.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				MyDialoug.closeDialoug();
			}
		});
		
		btnyes.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
			
				System.out.println("Deleted File "+ResultController.delfile);
				MyDialoug.closeDialoug();
				
				if(deleteFile(ResultController.delfile))
				{
				Openscreen.open("/userinput/result.fxml");
				}
				else
				{

					Toast.makeText(Main.mainstage, "Can't delete this file", 500, 500, 2000);	
				}
				
				
			}
		});
		
		
	}

	
	
	boolean deleteFile(String fname)
	{
		try{
		
				File f=new File("CsvFolder/"+"N0073"+"/"+"Nsample22"+"/"+fname+".csv");
				if(f.exists())
				{
					System.out.println("Found" +f.getAbsolutePath());
				}
				else
				{
					System.out.println("Not found "+f.getAbsolutePath());
				}
				if(f.delete())
					return true;
				else 
					return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
			
		}
	}
	
}
