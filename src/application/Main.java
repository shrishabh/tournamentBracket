package src.application;
	
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class Main extends Application {
	private List<Challenger> list = new ArrayList<Challenger>();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			GridPane grid = new GridPane();
			Label[] teams = new Label[list[].length()];
			for(int i=0; i<list[].lenght()/2; i++)
			{
				GridPane[i][0] = teams[i];
				GridPane[i+1][0] = new Button();
				GridPane[i+2][0] = teams[list.length()-i];
			}
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
