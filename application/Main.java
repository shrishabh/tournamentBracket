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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	private static List<Challenger> list = new ArrayList<Challenger>();
	private static int[] allowedTeams = {1,2,4,8,16};
	private GridPane grid = new GridPane();
	private HBox[] semiFinals = new HBox[4];
	
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
		return 0;
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
		if(numTeams == 1)
		{
			return matchupList;
		}
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
		newLabel.setPrefHeight(25);
		return newLabel;
	}
	
	/**
	 * Creates a placeHolder HBox that contains a label that in the future will have a winning team from the previous round.
	 */
	private HBox createPlaceHolder() {
	    HBox placeholder = new HBox(10);
	    TextField score = new TextField();
	    score.setPrefSize(50, 25);
        placeholder.getChildren().addAll(getLabel("____"), score);
        placeholder.setAlignment(Pos.CENTER);
        
        return placeholder;
	}
	/**
	 * Creates a placeholder HBox for the final round where the champion is crowned
	 */
	private HBox createFinalPlaceHolder() {
        HBox placeholder = new HBox(10);
        placeholder.getChildren().addAll(getLabel("CHAMPION?")); // TODO not sure what to put here, up to you
        placeholder.setAlignment(Pos.CENTER);
        return placeholder;
    }
	
	/**
	 * Creates a submit button for the 2 teams to the left of the button
	 * @param idHelper	The buttons row in the grid
	 * @param teamsScore	The HBox which contains the scores and team names
	 * @param matchupPos	The teams match up position array
	 * @param column		The column for the button to be added in the array
	 * @return				The button
	 */
	private Button createButton(int idHelper, int[] matchupPos, int column) {
	    Button b = new Button("Submit");
	    // literally used just to create uniqe ID
        b.setId(new Integer(7*idHelper + 31*column).toString());
        GridPane.setHalignment(b, HPos.CENTER);
        GridPane.setHgrow(b, Priority.ALWAYS);
        b.setPrefHeight(5);
        
        
        b.setOnAction(new EventHandler<ActionEvent>() {  // when button is clicked, this will run
            @Override
            public void handle(ActionEvent event) {
                // defining constants that are used multiple times below
                
                int rowPos = GridPane.getRowIndex(b);
                Node node = getNodeFromGridPane(grid, column, rowPos);
                Node node2 = getNodeFromGridPane(grid, column, rowPos+1);
                
                Label lab = ((Label) ((HBox) node).getChildren().get(0));
                Label lab2 = ((Label) ((HBox) node2).getChildren().get(0));
                // TODO what if a team's name is literally "____" from the txt file?
                // We might have to put a boolean somewhere to check for this
                // don't really feel like doing that so if u wanna ignore that or put something more 
                // random that would never be in a name list that's up to you
                // TODO delete this block of comments
                
                // do nothing but print if challengers haven't completed their necessary games
                if (lab.getText().equals("____") || lab2.getText().equals("____")) {
                    System.out.println("Please submit earlier rounds before coming to this one");
                    return;
                }

                ArrayList<Integer> rowListText = getNodeRowsFromColumn(grid, column+2);
                ArrayList<Integer> rowListButton = getNodeRowsFromColumn(grid, column+1);
                


                TextField t = (TextField) ((HBox) node).getChildren().get(1);
                TextField t2 = (TextField) ((HBox) node2).getChildren().get(1);

                for (int i = 0; i < rowListButton.size(); i++) {
                    if (rowListButton.get(i) == rowPos) {
                        rowPos = rowListText.get(i);
                        break;
                    }
                }
                int score = -1; int score2 = -2;
                boolean printed = false;
                try {
                    score = Integer.parseInt(t.getText());
                    score2 = Integer.parseInt(t2.getText());
                } catch (NumberFormatException e){
                    System.out.println("Please enter in a valid score");
                    printed = true;
                    return;
                }
                if (!printed && (score < 0 || score2 < 0)) {
                    System.out.println("Please enter in a valid score");
                    return;
                }
                else if (score != score2) {

                    t.setDisable(true);
                    t2.setDisable(true);
                    b.setDisable(true);
                    Label l = (Label) ((HBox) getNodeFromGridPane(grid, column+2, rowPos)).getChildren().get(0);
                    if (score > score2) {       // lab wins
                        if(l.getText() == "CHAMPION?" )  // make sure to change this if we want something other than "CHAMPION?"
                        {
                        	l.setTextFill(Color.GOLD);
                        	l.setStyle("-fx-font: 36 arial;");
                        	l.autosize();
                        	lab2.setTextFill(Color.SILVER);
                        	lab2.setStyle("-fx-font: 22 arial;");
                        	setThird((HBox) node, (HBox) node2, list.size());
                        	
                        }
                        l.setText(lab.getText());
                    } else {                    // lab2 wins
                        if(l.getText() == "CHAMPION?" )
                        {
                            l.setTextFill(Color.GOLD);
                            l.setStyle("-fx-font: 36 arial;");
                            lab.setTextFill(Color.SILVER);
                            lab.setStyle("-fx-font: 22 arial;");
                        	setThird((HBox) node, (HBox) node2, list.size());
                        }
                        l.setText(lab2.getText());
                    }
                } else {
                    System.out.println("Ties are not allowed.  Please input scores again");
                }

            }
        });
        
        return b;
    }
	/**
	 * Turns 3rd place's Label a different color to represent 3rd
	 * and slightly increases font size
	 * 
	 * @param A One of the finalists
	 * @param B The other finalists
	 */
	private void setThird(HBox A, HBox B, int numTeams)
	{
	    if (numTeams == 1 || numTeams == 2) {
	        return;
	    }
	    
        HBox C = null; //didn't win 1st or 2nd
        HBox D = null; //didn't win 1st or 2nd
        
        for(int i=0; i<semiFinals.length; i++)
        {
            if(!((Label) A.getChildren().get(0)).getText().equals(((Label) semiFinals[i].getChildren().get(0)).getText()) &&
                    !((Label) B.getChildren().get(0)).getText().equals(((Label) semiFinals[i].getChildren().get(0)).getText()))
            {
                C = semiFinals[i];
                i = 4;
            }
        }
        for(int i=0; i<semiFinals.length; i++)
        {
            if(!((Label) A.getChildren().get(0)).getText().equals(((Label) semiFinals[i].getChildren().get(0)).getText()) &&
                    !((Label) B.getChildren().get(0)).getText().equals(((Label) semiFinals[i].getChildren().get(0)).getText()) &&
                    !((Label) C.getChildren().get(0)).getText().equals(((Label) semiFinals[i].getChildren().get(0)).getText()))
            {
                D = semiFinals[i];
                i = 4;
            }
        }
        TextField t1 = (TextField) C.getChildren().get(1);
        TextField t2 = (TextField) D.getChildren().get(1);
        int scoreC = Integer.parseInt(t1.getText());
        int scoreD = Integer.parseInt(t2.getText());
        if(scoreC < scoreD)
        {
            ((Label) D.getChildren().get(0)).setTextFill(Color.CHOCOLATE);
            ((Label) D.getChildren().get(0)).setStyle("-fx-font: 20 arial;");
        }
        else if (scoreC > scoreD)
        {
            ((Label) C.getChildren().get(0)).setTextFill(Color.CHOCOLATE);
            ((Label) C.getChildren().get(0)).setStyle("-fx-font: 20 arial;");
        } 
        else 
        {
            ((Label) C.getChildren().get(0)).setTextFill(Color.CHOCOLATE);
            ((Label) C.getChildren().get(0)).setStyle("-fx-font: 20 arial;");

            ((Label) D.getChildren().get(0)).setTextFill(Color.CHOCOLATE);
            ((Label) D.getChildren().get(0)).setStyle("-fx-font: 20 arial;");

        }
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
            System.out.println("Something went wrong");
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
		     submitButtons[i] = createButton(i*2, matchupPos, 0);
		 }


		grid.setId("pane");
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.setPadding(new Insets(15, 15, 15, 15)); // TODO comment this, I'm not sure what it does
        
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
			 ((Label) teamsScore[0].getChildren().get(0)).setTextFill(Color.GOLD);
			 grid.add(teamsScore[0], 0, 0);
		 }
		 
		 else if(teams.length >= 2) 
		 {
		     if (teams.length == 4) { // special case
		         semiFinals = teamsScore;
		     }
		     // there's a lot of variables but they're all needed
			 boolean half= false;
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

				     if (powerCount != list.size()) { // if it's not the last round where the champion is crowned
				         spacing = (int)((list.size()*1.5-2*numGames)/(numGames+1)); 
				         grid.add(createPlaceHolder(), i+2, spacing*(incReset+1)-1);  
				         grid.add(createPlaceHolder(), i+2, spacing*(incReset+1));
				         grid.add(createButton(incReset*3, matchupPos, i+2), i+3, spacing*(incReset+1)-1, 1, 2);
				         if(powerCount*4 == list.size() && list.size() > 2)
				         {
				             if(!half && list.size() != 4)
				             {
				                 semiFinals[0] = (HBox) getNodeFromGridPane(grid, i+2, spacing*(incReset+1)-1);
				                 semiFinals[1] = (HBox) getNodeFromGridPane(grid, i+2, spacing*(incReset+1));
				                 half = true;
				             }
				             else if (half && list.size() != 4)
				             {
				                 semiFinals[2] = (HBox) getNodeFromGridPane(grid, i+2, spacing*(incReset+1)-1);
				                 semiFinals[3] = (HBox) getNodeFromGridPane(grid, i+2, spacing*(incReset+1));
				             }
				         }
				     } 
				     else if(spacing*(incReset+1)-1 > 0)
				     {
				         grid.add(createFinalPlaceHolder(), i+2, spacing*(incReset+1)-1, 1, 2);  
				     }
				     else
				     {
				         grid.add(createFinalPlaceHolder(), i+2, spacing*(incReset+1), 1, 2);
				     }
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
		 Scene scene = new Scene(root, 2000, 1000, Color.WHITE);
	     root.getChildren().add(grid);
	      
		 if (list.size() > 8) {
		     ScrollBar scVert = new ScrollBar(); 
		     root.getChildren().addAll(scVert);

		     // when changing the size of the screen, the scrollbars will move as well
		     scene.widthProperty().addListener(new ChangeListener<Number>() {
		         @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		             scVert.setLayoutX(newSceneWidth.doubleValue()-scVert.getWidth());

		         }
		     });
		     scene.heightProperty().addListener(new ChangeListener<Number>() {
		         @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
		             scVert.setLayoutY(newSceneHeight.doubleValue()-scene.getHeight());
		             scVert.setPrefHeight(newSceneHeight.doubleValue());

		         }
		     });

		     scVert.setPrefSize(20, scene.getHeight());	     
		     scVert.setLayoutX(scene.getWidth()-scVert.getWidth());
		     scVert.setOrientation(Orientation.VERTICAL);

		     // code to make sliding the scrollbar affect the grid
		     scVert.valueProperty().addListener(new ChangeListener<Number>() {
		         // new_val is a double from 0 - 100
		         public void changed(ObservableValue<? extends Number> ov,
		                 Number old_val, Number new_val) {
		             double test = ((80*list.size())-scVert.getPrefHeight())/scVert.getPrefHeight();
		             grid.setLayoutY(-new_val.doubleValue()*6*test); 
		         } 

		     });

		 }
		 scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
		 ScrollPane sp = new ScrollPane();
		 sp.setContent(grid);
		 primaryStage.setScene(scene);
		 primaryStage.show();
	
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
			launch();
		}
		else {
			System.out.println("Please enter valid number of teams");
			System.exit(-1);
		}

	}
}