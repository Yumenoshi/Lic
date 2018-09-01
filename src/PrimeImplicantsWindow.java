import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimeImplicantsWindow {
	private WorkWindowsCommon common;
	
	private Label command0;
	private Label command1;
	private Label command2;
	private SumsTextField sumsTextField;
	private SumsTextField dontCaresTextField;
	private Label alert;
	private Button solve;
	
	private TableView<ValueForTable> table;
	
	public PrimeImplicantsWindow(int variableNumber, String[] variablesNames) {
		common=new WorkWindowsCommon(variableNumber, variablesNames);
		StagePreparation();
		
	}
	
	@SuppressWarnings("unchecked")
	public void StagePreparation()
	{
		command0 = new Label();
		command1 = new Label();
		command2 = new Label();
		sumsTextField = new SumsTextField ();
		dontCaresTextField = new SumsTextField ();
		alert = new Label ();
		
		common.stage.setTitle("Implikanty Proste");

		command0.setText("Wpisz interesuj¹ce Ciê wartoœci funkcji, oddzielone przecinkiem: ");
		command1.setText("Wpisz nie interesuj¹ce Ciê wartoœci funkcji, oddzielone przecinkiem: ");
		command2.setText("Lub zaznacz je w tabeli: ");
		
		alert.setText("Uwaga! Program preferuje wartoœci wpisane. Jeœli wpiszesz wartoœci, to zaznaczenia w tabelii zostan¹ zignorowane");
		
		// Tworzenie tablicy wartoœci
		table=new TableView<>();
		table.setItems(getValueForTableData());

		TableColumn<ValueForTable, String> Dec = new TableColumn<ValueForTable, String>("Dec");
		Dec.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
		
		TableColumn<ValueForTable, String> variable0 = new TableColumn<ValueForTable, String>(common.variables[0]);
		variable0.setCellValueFactory(new PropertyValueFactory<>("variable0"));
		
		TableColumn<ValueForTable, String> variable1 = new TableColumn<ValueForTable, String>(common.variables[1]);
		variable1.setCellValueFactory(new PropertyValueFactory<>("variable1"));
		
		table.getColumns().addAll(Dec, variable0, variable1);
		if(common.variableNumber>2)
		{
			TableColumn<ValueForTable, String> variable2 = new TableColumn<ValueForTable, String>(common.variables[2]);
			variable2.setCellValueFactory(new PropertyValueFactory<>("variable2"));
			table.getColumns().add(variable2);
			
			if(common.variableNumber>3)
			{
				TableColumn<ValueForTable, String> variable3 = new TableColumn<ValueForTable, String>(common.variables[3]);
				variable3.setCellValueFactory(new PropertyValueFactory<>("variable3"));
				table.getColumns().add(variable3);
			}
		
		}
		TableColumn<ValueForTable, String> state = new TableColumn<ValueForTable, String>("Zaznaczenie");
		state.setCellValueFactory(new PropertyValueFactory<>("stan"));
		table.getColumns().add(state);
		
		//Listener - zaznaczacz wartoœci
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {

		        newSelection.setStan("");
		        table.refresh();
		        
		        //Unikamy nullpointerexception poprzez wywo³anie wyczyszczenia zaznaczenia ju¿ po zakoñczeniu dzia³ania listenera
		        Platform.runLater( ()-> {  table.getSelectionModel().clearSelection();  });
		    }
		});		
		
		solve = new Button();
		solve.setText("Rozwi¹¿"); 
		solve.setOnAction(new EventHandler<ActionEvent>() {
			 
            @Override
            public void handle(ActionEvent event) {
            	String sums="";
        		String dontCares="";
        		
            	//Zabezpieczenia i kontrola przekazywania danych do solvera
            	if(sumsTextField.getText().equals(""))
            	{
            		
            		//Odczytanie z tabeli i przekazanie danych do solvera
            		
            		for (ValueForTable element : table.getItems()) {
            			if(element.getStan().equals("Wybrane"))
            			{
            				sums+=element.getNazwa()+",";
            			}
            			if(element.getStan().equals("Bez Znaczenia"))
            			{
            				dontCares+=element.getNazwa()+",";
            			}
					}
            		
            		
            	}else {
            		//Przekazanie wartoœci z pól tekstowych
            		sums=sumsTextField.getText();
            		dontCares=dontCaresTextField.getText();
            	}
            	
            	common.solve(sums, dontCares);
            	
            	

            }
        });


		
		common.stage.setScene(new Scene(common.root, 650, 450));
		common.root.setAlignment(Pos.CENTER);
		common.root.getChildren().add(command0);
		common.root.getChildren().add(sumsTextField);
		common.root.getChildren().add(command1);
		common.root.getChildren().add(dontCaresTextField);
		common.root.getChildren().add(command2);
		common.root.getChildren().add(table);
		common.root.getChildren().add(alert);
		common.root.getChildren().add(solve);
		common.stage.show();
	}
	public ObservableList<ValueForTable> getValueForTableData() {
		ObservableList<ValueForTable> table = FXCollections.observableArrayList();

		
		for(int j=0;j<Math.pow(2,common.variableNumber);j++)
		{
			String s= String.format("%"+common.variableNumber+"s", Integer.toBinaryString(j)).replace(' ', '0');
			int zmienne[]=new int[common.variableNumber];
			
			for(int i=0;i<common.variableNumber;i++)
			{
				zmienne[i]=s.charAt(i)-48;
			}
			ValueForTable value=new ValueForTable(j, common.variableNumber, zmienne);
			table.add(value);	
		}

		return table;
	}
}


