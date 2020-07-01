package drawchart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import javax.imageio.ImageIO;

import com.sun.javafx.charts.Legend;

import application.DataStore;
import data_read_write.DatareadN;
import de.tesis.dynaware.javafx.fancychart.zoom.Zoom;
import drawchart.SmoothedChart.ChartType;

//create chart for report section and on result popup.
public class ChartPlot {

	List<String> grpclr;

	public ChartPlot(boolean bol) {

		grpclr = new ArrayList<String>();


		if (bol) {

			File f = new File("PororeportTool");
			if (f.exists()) {
				if (f.isDirectory()) {
					// delete files from it
					File folder = new File("PororeportTool");
					File[] listOfFiles = folder.listFiles();
					for (int i = 0; i < listOfFiles.length; i++) {
						listOfFiles[i].delete();
						System.out.println("delte file" + (i + 1));
					}

				} else {
					f.mkdir();
				}
			} else {
				f.mkdir();
			}
		}

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
	
	
	public Pane drawLinechart(double xsize, double ysize, String Title,
			String Xname, String Yname, List<DatareadN> d, boolean reverse,
			int xm, int ym,String mm) {
		System.out.println("In DrawLineChart multiple file");

		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel(Xname);
		yAxis.setLabel(Yname);
		// creating the chart

		SmoothedChart<Number, Number> lineChart = new SmoothedChart<>(xAxis,
				yAxis);
		lineChart.setSmoothed(true);
		lineChart.setChartType(ChartType.LINE);
		lineChart.setInteractive(true);
		lineChart.setSubDivisions(8);

		lineChart.setPrefSize(xsize, ysize - 50);
		lineChart.setMinSize(xsize, ysize - 50);
		lineChart.setMaxSize(xsize, ysize - 50);

		// lineChart.setTitle(Title);
		lineChart.setLegendVisible(false);
		lineChart
				.setStyle("-fx-background-color: rgba(255,255,355,0.05);-fx-background-radius: 10;");

		// xAxis.setStyle("-fx-tick-label-font-size: 1.0em;-fx-tick-label-fill: #000000;-fx-tick-length: 18;-fx-minor-tick-length: 10;");
		// yAxis.setStyle("-fx-tick-label-font-size: 1.0em;-fx-tick-label-fill: #000000;-fx-tick-length: 18;-fx-minor-tick-length: 10;");

		XYChart.Series[] series = new XYChart.Series[d.size()];
		for (int i = 0; i < d.size(); i++) {
			series[i] = new XYChart.Series();
			// System.out.println("File "+(i+1)+" is taking in action");

			series[i].setName(d.get(i).filename);

			DatareadN dr = d.get(i);

			List<String> x = null, y = null;

		
				x = dr.getValuesOf(dr.data.get("dpressure") + "");
				y = dr.getValuesOf(dr.data.get("dflow") + "");
			

			try {

				for (int j = 2; j < x.size() - 1; j++) {
					double xd = 0;
					double yd = 0;

				
						xd = Double.parseDouble(x.get(j));
						yd = Double.parseDouble(y.get(j));
					

					series[i].getData().add(new XYChart.Data(xd, yd));

				}

			} catch (Exception e) {
			}
		}
		lineChart.getStylesheets().add(
				getClass().getResource("dynamicgraph.css").toExternalForm());
		Pane p = new Pane();
		p.setStyle("-fx-background-color: rgba(0, 100, 100, 0.0);");
		p.setPrefSize(xsize, ysize - 50);
		p.setMinSize(xsize, ysize - 50);
		p.setMaxSize(xsize, ysize - 50);
		p.getChildren().add(lineChart);
		lineChart.setAnimated(false);
		lineChart.getData().addAll(series);

	       Zoom zoom =new Zoom(lineChart,p);
		return p;

	}
	public Pane drawLinechartKusum(double xsize, double ysize, String Title,
			String Xname, String Yname, List<DatareadN> d, boolean reverse,
			int xm, int ym,String mm) {
		System.out.println("In DrawLineChart multiple file");

		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel(Xname);
		yAxis.setLabel(Yname);
		// creating the chart

		SmoothedChart<Number, Number> lineChart = new SmoothedChart<>(xAxis,
				yAxis);
		lineChart.setSmoothed(true);
		lineChart.setChartType(ChartType.LINE);
		lineChart.setInteractive(true);
		lineChart.setSubDivisions(8);

		lineChart.setPrefSize(xsize, ysize - 50);
		lineChart.setMinSize(xsize, ysize - 50);
		lineChart.setMaxSize(xsize, ysize - 50);

		// lineChart.setTitle(Title);
		lineChart.setLegendVisible(false);
		lineChart
				.setStyle("-fx-background-color: rgba(255,255,355,0.05);-fx-background-radius: 10;");

		// xAxis.setStyle("-fx-tick-label-font-size: 1.0em;-fx-tick-label-fill: #000000;-fx-tick-length: 18;-fx-minor-tick-length: 10;");
		// yAxis.setStyle("-fx-tick-label-font-size: 1.0em;-fx-tick-label-fill: #000000;-fx-tick-length: 18;-fx-minor-tick-length: 10;");

		XYChart.Series[] series = new XYChart.Series[d.size()];
		for (int i = 0; i < d.size(); i++) {
			series[i] = new XYChart.Series();
			// System.out.println("File "+(i+1)+" is taking in action");

			series[i].setName(d.get(i).filename);

			DatareadN dr = d.get(i);

			List<String> x = null, y = null;

		
				x = dr.getValuesOf(dr.data.get("dpressure") + "");
				y = dr.getValuesOf(dr.data.get("dflow") + "");
			

			try {

				for (int j = 0; j < x.size() - 1; j++) {
					double xd = 0;
					double yd = 0;

				
						xd = Double.parseDouble(x.get(j));
						yd = Double.parseDouble(y.get(j));//*0.00211888;
					

					series[i].getData().add(new XYChart.Data(xd, yd));
				
				}

			} catch (Exception e) {
			}
		}
		lineChart.getStylesheets().add(
				getClass().getResource("dynamicgraph.css").toExternalForm());
		Pane p = new Pane();
		p.setStyle("-fx-background-color: rgba(0, 100, 100, 0.0);");
		p.setPrefSize(xsize, ysize - 50);
		p.setMinSize(xsize, ysize - 50);
		p.setMaxSize(xsize, ysize - 50);
		p.getChildren().add(lineChart);
		lineChart.setAnimated(false);
		lineChart.getData().addAll(series);

	       Zoom zoom =new Zoom(lineChart,p);
		return p;

	}

	
	
	
	public Pane drawLinechartWithScatterMultiple(double xsize, double ysize,
			String Title, String Xname, String Yname, List<DatareadN> d,
			String imgname) {

		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();

		yAxis.setLabel(Yname);
		xAxis.setLabel(Xname);

		// SmoothedChart<Number, Number> lineChart= new SmoothedChart<>(xAxis,
		// yAxis);
		LineChart<Number, Number> lineChart = new LineChart<>(xAxis,
				yAxis);

		lineChart.setPrefSize(xsize, ysize - 50);
		lineChart.setMinSize(xsize, ysize - 50);
		lineChart.setMaxSize(xsize, ysize - 50);
		//lineChart.setSmoothed(true);
		////lineChart.setChartType(ChartType.LINE);
		//lineChart.setInteractive(true);
		//lineChart.setSubDivisions(8);
		// creating the chart

		// lineChart.setTitle(Title);
		lineChart.setLegendVisible(false);
	
		List<String> clrlist = DataStore.getColorMultiple();
		List<String> leglab = new ArrayList<String>();

		XYChart.Series[] series = new XYChart.Series[d.size() * 2];
		// XYChart.Series[] series1 = new XYChart.Series[d.size()];
		// XYChart.Series[] series2 = new XYChart.Series[d.size()];
		int ik = 0;
		for (int i = 0; i < d.size(); i++) {
			String clr = clrlist.get(i);
			series[ik] = new XYChart.Series();
			leglab.add(d.get(i).filename);
			// System.out.println("File >>"+(i+1)+" is taking in action");

			// series[i].setName(d.get(i).getFileName());
			String strokeStyle;
			DatareadN dr = d.get(i);

			List<String> x = new ArrayList<String>();
			List<String> y = new ArrayList<String>();

		
			x = DataStore.ConvertPressure(dr.getValuesOf("" + dr.data.get("dpressureoriginal")));
			y = DataStore.ConvertFlow(dr.getValuesOf("" + dr.data.get("dfloworiginal")));
			try {
				for (int j = 0; j < x.size(); j++) {
					double xd = 0;
					double yd = 0;

					{
						xd = Double.parseDouble(x.get(j));
						yd = Double.parseDouble(y.get(j));
					}

					if(yd>=0)
					{
					series[ik].getData().add(new XYChart.Data(xd, yd));
					}
					
			

				}
				lineChart.getData().add(series[ik]);

				ObservableList<Data<Number, Number>> m = lineChart.getData()
						.get(ik).getData();
				strokeStyle = "-fx-stroke:transparent ;";

				lineChart.getData().get(ik).getNode()
						.lookup(".chart-series-line")
						.setStyle(strokeStyle);
				m = lineChart.getData().get(ik).getData();

				
				for (int i2 = 0; i2 < m.size(); i2++) {
					((Data) m.get(i2)).getNode().setStyle(
							"-fx-background-color: " + clr + ", transparent;");

				}
				ik++;

			} catch (Exception e) {

			}
			// series2------
			series[ik] = new XYChart.Series();

			series[ik].setName(d.get(i).filename);

			List<String> temppre =  DataStore.ConvertPressure(dr
					.getValuesOf(dr.data.get("dpressure") + ""));
			List<String> tempfl =  DataStore.ConvertFlow(dr.getValuesOf(dr.data.get("dflow") + ""));

			
			
			double mxpr = Double.parseDouble(x.get(1));

			List<Double> temphdpr = new ArrayList<Double>();
			List<Double> temphdfl = new ArrayList<Double>();

			for (int m = 0; m < temppre.size(); m++) {

				double xd = 0;
				double yd = 0;
				xd = Double.parseDouble(temppre.get(m));
				yd = Double.parseDouble(tempfl.get(m));

				
					temphdpr.add(xd);
					temphdfl.add(yd);
					
					
					if(yd>=0)
					{
					series[ik].getData().add(new XYChart.Data(xd, yd));
					}
				
				
			}

		
			try {
				lineChart.getData().add(series[ik]);
				

				ObservableList<Data<Number, Number>> m = lineChart.getData()
						.get(ik).getData();
				strokeStyle = "-fx-stroke:" + clr + " ;";

				
				
				lineChart.getData().get(ik).getNode()
						.lookup(".chart-series-line")
						.setStyle(strokeStyle);
				
			/*	String symbol="";
				lineChart.getData().get(ik).getNode()
				.lookup(".chart-series-line-symbol")
				.setStyle(strokeStyle);
				*/
				m = lineChart.getData().get(ik).getData();

				for (int i2 = 0; i2 < m.size(); i2++) {
					((Data) m.get(i2)).getNode().setStyle(
							"-fx-background-color:  transparent, transparent;");

				}
				ik++;
			} catch (Exception e) {

			}
		
		}

		Pane p = new Pane();
		p.setStyle("-fx-background-color: rgba(0, 100, 100, 0.0);");
		p.getChildren().add(lineChart);

		p.setPrefSize(xsize, ysize - 50);
		p.setMinSize(xsize, ysize - 50);
		p.setMaxSize(xsize, ysize - 50);
		lineChart.setAnimated(false);
		Legend ll = (Legend) lineChart.lookup(".chart-legend");
		ll.getItems().clear();
		for (int y = 0; y < leglab.size(); y++) {

			@SuppressWarnings("restriction")
			Legend.LegendItem ll1 = new Legend.LegendItem(leglab.get(y),
					new Circle(6, Paint.valueOf(clrlist.get(y))));

			String backgroundColorStyle = "-fx-background-color: "
					+ clrlist.get(y) + ", white;";

			ll.setStyle(backgroundColorStyle);

			lineChart.getStylesheets()
					.add(getClass().getResource("dynamicgraph.css")
							.toExternalForm());
			ll.getItems().add(ll1);
		}

		System.out.println("LineChart is Created");

	       Zoom zoom =new Zoom(lineChart,p);
		return p;
	}

}
