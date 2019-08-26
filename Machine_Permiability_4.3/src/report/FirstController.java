package report;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import syncing.Sync;
import toast.MyDialoug;
import toast.Openscreen;
import toast.Toast;
import application.Main;
import application.Myapp;

import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXToggleButton;

import data_read_write.DatareadN;
import extrafont.Myfont;

public class FirstController implements Initializable {

	Sync sync;

	String filepath = "TableCsvs/"+Myapp.uid;

	@FXML
	private Button filereset, btndownload;

	@FXML
	private Label filelab;

	@FXML
	Label sname;

	@FXML
	ScrollPane scroll, scrolltrial;

	@FXML
	Button btnprocedd, btnback, delbtn;

	static File file;

	ToggleGroup tgb;

	String uid = "";

	boolean isfirstfolder = true;

	Map<String, File> listoffiles;
	Map<String, List<CheckBox>> listoffilecheckbox;
	Map<String, List<Label>> listoffilecheckbox_datelab;

	static Map<String, List<String>> selectedbox;
	Map<String, CheckBox> mainchks;
	Map<String, Boolean> mainlistner;

	boolean onListner = true;

	Myfont fontss = new Myfont(14);

	int maxfile = 10;

	public static SimpleBooleanProperty isLogin = new SimpleBooleanProperty(
			true);
	public static SimpleBooleanProperty isDelete = new SimpleBooleanProperty(
			false);

	@FXML
	JFXToggleButton autosync;

	@FXML
	TextField searchsample;
	
	@FXML
	JFXSpinner syncicon;
	
	LinkedHashMap<String,VBox> allsamplebox;
	
	LinkedHashMap<String,ToggleButton> alltoggle;
	

	public SimpleBooleanProperty isSync = new SimpleBooleanProperty(false);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		allsamplebox=new LinkedHashMap<String, VBox>();
		alltoggle=new LinkedHashMap<String, ToggleButton>();
		tgb = new ToggleGroup();

		
		syncicon.visibleProperty().bind(isSync);

		scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		scrolltrial.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		uid = Myapp.uid;

		sync = new Sync(isSync);

		isLogin.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {

				if (newValue) {

					Openscreen.open("/report/first.fxml");
				}

			}
		});

		/*File Delete Button Click Event*/
		isDelete.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {

				if (newValue) {
					isLogin.set(false);


					Openscreen.open("/report/first.fxml");

				}

			}
		});

		if (Myapp.status.equals("true")) {
			autosync.setSelected(true);
			sync.SyncOnline();
		}

		listoffiles = new HashMap<>();
		listoffilecheckbox = new HashMap<String, List<CheckBox>>();
		listoffilecheckbox_datelab = new HashMap<String, List<Label>>();
		selectedbox = new HashMap<String, List<String>>();
		mainchks = new HashMap<String, CheckBox>();
		// getFolders();
		try {
			tgb.selectedToggleProperty().addListener(

			new ChangeListener<Toggle>() {
				public void changed(ObservableValue<? extends Toggle> ov,
						Toggle oldToggle, Toggle newToggle) {
					if (null != newToggle) {
						System.out.println(tgb.getSelectedToggle()
								.getUserData());
						folderUpdate(tgb.getSelectedToggle()
								.getUserData()+"");
						
						sname.setText(tgb.getSelectedToggle().getUserData()
								+ "");
						showCheckBox(tgb.getSelectedToggle().getUserData() + "");
						
					} else {
						oldToggle.setSelected(true);
					}
				}
			});
		} catch (Exception e) {
			System.out.println("Error 1 : " + e.getMessage());

		}
		getFolderList();
		/*Open Cloud File Popup*/
		btndownload.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								System.out.println("Open Online File......");
								
							

								MyDialoug my = new MyDialoug(Main.mainstage,
										"/report/onlinefilepopup.fxml");
								my.showDialoug();
							}
						});
					
					}
				}).start();
				

			}
		});

		/*Selected File To Open Report Screen*/
		btnprocedd.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				int ff=setSelectedLab();
				if (ff > 0 && ff<=10) {
					Openscreen.open("/report/report.fxml");
				} else {
					Toast.makeText(Main.mainstage,
							"Please select file in between 1 to 10.", 2000, 300, 300);
				}
			}
		});

		/*Back To Home Screen*/
		btnback.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Openscreen.open("/application/first.fxml");

			}
		});

		/*Seleted Sample or File Delete*/
		delbtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if (selectedbox.size() > 0) {
					MyDialoug d = new MyDialoug(Main.mainstage,
							"/report/deletepopup.fxml");
					d.showDialoug();
				} else {
					Toast.makeText(Main.mainstage,
							"Please select atleast one file", 2000, 300, 300);
				}
			}
		});

		/*Seleted Sample or File reset*/
		filereset.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				resetAll();

			}
		});

		/*All sample Sync in Cloud*/
		autosync.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				if (Myapp.uid.equals("")) {
					System.out.println("Not Login");
					verifyloginc();
				} else {
					if (autosync.isSelected()) {

						sync.SyncOnline();
						Myapp.updateInDatabaseAutosync(true);
					} else {
						isSync.setValue(false);
						Myapp.updateInDatabaseAutosync(false);
					}

				}

			}
		});

		
		searchsample.setOnKeyReleased(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				
				
					filterSample(searchsample.getText());
				
				
			}
		});
		
	}
	
	
	void filterSample(String st)
	{
		if(st.isEmpty())
		{
			   List<String> snames=new ArrayList<>(allsamplebox.keySet());
				VBox v = new VBox(20);
				HBox h = new HBox(20);
				int tempind=0;
				for(int i=0;i<allsamplebox.size();i++)
				{
					int num = tempind % 5;
						if (num == 0) {
							h = new HBox(30);
							v.getChildren().add(h);

						}
						h.getChildren().add(allsamplebox.get(snames.get(i)));
						
						
						tempind++;
					
				}
				
				
			scroll.setPadding(new Insets(10, 30, 10, 30));
			scroll.setContent(v);
		}
		else
		{
			System.out.println("search String : "+st);
		   List<String> snames=new ArrayList<>(allsamplebox.keySet());
			VBox v = new VBox(20);
			HBox h = new HBox(20);
			int tempind=0;
			boolean isFirst=true;
			for(int i=0;i<allsamplebox.size();i++)
			{
				
				if(Pattern.compile(Pattern.quote(st), Pattern.CASE_INSENSITIVE).matcher(snames.get(i)).find())
				{
					int num = tempind % 5;
					if (num == 0) {
						h = new HBox(30);
						v.getChildren().add(h);

					}
					h.getChildren().add(allsamplebox.get(snames.get(i)));
					if(isFirst)
					{
					alltoggle.get(snames.get(i)).setSelected(true);
					isFirst=false;
					}
					tempind++;
				}
			}
			
			
		scroll.setPadding(new Insets(10, 30, 10, 30));
		scroll.setContent(v);
		
		}
		
	}
	
	/* Show All Trials in Selected Sample*/

	void showCheckBox(String foldername) {
		VBox v = new VBox(20);
		// v.setStyle("-fx-background-color: #f8f8f8;");

		scrolltrial.setPadding(new Insets(20, 10, 20, 10));
		List<CheckBox> list = listoffilecheckbox.get(foldername);
		List<Label> listlab = listoffilecheckbox_datelab.get(foldername);
		for (int i = 0; i < list.size(); i++) {

			CheckBox c = list.get(i);

			Label l = listlab.get(i);
			Label l1 = new Label();
			l1.setText("" + (i + 1) + "\t");

			HBox h = new HBox();

			BorderPane bp = new BorderPane();
			bp.setPadding(new Insets(0, 0, 0, 0));

			h.getChildren().addAll(l1, c);

			bp.setLeft(h);
			bp.setRight(l);

			v.getChildren().add(bp);

		}
		v.setMinWidth(scrolltrial.getPrefWidth() - 50);
		scrolltrial.setContent(v);

	}

	/* Show Selected All Trials in Selected Sample all*/
	void setAllChecked(String name, boolean bol) {

		List<CheckBox> list = listoffilecheckbox.get(name);
		if (!bol) {
			selectedbox.remove(name);
		}

		if (bol) {
			if (selectedbox.containsKey(name)) {
				selectedbox.remove(name);
			}
			List<String> files = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				CheckBox cc = list.get(i);
				cc.setSelected(bol);
				files.add(cc.getId());

			}
			selectedbox.put(name, files);
		} else {
			for (int i = 0; i < list.size(); i++) {
				CheckBox cc = list.get(i);
				cc.setSelected(bol);

			}
		}

		setSelectedLab();

		// printSelectedFiles();

	}

	/*Clear All Selected Cheack box*/
	void resetAll() {
		List<String> folders = new ArrayList<String>(selectedbox.keySet());

		for (int i = 0; i < folders.size(); i++) {
			List<CheckBox> ll = listoffilecheckbox.get(folders.get(i));
			for (int j = 0; j < ll.size(); j++) {
				CheckBox cc = ll.get(j);
				cc.setSelected(false);
			}

		}

		List<String> allf = new ArrayList<String>(mainchks.keySet());
		for (int i = 0; i < allf.size(); i++) {
			CheckBox ff = mainchks.get(allf.get(i));
			System.out.println("" + ff.getText() + " : " + ff.isSelected());

			ff.setSelected(false);
		}

		selectedbox.clear();
	}

	void printSelectedFiles() {
		try {
			List<String> folders = new ArrayList<String>(selectedbox.keySet());
			System.out.println("\n\n\n");
			for (int i = 0; i < folders.size(); i++) {
				List<String> ff = selectedbox.get(folders.get(i));
				System.out.println("Folder   : " + folders.get(i) + " \n Size "
						+ ff.size() + "\ndata :" + ff);
			}

			List<String> allf = new ArrayList<String>(mainchks.keySet());
			for (int i = 0; i < folders.size(); i++) {
				CheckBox ff = mainchks.get(allf.get(i));
				System.out.println("" + ff.getText() + " : " + ff.isSelected());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*Get Date wise Folders*/
	String getDateofFile(File f) {
		
		
		DatareadN dd=new DatareadN();
		dd.fileRead(f);
		
		
		String ss;
		
		if(dd.data!=null)
		{
		
			if(dd.data.containsKey("testdate"))
			{
				ss=dd.data.get("testdate")+"";
				
			}
			else
			{
				ss="";
			}
			
		}
		else
		{
			ss="";
		}
		
		return ss;
		/*
		Path filePath = f.toPath();
		String s = "Unknown";
		BasicFileAttributes attributes = null;
		try {
			attributes = Files.readAttributes(filePath,
					BasicFileAttributes.class);
		} catch (IOException exception) {
			System.out.println("Exception handled when trying to get file "
					+ "attributes: " + exception.getMessage());
		}
		long milliseconds = attributes.creationTime().to(TimeUnit.MILLISECONDS);
		if ((milliseconds > Long.MIN_VALUE) && (milliseconds < Long.MAX_VALUE)) {
			Date creationDate = new Date(attributes.creationTime().to(
					TimeUnit.MILLISECONDS));
			SimpleDateFormat formatter = new SimpleDateFormat("dd MMM ,yyyy");

			s = formatter.format(creationDate);
		}

		return s;
		*/
	}
	
	
	List<File> filterfile(File[] flist)
	{
		
	
		List<File> ff=new ArrayList<File>();
		
		for(int i=0;i<flist.length;i++)
		{
		DatareadN d=new DatareadN();
		d.fileRead(flist[i]);
		
		if(d.data!=null)
		{
			try
			{
				String date=d.data.get("testdate").toString();
				
				ff.add(flist[i]);
			}
			catch(Exception e)
			{
				System.out.println("error22 : "+flist[i]);
				File fm=new File(flist[i].getAbsolutePath());
				if(fm.delete())
				{
					System.out.println("Deleted");
				}
				else
				{
					
					flist[i].delete();
					
				}
			}
				
		}
		else
		{

			System.out.println("error22 null : "+flist[i]);
			File fm=new File(flist[i].getAbsolutePath());
			if(fm.delete())
			{
				System.out.println("Deleted");
			}
			else
			{
				
				flist[i].delete();
				
			}

		}
		
		
		}
		
		
		return ff;
	}
	
	/*Get All Folder list*/
	void getFolderList() {
		removeEmptyFolder();
		allsamplebox.clear();
		alltoggle.clear();
		// File f=new File("CsvFolder");
		File f = new File(filepath);

		if (f.isDirectory()) {

			/*
			 * if(uid.equals("")) { f=new File("CsvFolder/"+Myapp.email);
			 * 
			 * } else { f=new File("CsvFolder/"+uid);
			 * 
			 * }
			 */
			if (f.isDirectory()) {

				File[] listOfFiles = f.listFiles();

Arrays.sort(listOfFiles, new Comparator<File>() {
    public int compare(File f1, File f2) {
        return Long.compare(f2.lastModified(),f1.lastModified());
    }
});
				VBox v = new VBox(20);
				HBox h = new HBox(20);
				for (int i = 0; i < listOfFiles.length; i++) {
					int num = i % 5;
					if (num == 0) {
						h = new HBox(30);
						v.getChildren().add(h);

					}

					File ftemp = listOfFiles[i];
					if (ftemp.isDirectory()) {
						
						File[] fftemp1 = ftemp.listFiles();
						
						List<File> fftemp=filterfile(fftemp1);
						
						List<CheckBox> ck = new ArrayList<CheckBox>();

						List<Label> ck_lab = new ArrayList<Label>();
						
						
						
						for (int j = 0; j < fftemp.size(); j++) {
							CheckBox chk = new CheckBox(fftemp.get(j).getName());
							chk.setId(fftemp.get(j).getAbsolutePath());
							chk.setTooltip(new Tooltip(ftemp.getName()));
							chk.prefWidth(500);
							chk.minWidth(500);
							chk.maxWidth(500);
							chk.setWrapText(true);
							chk.setTextFill(Color.web("727376"));
							chk.setFont(fontss.getM_M());

							Label l = new Label();
							l.setText(getDateofFile(fftemp.get(j)));
							l.setTextFill(Color.web("727376"));
							l.setFont(fontss.getR_R());

							chk.selectedProperty().addListener(
									new ChangeListener<Boolean>() {
										@Override
										public void changed(
												ObservableValue<? extends Boolean> observable,
												Boolean oldValue,
												Boolean newValue) {

											System.out.println(chk.getTooltip()
													.getText().toString()
													+ " : "
													+ chk.getText()
													+ " is " + newValue);
											String folname = chk.getTooltip()
													.getText().toString();

											if (newValue) {
												if (!selectedbox
														.containsKey(folname)) {
													List<String> ssd = new ArrayList<String>();
													selectedbox.put(folname,
															ssd);
												}
												List<String> ss = selectedbox
														.get(folname);
												ss.add(chk.getId());

												/*
												 * List<CheckBox>
												 * list=listoffilecheckbox
												 * .get(folname); int flag=0;
												 * for(int
												 * i=0;i<list.size();i++) {
												 * if(!list.get(i).isSelected())
												 * { flag=1; break; }
												 * 
												 * }
												 * 
												 * CheckBox
												 * folchk=mainchks.get(folname);
												 * 
												 * 
												 * if(flag==0) {
												 * folchk.setSelected(true); }
												 * else {
												 * folchk.setSelected(false); }
												 */
												// printSelectedFiles();
											} else {

												// CheckBox
												// folchk=mainchks.get(folname);
												// folchk.setSelected(false);

												try {
													List<String> ss = selectedbox
															.get(folname);
													ss.remove(chk.getId());
													if (ss.size() == 0) {
														selectedbox
																.remove(folname);
													}
												} catch (Exception e) {

												}
												// printSelectedFiles();
											}

											setSelectedLab();

										}
									});

							ck_lab.add(l);
							ck.add(chk);

						}
						listoffilecheckbox.put(ftemp.getName(), ck);
						listoffilecheckbox_datelab.put(ftemp.getName(), ck_lab);

					}
					listoffiles.put(ftemp.getName(), ftemp);
					VBox vtemp = getVBoxofFolder(ftemp.getName());
					allsamplebox.put(ftemp.getName(), vtemp);
					h.getChildren().add(vtemp);
				}
				scroll.setPadding(new Insets(10, 30, 10, 30));
				scroll.setContent(v);
				
			}
		}
		
		System.out.println("All Sample Box : "+allsamplebox.size());
	}
	
	/*Remove Empty Folder*/
	void removeEmptyFolder() {
		File f = new File(filepath);

		if (f.isDirectory()) {
			File[] ff = f.listFiles();
			if (ff.length > 0) {
				for (int i = 0; i < ff.length; i++) {
					if (ff[i].list().length == 0) {

						if(ff[i].delete())
						{
							System.out.println("Empty delete");
						}
						
					}
				}
			}
		}
	}

	/*Set Sample with Name and Image(Folder img)*/
	void setToggleButtonProperty(ToggleButton tb, String title, String img,
			ToggleGroup group) {
		Image image = new Image(this.getClass().getResourceAsStream(img));
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(60);
		imageView.setFitHeight(60);
		// tb.setGraphic(imageView);
		// //tb.setContentDisplay(ContentDisplay.TOP);
		tb.setToggleGroup(group);
		tb.setUserData(title);

		if (isfirstfolder) {
			tb.setSelected(true);
			isfirstfolder = false;
		}

		// tb.setText(title);

	}

	/*Check  Login  */
	public void verifyloginc() {

		MyDialoug m = new MyDialoug(Main.mainstage,
				"/cloud/verifycloudloginpopup.fxml");
		m.showDialoug();

	}

	/*Set Sample Details vertical Box*/
	VBox getVBoxofFolder(String name) {
		VBox v1 = new VBox(5);
		v1.setMinWidth(120);
		Button im1 = new Button();
		im1.setId(name);
		Image image = new Image(this.getClass().getResourceAsStream(
				"/report/fl.png"));
		im1.setGraphic(new ImageView(image));
		im1.setStyle("-fx-background-color: transparent;");

		ToggleButton tb = new ToggleButton();
		tb.setPrefSize(100, 100);
		tb.setId(name);
		alltoggle.put(name, tb);
		setToggleButtonProperty(tb, name, "/report/fl.png", tgb);

		im1.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				Button ivv = (Button) event.getSource();
				System.out.println("clicked " + ivv.getId());

				sname.setText(ivv.getId());
				showCheckBox(ivv.getId());

			}
		});

		CheckBox chk = new CheckBox(name);
		chk.setMaxWidth(100);
		chk.setPrefWidth(100);
		chk.setMinWidth(100);
		chk.setWrapText(true);

		chk.setFont(fontss.getM_M());
		chk.setTextFill(Color.web("#727376"));

		mainchks.put(name, chk);
		chk.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {

				System.out.println(chk.getText() + " is " + newValue);
				setAllChecked(chk.getText(), newValue);

			}
		});

		v1.getChildren().addAll(tb, chk);
		return v1;
	}
	
	/*Sample or File Delete after Sample Details Update*/
	void folderUpdate(String nm)
	{
		try{
			
			File f=new File(filepath+"/"+nm);
			if(f.exists())
			{
				File ff=new File(filepath+"/"+nm+"/temp.txt");
				FileWriter fw=new FileWriter(f.getPath()+"/"+"temp.txt");
				fw.write("temp");
				fw.close();
				
				ff.delete();
				
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println("Error  111 : "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/*Check Max File*/
	int setSelectedLab() {
		List<String> s = new ArrayList<String>(selectedbox.keySet());
		int cnt = 0;
		for (int i = 0; i < s.size(); i++) {
			List<String> templist = FirstController.selectedbox.get(s.get(i));

			for (int j = 0; j < templist.size(); j++) {
				cnt++;
			}
		}

		filelab.setText(cnt + "/" + maxfile + "");

		return cnt;
	}
	
	/*Check Internet Connection after Show and Hide Sync Icon */
	void setTimer() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				System.out.println("Connection : "
						+ Myapp.isInternetConnected.get());
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (Myapp.isInternetConnected.get() == false) {
							syncicon.setVisible(false);
							Toast.makeText(Main.mainstage,
									"Internet Connection Problem", 300, 100,
									100);
						}

						System.out.println(" I M  CALLLLLLLLINNNNNGGGGGGG");
					}
				});
			}

		};
		timer.schedule(task, 15000);
	}
}
