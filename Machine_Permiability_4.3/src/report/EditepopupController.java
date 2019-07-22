package report;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class EditepopupController implements Initializable {

		@FXML
		ScrollPane mainsrollpane;
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	
		DisplaySelectedfile();
	}
	
	
	void DisplaySelectedfile()
	{
		VBox v=new VBox(20);
		
		Label lab=new Label();
		lab.setText("File1");
		
		v.getChildren().addAll(lab);
		

		mainsrollpane.setContent(v);
	}

}
