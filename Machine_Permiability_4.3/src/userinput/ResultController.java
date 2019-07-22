package userinput;

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
import javafx.beans.binding.NumberExpression;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.util.Callback;

import javax.imageio.ImageIO;

import toast.MyDialoug;
import toast.Openscreen;
import application.Main;
import application.MainancController;
import application.Myapp;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;

import data_read_write.DatareadN;
import de.tesis.dynaware.javafx.fancychart.zoom.Zoom;
import drawchart.ChartPlot;
import drawchart.SmoothedChart;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import extrafont.Myfont;

public class ResultController implements Initializable {

	MyDialoug mydia;

	int Pind = 0;
	private static final String CHART_FILE_PREFIX = "chart_";
	private static final String WORKING_DIR = System.getProperty("user.dir")
			+ "/mypic/";

	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"HH:mm:ss.SSS");
	private final Random random = new Random();

	private final int N_CHARTS = 5;
	private final int PREVIEW_SIZE = 800;
	private final int CHART_SIZE = 600;

	final ExecutorService saveChartsExecutor = createExecutor("SaveCharts");

	ArrayList<Boolean> onlyonce;

	ChartPlot c;

	@FXML
	StackPane maincontent1;

	@FXML
	Button report;

	@FXML
	ScrollPane scrfilename;

	static String delfile;
	@FXML
	AnchorPane slides;

	@FXML
	Label avgbub, avgmean, lblsname;

	double avgbubpt = 0;

	@FXML
	Pagination pagination1;

	@FXML
	AnchorPane ancgauge, root;

	private Gauge gauge;

	Map<String, Map<String, DatareadN>> alldata;

	List<String> grpclr;

	static File file;
	static Image image;

	int max = 20;

	int totsample = 0;
	static List<Pane> listofchart = null;
	List<DatareadN> list_d;

	@FXML
	Button btnhome, btnlivetest;

	@FXML
	ImageView imgback;

	@FXML
	Rectangle recmain;

	Myfont fontss = new Myfont(14);

	public static JFXDialog df;
	JFXDialogLayout dll;

	SimpleBooleanProperty ispopup = new SimpleBooleanProperty(false);

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		image = new Image(this.getClass().getResourceAsStream(
				"/userinput/back.png"));
		imgback.setImage(image);

		// btnhome.getStyleClass().add("transperant_comm");
		// btnlivetest.getStyleClass().add("transperant_comm");

		dll = new JFXDialogLayout();
		df = new JFXDialog(maincontent1, dll, DialogTransition.CENTER);

		df.visibleProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				ispopup.set(newValue);
				System.out.println(newValue + "");
			}
		});

		System.out.println("Genrate Result....");
		list_d = new ArrayList<DatareadN>();
		listofchart = new ArrayList<Pane>();

		grpclr = new ArrayList<String>();

		scrfilename.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		alldata = new HashMap<String, Map<String, DatareadN>>();

		// btnback.setStyle("-fx-background-color: #f8f8f85c");

		// Image image1 = new
		// Image(this.getClass().getResourceAsStream("/report/back.png"));

		// btnback.setGraphic(new ImageView(image1));

		lblsname.setText(Myapp.sampleid);
		// lblsname.setText("Biotech_sam1");

		btnhome.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Openscreen.open("/application/first.fxml");
			}
		});

		btnlivetest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				Openscreen.open("/userinput/Nselectproject1.fxml");
			}
		});

		c = new ChartPlot(true);
		getFiles();

		final SaveChartsTask saveChartsTask = new SaveChartsTask(N_CHARTS);

		createChartImagePagination(saveChartsTask);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				setColor();
				saveChartsExecutor.execute(saveChartsTask);

				setResult();

			}
		});

	}

	void deletpopup() {

		/*
		 * AnchorPane popanc=new AnchorPane();
		 * 
		 * //popanc.setStyle(
		 * "-fx-background-color: rgba(100, 300, 100, 0.5); -fx-background-radius: 10;"
		 * );
		 * 
		 * FXMLLoader fxmlLoader = new
		 * FXMLLoader(getClass().getResource("/userinput/deletpopup.fxml")); try
		 * {
		 * 
		 * Pane cmdPane = (Pane) fxmlLoader.load();
		 * 
		 * popanc.getChildren().add(cmdPane);
		 * System.out.println("Load Anchorpane....."); } catch (Exception e) {
		 * e.printStackTrace(); }
		 * 
		 * 
		 * dll.setBody(popanc);
		 * 
		 * df.show();
		 */
		mydia = new MyDialoug(Main.mainstage, "/userinput/Ndeletpopup.fxml");
		mydia.showDialoug();

	}

	private void createChartImagePagination(final SaveChartsTask saveChartsTask) {

		onlyonce = new ArrayList<>();
		onlyonce.add(true);
		onlyonce.add(true);
		onlyonce.add(true);
		onlyonce.add(true);
		onlyonce.add(true);

		pagination1.setPageCount(5);
		pagination1.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(final Integer chartNumber) {
				final StackPane page = new StackPane();

				if (chartNumber < saveChartsTask.getWorkDone()) {
					Pane ap = listofchart.get(chartNumber);
try {
	

					SmoothedChart<Number, Number> lineChart = (SmoothedChart<Number, Number>) ap
							.getChildren().get(0);

					setDataPointPopup(lineChart);

					if (onlyonce.get(chartNumber)) {
						onlyonce.remove(chartNumber);
						onlyonce.add(false);
						Zoom zoom = new Zoom(lineChart, ap);
					}
} catch (Exception e) {
	// TODO: handle exception
}
					page.getChildren().setAll(ap);
				} else {
					ProgressIndicator progressIndicator = new ProgressIndicator();
					progressIndicator.setMaxSize(PREVIEW_SIZE * 1 / 10,
							PREVIEW_SIZE * 1 / 10);
					page.getChildren().setAll(progressIndicator);

					final ChangeListener<Number> WORK_DONE_LISTENER = new ChangeListener<Number>() {
						@Override
						public void changed(
								ObservableValue<? extends Number> observable,
								Number oldValue, Number newValue) {
							if (chartNumber < saveChartsTask.getWorkDone()) {
								Pane ap = listofchart.get(chartNumber);

								SmoothedChart<Number, Number> lineChart = (SmoothedChart<Number, Number>) ap
										.getChildren().get(0);

								setDataPointPopup(lineChart);

								if (onlyonce.get(chartNumber)) {
									onlyonce.remove(chartNumber);
									onlyonce.add(false);
									Zoom zoom = new Zoom(lineChart, ap);
								}

								page.getChildren().setAll(ap);

								saveChartsTask.workDoneProperty()
										.removeListener(this);

							}
						}
					};

					saveChartsTask.workDoneProperty().addListener(
							WORK_DONE_LISTENER);
				}

				return page;
			}
		});

	}

	void setResult() {

		VBox v = new VBox(10);
		v.setPadding(new Insets(0, 0, 0, 0));

		double avgmp = 0, avgmd = 0, avgbp = 0, avgbd = 0;
		String fff = null, dff = null, wpp = null, wff = null, dpp = null;
		BigDecimal bb = BigDecimal.valueOf(0);

		System.out.println("File List ------->" + bb);
		for (int i = 0; i < list_d.size(); i++) {

			fff = "" + list_d.get(i).filename;
			dff = "" + list_d.get(i).data.get("meanp");
			dpp = "" + list_d.get(i).data.get("meand");
			wff = "" + list_d.get(i).data.get("bpressure");
			wpp = "" + list_d.get(i).data.get("bdiameter");

			bb.add(BigDecimal.valueOf(Double.parseDouble(wff)));

			avgmp = avgmp + Double.parseDouble(dff);
			avgmd = avgmd + Double.parseDouble(dpp);
			avgbp = avgbp + Double.parseDouble(wff);
			avgbd = avgbd + Double.parseDouble(wpp);

			v.getChildren().add(getVBox(fff, Myapp.getRound(Double.parseDouble(wff), 2),Myapp.getRound(Double.parseDouble(wpp), 2), Myapp.getRound(Double.parseDouble(dff), 2), Myapp.getRound(Double.parseDouble(dpp), 2)));
		}

		BigDecimal b = BigDecimal.valueOf(avgmp / list_d.size());
		b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
		// avgminpress.setText(""+b);

		b = BigDecimal.valueOf(avgmd / list_d.size());
		b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
		// avgmindia.setText(""+b);
		avgmean.setText("" + b);

		b = BigDecimal.valueOf(avgbp / list_d.size());
		b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
		// avgbppress.setText(""+b);

		b = BigDecimal.valueOf(avgbd / list_d.size());
		b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
		// avgbpdia.setText(""+b);
		avgbub.setText("" + b);

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
				node.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
						new EventHandler<MouseEvent>() {

							private static final int X_OFFSET = 15;
							private static final int Y_OFFSET = -5;
							Label label = new Label();

							@Override
							public void handle(final MouseEvent event) {
								System.out.println("MOuse Event");
								final String colorString = "#cfecf0";
								label.setFont(new Font(20));
								popup.getContent().setAll(label);
								label.setStyle("-fx-background-color: "
										+ colorString + "; -fx-border-color: "
										+ colorString + ";");
								label.setText("x=" + data.getXValue() + ", y="
										+ data.getYValue());
								popup.show(data.getNode().getScene()
										.getWindow(), event.getScreenX()
										+ X_OFFSET, event.getScreenY()
										+ Y_OFFSET);
								event.consume();
							}

							public EventHandler<MouseEvent> init() {
								label.getStyleClass().add("chart-popup-label");
								return this;
							}

						}.init());

				node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,
						new EventHandler<MouseEvent>() {

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

	VBox getVBox(String name, double single, double bub) {
		VBox v = new VBox(10);
		v.setLayoutX(single * bub);

		Label l = new Label();
		l.setText(name);
		l.setTextFill(Color.BLACK);
		v.getChildren().add(l);
		Line li = new Line();
		li.setStroke(Color.BLACK);
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

	public void setColor() {

		grpclr.addAll(Myapp.colors);

	}

	void getFiles() {
		try {

			// File f=new File("CsvFolder/"N0073/+Myapp.uid+"/"+Myapp.sampleid);

			File fff = new File("TableCsvs");

			//File f = new File(fff.getPath() + "/" + Myapp.sampleid);
			File f = new File(fff.getPath() + "/sample022");

			File[] ff = f.listFiles();
			System.out.println("Total test :" + ff.length);

			double darcy = 0;

			for (int i = 0; i < ff.length; i++) {
				DatareadN d = new DatareadN();

				d.fileRead(ff[i]);

				list_d.add(d);

			}
		} catch (Exception e) {

		}

	}

	HBox getVBox(String name, String bubp, String bubd, String mp, String md) {
		HBox v1 = new HBox(0);
		v1.setPadding(new Insets(5, 0, 0, 3));

		Label l1 = new Label();
		l1.setMaxWidth(200);
		l1.setMinWidth(200);
		l1.setPrefWidth(200);
		l1.setWrapText(true);
		l1.setFont(fontss.getM_M());
		l1.setTextFill(Color.web("#727376"));
		l1.setText(name);

		Label lv = new Label();
		lv.setText(bubp);

		lv.setMaxWidth(60);
		lv.setMinWidth(60);
		lv.setPrefWidth(60);
		lv.setFont(fontss.getM_M());
		lv.setTextFill(Color.web("#727376"));

		Label lv1 = new Label();
		lv1.setText(bubd);

		lv1.setMaxWidth(60);
		lv1.setMinWidth(60);
		lv1.setPrefWidth(60);
		lv1.setFont(fontss.getM_M());
		lv1.setTextFill(Color.web("#727376"));

		Label lv2 = new Label();
		lv2.setText(mp);
		lv2.setMaxWidth(60);
		lv2.setMinWidth(60);
		lv2.setPrefWidth(60);
		lv2.setFont(fontss.getM_M());
		lv2.setTextFill(Color.web("#727376"));

		Label lv3 = new Label();
		lv3.setText(md);
		lv3.setMaxWidth(60);
		lv3.setMinWidth(60);
		lv3.setPrefWidth(60);
		lv3.setFont(fontss.getM_M());
		lv3.setTextFill(Color.web("#727376"));

		Button btn = new Button();
		btn.setId(name);
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Button b = (Button) arg0.getSource();
				System.out.println(b.getId());
				delfile = b.getId();
				deletpopup();
			}
		});

	//	btn.setStyle("-fx-background-color: #f8f8f85c; -fx-text-fill: #254F94;");
		//btn.setText("Delete");
		
		btn.getStyleClass().add("cmbtransperant");
		image = new
		 Image(this.getClass().getResourceAsStream("/userinput/deleteblue.png"),
	 20, 20, false, false);
		btn.setGraphic(new ImageView(image));

		v1.getChildren().addAll(l1, lv, lv1, lv2, lv3, btn);
		return v1;
	}

	void report() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"/application/first.fxml"));
		try {
			Pane cmdPane = (Pane) fxmlLoader.load();

			MainancController.mainanc1.getChildren().clear();
			MainancController.mainanc1.getChildren().add(cmdPane);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void setgauge() {

		VBox v = new VBox(5);

		gauge = GaugeBuilder
				.create()
				.skinType(SkinType.DASHBOARD)
				.animated(true)
				// .title("Pressure")
				.unit("MI")
				.maxValue(1000)
				.barColor(Color.CRIMSON)
				.valueColor(Color.WHITE)
				.titleColor(Color.WHITE)
				.minWidth(280)
				.maxValue(30)
				.minHeight(180)
				.unitColor(Color.WHITE)
				.thresholdVisible(false)
				.shadowsEnabled(true)
				.gradientBarEnabled(true)
				.barColor(Color.BISQUE)
				.gradientBarStops(new Stop(0.33, Color.GREEN),
						new Stop(0.66, Color.CYAN), new Stop(1.00, Color.LIME))
				.build();

		ancgauge.getChildren().add(gauge);

		//

	}

	private ImageView createImageViewForChartFile(Integer chartNumber) {
		ImageView imageView = new ImageView(new Image("file:///"
				+ getChartFilePath(chartNumber)));
		imageView.setFitWidth(PREVIEW_SIZE);
		imageView.setPreserveRatio(true);
		return imageView;
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

		@Override
		protected Void call() throws Exception {
			int i = nCharts;
			while (i > 0) {
				if (isCancelled()) {
					break;
				}
				System.out.println("charts : " + i);
				if (i == 5) {
					Pane p = c.drawLinechartMix2(pagination1.getPrefWidth(),
							pagination1.getPrefHeight(), "Flow vs Pressure",
							"Pressure (psi)", "Flow (L/mn)", list_d,
							"(1) Flow vs Pressure");
					charts.put(p);
					listofchart.add(p);
				} else if (i == 4) {

					Pane p;
					if (list_d.size() > 1) {
						p = c.drawLinechart(pagination1.getPrefWidth(),
								pagination1.getPrefHeight(),
								"Pore size Distribution vs Diameter",
								"Diameter (\u03BCm)", "Pore size Distribution",
								list_d, false, 8, 10,
								"(2) Pore size Distribution vs Diameter");
					} else {
						p = c.drawBarchart(
								pagination1.getPrefWidth(),
								pagination1.getPrefHeight(),
								"Pore size Distribution vs Diameter",
								"Diameter (\u03BCm)",
								"Por esize Distribution",
								list_d.get(0).getDistributionChart(
										list_d.get(0).getValuesOf(
												list_d.get(0).data
														.get("diameter") + ""),
										list_d.get(0).getValuesOf(
												list_d.get(0).data.get("psd")
														+ ""), 6));
					}

					listofchart.add(p);
					charts.put(p);

				} else if (i == 3) {

					Pane p;

					if (list_d.size() > 1) {
						p = c.drawLinechart(pagination1.getPrefWidth(),
								pagination1.getPrefHeight(),
								"Pore size Distribution vs Average Diameter",
								"Avarage Diameter (\u03BCm)",
								"Por esize Distribution", list_d, false, 9, 10,
								"Pore size Distribution vs Average Diameter");

					} else {
						// p=c.drawLinechart(pagination1.getPrefWidth(),pagination1.getPrefHeight(),"Pore size Distribution vs Average Diameter",
						// "Avarage Diameter (\u03BCm)",
						// "Por esize Distribution",list_d,false,9,10,"Pore size Distribution vs Average Diameter");
						p = c.drawBarchart(
								pagination1.getPrefWidth(),
								pagination1.getPrefHeight(),
								"Pore size Distribution vs Average Diameter",
								"Avarage Diameter (\u03BCm)",
								"Por esize Distribution",
								list_d.get(0).getDistributionChart(
										list_d.get(0).getValuesOf(
												list_d.get(0).data
														.get("avgdia") + ""),
										list_d.get(0).getValuesOf(
												list_d.get(0).data.get("psd")
														+ ""), 6));
					}

					listofchart.add(p);
					charts.put(p);
				} else if (i == 2) {
					Pane p = c.drawLinechart(pagination1.getPrefWidth(),
							pagination1.getPrefHeight(),
							"Cumunative Filter-Flow % vs Diameter",
							"Diameter (\u03BCm)", "Cumunative Filter-Flow %",
							list_d, false, 8, 6,
							"(4) Cumunative Filter-Flow % vs Diameter");
					listofchart.add(p);

					charts.put(p);
				} else if (i == 1) {
					Pane p = c.drawLinechart(pagination1.getPrefWidth(),
							pagination1.getPrefHeight(),
							"Incremental Filter-Flow % vs Diameter ",
							"Diameter (\u03BCm)", "Incremental Filter-Flow %",
							list_d, false, 10, 11,
							"(3) Incremental Filter-Flow % vs Diameter");
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

		ChartsSnapshotTask(BlockingQueue<Parent> charts,
				BlockingQueue<BufferedImage> images, final int nCharts) {
			this.charts = charts;
			this.images = images;
			this.nCharts = nCharts;
			updateProgress(0, nCharts);

		}

		@Override
		protected Void call() throws Exception {
			int i = nCharts;
			while (i > 0) {
				if (isCancelled()) {
					break;
				}
				images.put(snapshotChart(charts.take()));
				// if(i==1)
				// {
				// Platform.runLater(new Runnable() {
				//
				// @Override
				// public void run() {
				//
				// createPagination();
				// }
				// });
				// }
				i--;
				updateProgress(nCharts - i, nCharts);
			}

			return null;
		}

		private BufferedImage snapshotChart(final Parent chartContainer)
				throws InterruptedException {
			final CountDownLatch latch = new CountDownLatch(1);
			// render the chart in an offscreen scene (scene is used to allow
			// css processing) and snapshot it to an image.
			// the snapshot is done in runlater as it must occur on the javafx
			// application thread.
			final SimpleObjectProperty<BufferedImage> imageProperty = new SimpleObjectProperty();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Scene snapshotScene = new Scene(chartContainer);
					snapshotScene.getStylesheets().add(
							getClass().getResource("dynamicgraph.css")
									.toExternalForm());
					chartContainer.getStylesheets().add(
							getClass().getResource("dynamicgraph.css")
									.toExternalForm());

					final SnapshotParameters params = new SnapshotParameters();
					params.setFill(Color.WHITE);
					chartContainer.snapshot(
							new Callback<SnapshotResult, Void>() {
								@Override
								public Void call(SnapshotResult result) {
									imageProperty.set(SwingFXUtils.fromFXImage(
											result.getImage(), null));
									latch.countDown();
									return null;
								}
							}, params, null);
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

		@Override
		protected Void call() throws Exception {
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
				Logger.getLogger(ChartPlot.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
	}

	class SaveChartsTask<Void> extends Task {
		private final BlockingQueue<Parent> charts = new ArrayBlockingQueue(10);
		private final BlockingQueue<BufferedImage> bufferedImages = new ArrayBlockingQueue(
				10);
		private final ExecutorService chartsCreationExecutor = createExecutor("CreateCharts");
		private final ExecutorService chartsSnapshotExecutor = createExecutor("TakeSnapshots");
		private final ExecutorService imagesExportExecutor = createExecutor("ExportImages");
		private final ChartsCreationTask chartsCreationTask;
		private final ChartsSnapshotTask chartsSnapshotTask;
		private final PngsExportTask imagesExportTask;

		SaveChartsTask(final int nCharts) {
			chartsCreationTask = new ChartsCreationTask(charts, nCharts);
			chartsSnapshotTask = new ChartsSnapshotTask(charts, bufferedImages,
					nCharts);
			imagesExportTask = new PngsExportTask(bufferedImages, nCharts);

			setOnCancelled(new EventHandler() {
				@Override
				public void handle(Event event) {
					chartsCreationTask.cancel();
					chartsSnapshotTask.cancel();
					imagesExportTask.cancel();
				}
			});

			imagesExportTask.workDoneProperty().addListener(
					new ChangeListener<Number>() {
						@Override
						public void changed(
								ObservableValue<? extends Number> observable,
								Number oldValue, Number workDone) {
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

		@Override
		protected Void call() throws Exception {
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

		String name = "";
		if (chartNumber == 0)
			if (chartNumber == 0) {
				name = "(1) Flow vs Pressure";
			} else if (chartNumber == 1) {
				name = "(2) Pore size Distribution vs Diameter";
			} else if (chartNumber == 2) {
				name = "(3) Pore size Distribution vs Average Diameter";
			} else if (chartNumber == 3) {
				name = "(4) Cumunative Filter-Flow % vs Diameter";
			} else if (chartNumber == 4) {
				name = "(5) Incremental Filter-Flow % vs Diameter";

			}

		return new File(WORKING_DIR, "" + name + ".png").getPath();
	}

	private ExecutorService createExecutor(final String name) {
		ThreadFactory factory = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName(name);
				t.setDaemon(true);
				return t;
			}
		};

		return Executors.newSingleThreadExecutor(factory);
	}

}
