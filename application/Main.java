package application;
	
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
<<<<<<< HEAD
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
=======
>>>>>>> f701daf331119b3ae0867d4ada9f5064648beac2
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	private static List<Challenger> list = new ArrayList<Challenger>();
	private static int[] allowedTeams = {0,1,2,4,8,16,32,64};
	
	
	/***
	 * Function to get number of columns based on how many rounds there will be. This
	 * will be used to set constraints on the column size
	 * @return number of columns required for the complete matchup (# of rounds)
	 */
	private int getNumCol() {
		int length = list.size();
		if (length <= 2) return 2;
		else if (length == 4 ) return 3 ;
		else if (length == 8) return 4;
		else if (length == 16) return 5;
		else if (length == 32) return 6;
		return 7;
	}

	/****
	 * Function to get the fixtures given the number of teams
	 * @param numTeams the number of teams participating in the tournament
	 * @return an array containing fixtures in order
	 */
	private List<Integer> getMatchups(int numTeams){
		List<Integer> matchupList = new ArrayList<Integer>();
		matchupList.add(1);
		matchupList.add(2);
		//System.out.println(matchupList.indexOf(1));
		int lenList = 2;
		int newLenList;
		int counter;
		while (lenList != numTeams) {
			matchupList = modifyList(matchupList,lenList);
			newLenList = 2*lenList;
			counter = 1;
			
			for (int i = newLenList; i > lenList ; i--) {
				//System.out.println((i) + "  " +(counter)+ "  "+matchupList.indexOf(counter));
				matchupList.set(matchupList.indexOf(counter)+1, i);
				counter = counter + 1;
				
			}
			lenList = newLenList;
			if(lenList == numTeams) break;
		}
		
		return matchupList;
	}
	
	/***
	 * Helper function to get the fixtures. 
	 */
	
	private List<Integer> modifyList(List<Integer> matchupList,int lenList){
		List<Integer> modifiedList = new ArrayList<Integer>();
		//for (int i:matchupList) System.out.println(i);
		for (int i = 1; i < lenList+1; i++) {
			modifiedList.add(matchupList.get(i-1));
			modifiedList.add(-100);
		}
		//for (int i:modifiedList) System.out.println(i);
		return modifiedList;
	}
	/***
	 * Get the challenger given the name
	 * @param name of the team
	 * @return Challenger with the same team
	 */
	private Challenger getChallengerFromName(String name) {
		for (Challenger temp:list) {
			if (temp.getName().equals(name)) return temp;
		}
		return null;
	}
	
	/***
	 * Method to create a label with given settings
	 * @param name name of the label
	 * @return the label
	 */
	private Label getLabel(String name) {
		Label newLabel = new Label(name);
		newLabel.setTextFill(Color.CRIMSON);
		newLabel.setFont(Font.font("Arial", 16));
		return newLabel;
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
<<<<<<< HEAD
		

=======
>>>>>>> f701daf331119b3ae0867d4ada9f5064648beac2
		int numTeams = list.size();
		List<Integer> matchup = getMatchups(numTeams);
		for(int i : matchup) {
			System.out.print(i + " "); // TODO Delete
		}
		Iterator<Integer> itr = matchup.iterator();
		
		 Label teams[] = new Label[list.size()];
		 for(int i = 0; i<teams.length; i++)
		 {				
			 teams[i]= getLabel(list.get(i).getName());
			 
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
		 
		 Button[] submitButtons = new Button[teams.length/2];
		 for(int i=0; i< submitButtons.length; i++)
		 {
		     Button b = new Button("Submit");
		     b.setId(new Integer(i).toString());
             GridPane.setHalignment(b, HPos.CENTER);
             b.setOnAction(new EventHandler<ActionEvent>() {
                 
                 @Override
                 public void handle(ActionEvent event) {
                     TextField t = (TextField) teamsScore[0].getChildren().get(1); // prints team's score
                     TextField t2 = (TextField) teamsScore[1].getChildren().get(1); // input functionality for milestone 3
                     System.out.println(Integer.parseInt(t.getText()) + " " + Integer.parseInt(t2.getText()));
                 }
             });
             b.setPrefHeight(5);
			 submitButtons[i] = b;
		 }

		GridPane grid = new GridPane();
		grid.setId("pane");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        //grid.setGridLinesVisible(true);
        
		Scene scene = new Scene(grid, 800, 600, Color.DARKGRAY);
		scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        
    	if(teams.length > 4)
    	{
    		ScrollBar sc = new ScrollBar();
    		grid.getChildren().add(sc);
    		sc.setMin(0);
    		sc.setMax(200*teams.length);
    		sc.setPrefHeight(scene.getHeight());
    		sc.setValue(0);
    		sc.setLayoutX(scene.getWidth()-sc.getWidth());
    		sc.setOrientation(Orientation.VERTICAL);
//    		sc.setBlockIncrement(100);
//    		sc.setUnitIncrement(200);
            sc.valueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                        Number old_val, Number new_val) {
                    grid.setLayoutY(-new_val.doubleValue());
                }
            });
    	}
        
        grid.setGridLinesVisible(true);
       
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
            	rowConst.setPrefHeight(20);
            	lstCount = i;
            }
            else {
            		if (i == lstCount + 3) rowConst.setPrefHeight(20);
            		else rowConst.setPercentHeight(70.0/list.size());
            }
            grid.getRowConstraints().add(rowConst);         
        }
		primaryStage.setTitle("Tournament Bracket");
		 if(teams.length == 1)
		 {
			 grid.add(teamsScore[0], 0, 0);
		 }
		 else if(teams.length >= 2)  // TODO failing in this part
		 {
			 int i = 0;
			 while(itr.hasNext())
			 {
			     
		
				 int teamA = itr.next()-1;
				 int teamB = itr.next()-1;
				 grid.add(teamsScore[teamA], 0, i);
				 grid.add(submitButtons[i/3], 0, i+1);

				 GridPane.setHalignment(submitButtons[i/3], HPos.CENTER);
//				 submitButtons[i/2].setOnAction(new EventHandler<ActionEvent>(){
//
//					@Override
//					public void handle(ActionEvent arg0) {
//						// TODO Auto-generated method stub
//						Label teamName1 = (Label) teamsScore[i].getChildren().get(0);
//						TextField t1 = (TextField) teamsScore[i].getChildren().get(1);
//						int score1 = Integer.parseInt(t1.getText());
//						
//						Label teamName2 = (Label) teamsScore[i+2].getChildren().get(0);
//						TextField t2 = (TextField) teamsScore[i+2].getChildren().get(1);
//						int score2 = Integer.parseInt(t2.getText());
//						Challenger team1 = getChallengerFromName(teamName1.getText());
//						Challenger team2 = getChallengerFromName(teamName2.getText());
//						
//						Challenger winner = getWinner(team1,team2);
//						Label newLable = getLabel(winner.getName());
//						HBox newHBox = new HBox(10);
//						TextField newScore =  new TextField();
//						newScore.setPrefWidth(50);
//						newHBox.getChildren().addAll(newLable, newScore);
//						newHBox.setAlignment(Pos.CENTER);
//						
//						grid.add(newHBox, 1, i+1, 1,2);
//					}
//					 
//				 });
				 
				 grid.add(teamsScore[teamB], 0, i+2);
				 System.out.println("i : "+ i);
				 i = i+3;
			 }
		 }
//		 else if(teams.length == 4)
//		 {
//			 grid.add(teamsScore[0], 0, 0);
//			 Button b1 = new Button("Submit Scores");
//			 b1.setId("1");
//			 grid.add(b1, 0, 1);
//			 GridPane.setHalignment(b1,HPos.CENTER);
//			 b1.setOnAction(new EventHandler<ActionEvent>() {
//
//					@Override
//					public void handle(ActionEvent event) {
//						Label teamName1 = (Label) teamsScore[0].getChildren().get(0);
//						TextField t1 = (TextField) teamsScore[0].getChildren().get(1);
//						int score1 = Integer.parseInt(t1.getText());
//						
//						Label teamName2 = (Label) teamsScore[3].getChildren().get(0);
//						TextField t2 = (TextField) teamsScore[3].getChildren().get(1);
//						int score2 = Integer.parseInt(t2.getText());
//						Challenger team1 = getChallengerFromName(teamName1.getText());
//						Challenger team2 = getChallengerFromName(teamName2.getText());
//						
//						Challenger winner = getWinner(team1,team2);
//						Label newLable = getLabel(winner.getName());
//						HBox newHBox = new HBox(10);
//						TextField newScore =  new TextField();
//						newScore.setPrefWidth(50);
//						newHBox.getChildren().addAll(newLable, newScore);
//						newHBox.setAlignment(Pos.CENTER);
//						
//						grid.add(newHBox, 1, 0, 1,2);
//						
//						//System.out.println("Score 1 is:" + score1);
//					}
//				});
//			 grid.add(teamsScore[3], 0, 2);
//			 grid.add(teamsScore[1], 0, 3);
//			 Button b2 = new Button("Submit Scores");
//			 grid.add(b2, 0, 4);
//			 GridPane.setHalignment(b2,HPos.CENTER);
//			 grid.add(teamsScore[2], 0, 5);
//			 
//			 grid.add(teamsScore[2], 1, 0,1,2);
//			 Button b3 = new Button("Submit Scores");
//			 grid.add(b3, 1, );
//			 grid.setHalignment(b3,HPos.CENTER);
//		 }
<<<<<<< HEAD

=======
		 Group root = new Group();
		 Scene scene = new Scene(root, 800, 600, Color.DARKGRAY);
		 ScrollBar sc = new ScrollBar();
		 root.getChildren().addAll(grid, sc);
		 
	        
	        sc.setLayoutX(scene.getWidth()-sc.getWidth());
	        sc.setOrientation(Orientation.VERTICAL);
	        sc.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                   grid.setLayoutY(-new_val.doubleValue());
	            }
	        });
		 scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
>>>>>>> f701daf331119b3ae0867d4ada9f5064648beac2
		 //primaryStage.setScene(scene);
		 //primaryStage.show();
//		    VBox vbox = new VBox(10);
//	        
//	        
//	        Label label = new Label();
//	        label.setAlignment(Pos.CENTER);
//	        label.setMinHeight(25);
//	        label.setText("Team 1 ");
//	        
//	        Label label2 = new Label();
//	        label2.setAlignment(Pos.CENTER);
//	        label2.setMinHeight(25);
//	        label2.setText("Team 2 ");
//	        
//	        TextField input = new TextField();
//	        TextField input2 = new TextField();
//	        input.setMaxHeight(20); input.setMaxWidth(75);
//	        input2.setMaxHeight(20); input2.setMaxWidth(75);
//	        input.setPromptText("Score");
//	        input2.setPromptText("Score");
//	        input.setFocusTraversable(false);
//	        input2.setFocusTraversable(false);
//	        
//	        
//	        Button submitButton = new Button();
//	        submitButton.setText("Submit");
//	        submitButton.setOnAction(new EventHandler<ActionEvent>() {
//
//	            @Override
//	            public void handle(ActionEvent event) {
//	                int score1 = Integer.parseInt(input.getText());
//	                int score2 = Integer.parseInt(input.getText()); // TODO fix this
//	                
//	                System.out.println(score1>score2);
//	            }
//	        });
//
//	        vbox.getChildren().addAll(label, label2, input, input2, submitButton);
//
//	

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