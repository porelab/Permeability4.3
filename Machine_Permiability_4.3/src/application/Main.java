package application;
	
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
	
	
	 private static final int SINGLE_INSTANCE_LISTENER_PORT = 9999;
	    private static final String SINGLE_INSTANCE_FOCUS_MESSAGE = "focus";

	    private static final String instanceId = UUID.randomUUID().toString();
	    private static final int FOCUS_REQUEST_PAUSE_MILLIS = 500;
	
 public void init() {
     CountDownLatch instanceCheckLatch = new CountDownLatch(1);

     
     Thread instanceListener = new Thread(() -> {
         try (ServerSocket serverSocket = new ServerSocket(SINGLE_INSTANCE_LISTENER_PORT, 10)) {
             instanceCheckLatch.countDown();

             while (true) {
                 try (
                         Socket clientSocket = serverSocket.accept();
                         BufferedReader in = new BufferedReader(
                                 new InputStreamReader(clientSocket.getInputStream()))
                 ) {
                     String input = in.readLine();
                     System.out.println("Received single instance listener message: " + input);
                     if (input.startsWith(SINGLE_INSTANCE_FOCUS_MESSAGE) && mainstage != null) {
                         Thread.sleep(FOCUS_REQUEST_PAUSE_MILLIS);
                         Platform.runLater(() -> {
                             System.out.println("To front " + instanceId);
                             mainstage.setIconified(false);
                             mainstage.show();
                             mainstage.toFront();
                         });
                     }
                 } catch (IOException e) {
                     System.out.println("Single instance listener unable to process focus message from client");
                     e.printStackTrace();
                 }
             }
         } catch(java.net.BindException b) {
             System.out.println("SingleInstanceApp already running");

             try (
                     Socket clientSocket = new Socket(InetAddress.getLocalHost(), SINGLE_INSTANCE_LISTENER_PORT);
                     PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
             ) {
                 System.out.println("Requesting existing app to focus");
                 out.println(SINGLE_INSTANCE_FOCUS_MESSAGE + " requested by " + instanceId);
             } catch (IOException e) {
                 e.printStackTrace();
             }

             System.out.println("Aborting execution for instance " + instanceId);
             Platform.exit();
         } catch(Exception e) {
             System.out.println(e.toString());
         } finally {
             instanceCheckLatch.countDown();
         }
     }, "instance-listener");
     instanceListener.setDaemon(true);
     instanceListener.start();

     try {
         instanceCheckLatch.await();
     } catch (InterruptedException e) {
         Thread.interrupted();
     }
 }
	
	@Override
	public void start(Stage primaryStage) {
		try {
			try {	

				ErrorList.setErrorList();
				FirebaseConnect f=new FirebaseConnect();
				FirestoreOptions options;
				Systemtime.StartTime();
				mainstage=primaryStage;
				
				clsObj=getClass();
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
