package report;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberExpression;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.SnapshotResult;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.imageio.ImageIO;

import org.apache.commons.math3.util.MathArrays.Position;

import toast.MyDialoug;
import toast.Openscreen;
import application.Main;
import application.Myapp;
import data_read_write.DatareadN;
import de.tesis.dynaware.javafx.fancychart.zoom.Zoom;
import drawchart.ChartPlot;
import drawchart.SmoothedChart;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import extrafont.Myfont;

public class ReportController implements Initializable
{
	
	 private static final String CHART_FILE_PREFIX = "chart_";
	  private static final String WORKING_DIR = System.getProperty("user.dir")+"/mypic/";
	  
	  private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
	  private final Random random = new Random();

	  private final int N_CHARTS     = 1;
	  private final int PREVIEW_SIZE = 800;
	  private final int CHART_SIZE   = 600;

	  final ExecutorService saveChartsExecutor = createExecutor("SaveCharts");
	          
	  ChartPlot c;
	  
	 ArrayList<Boolean> onlyonce;
	  
	@FXML
	Button report,btnback,btnsavereport,btnhome;
	
	@FXML
	ScrollPane scrfilename;
	
	@FXML
	AnchorPane slides,ancgaugefaz,ancgaugegur;
	
	
	
	double avgbubpt=0;
	
	
	
	
	@FXML
	Label grlable,lbldarcy,lblgurl,lblfraz;
	
	@FXML
	AnchorPane ancgauge1,pagination1,ancguagesdarcy;

	 private Gauge   gauge,gauge1;
	 
	 private Tile numbertile,numbertile1,numbertile2;
	 
	 Map<String,Map<String,DatareadN>> alldata;
	 
	 List<String> grpclr;
	 
	 static File file;
		
	 int max=20;
	
	    Myfont fontss=new Myfont(14);

	 
 int totsample=0;
 static List<Pane> listofchart=null;
 static List<DatareadN> list_d;
 static File pdffilepath=null;
 MyDialoug mydia;
 
 
 private ImageView createImageViewForChartFile(Integer chartNumber) {
   ImageView imageView = new ImageView(new Image("file:///" + getChartFilePath(chartNumber)));
   imageView.setFitWidth(PREVIEW_SIZE);
   imageView.setPreserveRatio(true);
   return imageView;
 }

 private Pane createProgressPane(SaveChartsTask saveChartsTask) {
   GridPane progressPane = new GridPane();
   
   progressPane.setHgap(5);
   progressPane.setVgap(5);
   progressPane.addRow(0, new Label("Create:"),     createBoundProgressBar(saveChartsTask.chartsCreationProgressProperty()));
   progressPane.addRow(1, new Label("Snapshot:"),   createBoundProgressBar(saveChartsTask.chartsSnapshotProgressProperty()));
   progressPane.addRow(2, new Label("Save:"),       createBoundProgressBar(saveChartsTask.imagesExportProgressProperty()));
   progressPane.addRow(3, new Label("Processing:"), 
     createBoundProgressBar(
       Bindings
         .when(saveChartsTask.stateProperty().isEqualTo(Worker.State.SUCCEEDED))
           .then(new SimpleDoubleProperty(1))
           .otherwise(new SimpleDoubleProperty(ProgressBar.INDETERMINATE_PROGRESS))
     )
   );

   return progressPane;
 }

 private ProgressBar createBoundProgressBar(NumberExpression progressProperty) {
   ProgressBar progressBar = new ProgressBar();
   progressBar.setMaxWidth(Double.MAX_VALUE);
   progressBar.progressProperty().bind(progressProperty);
   GridPane.setHgrow(progressBar, Priority.ALWAYS);
   return progressBar;
 }

 class ChartsCreationTask extends Task<Void> {
   private final int nCharts;
   private final BlockingQueue<Parent> charts;
   
   ChartsCreationTask(BlockingQueue<Parent> charts, final int nCharts) {
     this.charts = charts;
     this.nCharts = nCharts;
     updateProgress(0, nCharts);
   }
   
   @Override protected Void call() throws Exception {
     int i = nCharts;
     while (i > 0) {
       if (isCancelled()) {
         break;
       }
       System.out.println("charts : "+i);
    
       if(i==1)
       {
    	   Pane p=c.drawLinechart(pagination1.getPrefWidth(),pagination1.getPrefHeight(),"Flow vs Pressure", "Pressure (psi)", "Flow (cm3/m)",list_d,false,11,12,"(3) Incremental Filter-Flow % vs Diameter");
    	   charts.put(p);
    	   listofchart.add(p);
    	 
    	   
    	
       }
       i--;
       updateProgress(nCharts - i, nCharts);
     }
     
     return null;
   }
  
 }
 
 class ChartsSnapshotTask extends Task<Void> {
   private final int nCharts;
   private final BlockingQueue<Parent> charts;
   private final BlockingQueue<BufferedImage> images;
   
   ChartsSnapshotTask(BlockingQueue<Parent> charts, BlockingQueue<BufferedImage> images, final int nCharts) {
     this.charts = charts;
     this.images = images;
     this.nCharts = nCharts;
     updateProgress(0, nCharts);
     
   }
   
   @Override protected Void call() throws Exception {
     int i = nCharts;
     while (i > 0) {
       if (isCancelled()) {
         break;
       }
       images.put(snapshotChart(charts.take()));
         i--;
       updateProgress(nCharts - i, nCharts);
     }
     
     return null;
   }
   
   private BufferedImage snapshotChart(final Parent chartContainer) throws InterruptedException {
     final CountDownLatch latch = new CountDownLatch(1);
     // render the chart in an offscreen scene (scene is used to allow css processing) and snapshot it to an image.
     // the snapshot is done in runlater as it must occur on the javafx application thread.
     final SimpleObjectProperty<BufferedImage> imageProperty = new SimpleObjectProperty();
     Platform.runLater(new Runnable() {
       @Override public void run() {
         Scene snapshotScene = new Scene(chartContainer);
        snapshotScene.getStylesheets().add(getClass().getResource("dynamicgraph.css").toExternalForm());
        chartContainer.getStylesheets().add(getClass().getResource("dynamicgraph.css").toExternalForm());
        
         final SnapshotParameters params = new SnapshotParameters();
        
         chartContainer.snapshot(
           new Callback<SnapshotResult, Void>() {
             @Override public Void call(SnapshotResult result) {
               imageProperty.set(SwingFXUtils.fromFXImage(result.getImage(), null));
               latch.countDown();
               return null;
             }
           },
           params, 
           null
         );
       }
     });

     latch.await();
     
     return imageProperty.get();
   }
 }
 
 class PngsExportTask extends Task<Void> {
   private final int nImages;
   private final BlockingQueue<BufferedImage> images;
   
   PngsExportTask(BlockingQueue<BufferedImage> images, final int nImages) {
     this.images = images;
     this.nImages = nImages;
     updateProgress(0, nImages);
   }
   
   @Override protected Void call() throws Exception {
     int i = nImages;
     while (i > 0) {
       if (isCancelled()) {
         break;
       }
       exportPng(images.take(), getChartFilePath(nImages - i));
       i--;
       updateProgress(nImages - i, nImages);
     }
     
     return null;
   }
   
   private void exportPng(BufferedImage image, String filename) {
     try {
       ImageIO.write(image, "png", new File(filename));
     } catch (IOException ex) {
       Logger.getLogger(ChartPlot.class.getName()).log(Level.SEVERE, null, ex);
     }
   }
 }
 
 class SaveChartsTask<Void> extends Task {
   private final BlockingQueue<Parent>        charts         = new ArrayBlockingQueue(10);
   private final BlockingQueue<BufferedImage> bufferedImages = new ArrayBlockingQueue(10);
   private final ExecutorService    chartsCreationExecutor   = createExecutor("CreateCharts");
   private final ExecutorService    chartsSnapshotExecutor   = createExecutor("TakeSnapshots");
   private final ExecutorService    imagesExportExecutor     = createExecutor("ExportImages");
   private final ChartsCreationTask chartsCreationTask;
   private final ChartsSnapshotTask chartsSnapshotTask;
   private final PngsExportTask     imagesExportTask;
   
   SaveChartsTask(final int nCharts) {
     chartsCreationTask = new ChartsCreationTask(charts, nCharts);
     chartsSnapshotTask = new ChartsSnapshotTask(charts, bufferedImages, nCharts);
     imagesExportTask   = new PngsExportTask(bufferedImages, nCharts);

     setOnCancelled(new EventHandler() {
       @Override public void handle(Event event) {
         chartsCreationTask.cancel();
         chartsSnapshotTask.cancel();
         imagesExportTask.cancel();
       }
     });
     
     imagesExportTask.workDoneProperty().addListener(new ChangeListener<Number>() {
       @Override public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number workDone) {
         updateProgress(workDone.intValue(), nCharts);
       }
     });
   }
   
   ReadOnlyDoubleProperty chartsCreationProgressProperty() {
     return chartsCreationTask.progressProperty();
   }
          
   ReadOnlyDoubleProperty chartsSnapshotProgressProperty() {
     return chartsSnapshotTask.progressProperty();
   }
          
   ReadOnlyDoubleProperty imagesExportProgressProperty() {
     return imagesExportTask.progressProperty();
   }
          
   @Override protected Void call() throws Exception {
     chartsCreationExecutor.execute(chartsCreationTask);
     chartsSnapshotExecutor.execute(chartsSnapshotTask);
     imagesExportExecutor.execute(imagesExportTask);
     
     chartsCreationExecutor.shutdown();
     chartsSnapshotExecutor.shutdown();
     imagesExportExecutor.shutdown();
     
     try {
       imagesExportExecutor.awaitTermination(1, TimeUnit.DAYS);
     } catch (InterruptedException e) {
       /** no action required */
     } 
     
     return null;
   }
 }
 
 private String getChartFilePath(int chartNumber) {
	 String name="";
	 if(chartNumber == 0)
	 {
		 name="Flow vs Pressure";
	 }

   return new File(WORKING_DIR,""+ name + ".png").getPath();
 }

 private ExecutorService createExecutor(final String name) {       
   ThreadFactory factory = new ThreadFactory() {
     @Override public Thread newThread(Runnable r) {
       Thread t = new Thread(r);
       t.setName(name);
       t.setDaemon(true);
       return t;
     }
   };
   
   return Executors.newSingleThreadExecutor(factory);
 }  
 
 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	
		System.out.println("Genrate Result....");
		list_d=new ArrayList<DatareadN>();
    	listofchart=new ArrayList<Pane>();
    	
		grpclr=new ArrayList<String>();
		
		scrfilename.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		
		alldata=new HashMap<String, Map<String,DatareadN>>();
		
	


    	listofchart=new ArrayList<Pane>();
		c=new ChartPlot(true);
		//
		
		btnhome.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Openscreen.open("/application/first.fxml");
				
			}
		});
		
	btnsavereport.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

			
				Stage stage=new Stage();
				 FileChooser fileChooser = new FileChooser();
				  
	             //Set extension filter
	            // FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
	           //  fileChooser.getExtensionFilters().add(extFilter);
	             
	             if(list_d.size()==1)
	             {
	            	 fileChooser.setInitialFileName(list_d.get(0).filename);
	             }
	             
	             //Show save file dialog
	            pdffilepath = fileChooser.showSaveDialog(Main.mainstage);
	             
	             if(pdffilepath!=null)
	             {
	         	
					mydia=new MyDialoug(Main.mainstage, "/report/pdfselection.fxml");
						
						mydia.showDialoug();
	             
	             }

				
			  
					
			}
		});
			
		
		btnback.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Openscreen.open("/report/first.fxml");
				
			}
		});

		
		report.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Openscreen.open("/application/first.fxml");
			}
		});
		
		

	    final SaveChartsTask saveChartsTask = new SaveChartsTask(N_CHARTS);

	    	createChartImagePagination(saveChartsTask);
	    
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				
					setColor();
					setTableAndGuage();

					setResult();	
					 saveChartsExecutor.execute(saveChartsTask);
					    
			}
		});
		

			     //root.getChildren().add(createChartImagePagination(saveChartsTask));
			 			     System.out.println("hello");
	}
	


	
	void setResult()
	{

		VBox v=new VBox(10);
		v.setPadding(new Insets(0,0,0,10));
		
		   double avgbp=0,avggur=0,avgfra=0;
	   	   String fff=null,wff=null,gurff=null,fraff=null;
	   	   
	   	   
	   	   BigDecimal bb=BigDecimal.valueOf(0);
	   	   List<String> clrs=getColorMultiple();
		for(int i=0;i<list_d.size();i++)
		{	
			fff=""+list_d.get(i).filename;
		
			wff=""+list_d.get(i).data.get("darcy avg");
			gurff=""+list_d.get(i).data.get("gurley");
			fraff=""+list_d.get(i).data.get("frazier");
		
			bb.add(BigDecimal.valueOf(Double.parseDouble(wff)));
			avgbp=avgbp+Double.parseDouble(wff);
			
			bb.add(BigDecimal.valueOf(Double.parseDouble(gurff)));
			avggur=avggur+Double.parseDouble(gurff);
			
			bb.add(BigDecimal.valueOf(Double.parseDouble(fraff)));
			avgfra=avgfra+Double.parseDouble(fraff);
			
		
			v.getChildren().add(getVBox(fff, Myapp.getRound(Double.parseDouble(wff), 2),Myapp.getRound(Double.parseDouble(gurff), 2),Myapp.getRound(Double.parseDouble(fraff), 2),clrs.get(i)));
			
		
		}
		
		
		BigDecimal b=BigDecimal.valueOf(avgbp/list_d.size());
		
		b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		
		BigDecimal b1=BigDecimal.valueOf(avggur/list_d.size());
		
		b1 = b1.setScale(2, BigDecimal.ROUND_HALF_UP);
			
		BigDecimal b2=BigDecimal.valueOf(avgfra/list_d.size());
		
		b2 = b2.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		
		lblgurl.setText(""+b1);
		lblfraz.setText(""+b2);
		lbldarcy.setText(""+b);
		
		
		
		scrfilename.setContent(v);
	}
	
	

	
    private void setDataPointPopup(XYChart<Number, Number> sc) {
		final Popup popup = new Popup();
		popup.setHeight(20);
		popup.setWidth(60);

		for (int i = 0; i < sc.getData().size(); i++) {
			final int dataSeriesIndex = i;
			final XYChart.Series<Number, Number> series = sc.getData().get(i);
			for (final Data<Number, Number> data : series.getData()) {
				final Node node = data.getNode();
				node.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>() {

					private static final int X_OFFSET = 15;
					private static final int Y_OFFSET = -5;
					 Label label = new Label();
        
					@Override
					public void handle(final MouseEvent event) {
					//	System.out.println("MOuse Event");
						final String colorString = "#cfecf0";
						label.setFont(new Font(20));
						popup.getContent().setAll(label);
						label.setStyle("-fx-background-color: " + colorString + "; -fx-border-color: " + colorString
								+ ";");
						label.setText("x=" + data.getXValue() + ", y=" + data.getYValue());
						popup.show(data.getNode().getScene().getWindow(), event.getScreenX() + X_OFFSET,
								event.getScreenY() + Y_OFFSET);
						event.consume();
					}
					
					
					
					public EventHandler<MouseEvent> init() {
						label.getStyleClass().add("chart-popup-label");
						return this;
					}

				}.init());

				node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {

					@Override
					public void handle(final MouseEvent event) {
						popup.hide();
						event.consume();
					}
				});

				// this handler selects the corresponding table item when a data
				// item in the chart was clicked.
		
				
			}
		}

	}
    
	
	VBox getVBox(String name,double single,double bub)
	{
		VBox v=new VBox(10);
		v.setLayoutX(single*bub);
		
		Label l=new Label();
		l.setText(name);
		l.setTextFill(Color.web("#727376"));
		l.setFont(fontss.getM_M());

		v.getChildren().add(l);
		Line li=new Line();
		li.setStroke(Color.web("#727376"));
		li.setStrokeWidth(3);
		li.setStartY(50);
		li.setEndY(-50);

		li.setStartX(0);
		li.setEndX(0);
		li.setLayoutX(0);
		li.setLayoutY(0);
		v.getChildren().add(li);
		
		
		
		
		return v;
	}
	
	public void setColor()
	{
		
		grpclr.addAll(Myapp.colors);
		
	}
	
	void setTableAndGuage()
	{
		double wid=slides.getPrefWidth();
		double singlepart=wid/max;
		System.out.println("width : "+wid +  " - singlepart : "+singlepart);
		
		List<String> s =new ArrayList<String>( FirstController.selectedbox.keySet());
		for(int i=0;i<s.size();i++)
		{
			List<String> templist=FirstController.selectedbox.get(s.get(i));
			Map<String,DatareadN> datas=new HashMap<String, DatareadN>();
			System.out.println("Sample name : "+s.get(i));
			for(int j=0;j<templist.size();j++)
			{
				File f=new File(templist.get(j));
				DatareadN d=new DatareadN();
				d.fileRead(f);
				list_d.add(d);
				datas.put(f.getName(), d);
			//	avgbubpt=avgbubpt+Double.parseDouble(d.getDarcy());
				
			//	slides.getChildren().add(getVBox(f.getName(),singlepart,Double.parseDouble(d.getDarcy())));
				
				totsample++;
			//	v.getChildren().add(getVBox(f.getName(), d.getDarcy()));
				System.out.println(templist.get(j));
				
			}
		//	alldata.put(s.get(i), datas);
			
		}
		//avgbub.setText(""+(avgbubpt/totsample));
		
	//	gauge.setValue(avgbubpt/totsample);
		//scrfilename.setContent(v);
		
		
	}
	
	public List<String> getColorMultiple() {
		
		
		
		List<String> grpclr=new ArrayList<String>();
		grpclr.add("#DBBA4F");
		grpclr.add("#3F76B5");
		grpclr.add("#D67479");
		grpclr.add("#12B59F");
		grpclr.add("#F5903D");
		grpclr.add("#BC4644");
		grpclr.add("#AD4F73");
		grpclr.add("#40A7C1");
		grpclr.add("#95B64F");
		
		
		grpclr.add("#613769");
		grpclr.add("#234882");
		grpclr.add("#A1846A");
		return grpclr;
	}
	HBox getVBox(String name,String bubp,String gur,String fraz,String clr)
	{
		
		HBox v1 = new HBox(5);
		v1.setPadding(new Insets(5, 0, 0, 3));

		Label l1 = new Label();
		l1.setMaxWidth(135);
		l1.setMinWidth(135);
		l1.setPrefWidth(135);
		l1.setWrapText(true);
		l1.setFont(fontss.getM_M());
		l1.setTextFill(Color.web(clr));
		l1.setText(name);

		Label lv = new Label();
		lv.setText(bubp);
		lv.setMaxWidth(90);
		lv.setMinWidth(90);
		lv.setPrefWidth(90);
		lv.setFont(fontss.getM_M());
		lv.setTextFill(Color.web("#727376"));
		
		Label lv1 = new Label();
		lv1.setText(gur);
		lv1.setMaxWidth(95);
		lv1.setMinWidth(95);
		lv1.setPrefWidth(95);
		lv1.setFont(fontss.getM_M());
		lv1.setTextFill(Color.web("#727376"));
		
		Label lv2 = new Label();
		lv2.setText(fraz);
		lv2.setMaxWidth(70);
		lv2.setMinWidth(70);
		lv2.setPrefWidth(70);
		lv2.setFont(fontss.getM_M());
		lv2.setTextFill(Color.web("#727376"));

		Circle rectangle=new Circle(7);
		rectangle.setFill(Paint.valueOf(clr));
		


		v1.getChildren().addAll(rectangle,l1, lv,lv1, lv2);
		return v1;
	}
		
	

	void setgaugeBubb(double d)
	{
		
		
		 VBox v=new VBox(5);
		
		 gauge1=GaugeBuilder.create()
		         .skinType(SkinType.SIMPLE_SECTION)
		         .title("BP Diameter")
		         .unit("\u03BC")
		         .titleColor(Color.web("#727376"))
		         .unitColor(Color.web("#727376"))
		         .valueColor(Color.web("#727376"))
		         .barColor(Color.BLACK)
		         .animated(true)
		         .maxValue(200)
		         .animationDuration(5000)
		         .sections(new Section(0, 66, Color.GRAY),
		                   new Section(66, 132, Color.DARKGRAY),
		                   new Section(132, 200, Color.DARKSLATEGRAY))
		         .build();
		gauge1.setPrefSize(150, 150);
				gauge1.setValue(d);
		
	        ancgauge1.getChildren().add(gauge1);

	    //   
			

	        
	}
	private void createChartImagePagination(final SaveChartsTask saveChartsTask) {
	
		
	         ProgressIndicator progressIndicator = new ProgressIndicator();
	         progressIndicator.setMaxSize(PREVIEW_SIZE * 1/10, PREVIEW_SIZE * 1/10);
	         BorderPane b=new BorderPane();
	         b.setPrefSize(pagination1.getPrefWidth(), pagination1.getPrefHeight());
	         b.setCenter(progressIndicator);
	         
	         pagination1.getChildren().setAll(b);
	         
	         final ChangeListener<Number> WORK_DONE_LISTENER = new ChangeListener<Number>() {
	           @Override public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
	             if (0 < saveChartsTask.getWorkDone()) {
	            	  Pane ap=listofchart.get(0);
	            	   
		         	  SmoothedChart<Number, Number>  lineChart=(SmoothedChart<Number, Number>) ap.getChildren().get(0);
		         	 
		         	    setDataPointPopup(lineChart);
		     	     
		         	
		         	       Zoom zoom =new Zoom(lineChart,ap);    	
		         	
		           pagination1.getChildren().setAll(ap);

	               saveChartsTask.workDoneProperty().removeListener(this);
	             
	        	   
	             }
	           }
	         };

	        
	         saveChartsTask.workDoneProperty().addListener(WORK_DONE_LISTENER);
	       }
	    
	     
	   
	   
	 
	
}
