package application;
	
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

//Me, Natalia, and Juliano did this project with Ali
public class Main extends Application {
	
	ArrayList<Run> runs = new ArrayList<>();
	
	
	private int counter = 0;
	private int randomNumber = 0;
	VBox root = new VBox(); // a container of all variable, put Vbox here can access all the things in VBox (like text label)
	
	
	private void newGame()
	{
		counter = 0;
		randomNumber = (int) (1 + Math.random()*100);
		
		// I want to clear the text in the screen Text
		for( Node n : root.getChildren())
		{
			if(n instanceof TextField)
			{
				//((TextField) n).setText(arg0);
				((TextField)n).setText("Type in something");
			}
			else if (n instanceof Text && !((Text)n).getText().equals("Please enter your guess!"))
			{				
				((Text)n).setText("");
			}
			
		}
		
		System.out.println("Random = " + randomNumber +" with counter = " + counter);
		
	}
	
	
	private Node getField(int mark, Color c) // TextField: 1, text : 2, .....
	{
		//textField and text label is inside other method, to get access create a method, pass it to variable textField/text label and other method can access these variable from this method instead
		
		for( Node n : root.getChildren())
		{
			if(n instanceof TextField && mark == 1)
				return n;
			else if (n instanceof Text && mark == 2 && ((Text)n).getFill() == c)
				return n;
				
		}
		return null;
		
	}
	
	private void playGame()
	{
		int input = Integer.parseInt(((TextField)getField(1, null)).getText());
		Text x = (Text)getField(2, Color.RED);
		if(input == randomNumber)
		{			
			counter++;
			x.setText("You won with " + counter +" attempts");
			
			runs.add(new Run(randomNumber, counter));
			
		}
		else if (input > randomNumber)
		{
			x.setText("Too High");
			counter++;
		}
		else
		{
			x.setText("Too Low");
			counter++;
		}
		
		if(counter >= 10)
		{
			x.setText("Try another time!!!! Solution was " + randomNumber +". Start new game");
			runs.add(new Run(randomNumber, counter));
		}
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			newGame();
					
			
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			Button newGameBtn = new Button("New Game");
			//Button resetBtn = new Button("Reset Game");
			Button saveScoreBtn = new Button("Save Score");
			Button loadScoreBtn = new Button("Load Score");
			
			Text lable = new Text("Please enter your guess!");
			TextField inputField = new TextField("Type in something");
			
			Text screen = new Text(".................................");
			
			Button playBtn = new Button("Play");
			root.getChildren().addAll(newGameBtn, saveScoreBtn, loadScoreBtn, lable, inputField, playBtn, screen);
			
			
					
			newGameBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> newGame());
			playBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> playGame());
			
			inputField.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> inputField.selectAll());
			
			screen.setFill(Color.RED);
			
			
			saveScoreBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> saveData());
			
			loadScoreBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loadData());
			
			
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void loadData()
	{
		Text x = (Text)getField(2, Color.RED);
		try {
			
			Scanner sc= new Scanner(new File("gameData.text"));
			while (sc.hasNextInt()) 
			{
			   runs.add(new Run(sc.nextInt(), sc.nextInt()));
			   sc.nextLine();
			}
			sc.close();
			x.setText("data loaded successfuly");			
		} 
		catch (IOException e) {
			x.setText("File failure");
			}
	}
	
	private void saveData()
	{
		Text x = (Text)getField(2, Color.RED);
		
		try
		{	
			java.io.File outfile = new java.io.File("gameData.text");			
			PrintStream out = new PrintStream(outfile);
			for(Run p : runs)
			{
				out.println(p.getRandomNumber()+ " " + p.getAttempts());
			}
			out.close();
			x.setText("data saved successfuly");
			
		} 
		catch (IOException e) {
			x.setText("File failure");
			}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
