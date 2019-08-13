package toast;

import com.google.firebase.database.core.Platform;

import application.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


//dialoug class for popups

public class MyDialoug {

	public static Stage dialog, mainstage;
	public static boolean bol = true;
	public static int timer = 200;

	public static void closeDialoug() {
		if (bol) {
			try {
				mainstage.getScene().getRoot().setEffect(null);
				dialog.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("fxml error");
		}
	}

	public static void showDialoug() {
		if (bol) {
			try {

				// double centerXPosition = mainstage.getX() +
				// mainstage.getWidth()/2d;
				// double centerYPosition = mainstage.getY() +
				// mainstage.getHeight()/2d;

				// dialog.setX(centerXPosition - dialog.getWidth());
				// dialog.setY(centerYPosition - dialog.getHeight()/2d);
				// dialog.show();

				enableBlur();
				dialog.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("fxml error");
		}
	}

	public static void enableBlur() {
		ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);

		ColorAdjust adj1 = new ColorAdjust(0, 0, -0.8, 0);

		mainstage.getScene().getRoot().setEffect(adj1);

	}

	public static void hideDialoug() {
		if (bol) {
			try {
				mainstage.getScene().getRoot().setEffect(null);
				dialog.hide();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("fxml error");
		}
	}

	public MyDialoug(Stage mainstage, String page, int timer) {
		if (dialog == null) {
			dialog = new Stage();
			dialog.initStyle(StageStyle.UNDECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(mainstage);
		}

		this.timer = timer;
		this.mainstage = mainstage;
		setBase(page);
	}

	public MyDialoug(Stage mainstage, String page) {

		if (dialog == null) {
			dialog = new Stage();
			dialog.initStyle(StageStyle.UNDECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(mainstage);
		}

		this.mainstage = mainstage;
		setBase(page);

	}

	void setShortCut() {
		KeyCombination fullscreenshortcut = new KeyCodeCombination(KeyCode.B,
				KeyCombination.CONTROL_ANY);
		dialog.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {

				if (ke.getCode() == KeyCode.ESCAPE || fullscreenshortcut.match(ke)) {
					if (bol) {
						try {
							mainstage.getScene().getRoot().setEffect(null);
							dialog.hide();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("fxml error");
					}
				}

			}
		});
	}

	void setBase(String page) {

		

				try {
					Parent root = FXMLLoader.load(getClass().getResource(page));
					Scene dialogScene = new Scene(root);
					dialog.setScene(dialogScene);
					bol = true;
					setShortCut();
				} catch (Exception e) {
					bol = false;
					e.printStackTrace();
				}
			}
		

}
