package application;
	
import java.io.File;
import java.util.concurrent.ScheduledExecutorService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {	
			// Background music
			MusicController music = MusicController.getInstance();
			File file = new File("src/application/music/Carefree.mp3");
			music.changeSong(file);
			
			Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			Image icon = new Image(getClass().getResourceAsStream("images/icon.jpg"));
			primaryStage.getIcons().add(icon);
			primaryStage.setTitle("Kēmu Kupu");
			
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			

			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);

	}
	
	// Stop the timer on window close
	@Override
	public void stop(){
		try {
			QuizController.update.cancel();
		} catch (Exception e) {}
		Platform.exit();
        
	}
}
