package userinput;

import java.net.URL;
import java.text.Format;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.poi.hssf.util.HSSFColor.WHITE;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import toast.Openscreen;
import application.DataStore;
import application.Myapp;
import application.SerialWriter;
import application.writeFormat;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import gnu.io.CommPortIdentifier;
public class manualcontroller implements Initializable {

	
	ObservableList<Data> alldata;
	
	public static ObservableList<String> fm1,fm2,pg1,pg2;
	
	@FXML
	ToggleButton recordbtn;
	
	@FXML
	AnchorPane root;


	@FXML
	Label lblfm1,lblfm2, pg1value, labp1, labp2,lblfm1max,lblfm2max,lblpg1max,lblpg2max;

	@FXML
	Label lblanc, lblanc2, lblconnection,lblrealeas;

	@FXML
	AnchorPane ap, ap1, ap2, ap3, ap4, ap5, ap6, ap7;

	@FXML
	Button setpr, setfc, btnback, btnreconnect;

	@FXML
	TextField pr, fc;

	
	@FXML
	TableView datatable;
	
	@FXML
	ImageView v1, v2, v3, v4, v5new, liveimg, imgreg1, imgv1, imgreg2, imgv2,
			imgv3, imgv4, imgv5,onoffimg;

	@FXML
	ToggleButton valve1, valve2, valve3, valve4, valve5new, valveonoff;

	public static ToggleButton valve1s, valve2s, valve3s, valve4s;

	public Gauge gauge23, gauge23c, gauge20, gauge12, gauge12c, gauge12c1,
			gauge12c2;
	writeFormat wrD;
	
	 @FXML
	 Circle concircle;
	 

	final BooleanProperty spacePressed = new SimpleBooleanProperty(false);
	final BooleanProperty rightPressed = new SimpleBooleanProperty(false);
	final BooleanBinding spaceAndRightPressed = spacePressed.and(rightPressed);

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		addShortCut();

		fm1=FXCollections.observableArrayList();
		fm2=FXCollections.observableArrayList();
		pg1=FXCollections.observableArrayList();
		pg2=FXCollections.observableArrayList();
		
		
		recordbtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
			
			if(recordbtn.isSelected())
			{
				recordbtn.setText("Pause");
			}
			else
			{

				recordbtn.setText("Record");
			}
				
			}
		});
		
		alldata=FXCollections.observableArrayList();
		setupMainTableColumns();
		//addDataToTable();
		Image image1 = new Image(this.getClass().getResourceAsStream(
				"/userinput/btnimg.png"));
		setpr.setGraphic(new ImageView(image1));
		setfc.setGraphic(new ImageView(image1));

		Image image = new Image(this.getClass().getResourceAsStream(
				"/userinput/valve OFF.png"));
		
		valveonoff.setText("START");
		recordbtn.setText("Record");
		valveonoff.setStyle("-fx-background-color: #0e3c87;");
		Image image2 = new Image(this.getClass()
				.getResourceAsStream("/userinput/starticon.png"));
		valveonoff.setGraphic(new ImageView(image2));
		
		
		v1.setImage(image);
		v2.setImage(image);
		v3.setImage(image);
		v4.setImage(image);
		v5new.setImage(image);

		DataStore.getconfigdata();

		valve1s = valve1;
		valve2s = valve2;
		valve3s = valve3;
		valve4s = valve4;

		DataStore.spg2.addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				javafx.application.Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						labp2.setText("" + round(DataStore.spg2.get(), 2));

					}
				});
			}
		});
		DataStore.spg1.addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				javafx.application.Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						labp1.setText("" + round(DataStore.spg1.get(), 2));

					}
				});
			}
		});
		
		DataStore.sfm1.addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				javafx.application.Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int v=(int)DataStore.sfm1.get();
						lblfm1.setText(""+v);

					}
				});
			}
		});
		
		DataStore.sfm2.addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {

				javafx.application.Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int v1=(int)DataStore.sfm2.get();
						lblfm2.setText(""+v1);

					}
				});
			}
		});
		
		
		// valve1.selectedProperty().bind(DataStore.sv1);
		// valve2.selectedProperty().bind(DataStore.sv2);
		// valve3.selectedProperty().bind(DataStore.sv3);
		// valve4.selectedProperty().bind(DataStore.sv4);
		if (DataStore.connect_hardware.get()) {
			lblconnection.setText("Connected with  : " + DataStore.getCom());
			lblconnection.setTextFill(Paint.valueOf("#0e3c87"));
			concircle.setStyle("-fx-fill: #00ff00;");
			
		} else {
			lblconnection.setText("Not Connected");
			lblconnection.setTextFill(Paint.valueOf("#0e3c87"));
			concircle.setStyle("-fx-fill: #ff0000;");
			
		}

		DataStore.connect_hardware.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub

				if (newValue) {
					lblconnection.setText("Connected with  : "
							+ DataStore.getCom());
					lblconnection.setTextFill(Paint.valueOf("#00ff00"));
				} else {
					lblconnection.setText("Not Connected");
					lblconnection.setTextFill(Paint.valueOf("#ff0000"));
				}

			}
		});

		btnback.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				Openscreen.open("/application/first.fxml");

			}
		});

		btnreconnect.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				connectHardware(DataStore.getCom());

			}
		});

		valve1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (valve1.isSelected()) {
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve ON.png"));
					v1.setImage(image);

					imgv2.setVisible(true);

					wrD = new writeFormat();
					wrD.addChar('V');
					wrD.addChar('6');
					wrD.addChar('1');
					wrD.addBlank(2);
					wrD.addLast();
					sendData(wrD);

				} else {
					imgv2.setVisible(false);

					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve OFF.png"));
					v1.setImage(image);
					wrD = new writeFormat();
					wrD.addChar('V');
					wrD.addChar('6');
					wrD.addChar('0');
					wrD.addBlank(2);
					wrD.addLast();
					sendData(wrD);
				}

			}
		});

		valve2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (valve2.isSelected()) {
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve ON.png"));

					v2.setImage(image);

					imgv3.setVisible(true);
					wrD = new writeFormat();
					wrD.addChar('V');
					wrD.addChar('3');
					wrD.addChar('1');
					wrD.addBlank(2);
					wrD.addLast();
					sendData(wrD);
				} else {
					imgv3.setVisible(false);

					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve OFF.png"));
					v2.setImage(image);
					wrD = new writeFormat();
					wrD.addChar('V');
					wrD.addChar('3');
					wrD.addChar('0');
					wrD.addBlank(2);
					wrD.addLast();
					sendData(wrD);
				}

			}
		});

		valve3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (valve3.isSelected()) {
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve ON.png"));
					v3.setImage(image);

					imgv4.setVisible(true);

					wrD = new writeFormat();
					wrD.addChar('V');
					wrD.addChar('4');
					wrD.addChar('1');
					wrD.addBlank(2);
					wrD.addLast();
					sendData(wrD);
				} else {
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve OFF.png"));
					v3.setImage(image);

					imgv4.setVisible(false);

					wrD = new writeFormat();
					wrD.addChar('V');
					wrD.addChar('4');
					wrD.addChar('0');
					wrD.addBlank(2);
					wrD.addLast();
					sendData(wrD);
				}

			}
		});
		valve4.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (valve4.isSelected()) {
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve ON.png"));
					v4.setImage(image);
					lblrealeas.setVisible(true);
					imgv5.setVisible(true);

					wrD = new writeFormat();
					wrD.addChar('V');
					wrD.addChar('5');
					wrD.addChar('1');
					wrD.addBlank(2);
					wrD.addLast();
					sendData(wrD);
				} else {
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve OFF.png"));
					v4.setImage(image);
					lblrealeas.setVisible(false);
					imgv5.setVisible(false);

					wrD = new writeFormat();
					wrD.addChar('V');
					wrD.addChar('5');
					wrD.addChar('0');
					wrD.addBlank(2);
					wrD.addLast();
					sendData(wrD);
				}

			}
		});

		valve5new.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (valve5new.isSelected()) {
					imgv1.setVisible(true);
					wrD = new writeFormat();
					wrD.addChar('V');
					wrD.addChar('2');
					wrD.addChar('1');
					wrD.addBlank(2);
					wrD.addLast();
					sendData(wrD);

					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve ON.png"));
					v5new.setImage(image);

				} else {
					imgv1.setVisible(false);
					imgreg2.setVisible(false);

					wrD = new writeFormat();
					wrD.addChar('V');
					wrD.addChar('2');
					wrD.addChar('0');
					wrD.addBlank(2);
					wrD.addLast();
					sendData(wrD);
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/valve OFF.png"));
					v5new.setImage(image);

				}

			}
		});

		valveonoff.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (valveonoff.isSelected()) {
					
					valveonoff.setText("STOP");
					valveonoff.setStyle("-fx-background-color: green;");
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/stopicon.png"));
					
					

					valveonoff.setGraphic(new ImageView(image));
					
					liveimg.setVisible(true);
					wrD = new writeFormat();
					wrD.addChar('S');
					wrD.addChar('M');
					wrD.addBlank(3);
					wrD.addLast();
					sendData(wrD);

				} else {
					valveonoff.setText("START");
					valveonoff.setStyle("-fx-background-color: #0e3c87;");
					Image image = new Image(this.getClass()
							.getResourceAsStream("/userinput/starticon.png"));
					valveonoff.setGraphic(new ImageView(image));
					
					
					liveimg.setVisible(false);
					imgreg1.setVisible(false);
					imgreg2.setVisible(false);
					imgv1.setVisible(false);
					imgv2.setVisible(false);
					imgv3.setVisible(false);
					imgv4.setVisible(false);
					imgv5.setVisible(false);

					
					
					
					wrD = new writeFormat();
					wrD.addChar('X');
					wrD.addChar('M');
					wrD.addBlank(3);
					//wrD.stopTN();
					wrD.addLast();
					sendData(wrD);

				}

			}
		});

	
		
	/*
		 * 
		 * //pressure regulator gauge23 = GaugeBuilder.create()
		 * .skinType(SkinType.SIMPLE_DIGITAL).maxSize(100, 100)
		 * 
		 * .foregroundBaseColor(Color.rgb(0,0,0)) .barColor(Color.rgb(0,0, 0))
		 * .maxValue(Integer.parseInt(DataStore.getPr())) .animated(true)
		 * .build(); ap.getChildren().add(gauge23);
		 * 
		 * gauge20 = GaugeBuilder.create() .skinType(SkinType.LEVEL)
		 * .title("Capacity") .titleColor(Color.WHITE) .animated(true)
		 * .maxValue(Integer.parseInt(DataStore.getFc()))
		 * .gradientBarEnabled(true) .maxSize(140, 200) .gradientBarStops(new
		 * Stop(0.0, Color.RED), new Stop(0.25, Color.ORANGE), new Stop(0.5,
		 * Color.YELLOW), new Stop(0.75, Color.YELLOWGREEN), new Stop(1.0,
		 * Color.LIME)) .build();
		 * 
		 * ap1.getChildren().add(gauge20); // gauge23c = GaugeBuilder.create()
		 * .skinType(SkinType.SIMPLE_DIGITAL).maxSize(100, 100)
		 * .maxValue(Integer.parseInt(DataStore.getFc()))
		 * .foregroundBaseColor(Color.rgb(0,0,0)) .barColor(Color.rgb(0,0, 0))
		 * .unit("KPH") .animated(true) .build();
		 * ap2.getChildren().add(gauge23c);
		 * 
		 * //...... gauge12 = GaugeBuilder.create() .skinType(SkinType.KPI)
		 * .foregroundBaseColor(Color.BLACK) .needleColor(Color.BLACK)
		 * .barBackgroundColor(Color.GREEN)
		 * .maxValue(Integer.parseInt(DataStore.getFm1())) .animated(true)
		 * //.threshold(75) .maxSize(100, 100) .build();
		 * ap3.getChildren().add(gauge12);
		 * gauge12.valueProperty().bind(DataStore.sfm1);
		 * fm1value.textProperty().bind(DataStore.ssfm1);
		 * 
		 * 
		 * gauge12c = GaugeBuilder.create() .skinType(SkinType.KPI)
		 * .foregroundBaseColor(Color.BLACK) .needleColor(Color.BLACK)
		 * .animated(true) .maxValue(Integer.parseInt(DataStore.getFm2())) //
		 * .threshold(75) .maxSize(100, 100) .build();
		 * ap4.getChildren().add(gauge12c);
		 * gauge12c.valueProperty().bind(DataStore.sfm2);
		 * fm2value.textProperty().bind(DataStore.ssfm2);
		 * 
		 * gauge12c1 = GaugeBuilder.create() .skinType(SkinType.KPI)
		 * .foregroundBaseColor(Color.BLACK) .needleColor(Color.BLACK)
		 * .animated(true) //.threshold(75)
		 * 
		 * .maxValue(Integer.parseInt(DataStore.getPg2())) .maxSize(100, 100)
		 * .build(); gauge12c1.valueProperty().bind(DataStore.spg2);
		 * pg1value.textProperty().bind(DataStore.sspg1);
		 * ap5.getChildren().add(gauge12c1);
		 * 
		 * gauge12c2 = GaugeBuilder.create() .skinType(SkinType.KPI)
		 * .foregroundBaseColor(Color.BLACK) .needleColor(Color.BLACK)
		 * .animated(true) .maxValue(Integer.parseInt(DataStore.getPg1())) //
		 * .threshold(75) .maxSize(100, 100) .build();
		 * ap6.getChildren().add(gauge12c2);
		 */

		// NEW Gauge

		int prval=Integer.parseInt(DataStore.getPr());
		
		List<Double> da=new ArrayList<>();
		for(int j=1;j<=6;j++)
		{
			double d=prval/6;
			da.add(d*j);
		}

		
		Gauge gauge1 = GaugeBuilder
				.create()
				.skinType(SkinType.SIMPLE)
				.needleColor(Color.WHITE)
				// Dial Color
				.needleBorderColor(Color.web("#228CCC"))
				// Dial Border
				.foregroundBaseColor(Color.WHITE)
				// Value Color
				.sections(
						new Section(0, da.get(0), "0", Color.web("#E7E7E8")),
						new Section(da.get(0), da.get(1), "1", Color
								.web("#C4D0E2")),
						new Section(da.get(1), da.get(2), "2", Color.web("#A4BBDB")),
						new Section(da.get(2), da.get(3), "3", Color.web("#83A9D6")),
						new Section(da.get(3), da.get(4), "4", Color
								.web("#5F99D0")),
						new Section(da.get(4), prval, "5", Color.web("#228CCC")))
				// .title("700")
				// .value(20)
						.maxValue(prval)
				.animated(true).build();
		
					
		
			
		
int fcval=Integer.parseInt(DataStore.getFc());
		
		List<Double> dfc=new ArrayList<>();
		for(int j=1;j<=6;j++)
		{
			double d=fcval/6;
			dfc.add(d*j);
		}
		gauge1.setPrefSize(ap.getPrefWidth(), ap.getPrefHeight());
		ap.getChildren().add(gauge1);

		Gauge gauge2 = GaugeBuilder.create()
				.skinType(SkinType.SIMPLE)
				.skinType(SkinType.SIMPLE)
				.needleColor(Color.WHITE)
				// Dial Color
				.needleBorderColor(Color.web("#228CCC"))
				// Dial Border
				.foregroundBaseColor(Color.WHITE)
				// Value Color
				.maxValue(fcval)
				
				.sections(
						new Section(0, dfc.get(0), "0", Color.web("#E7E7E8")),
						new Section(dfc.get(0), dfc.get(1), "1", Color
								.web("#C4D0E2")),
						new Section(dfc.get(1), dfc.get(2), "2", Color.web("#A4BBDB")),
						new Section(dfc.get(2), dfc.get(3), "3", Color.web("#83A9D6")),
						new Section(dfc.get(3), dfc.get(4), "4", Color
								.web("#5F99D0")),
						new Section(dfc.get(4), fcval, "5", Color.web("#228CCC")))
				
						.animated(true).build();
		gauge2.setPrefSize(ap2.getPrefWidth(), ap2.getPrefHeight());
		ap2.getChildren().add(gauge2);

		Gauge gauge3 = GaugeBuilder
				.create()
				.skinType(SkinType.DASHBOARD)
				.animated(true)
				// .title("Pressure")
				.barColor(Color.CRIMSON)
				.valueColor(Color.WHITE)
				.titleColor(Color.GRAY)
				.unitColor(Color.GRAY)
				.thresholdVisible(false)
				.shadowsEnabled(true)
				.gradientBarEnabled(true)
				.gradientBarStops(new Stop(0.00, Color.BLUE),
						new Stop(0.25, Color.CYAN), new Stop(0.50, Color.LIME),
						new Stop(0.75, Color.YELLOW), new Stop(1.00, Color.RED))
				.build();

		gauge3.setPrefSize(ap3.getPrefWidth(), ap3.getPrefHeight());
		lblfm1max.setText("FM1 : "+Double.parseDouble(DataStore.getFm1())+" (sccm)");
		gauge3.setMaxValue(Double.parseDouble(DataStore.getFm1()));
		gauge3.valueProperty().bind(DataStore.sfm1);

		ap3.getChildren().add(gauge3);

		// PG-2

		Gauge gauge4 = GaugeBuilder
				.create()
				.skinType(SkinType.DASHBOARD)
				.animated(true)
				// .title("Pressure")
				.barColor(Color.CRIMSON)
				.valueColor(Color.WHITE)
				.titleColor(Color.GRAY)
				.unitColor(Color.GRAY)
				.thresholdVisible(false)
				.threshold(35)
				.shadowsEnabled(true)
				.gradientBarEnabled(true)
				
				.gradientBarStops(new Stop(0.00, Color.BLUE),
						new Stop(0.25, Color.CYAN), new Stop(0.50, Color.LIME),
						new Stop(0.75, Color.YELLOW), new Stop(1.00, Color.RED))
				.build();

		gauge4.setMaxValue(Double.parseDouble(DataStore.getFm2()));
		lblfm2max.setText("FM2 : "+Double.parseDouble(DataStore.getFm2())+" (sccm)");
		gauge4.valueProperty().bind(DataStore.sfm2);
		gauge4.setPrefSize(ap4.getPrefWidth(), ap4.getPrefHeight());
		ap4.getChildren().add(gauge4);

		// PG3........................
		Gauge gauge5 = GaugeBuilder
				.create()
				.skinType(SkinType.DASHBOARD)
				.animated(true)
				// .title("Pressure")
				.unit("\u00B0C")
				// .value(0.50)
				// .maxValue(40)
				.barColor(Color.CRIMSON)
				.valueColor(Color.WHITE)
				.titleColor(Color.GRAY)
				.unitColor(Color.GRAY)
				.thresholdVisible(false)
				.threshold(35)
				.shadowsEnabled(true)
				.gradientBarEnabled(true)
				.gradientBarStops(new Stop(0.00, Color.BLUE),
						new Stop(0.25, Color.CYAN), new Stop(0.50, Color.LIME),
						new Stop(0.75, Color.YELLOW), new Stop(1.00, Color.RED))
				.build();

		gauge5.setMaxValue(Integer.parseInt(DataStore.getPg2()));
		gauge5.valueProperty().bind(DataStore.spg2);
		lblpg2max.setText("PG2"+"\n"+Integer.parseInt(DataStore.getPg2())+" (psi)");
		
		gauge5.setPrefSize(ap6.getPrefWidth(), ap6.getPrefHeight());
		ap6.getChildren().add(gauge5);

		// PG-4

		Gauge gauge6 = GaugeBuilder
				.create()
				.skinType(SkinType.DASHBOARD)
				.animated(true)

				.barColor(Color.CRIMSON)
				.valueColor(Color.WHITE)
				.titleColor(Color.GRAY)
				.unitColor(Color.GRAY)
				.thresholdVisible(false)
				.threshold(35)
				.shadowsEnabled(true)
				.gradientBarEnabled(true)
				.gradientBarStops(new Stop(0.00, Color.BLUE),
						new Stop(0.25, Color.CYAN), new Stop(0.50, Color.LIME),
						new Stop(0.75, Color.YELLOW), new Stop(1.00, Color.RED))
				.maxValue(Integer.parseInt(DataStore.getPg1())).build();

		gauge6.setMaxValue(Integer.parseInt(DataStore.getPg1()));
		lblpg1max.setText("PG1"+"\n"+Integer.parseInt(DataStore.getPg1())+" (psi)");
		gauge6.setPrefSize(ap5.getPrefWidth(), ap5.getPrefHeight());
		gauge6.valueProperty().bind(DataStore.spg1);
		// pg2value.textProperty().bind(DataStore.sspg2);

		// labp1.textProperty().bind(DataStore.sspg1);
		// labp2.textProperty().bind(DataStore.sspg2);

		ap5.getChildren().add(gauge6);

		pr.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				setPR(gauge1);
			}
		});

		fc.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				setFC(gauge2);

			}
		});

		setpr.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				setPR(gauge1);

			}
		});
		setfc.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				setFC(gauge2);
			}
		});
	}
	

	void addShortCut() {
		 KeyCombination backevent = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_ANY);
			
			root.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent ke) {
					
					if(backevent.match(ke))
					{

						
						Openscreen.open("/application/first.fxml");
					}
					
				}
			});

			

	}

	void setPR(Gauge gauge1) {

		try {
			imgreg1.setVisible(true);
			double d1 = Double.parseDouble(pr.getText());
			double d=(double)65535*d1 / Integer.parseInt(DataStore.getPr());
			System.out.println("Sending  : "+(int)d);
			List<Integer> ss = getValueList((int) d);
			wrD = new writeFormat();
			wrD.addChar('P');
			wrD.addChar('R');
			wrD.addData1(ss);
			wrD.addLast();
			sendData(wrD);

			lblanc.setText("" + d1);
			gauge1.setValue(d1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void setFC(Gauge gauge2) {
		try {
			imgreg2.setVisible(true);

			double d1 = Double.parseDouble(fc.getText());
			// double d=Double.parseDouble(pr.getText());

			double d=(double)65535*d1 / Integer.parseInt(DataStore.getFc());
			System.out.println("Sending  : "+(int)d);

			List<Integer> ss = getValueList((int) d);
			wrD = new writeFormat();
			wrD.addChar('F');
			wrD.addChar('C');
			wrD.addData1(ss);
			wrD.addChkSm();
			wrD.addLast();
			sendData(wrD);

			gauge2.setValue(d1);
			lblanc2.setText("" + d1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendData(writeFormat w) {
		System.out.println("Sending Data......");
		w.showData();
		Thread t = new Thread(new SerialWriter(DataStore.out, w));
		t.start();
		// start.setDisable(true);
	}

	public boolean connectHardware(String st) {

		boolean bol = false;

		// sendDataToWeb();
		Enumeration pList = CommPortIdentifier.getPortIdentifiers();

		int count = 0;

		while (pList.hasMoreElements()) {

			CommPortIdentifier cpi = (CommPortIdentifier) pList.nextElement();
			System.out.print("Port " + cpi.getName() + " " + cpi.getPortType());
			if (cpi.getName().equals(st)) {
				DataStore.connect_hardware.set(true);
				try {

					DataStore.sc.connect(st);
					bol = true;
					Myapp.hb.set(false);
					writeFormat wrD = new writeFormat();
					wrD.stopTN();
					wrD.addLast();

					sendData(wrD);

				} catch (Exception e) {

					e.printStackTrace();
				}

				break;
			}

			System.out.println("PORT :" + cpi.getName());
			count++;
		}

		DataStore.connect_hardware.set(bol);
		if (bol == false) {
			// Toast.makeText(Main.mainstage,
			// "Hardware not connected please plugout and plugin", 200, 200,
			// 3000);
		} else {
			// Toast.makeText(Main.mainstage, "Successfully Connected", 200,
			// 200, 3000);

		}

		return bol;
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
    public static class Data {
    	 
        private  SimpleStringProperty title;
        private  SimpleStringProperty v1,v2,v3,v4;
        
        public Data(String title, String v1, String v2, String v3,String v4) {
        	
            this.title = new SimpleStringProperty(title);

        	this.v1 = new SimpleStringProperty( v1);
        	this.v2 = new SimpleStringProperty( v2);
        	this.v3 = new SimpleStringProperty( v3);
        	this.v4 = new SimpleStringProperty( v4);
        	
        }
        
        public void setData(int i,String data)
        {
        	if(i==1)
        	{
        		v1.set(data);
        	}
        	else if(i==2)
        	{

        		v2.set(data);
        	}
        	else if(i==3)
        	{

        		v3.set(data);
        	}
        	else if(i==4)
        	{

        		v4.set(data);
        	}        		
        }
        
        public Data()
        {
        	this.title = new SimpleStringProperty();

        	this.v1 = new SimpleStringProperty();
        	this.v2 = new SimpleStringProperty();
        	this.v3 = new SimpleStringProperty();
        	this.v4 = new SimpleStringProperty();
            
        }
        
        public final String getTitle()
        {
        	return title.get();
        }
        public final void setTitle(final String title)
        {
        	this.title.set(title);
        }
        
		public final String getV3() {
			return this.v3.get();
		}
		public final void setV3(final String v3) {
			this.v3.set(v3);
		}
 
		public final String getV4() {
			return this.v4.get();
		}
		public final void setV4(final String v4) {
			this.v4.set(v4);
		}
		
		public final String getV1() {
			return this.v1.get();
		}
		public final void setV1(final String V1) {
			this.v1.set(V1);
		}
		public final String getV2() {
			return this.v2.get();
		}
		public final void setV2(final String v2) {
			this.v2.set(v2);
		}
 
    }
    
    
 
	  private void setupMainTableColumns() {
	    	
			pg2.addListener(new ListChangeListener<String>() {

				@Override
				public void onChanged(
						javafx.collections.ListChangeListener.Change<? extends String> arg0) {
				
					if(recordbtn.isSelected())
					{
						
					addDataToTable();
					}
				}
			});

	        
			TableColumn<Data, LocalDate> dateCol = new TableColumn<>("Title");
			dateCol.setPrefWidth(300);
			dateCol.setCellValueFactory(new PropertyValueFactory<>("title"));

			TableColumn<Data, Double> value1Col = new TableColumn<>("1");
			value1Col.setPrefWidth(255);
			value1Col.setCellValueFactory(new PropertyValueFactory<>("v1"));
			
			TableColumn<Data, Double> value2Col = new TableColumn<>("2");
			value2Col.setPrefWidth(255);
			value2Col.setCellValueFactory(new PropertyValueFactory<>("v2"));

			TableColumn<Data, Double> value3Col = new TableColumn<>("3");
			value3Col.setPrefWidth(255);
			value3Col.setCellValueFactory(new PropertyValueFactory<>("v3"));

			TableColumn<Data, Double> value4Col = new TableColumn<>("4");
			value4Col.setPrefWidth(255);
			value4Col.setCellValueFactory(new PropertyValueFactory<>("v4"));

			datatable.setStyle("-fx-cell-size: 50px;");
			datatable.getColumns().addAll( dateCol, value1Col, value2Col, value3Col, value4Col);
			datatable.setItems(alldata);

	    }

		public static class FormattedTableCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
			
			private TextAlignment alignment = TextAlignment.LEFT;
			private Format format;

			public FormattedTableCellFactory() {
			}

			public FormattedTableCellFactory( TextAlignment alignment) {
			}
			
			public TextAlignment getAlignment() {
				return alignment;
			}

			public void setAlignment(TextAlignment alignment) {
				this.alignment = alignment;
			}

			public Format getFormat() {
				return format;
			}

			public void setFormat(Format format) {
				this.format = format;
			}

			@Override
			@SuppressWarnings("unchecked")
			public TableCell<S, T> call(TableColumn<S, T> p) {
				TableCell<S, T> cell = new TableCell<S, T>() {
					@Override
					public void updateItem(Object item, boolean empty) {
						if (item == getItem()) {
							return;
						}
						super.updateItem((T) item, empty);
						if (item == null) {
							super.setText(null);
							super.setGraphic(null);
						} else if (format != null) {
							super.setText(format.format(item));
						} else if (item instanceof Node) {
							super.setText(null);
							super.setGraphic((Node) item);
						} else {
							super.setText(item.toString());
							super.setGraphic(null);
						}
					}
				};
				cell.setTextAlignment(alignment);
				switch (alignment) {
				case CENTER:
					cell.setAlignment(Pos.CENTER);
					break;
				case RIGHT:
					cell.setAlignment(Pos.CENTER_RIGHT);
					break;
				default:
					cell.setAlignment(Pos.CENTER_LEFT);
					break;
				}
				return cell;
			}
		}
	
		
		void addDataToTable()
		{
			alldata.clear();
			
			List<String> lab=new ArrayList<String>();
			lab.add("Flow meter 1");
			lab.add("Flow meter 2");
			lab.add("Pressure Gauge 1");
			lab.add("Pressure Gauge 2");
			int in=0;
		
			for(int i=0;i<4;i++)
			{
				Data d=new Data();
				d.setTitle(lab.get(i));	
				alldata.add(d);
			}
			
			int temp=0;
			
			for(int i=fm1.size()-1;i>=0;i--)
			{
				if(temp>=4)
				{
					break;
				}
				System.out.println("fm1 : "+fm1.size()+" I : "+i);
				alldata.get(0).setData(temp+1, fm1.get(i));
				alldata.get(1).setData(temp+1, fm2.get(i));
				alldata.get(2).setData(temp+1, pg1.get(i));
				alldata.get(3).setData(temp+1, pg2.get(i));
				temp++;
			}
			
			
		}

}
