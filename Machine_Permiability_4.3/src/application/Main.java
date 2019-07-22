package application;
	
import java.io.InputStream;

import javafx.application.Application;
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

import firebase.FirebaseConnect;


public class Main extends Application {
	public static Stage mainstage;
	public static Class<? extends Main> clsObj;
	@Override
	public void start(Stage primaryStage) {
		try {
			try {	
				
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
