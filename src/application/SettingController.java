package application;

import java.io.File;
import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class SettingController {

	private Stage stage;
	private Scene scene;
	private Parent root;
    
    CommandController controller = new CommandController();
    
	@FXML
	private Slider musicSlider;
	
	@FXML
	private Slider voiceSlider;
	
	@FXML
	private Slider alertSlider;
	
	@FXML
	public void initialize() {
		// get the slider initial values from the file
		MusicController music = MusicController.getInstance();
		
		double musicVolume = music.getVolume();
		musicSlider.setValue(musicVolume);
		double alertVolume = music.getAlertVolume();
		alertSlider.setValue(alertVolume);
		String voiceVolume = controller.runCommand("sed -n 6p word.scm");
		voiceSlider.setValue(Double.parseDouble(voiceVolume));
	}
	
	public void switchToMenu(Event event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,800,600);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
	}
	
	public void setMusicVolume(Event event) throws IOException {
		MusicController music = MusicController.getInstance();
		music.setMusicVolume(musicSlider.getValue());
	}
	
	public void setAlertVolume(Event event) throws IOException {
		MusicController music = MusicController.getInstance();
		music.setAlertVolume(alertSlider.getValue());
	}
	
	public void setVoiceVolume(Event event) throws IOException {
		// set volume on line 6 and play test sound
		controller.runCommand("sed -i '6s/.*/" + voiceSlider.getValue() + "/' word.scm");
	}
	
	public void testVoice(Event event) throws IOException {
		controller.runCommand("sed -i '9s/.*/" + "Kēmu Kupu" + "/' word.scm");
		controller.runCommand("festival -b word.scm");
	}
	
	public void testAlert(Event event) throws IOException {
		MusicController music = MusicController.getInstance();
		music.correct();
	}
}
