package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class TopicSelectionController {

	QuizController controller = new QuizController();



	private Stage stage;
	private Scene scene;
	private Parent root;

	private boolean isPractice;

	// booleans carries over to quiz game to check for practice mode
	public void checkPractice(boolean isPractice) {
		this.isPractice = isPractice;
	}
 
	public void topicBabies(Event event) throws IOException { 

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
		root = loader.load();
		QuizController quizController = loader.getController();
		quizController.checkPractice(this.isPractice);
		quizController.setWordList("Babies");
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root,800,600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public void topicColours(Event event) throws IOException { 

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
		root = loader.load();
		QuizController quizController = loader.getController();
		quizController.checkPractice(this.isPractice);
		quizController.setWordList("Colours");
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root,800,600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public void topicDaysOfTheWeek(Event event) throws IOException { 

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
		root = loader.load();
		QuizController quizController = loader.getController();
		quizController.checkPractice(this.isPractice);
		quizController.setWordList("Days of Week");
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root,800,600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public void topicMonthsOfTheYear(Event event) throws IOException { 

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
		root = loader.load();
		QuizController quizController = loader.getController();
		quizController.checkPractice(this.isPractice);
		quizController.setWordList("Months of the year");
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root,800,600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public void topicFeelings(Event event) throws IOException { 

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
		root = loader.load();
		QuizController quizController = loader.getController();
		quizController.checkPractice(this.isPractice);
		quizController.setWordList("Feelings");
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root,800,600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public void topicWeather(Event event) throws IOException { 

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
		root = loader.load();
		QuizController quizController = loader.getController();
		quizController.checkPractice(this.isPractice);
		quizController.setWordList("Weather");
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root,800,600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public void topicWork(Event event) throws IOException { 

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
		root = loader.load();
		QuizController quizController = loader.getController();
		quizController.checkPractice(this.isPractice);
		quizController.setWordList("Work");
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root,800,600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}



	public void switchToMenuFromTopicSelection(Event event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root,800,600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}
}
