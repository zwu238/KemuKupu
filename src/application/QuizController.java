package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class QuizController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private static String wordList;
	private static List<String> spellingWords;
	private static List<String> answerState  = new ArrayList<>();
	private static List<Integer> answerTime  = new ArrayList<>();
	
	@FXML
	private Label topicLabel;
	@FXML
	private Label questionLabel;
	@FXML
	private Label encouragingMessageLabel;
	@FXML
	private Label hintLabel;
	@FXML
	private Label practiceLabel;
	@FXML
	private Label answerLabel;
	@FXML
	private TextField inputTextfield;
	@FXML
	private Slider slider;
	@FXML
	private Text score;
	@FXML
	private Button submitButton;
	@FXML
	private Button skipButton;
	@FXML
	private Button replayButton;
	@FXML
	private Text timer;
	@FXML
	private Text bonus;
	@FXML 
	private AnchorPane macronPlane;
	@FXML 
	private AnchorPane answerPlane;
	
	private int questionNumber = 1;
	private Boolean firstAttempt = true;
	private boolean isPractice;

	int startTime = 0; //Relative time to calculate points
	int totalTime = 0;
	public static Timer update;

	// This controller contains the bash script generator
	CommandController controller = new CommandController();

	// Sound effects controller
	MusicController buzzer = MusicController.getInstance();
	
	@FXML
	public void initialize() throws InterruptedException, IOException {
		// Background timer that updates per second
		update = new Timer();
		update.scheduleAtFixedRate(screenTimerTask, 0, 1000);
		macronPlane.setVisible(false);
		answerPlane.setVisible(false);
	}

	public void checkPractice(boolean isPractice) {
		this.isPractice = isPractice;
	}


	public void setWordList(String words) throws IOException {
		wordList = words;
		topicLabel.setText(words);
		spellingWords = topicToWords();
		giveUnderScoreOfWord();
		replaySound();
		answerState  = new ArrayList<>();
		answerTime  = new ArrayList<>();
	}

	public void switchToReward(Event event) throws IOException {
		update.cancel();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Reward.fxml"));
		root = loader.load();

		RewardController rewardController = loader.getController();
		rewardController.setScore(score.getText(), controller.runCommand("cat high_score.txt"));
		rewardController.setTime(totalTime);
		rewardController.getStats(spellingWords, answerState, answerTime);;
		

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root,800,600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public void switchToMenuFromGame(Event event) throws IOException {

		update.cancel();
		root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root,800,600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public void skipQuestion(Event event) throws IOException {
		firstAttempt = true;
		questionNumber += 1;
		encouragingMessageLabel.setText("");
		answerPlane.setVisible(false);
		answerState.add("skipped");
		answerTime.add(-1);
		
		replaySound();
		setQuestion();
		
	
		if (questionNumber>spellingWords.size()) {	
			switchToReward(event);
		}
		giveUnderScoreOfWord();
		startTime = totalTime;

	}

	public String encouragingMessage() {

		List<String> encouragingMessageList = new ArrayList<>();
		// message used in the quiz screen
		encouragingMessageList.add("You can do it!");
		encouragingMessageList.add("Keep trying!");
		encouragingMessageList.add("You got this!");

		Random rand = new Random();
		int index = rand.nextInt(encouragingMessageList.size());

		String encouragingSentence = encouragingMessageList.get(index);
		return encouragingSentence;

	}


	public void replaySound() throws IOException {
		PlaySound task = new PlaySound();

		new Thread(task).start();
	}

	// Generate words for chosen topic
	public List<String> topicToWords() throws IOException {


		File file = new File("src/application/words/" + wordList);
		List<String> spellingList = new ArrayList<>();
		List<String> spellingWords = new ArrayList<>();

		if (!file.exists()) {
			return spellingWords;
		}

		Scanner sc = new Scanner(file);
		while(sc.hasNextLine()) {
			spellingList.add(sc.nextLine());
		}

		if(isPractice) {
			practiceLabel.setText("Practice Mode");
			return spellingList;

		}

		Random rand = new Random();
		int size = spellingList.size();
		int numOfWords = 5;

		for (int i = 0; i<numOfWords;i++) {
			int index = rand.nextInt(size);
			spellingWords.add(spellingList.get(index));
			spellingList.remove(index);
			size--;
		}

		return spellingWords;
	} 


	public void submit(ActionEvent event) throws FileNotFoundException, IOException {
		String userInput = inputTextfield.getText();
		encouragingMessageLabel.setText("");
		String result = quizOutcome(userInput);
		inputTextfield.clear();

		if (firstAttempt) { // first attempt options
			answerPlane.setVisible(false);
			if (result.equals("correct"))  { 
				firstAttempt = true;
				updateScore();
				answerState.add(result);
				answerTime.add(totalTime - startTime);
				if (questionNumber == spellingWords.size()) {
					switchToReward(event);
				}
				questionNumber += 1;
				setQuestion();
				buzzer.correct();
				replaySound();
				giveUnderScoreOfWord();

				startTime = totalTime;
			} 
			else if (result.equals("incorrect")){
				firstAttempt = false;
				buzzer.incorrect();
				encouragingMessageLabel.setText(encouragingMessage());
				giveHint();


			}
		} else if ((!firstAttempt)) { // second attempt options
			firstAttempt = true;
			if (result.equals("correct"))  {
				questionNumber += 1;
				setQuestion();
				updateScore();
				answerState.add(result);
				answerTime.add(totalTime - startTime);
				replaySound();
				buzzer.correct();
				startTime = totalTime;

				// if questions go over 5, quiz ends.
				if (questionNumber>spellingWords.size()) {	
					switchToReward(event);
				}
				giveUnderScoreOfWord();

			} 
			else if (result.equals("incorrect")){
				answerState.add(result);
				answerTime.add(totalTime - startTime);
				questionNumber += 1;
				showAnswer();
				setQuestion();
				replaySound();
				buzzer.incorrect();
				if (questionNumber>spellingWords.size()) {	
					switchToReward(event);
				}
				giveUnderScoreOfWord();
				startTime = totalTime;
				
			}
		}
	}
	
	public void showAnswer() {
		if (isPractice) {
			answerPlane.setVisible(true);
			answerLabel.setText("Correct Answer: "+spellingWords.get(questionNumber-2));
		} else {
			answerPlane.setVisible(false);
		}
	}

	public void giveHint() {
		// give the user a random character in the word
		String word = spellingWords.get(questionNumber - 1);
		int size = word.length();		

		Random rand = new Random();
		int index = rand.nextInt(size);

		while (word.charAt(index) == ' ') {
			index = rand.nextInt(size);
		}

		String hint = "";
		for (int i = 0; i < word.length(); i++) {
			if (i == index) {
				hint = hint.concat(Character.toString(word.charAt(index)));
			} else if (word.charAt(i) == ' '){
				hint = hint.concat("  ");
			} else {
				hint = hint.concat("_ ");
			}
		}
		hintLabel.setText(hint);

	}

	public void giveUnderScoreOfWord() {
		// Each letter is printed out as an underscore
		if (questionNumber>spellingWords.size()) {
			hintLabel.setText("");
		} else {
			String word = spellingWords.get(questionNumber - 1);
			int size = word.length();		

			String clue = "";
			for (int i = 0; i < word.length(); i++) {
				if (word.charAt(i) == ' '){
					clue = clue.concat("  ");
				} else {
					clue = clue.concat("_ ");
				}
			}
			hintLabel.setText(clue);
		}
	}

	public boolean checkWord(String userInput, String word) { 
		// Compares two Strings
		if (userInput.toLowerCase().equals((word.strip()).toLowerCase())) {
			return true;
		} else return false;

	}

	// Update question text in layout file
	public void setQuestion() { 
		questionLabel.setText(Integer.toString(questionNumber));
	}

	public String quizOutcome(String userInput) throws FileNotFoundException, IOException { 
		// Returns whether the answer is correct
		String result = null;
		String word = spellingWords.get(questionNumber - 1);

		if (checkWord(userInput, word)) {
			result = "correct";

		}  else if (!checkWord(userInput, word)) {
			result = "incorrect";
		}
		return result;
	}

	public void updateScore() {
		Integer scoreInt = Integer.parseInt(score.getText());
		int diffTime = totalTime - startTime;
		int extraScore = 0;

		// Scoring system
		if (diffTime > 15) {
			extraScore = 50;
		} else if (diffTime > 11) {
			extraScore = 200;
		} else if (diffTime > 7) {
			extraScore = 500;
		} else {
			extraScore = 1000;
		}
		FadeTransition fade = new FadeTransition();
		
		score.setText(String.valueOf(scoreInt + extraScore));
		bonus.setText("+" + String.valueOf(extraScore));
		
		// Animation for score of answered question
		fade.setNode(bonus);
		fade.setDuration(Duration.millis(1500));
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.setOnFinished((event) -> {
			bonus.setText("");
			bonus.setOpacity(1.0);
		});
		fade.play();
		
		firstAttempt = true;
	}

	public void addMacron(ActionEvent event) {
		// add macron at caret
		String textFieldContent = inputTextfield.getText();
		String macron = ((Button)event.getSource()).getText();
		int caretPos = inputTextfield.getCaretPosition();
		inputTextfield.setText(textFieldContent.substring(0, caretPos) + macron + 
				textFieldContent.substring(caretPos, textFieldContent.length()));

		inputTextfield.positionCaret(caretPos + 1);
	}

	public void typeMacron(KeyEvent event) {
		// type macron of previous vowel if user pressed alt


		if (event.getCode().equals(KeyCode.ALT)) {
			String textFieldContent = inputTextfield.getText();
			int caretPos = inputTextfield.getCaretPosition();
			if (caretPos > 0) {
				String macron = textFieldContent.substring(caretPos-1 ,caretPos);

				switch (macron) {
				case "a":  
					macron = "ā";
					break;
				case "e":  
					macron = "ē";
					break;
				case "i":  
					macron = "ī";
					break;
				case "o":  
					macron = "ō";
					break;
				case "u":  
					macron = "ū";
					break;

				}

				inputTextfield.setText(textFieldContent.substring(0, caretPos-1) + macron + 
						textFieldContent.substring(caretPos, textFieldContent.length()));

				inputTextfield.positionCaret(caretPos);
			}
		}

	}
	
	public void showMacronInfo()  {
		// explains how to type macrons
		if(!macronPlane.isVisible()) {
			
			macronPlane.setVisible(true);
		} else {
			macronPlane.setVisible(false);
		}
	}
		
	
	
	
	class PlaySound extends Task<Integer> {

		@Override
		protected Integer call() throws Exception {
			String word = spellingWords.get(questionNumber - 1);
			// sed -i 'Ns/.... where N is the line number to change
			// Line number 3 is the speed and 6 is the word

			// Change speed 
			controller.runCommand("sed -i '3s/.*/" + slider.getValue() + "/' word.scm");
			// change word file to be word we want to speak
			controller.runCommand("sed -i '9s/.*/" + word + "/' word.scm");
			// speak word
			controller.runCommand("festival -b word.scm");
			return null;
		}

	}
	
	//	Timer text is updated at a regular rate
	TimerTask screenTimerTask = new TimerTask()
	{
		public void run()
		{
			totalTime = totalTime + 1;
			timer.setText(Integer.toString(totalTime-1));
		}

	};
}
