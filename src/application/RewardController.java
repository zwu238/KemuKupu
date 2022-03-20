package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RewardController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Text scoreText;
	@FXML
	private Text timeText;
	@FXML
	private ImageView star1;
	@FXML
	private ImageView star2;
	@FXML
	private ImageView star3;
	@FXML
	private ListView<String> wordListView;
	@FXML
	private ListView<String> answerListView;
	@FXML
	private ListView<String> timeListView;
	@FXML
	private ListView<String> scoreListView;
	@FXML 
	private AnchorPane scorePlane;
	
	CommandController controller = new CommandController();
	
	@FXML
	public void initialize() throws InterruptedException, IOException {
		scorePlane.setVisible(false);
	}

	public void showStats()  {
		if(!scorePlane.isVisible()) {

			scorePlane.setVisible(true);
		} else {
			scorePlane.setVisible(false);
		}
	}
	
	public void setScore(String score, String highScore) {
		scoreText.setText(score);
		
		int scoreInt = Integer.parseInt(score);
		
		Image starWinImage = new Image(getClass().getResourceAsStream("images/stargold.png"));
		if (scoreInt >= 300) {
			star1.setImage(starWinImage);
		}
		if (scoreInt >= 2500) {
			star2.setImage(starWinImage);
		} 
		if (scoreInt >= 4000) {
			star3.setImage(starWinImage);
		}
	}
	
	public void getStats(List<String> spellingWords, List<String> answerState,List<Integer> answerTimes) {
		wordListView.getItems().clear();
		answerListView.getItems().clear();
		timeListView.getItems().clear();
		scoreListView.getItems().clear();
		wordListView.getItems().add("words");
		answerListView.getItems().add("answer");
		timeListView.getItems().add("time");
		scoreListView.getItems().add("score");
		
		wordListView.getItems().addAll(spellingWords);
		answerListView.getItems().addAll(answerState);
		List<String> stringAnswerTimes = new ArrayList<>();
		int wordCount = 0;
		for (int time : answerTimes) {
			String answer = answerState.get(wordCount);
			if (!answer.equals("skipped")) {
				stringAnswerTimes.add(String.valueOf(time));
			} else {
				stringAnswerTimes.add("");
			}

			if (answer.equals("correct")) {
				if (time > 15) {
					scoreListView.getItems().add("50");
				} else if (time > 11) {
					scoreListView.getItems().add("200");
				} else if (time > 7) {
					scoreListView.getItems().add("500");
				} else {
					scoreListView.getItems().add("1000");
				}
			} else {
				scoreListView.getItems().add("0");
			}
			wordCount++;
		}
		timeListView.getItems().addAll(stringAnswerTimes);

	}
	
	public void setTime(int totalTime) {
		timeText.setText("Total time taken: " + String.valueOf(totalTime) + "s");
	}
	
	public void quitGame() throws IOException {
		Platform.exit();
	}
	
	public void setResults(String[] questions, String[] results) {
		StringBuilder msg = new StringBuilder();
		for (int i = 0; i < questions.length; i++) {
			msg.append(questions[i] + " (" + results[i] + ")");
			msg.append("\n");
		}
	}

	public void switchToTopicSelectionFromRewardScreen(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("TopicSelection.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,800,600);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
	}
	
	public void switchToMenuFromRewardScreen(Event event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root,800,600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}
	
}
