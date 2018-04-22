package application;
	
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class Main extends Application {
	private static List<Challenger> list = new ArrayList<Challenger>();
	private static int[] allowedTeams = {0,1,2,4,8,16,32,64};
	
	private int getNumCol() {
		int length = list.size();
		if (length <= 2) return 2;
		else if (length == 4 ) return 3;
		else if (length == 8) return 4;
		return 5;
	}

	private Challenger getChallengerFromName(String name) {
		for (Challenger temp:list) {
			if (temp.getName().equals(name)) return temp;
		}
		return null;
	}
	
	@Override
	public void start(Stage primaryStage) {
			 //Testing a piece of code - Rishabh
//		Screen screen = Screen.getPrimary();
//        Rectangle2D bounds = screen.getVisualBounds();
//
//        primaryStage.setX(bounds.getMinX());
//        primaryStage.setY(bounds.getMinY());
//        primaryStage.setWidth(bounds.getWidth());
//        primaryStage.setHeight(bounds.getHeight());
		 Label teams[] = new Label[list.size()];
		 for(int i = 0; i<teams.length; i++)
		 {				
			 teams[i]= new Label(list.get(i).getName());
			 teams[i].setTextFill(Color.CRIMSON);
			 teams[i].setFont(Font.font("Arial",23));
			 
		 }

		 int numCol = getNumCol();
		 
		 
		 HBox teamsScore[] = new HBox[teams.length];
		 TextField score;
		 for(int i = 0; i<teamsScore.length; i++)
		 {
			 teamsScore[i] = new HBox(10);
			 score =  new TextField();
			 score.setPrefWidth(50);
			 teamsScore[i].getChildren().addAll(teams[i], score);
			 teamsScore[i].setAlignment(Pos.CENTER);
		 }
		GridPane grid = new GridPane();
		grid.setId("pane");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        //grid.setGridLinesVisible(true);
        for (int i = 0; i < numCol; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCol);
            grid.getColumnConstraints().add(colConst);
        }
        
        int numRows = list.size() + list.size()/2;
        int lstCount = 0;
        
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints(); 
            if (i==1) {
            	rowConst.setPrefHeight(50);
            	lstCount = i;
            }
            else {
            		if (i == lstCount + 3) rowConst.setPrefHeight(50);
            		else rowConst.setPercentHeight(80.0/list.size());
            }
            grid.getRowConstraints().add(rowConst);         
        }
        
		primaryStage.setTitle("Tournament Bracket");
		 if(teams.length == 1)
		 {
			 grid.add(teamsScore[0], 0, 0);
		 }
		 else if(teams.length == 2)
		 {
			 grid.add(teamsScore[0], 0, 0);
			 grid.add(new Button("Submit Scores"), 0, 1);
			 grid.add(teamsScore[1], 0, 2);
		 }
		 else if(teams.length == 4)
		 {
			 grid.add(teamsScore[0], 0, 0);
			 Button b1 = new Button("Submit Scores");
			 b1.setId("1");
			 grid.add(b1, 0, 1);
			 grid.setHalignment(b1,HPos.CENTER);
			 b1.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						Label teamName1 = (Label) teamsScore[0].getChildren().get(0);
						TextField t1 = (TextField) teamsScore[0].getChildren().get(1);
						int score1 = Integer.parseInt(t1.getText());
						
						Label teamName2 = (Label) teamsScore[3].getChildren().get(0);
						TextField t2 = (TextField) teamsScore[3].getChildren().get(1);
						int score2 = Integer.parseInt(t2.getText());
						Challenger team1 = getChallengerFromName(teamName1.getText());
						Challenger team2 = getChallengerFromName(teamName2.getText());
						
						Challenger winner = getWinner(team1,team2);
						Label newLable = new Label(winner.getName());
						HBox newHBox = new HBox(10);
						TextField newScore =  new TextField();
						newScore.setPrefWidth(50);
						newHBox.getChildren().addAll(newLable, newScore);
						newHBox.setAlignment(Pos.CENTER);
						
						grid.add(newHBox, 1, 0, 1,2);
						
						//System.out.println("Score 1 is:" + score1);
					}
				});
			 grid.add(teamsScore[3], 0, 2);
			 grid.add(teamsScore[1], 0, 3);
			 Button b2 = new Button("Submit Scores");
			 grid.add(b2, 0, 4);
			 grid.setHalignment(b2,HPos.CENTER);
			 grid.add(teamsScore[2], 0, 5);
			 
//			 grid.add(teamsScore[2], 1, 0,1,2);
//			 Button b3 = new Button("Submit Scores");
//			 grid.add(b3, 1, );
//			 grid.setHalignment(b3,HPos.CENTER);
		 }
		 else if(teams.length == 8)
		 {
			 grid.add(teamsScore[0], 0, 0);
			 grid.add(new Button("Submit Scores"), 0, 1);
			 grid.add(teamsScore[7], 0, 2);
			 grid.add(teamsScore[3], 0, 3);
			 grid.add(new Button("Submit Scores"), 0, 4);
			 grid.add(teamsScore[4], 0, 5);
			 grid.add(teamsScore[1], 0, 6);
			 grid.add(new Button("Submit Scores"), 0, 7);
			 grid.add(teamsScore[6], 0, 8);
			 grid.add(teamsScore[2], 0, 9);
			 grid.add(new Button("Submit Scores"), 0, 10);
			 grid.add(teamsScore[5], 0, 11);
		 }
		 else if(teams.length == 16)
		 {
			 
		 }
		 else if(teams.length == 32)
		 {
			 
		 }
		 Scene scene = new Scene(grid, 800, 600, Color.DARKGRAY);
		 scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
		 //primaryStage.setScene(scene);
		 //primaryStage.show();

		VBox vbox = new VBox(10);
		
		
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

		vbox.getChildren().addAll(label, label2, input, input2, submitButton);


			
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

		String fileName = "challengerList.txt";
		processFile(fileName);
		if(checkForNumTeams()){
			for (Challenger temp : list) {
				//System.out.println(temp.getName());
			}
			launch();
		}
		else {
			System.out.println("Please enter valid number of teams");
			System.exit(-1);
		}
		//launch(args);

	}
}