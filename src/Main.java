import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
public class Main extends Application {
	
	private String[] methodStrings= {"Karnaughta","Implikantów prostych"};
	private String [] variablesNumber= {"2","3","4"};
	private String[] variablesNames= {"A","B","C","D","E"};

	
    public static void main(String[] args) {
    	
        launch(args);
    }
    
    
    @Override
    public void start(Stage primaryStage) {	
        primaryStage.setTitle("Okno Startowe");
        
        
        ObservableList<String> methodList = FXCollections.observableArrayList(methodStrings);
        ChoiceBox<String> method= new ChoiceBox<String>(methodList);
        method.getSelectionModel().selectFirst();
        
        
        
        ObservableList<String> variablesList = FXCollections.observableArrayList(variablesNumber);
        ChoiceBox<String> variablesNumber= new ChoiceBox<String>(variablesList);
        variablesNumber.getSelectionModel().selectFirst();
        
        
        Button nextWindow = new Button();
        nextWindow.setText("Rozpocznij"); 
        nextWindow.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                //Wywo³aj nowe okno z parametrami
            	int x= Integer.parseInt(variablesNumber.getSelectionModel().getSelectedItem());
        		
        		if(method.getSelectionModel().getSelectedIndex()==0)
        		{
        			new KarnaughtWindow(x,variablesNames);
        		}else {
        			new PrimeImplicantsWindow(x,variablesNames);
        		}
            }
        });
        
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(method);
        root.getChildren().add(variablesNumber);
        root.getChildren().add(nextWindow);
        primaryStage.setScene(new Scene(root, 200, 200));
        primaryStage.show();
    }
}