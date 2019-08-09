package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Splashscreen implements Initializable {

	
	@FXML
	AnchorPane root;
	
	
	Stage primaryStage=new Stage();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		System.out.println("SpashScreen");
		
		new SplashSleep().start();
		DataStore.isconfigure.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(
					ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
			
				System.out.println("Chaangedd ------->"+arg2+" : "+arg1);
				if(arg2==true)
				{
					
					root.getScene().getWindow().hide();
					primaryStage.show();
					
				}
				
			}
		});
	}

	
	class SplashSleep extends Thread
	{
		
		@Override
		public void run()
		{
			System.out.println("here");
			try {
				//Thread.sleep(5000);
				System.out.println("i m here");
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						try{Parent root1 = FXMLLoader.load(getClass().getResource("mainanc.fxml"));
					    Scene scene = new Scene(root1,1366,768);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					
						Main.mainstage=primaryStage;
						Image image = new Image(this.getClass().getResourceAsStream(
								"/application/shorticon.png"));
						primaryStage.getIcons().add(image);
						primaryStage.setTitle("NewYork-Instruments");
						primaryStage.setFullScreen(false);
						primaryStage.setScene(scene);
					//	primaryStage.setMaximized(true);
						primaryStage.setFullScreen(true);
						
						Main.mainstage.setOnCloseRequest(new EventHandler<WindowEvent>() {
							
							@Override
							public void handle(WindowEvent arg0) {
								
								try
								{
									System.exit(0);
									Platform.exit();
									
								}
								catch(Exception e)
								{
									
								}
								
							}
						});
						
						Main.shortCut();
						//primaryStage.show();
						
					}
						catch(Exception e)
						{
							e.printStackTrace();
							System.out.println("Error");
						}
					}
				});
			
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
}
