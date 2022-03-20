package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MusicController {
	// singleton music controller class
    private static MusicController musicController = null;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Media buzzer;
    private MediaPlayer buzzerPlayer;
    private File file;
    private double volume = 0.1;
    private double buzzerVolume = 0.6;
 
    public double getVolume() {
		return volume;
	}
    public double getAlertVolume() {
 		return buzzerVolume;
 	}

	private MusicController() {
		File file = new File("src/application/music/correct.aiff");
    	buzzer = new Media(file.toURI().toString());
    	buzzerPlayer = new MediaPlayer(buzzer);
    }
 
    public static MusicController getInstance() {
        if (musicController == null)
        	musicController = new MusicController();
 
        return musicController;
    }
    
    public void pauseSong() {
    	mediaPlayer.pause();
    }

    public void changeSong(File songFile) {
    	file = songFile;
    	media = new Media(file.toURI().toString());
    	mediaPlayer = new MediaPlayer(media);
    	mediaPlayer.play();
    	mediaPlayer.setVolume(volume);
    	mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
    }
    
    public void setMusicVolume(double volume) {
    	this.volume = volume;
    	mediaPlayer.setVolume(this.volume);
    }
    
    public void setAlertVolume(double volume) {
    	this.buzzerVolume = volume;
    	buzzerPlayer.setVolume(this.buzzerVolume);
    }
    
    public void correct() {
		File file = new File("src/application/music/correct.aiff");
    	buzzer = new Media(file.toURI().toString());
    	buzzerPlayer = new MediaPlayer(buzzer);
    	buzzerPlayer.setVolume(buzzerVolume);
    	buzzerPlayer.play();
    }

    
    public void incorrect() {
		File file = new File("src/application/music/incorrect.aiff");
    	buzzer = new Media(file.toURI().toString());
    	buzzerPlayer = new MediaPlayer(buzzer);
    	buzzerPlayer.setVolume(buzzerVolume);
    	buzzerPlayer.play();
    }
    
}

