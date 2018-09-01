import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WorkWindowsCommon {

	public String[] variables;
	public int variableNumber;
	
	public Stage stage;
	public VBox root;

	
		public WorkWindowsCommon(int variableNumber, String[] variablesNames)
		{
			stage = new Stage();
			root = new VBox();
			variables =new String[variableNumber];
			this.variableNumber=variableNumber;
			 
			for(int i=0;i<variableNumber;i++)
			{
				variables[i]=variablesNames[i];
			}


		}
		public void solve(String sums, String dontCares)
		{
			
	        //Wy�apanie mo�liwego b��du
	        try {
	        	//Utworzenie obiektu Solvera, kt�ry rozwi��e zadanie
		        Solver solver = new Solver(sums, dontCares,variableNumber);
	        	
		        solver.solve();
	        	
	        	//Wywo�aj dialog
		        makeResultAlert(solver.returnResults());
			} catch (IllegalArgumentException e) {
				makeErrorAlert(e.getMessage());
			}
	        
	        

			
		}
		private void makeResultAlert(String result) {
        	Alert resultAlert = new Alert(AlertType.INFORMATION);
        	resultAlert.setTitle("Wynik");
        	resultAlert.setHeaderText("Twoje mo�liwe zoptymalizowane funkcje to:");
        	resultAlert.setContentText(result);

        	resultAlert.showAndWait();
		}
		private void makeErrorAlert(String error) {
			Alert resultAlert = new Alert(AlertType.ERROR);
        	resultAlert.setTitle("B��d");
        	resultAlert.setHeaderText("Co� posz�o nie tak:");
        	resultAlert.setContentText(error);

        	resultAlert.showAndWait();
		}
}
