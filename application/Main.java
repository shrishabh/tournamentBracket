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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	private static List<Challenger> list = new ArrayList<Challenger>();
	private static List<Challenger> winnerList = new ArrayList<Challenger>();
	private static int[] allowedTeams = {0,1,2,4,8,16,32,64};
	private GridPane grid = new GridPane();
	
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
		int lenList = 2;
		int newLenList;
		int counter;
		while (lenList != numTeams) {
			matchupList = modifyList(matchupList,lenList);
			newLenList = 2*lenList;
			counter = 1;
			
			for (int i = newLenList; i > lenList ; i--) {
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
		for (int i = 1; i < lenList+1; i++) {
			modifiedList.add(matchupList.get(i-1));
			modifiedList.add(-100);
		}
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
	
	/**
	 * Creates a placeHolder HBox that contains a label that in the future will have a winning team from the previous round.
	 */
	private HBox createPlaceHolder() {
	    HBox placeholder = new HBox(10);
	    TextField score = new TextField();
	    score.setPrefWidth(50);
        placeholder.getChildren().addAll(getLabel("____"), score);
        placeholder.setAlignment(Pos.CENTER);
        return placeholder;
	}
	/**
	 * Creates a submit button for the 2 teams to the left of the button
	 * @param pos	The buttons row in the grid
	 * @param teamsScore	The HBox which contains the scores and team names
	 * @param matchupPos	The teams match up position array
	 * @param column		The column for the button to be added in the array
	 * @return				The button
	 */
	private Button createButton(int pos, HBox[] teamsScore, int[] matchupPos, int column) {
	    Button b = new Button("Submit");
        b.setId(new Integer(7*pos + 31*column).toString());
        GridPane.setHalignment(b, HPos.CENTER);
        b.setPrefHeight(5);
        b.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                int rowPos = 0;
                ArrayList<Integer> rowListText = getNodeRowsFromColumn(grid, column+2);
                ArrayList<Integer> rowListButton = getNodeRowsFromColumn(grid, column+1);
                
                rowPos = GridPane.getRowIndex(b);
                

                for (int i : rowListText) {
                    System.out.print(i + " ");
                }
                System.out.println();
                for (int i : rowListButton) {
                    System.out.print(i+ " ");
                }
                System.out.println();
                System.out.println(rowPos);
                for (int i = 0; i < rowListButton.size(); i++) {
                    if (rowListButton.get(i) == rowPos) {
                        rowPos = rowListText.get(i);
                        break;
                    }
                }
                System.out.println(rowPos + "\n");
                TextField t = (TextField) teamsScore[matchupPos[pos]].getChildren().get(1); // TODO fix to print team's score
                TextField t2 = (TextField) teamsScore[matchupPos[pos+1]].getChildren().get(1); // input functionality for milestone 3

                int score = Integer.parseInt(t.getText());
                int score2 = Integer.parseInt(t2.getText());

                if (score != score2) {
                    t.setDisable(true);
                    t2.setDisable(true);
                    b.setDisable(true);
                    list.get(matchupPos[pos]).setScore(score);
                    list.get(matchupPos[pos+1]).setScore(score2);
                    Challenger winner = getWinner(list.get(matchupPos[pos]), list.get(matchupPos[pos+1]));

                    Label l = (Label) ((HBox) getNodeFromGridPane(grid, column+2, rowPos)).getChildren().get(0); //TODO- get the correct position relative to the button
                    l.setText(winner.getName());


                    if (!winnerList.contains(winner)) // if someone wants to submit a different score
                        winnerList.add(winner);           // then the same winner won't be added multiple times
                    // TODO if the winner changes, replace in winnerList
                } else {
                    System.out.println("Ties are not allowed.  Please input scores again");
                }

            }
        });
        
        return b;
    }
	
	/**
	 * Finds the node at the given coordinates within the gridPane
	 * @param gridPane	The given gridPane
	 * @param col		The column value
	 * @param row		The row value
	 * @return			The node at (row, col)
	 */
	private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row && node != null) {
	            return node;
	        }
	    }
	    System.out.println("node not found"); // TODO delete
	    return null;
	    
	}
	
	private ArrayList<Integer> getNodeRowsFromColumn(GridPane gridPane, int col) {
	    ArrayList<Integer> rowList = new ArrayList<Integer>();
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && node != null) {
                rowList.add(GridPane.getRowIndex(node));
            }
        }
        if (rowList.isEmpty())
            System.out.println("Somethine went wrong");
        return rowList;
        
    }
	
	@Override
	public void start(Stage primaryStage) {
			 //Testing a piece of code - Rishabh
//		Screen screen = Screen.getPrimary();  // TODO delete if unnecessary, I want to clean this code up
//        Rectangle2D bounds = screen.getVisualBounds();
//
//        primaryStage.setX(bounds.getMinX());
//        primaryStage.setY(bounds.getMinY());
//        primaryStage.setWidth(bounds.getWidth());
//        primaryStage.setHeight(bounds.getHeight());
	    
		int numTeams = list.size();
		List<Integer> matchup = getMatchups(numTeams);

		Iterator<Integer> itr = matchup.iterator();
		Iterator<Integer> itr2 = matchup.iterator();
		
		 Label teams[] = new Label[list.size()];
		 for(int i = 0; i<teams.length; i++)
		 {				
			 teams[i]= getLabel(list.get(i).getName());
			 teams[i].setPrefSize(80, 10);

			 
		 }
		 int numCol = getNumCol();
		 
		 
		 HBox teamsScore[] = new HBox[teams.length];
		 final int[] matchupPos = new int[teams.length+1];  // last index will be unused unless teams.length == 1
		                                                    // need to do this for code later on (delete if you don't remember)
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
		 
		 TextField score;
		 
		 
		 /** adds a HBox with a textfield and an array of labels
		  * 
		  * @param teams[i] is an array of labels
		  * 
		  */
		 for(int i = 0; i<teamsScore.length; i++)
		 {
			 teamsScore[i] = new HBox(10);
			 score = new TextField();
	         score.setPrefWidth(50);
			 teamsScore[i].getChildren().addAll(teams[i], score);
			 teamsScore[i].setAlignment(Pos.CENTER);
		 }

		 
		 // creates a a button for each game in the first round
		 Button[] submitButtons = new Button[teams.length/2];
		 for(int i=0; i< submitButtons.length; i++)   
		 {
		     submitButtons[i] = createButton(i*2, teamsScore, matchupPos, 0);
		 }


		grid.setId("pane");
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.setPadding(new Insets(10)); // TODO comment this, I'm not sure what it does
        
        int subCol = numCol - list.size();
        
        // TODO add comment for this for loop
        for (int i = 0; i < numCol; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            if (i%2 == 1) colConst.setPercentWidth(30.0 / subCol);
            else colConst.setPercentWidth(100.0 / list.size());
            grid.getColumnConstraints().add(colConst);
        }

        // TODO add comment for this for loop
        for (int i = 0; i < numCol; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCol);
            grid.getColumnConstraints().add(colConst);
        }
        

        // TODO add comment for this for loop
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
		     // there's a lot of variables but they're all needed
			 int i = 0; int row = 0; int incReset; int powerCount = 2; int spacing = 0;
			 while(itr.hasNext())
			 { 
			     incReset = 0;
				 int teamA = itr.next()-1;
				 int teamB = itr.next()-1;
				 grid.add(teamsScore[teamA], 0, row);
				 grid.add(submitButtons[i/2], 1, row, 1, 2);
				 while (incReset < list.size()/powerCount) {
				     double numGames = (double)list.size()/(powerCount*2);
				     if (powerCount != list.size())
				         spacing = (int)((list.size()*1.5-2*numGames)/(numGames+1));  
				         
				         if (powerCount != list.size()) { // if it's not the last round where the champion is crowned
				             grid.add(createPlaceHolder(), i+2, spacing*(incReset+1)-1);  
				             grid.add(createPlaceHolder(), i+2, spacing*(incReset+1));
				        	 grid.add(createButton(incReset*2, teamsScore, matchupPos, i+3), i+3, spacing*(incReset+1)-1, 1, 2);
				         } else 
				             grid.add(createPlaceHolder(), i+2, spacing*(incReset+1)-1, 1, 2);  
				         
				         incReset+=2;
				 }
				 GridPane.setHalignment(submitButtons[i/3], HPos.CENTER);
				 grid.add(teamsScore[teamB], 0, row+1);
				 i = i+2;
				 row = row+3;
				 
				 powerCount*=2;

			 }
		 }

		 Group root = new Group();
		 Scene scene = new Scene(root, 1000, 800, Color.DARKGRAY);
		 ScrollBar scVert = new ScrollBar(); 
		 ScrollBar scHoriz = new ScrollBar(); 
		 root.getChildren().addAll(grid, scHoriz, scVert);

		 // when changing the size of the screen, the scrollbars will move as well
		 scene.widthProperty().addListener(new ChangeListener<Number>() {
		     @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		         scVert.setLayoutX(newSceneWidth.doubleValue()-scVert.getWidth());
		         
		         scHoriz.setLayoutX(newSceneWidth.doubleValue()-scene.getWidth());
		         scHoriz.setPrefWidth(newSceneWidth.doubleValue()-scVert.getWidth());
		     }
		 });
		 scene.heightProperty().addListener(new ChangeListener<Number>() {
		     @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
		         scVert.setLayoutY(newSceneHeight.doubleValue()-scene.getHeight());
		         scVert.setPrefHeight(newSceneHeight.doubleValue());
		         
		         scHoriz.setLayoutY(newSceneHeight.doubleValue()-scHoriz.getHeight());
		     }
		 });
		 
	     scVert.setPrefSize(20, scene.getHeight());	     
	     scVert.setLayoutX(scene.getWidth()-scVert.getWidth());
	     scVert.setOrientation(Orientation.VERTICAL);
	     
	     scHoriz.setOrientation(Orientation.HORIZONTAL);
	     scHoriz.setPrefSize(scene.getWidth()-scVert.getWidth(), 20);
	     scHoriz.setLayoutY(scene.getHeight()-scHoriz.getPrefHeight());

	     
	     // code to make sliding the scrollbar affect the grid
	     scVert.valueProperty().addListener(new ChangeListener<Number>() {
	    	 public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                   grid.setLayoutY(-new_val.doubleValue()*list.size()/3); // TODO get math right so the scrollbar looks ok
	            }
	     });
	     scHoriz.valueProperty().addListener(new ChangeListener<Number>() {
	    	 public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                   grid.setLayoutX(-new_val.doubleValue()*list.size()/3);
	            }
	     });
		 scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());


	primaryStage.setScene(scene);
	primaryStage.show();
	// TODO need to handle ties as well
	
	}

	   
	/**
	 * Method checks if integer is a power of 2
	 * @param number
	 * @return
	 */ // TODO delete if unneeded, will keep for now
	private static boolean isPowerOfTwo(int number) {

	    return number > 0 && ((number & (number - 1)) == 0);

	}
	/**\
	 * Method that takes a given file name and processes it into a list of type Challenger
	 * @param fileName The name of the txt file that contains the team list.
	 */
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
	/**
	 * Checks if the given number of teams is a power of 2
	 * @return true if the number teams is a power of 2 else false
	 */
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
	/**
	 * Checks which Challenger won in a match up between the two teams
	 * @param team1
	 * @param team2
	 * @return	Returns the winning team.
	 */
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