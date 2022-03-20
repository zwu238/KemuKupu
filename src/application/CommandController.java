package application;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CommandController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	// Generates bash command
	public String runCommand(String command) {
	    ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);
	 
	    try {
	        Process process = processBuilder.start();
	 
	        //read the output
	        InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
	        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	        String output = null;
	        String realOutput = null;
	        while ((output = bufferedReader.readLine()) != null) {
	        	realOutput = output;
	        }
	 
	        //wait for the process to complete
	        process.waitFor();
	 
	        //close the resources
	        bufferedReader.close();
	        process.destroy();
	        
	        return realOutput;
	 
	    } catch (IOException | InterruptedException e) {
	        e.printStackTrace();
		    return null;
	    }
	}
	
	
}
