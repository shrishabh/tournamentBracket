package application;
	
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {
	private static List<Challenger> list = new ArrayList<Challenger>();
	private static int[] allowedTeams = {0,1,2,4,8,16,32,64};
	
	
	@Override
	public void start(Stage primaryStage) {
			primaryStage.setTitle("Tournament Bracket");
			primaryStage.show();
			primaryStage.setTitle("Stage and Scene");
			BorderPane bPane = new BorderPane();
			/////////
			 Label teams[] = new Label[list.size()];
			 for(int i = 0; i<teams.length; i++)
			 {
				 teams[i].setText(list.get(i).getName());
			 }
			 VBox teamsScore[] = new VBox[teams.length];
			 for(int i = 0; i<teamsScore.length; i++)
			 {
				 teamsScore[i] = new VBox();
				 teamsScore[i].getChildren().addAll(teams[i], new TextField());
			 }
			 GridPane grid = new GridPane();
			 if(teams.length == 1)
			 {
				 grid.add(teamsScore[0], 0, 0);
			 }
			 else if(teams.length == 2)
			 {
				 grid.add(teamsScore[0], 0, 0);
				 grid.add(new Button(), 0, 1);
				 grid.add(teamsScore[1], 0, 2);
			 }
			 else if(teams.length == 4)
			 {
				 grid.add(teamsScore[0], 0, 0);
				 grid.add(new Button(), 0, 1);
				 grid.add(teamsScore[3], 0, 2);
				 grid.add(teamsScore[1], 0, 3);
				 grid.add(new Button(), 0, 4);
				 grid.add(teamsScore[2], 0, 5);
			 }
			 else if(teams.length == 8)
			 {
				 
			 }
			 else if(teams.length == 16)
			 {
				 
			 }
			 else if(teams.length == 32)
			 {
				 
			 }
			 /////////
			
			primaryStage.setTitle("TextField");
			VBox hbox = new VBox();
			Scene scene = new Scene(hbox, 50, 50, Color.DARKGRAY);
			
			Label label = new Label();
			label.setAlignment(Pos.CENTER);
			label.setMinHeight(25);
			label.setText("Team 1 ");
			
			Label label2 = new Label();
			label2.setAlignment(Pos.CENTER);
			label2.setMinHeight(25);
			label2.setText("Team 2 ");
			
			TextField input = new TextField();
			TextField input2 = new TextField();
			input.setMaxHeight(20); input.setMaxWidth(75);
			input2.setMaxHeight(20); input2.setMaxWidth(75);
			input.setPromptText("Score");
			input2.setPromptText("Score");
			input.setFocusTraversable(false);
			input2.setFocusTraversable(false);
			
			
			Button submitButton = new Button();
			submitButton.setText("Submit");
			submitButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					int score1 = Integer.parseInt(input.getText());
					int score2 = Integer.parseInt(input.getText()); // TODO fix this
					
					System.out.println(score1>score2);
				}
			});
	
			hbox.getChildren().addAll(label, label2, input, input2, submitButton);
	
	primaryStage.setScene(scene);
	primaryStage.show();
	}

	private static void processFile(String fileName) {
		File inputFile = null;
		Scanner sc = null;
		
		try {
			inputFile = new File(fileName);
			sc = new Scanner(inputFile);
			int seed = 1;
			Challenger newChallenger;
			while(sc.hasNextLine()) {
				String name = sc.nextLine();
				newChallenger = new Challenger(name,seed);
				list.add(newChallenger);
				seed = seed + 1;
			}
			sc.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
	}
	
	private static boolean checkForNumTeams(){
		int numTeams = list.size();
		boolean checkPassed = false;
		for (int num : allowedTeams) {
			if (num == numTeams) {
				checkPassed = true;
				break;
			}
		}
		return checkPassed;
		
	}
	
	private Challenger getWinner(Challenger team1, Challenger team2) {
		if (team1.getScore() > team2.getScore()) {
			return team1;
		}
		else if (team1.getScore() < team2.getScore()) {
			return team2;
		}
		else {
			if (team1.getSeed() > team2.getSeed()) {
				return team1;
			}
			else return team2;
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		/* String fileName = "/Users/rkhandelwal/Rishabh/Acads/programming3/Assignments/tournamentBracket/src/filename.txt";
		processFile(fileName);
		if(checkForNumTeams()){
			for (Challenger temp : list) {
				System.out.println(temp.getName());
			}
			launch();
		}
		else {
			System.out.println("Please enter valid number of teams");
			System.exit(-1);
		} */
		launch();
//		String fileName = "/Users/rkhandelwal/Rishabh/Acads/programming3/Assignments/tournamentBracket/src/filename.txt";
//		processFile(fileName);
//		if(checkForNumTeams()){
//			for (Challenger temp : list) {
//				System.out.println(temp.getName());
//			}
//			launch();
//		}
//		else {
//			System.out.println("Please enter valid number of teams");
//			System.exit(-1);
//		}

	}
}