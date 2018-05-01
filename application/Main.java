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
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
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
		if (length <= 2) return 3;
		else if (length == 4 ) return 5 ;
		else if (length == 8) return 7;
		else if (length == 16) return 9;
		else if (length == 32) return 11;
		return 13;
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
		newLabel.setFont(Font.font("Arial", 18));
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
		int numTeams = list.size();
		List<Integer> matchup = getMatchups(numTeams);
//		for(int i : matchup) {
//			System.out.print(i + " "); // TODO Delete
//		}
		Iterator<Integer> itr = matchup.iterator();
		Iterator<Integer> itr2 = matchup.iterator();
		
		 Label teams[] = new Label[list.size()];
		 for(int i = 0; i<teams.length; i++)
		 {				
			 teams[i]= getLabel(list.get(i).getName());
			 teams[i].setPrefSize(80, 10);
			 //System.out.println(teams[i].getText());

			 
		 }
		 int numCol = getNumCol();
		 
		 
		 HBox teamsScore[] = new HBox[teams.length];
		 TextField score;
		 final int[] matchupPos = new int[teams.length+1];  // last one will be unused unless teams.length == 1
		 if (teams.length == 1) {
		     matchupPos[0] = 1;
		     matchupPos[1] = 1;
		 }
		 else if (teams.length >= 2) {
		     int count = 0;
		     while (itr2.hasNext()) {
		         matchupPos[count] = itr2.next()-1;
		         count++;
		     }
		 }
		 
		 for(int i = 0; i<teamsScore.length; i++)
		 {
			 teamsScore[i] = new HBox(10);
			 score =  new TextField();
			 score.setPrefWidth(50);
			 teamsScore[i].getChildren().addAll(teams[i], score);

			 //teamsScore[i].getChildren().addAll(teams[i]);
			 teamsScore[i].setAlignment(Pos.BASELINE_LEFT);
			 teamsScore[i].setAlignment(Pos.CENTER);
		 }
		 class ButtonList {
		     Button b;
		     
		     private ButtonList(int pos) {
		         b = new Button("Submit");
		         b.setId(new Integer(pos/2).toString());
		         GridPane.setHalignment(b, HPos.CENTER);
		         b.setOnAction(new EventHandler<ActionEvent>() {
	                 
	                 @Override
	                 public void handle(ActionEvent event) {
	                     TextField t = (TextField) teamsScore[matchupPos[pos]].getChildren().get(1); // TODO fix to print team's score
	                     TextField t2 = (TextField) teamsScore[matchupPos[pos+1]].getChildren().get(1); // input functionality for milestone 3
	                     list.get(matchupPos[pos]).setScore(Integer.parseInt(t.getText()));
	                     list.get(matchupPos[pos+1]).setScore(Integer.parseInt(t2.getText()));
	                     System.out.println(Integer.parseInt(t.getText()) + " " + Integer.parseInt(t2.getText()));
	                 }
	             });
		         b.setPrefHeight(5);
		     }
		 }
		 
		 ButtonList[] submitButtons = new ButtonList[teams.length/2];
		 for(int i=0; i< submitButtons.length; i++)   
		 {
		     submitButtons[i] = new ButtonList(2*i);
		 }

		GridPane grid = new GridPane();
		grid.setId("pane");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        //grid.setGridLinesVisible(true);
        int subCol = numCol - list.size();
        for (int i = 0; i < numCol; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            if (i%2 == 1) colConst.setPercentWidth(30.0 / subCol);
            else colConst.setPercentWidth(100.0 / list.size());
            grid.getColumnConstraints().add(colConst);
        }
//        int numRows = list.size() + list.size()/2;
//        int lstCount = 0;
        for (int i = 0; i < numCol; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCol);
            grid.getColumnConstraints().add(colConst);
        }
        

        
        for (int i = 0; i < list.size(); i++) {
          RowConstraints rowConst = new RowConstraints(); 
          rowConst.setPercentHeight(100.0/list.size());
          grid.getRowConstraints().add(rowConst);     
      }
		primaryStage.setTitle("Tournament Bracket");
		 if(teams.length == 1)
		 {
			 grid.add(teamsScore[0], 0, 0);
		 }
		 else if(teams.length >= 2) 
		 {
			 int i = 0; int k = 0;
			 while(itr.hasNext())
			 {
			     
				 int teamA = itr.next()-1;
				 int teamB = itr.next()-1;
				 grid.add(teamsScore[teamA], 0, k);
				 grid.add(submitButtons[i/2].b, 1, k,1,2);
				

				 GridPane.setHalignment(submitButtons[i/3].b, HPos.CENTER);

				 
				 grid.add(teamsScore[teamB], 0, k+1);
				 i = i+2;
				 k = k+3;
			 }
		 }


		 Group root = new Group();
		 Scene scene = new Scene(root, 1000, 800, Color.DARKGRAY);
		 ScrollBar sc = new ScrollBar();
		 root.getChildren().addAll(grid, sc);
	     sc.setPrefSize(20, scene.getHeight());
	     sc.setLayoutX(scene.getWidth()-sc.getWidth());
	     sc.setOrientation(Orientation.VERTICAL);
	     sc.valueProperty().addListener(new ChangeListener<Number>() {
	    	 public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                   grid.setLayoutY(-new_val.doubleValue()*list.size()/3);
	            }
	     });
		 scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());


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