package application;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileLock;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import toast.Systemtime;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.FirestoreOptions;

import errorcodes.ErrorList;
import firebase.FirebaseConnect;


public class Main extends Application {
	public static Stage mainstage;
	public static Class<? extends Main> clsObj;
	

	private static boolean lockInstance(final String lockFile) {
	    try {
	        final File file = new File(lockFile);
	        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
	        final FileLock fileLock = randomAccessFile.getChannel().tryLock();
	        if (fileLock != null) {
	            Runtime.getRuntime().addShutdownHook(new Thread() {
	                public void run() {
	                    try {
	                        fileLock.release();
	                        randomAccessFile.close();
	                        file.delete();
	                    } catch (Exception e) {
	                        System.out.println("Error : "+e.getMessage());

	                    	e.printStackTrace();
	                    }
	                }
	            });
	            return true;
	        }
	    } catch (Exception e) { 
	    	System.out.println("Error1 : "+e.getMessage());

    	e.printStackTrace();
	    }
	    return false;
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			mainstage=primaryStage;
			ErrorList.setErrorList();

			clsObj=getClass();
			if(lockInstance("temp.log"))
			{
			
			try {	
			
			    FirebaseConnect f=new FirebaseConnect();
				FirestoreOptions options;
				Systemtime.StartTime();
				
				InputStream ii=(InputStream) this.getClass().getResourceAsStream("/application/serviceAccountKey.json");
				InputStream ii1=(InputStream) this.getClass().getResourceAsStream("/firebase/serviceAccountKey.json");
				
				 
			 
/*				FileInputStream serviceAccount =
						  new FileInputStream("src/firebase/serviceAccountKey.json");*/
				options =
					    FirestoreOptions.getDefaultInstance().toBuilder()
					        .setProjectId("nyiapp-3a612")
					        .setCredentials(GoogleCredentials.fromStream(ii))
					        .setTimestampsInSnapshotsEnabled(true)
					        .setDatabaseId("(default)")
					        .build();					
							FirebaseConnect.InitApp(options,ii1);
						
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
		    Myapp.initNotifier();
		    Myapp.setColor();
			Parent root = FXMLLoader.load(getClass().getResource("/application/splashscreen.fxml"));
			 Scene scene = new Scene(root,600,400);
			 primaryStage.initStyle(StageStyle.UNDECORATED);
			scene.getStylesheets().add(getClass().getResource("new_application.css").toExternalForm());
			fontload();
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("NewYork-Instruments");
			primaryStage.show(); 
			}
			else
			{
				Parent root = FXMLLoader.load(getClass().getResource("/newstageerror/splashscreen.fxml"));
				 Scene scene = new Scene(root,576,328);
				 primaryStage.initStyle(StageStyle.UNDECORATED);
				 Image image = new Image(this.getClass().getResourceAsStream(
							"/application/shorticon.png"));
					primaryStage.getIcons().add(image);
				primaryStage.setScene(scene);
				primaryStage.setTitle("NewYork-Instruments");
				primaryStage.show(); 
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	static void shortCut()
	{
		  KeyCombination fullscreenshortcut = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_ANY);
		  Main.mainstage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent ke) {
					
					
					
					 if(fullscreenshortcut.match(ke))
					 {

							Main.mainstage.setMaximized(true);
							Main.mainstage.setFullScreen(true);
					 }
					
				}
			});

	}
	
	void fontload()
	{
		
		Font.loadFont(Main.class.getResource("Montserrat-SemiBold.ttf").toExternalForm(), 100);
		Font.loadFont(Main.class.getResource("Roboto-Regular.ttf").toExternalForm(), 100);
		Font.loadFont(Main.class.getResource("Montserrat-Medium.ttf").toExternalForm(), 100);
		Font.loadFont(Main.class.getResource("BebasNeue.otf").toExternalForm(), 100);
		     
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
