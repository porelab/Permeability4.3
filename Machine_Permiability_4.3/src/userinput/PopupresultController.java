package userinput;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import toast.MyDialoug;
import toast.Openscreen;
import application.DataStore;
import application.Myapp;
import data_read_write.DatareadN;
import drawchart.ChartPlot;

public class PopupresultController implements Initializable {

    @FXML
    private Label lblsamplename,lblbpp,lblbpdiamter,lblmeanp,lblunitegurley,lblunitefrazier;

    @FXML
    private Button btnhome,startautotest;

    @FXML
    private AnchorPane pagination1;
    
    ChartPlot c;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//File f=new File("test_sample0221.csv");
		
		/*After Test Complete Open a Result Screen*/
		
		
		DatareadN d=new DatareadN();
		d.fileRead(NLivetestController.savefile);
		
		setData(d);
		
		btnhome.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				MyDialoug.closeDialoug();
				Openscreen.open("/application/first.fxml");
				
			}
		});
		
		startautotest.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
			
				MyDialoug.closeDialoug();
				Openscreen.open("/userinput/Nlivetest.fxml");
				
				
			}
		});
	}
	
	void setData(DatareadN dr)	
	{
		
		
		
		List<DatareadN> list_d =new ArrayList<DatareadN>();
			c=new ChartPlot(true);
		list_d.add(dr);
	//pagination1.getChildren().add(c.drawBarchart(pagination1.getPrefWidth(),pagination1.getPrefHeight(),"Pore size Distribution", "Diameter (micron)", "Percentage (%)", list_d.get(0).getDistributionChart(list_d.get(0).getValuesOf(list_d.get(0).data.get("diameter")+""),list_d.get(0).getValuesOf(list_d.get(0).data.get("psd")+""),30)));
	//pagination1.getChildren().add(c.drawBarchartNumber(pagination1.getPrefWidth(),pagination1.getPrefHeight(),"Pore size Distribution", "Diameter (micron)", "Percentage (%)", list_d.get(0).getValuesOf(list_d.get(0).data.get("diameter").toString()),list_d.get(0).getValuesOf(list_d.get(0).data.get("psd").toString())));
	
		pagination1.getChildren().add(c.drawLinechart(pagination1.getPrefWidth(),pagination1.getPrefHeight(),"Pressure vs Flow", "Pressure (psi)", "Flow (sccs)",list_d,false,8,10,"(4) Pressure vs Flow"));
		
		
		lblsamplename.setText(""+dr.data.get("sample"));

	     lblmeanp.setText(""+DataStore.ConvertGurely(""+dr.data.get("gurley")));
	     lblbpp.setText("" +Myapp.getRound(""+dr.data.get("darcy avg"), DataStore.getRoundOff()));
	     lblbpdiamter.setText("" +DataStore.ConvertFrazier(""+dr.data.get("frazier")));
	     
	     lblunitefrazier.setText( "("+DataStore.getUnitefrazier()+")");
	     lblunitegurley.setText(" ("+DataStore.getUnitegurely()+")");

	}
}
