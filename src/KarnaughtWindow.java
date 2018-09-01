import java.util.HashSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class KarnaughtWindow{
	private WorkWindowsCommon common;
	private GridPane gridPane;
	private ButtonForKarnaught[] buttons;
	private int numberOfButtons;
	private HashSet<Integer> sums;
	private HashSet<Integer> dontCares;
	private EventHandler<ActionEvent> onButtonPressed;
	private Button solve;
	
	public KarnaughtWindow(int variableNumber, String[] variables) {
		
		//Inicjalizacja zmiennych
		common=new WorkWindowsCommon(variableNumber, variables);
		this.gridPane=new GridPane();
		this.buttons=new ButtonForKarnaught[16];
		this.sums=new HashSet<>();
		this.dontCares=new HashSet<>();
		
		//Przygotowujemy listener naciœniêæ na przyciski
		onButtonPressed=new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				//Odbieramy wartoœæ zmienion¹ z przycisku naciœniêtego
				int value=((ButtonForKarnaught)event.getSource()).OnClick();
				
				//Zmieniamy stan setów na podstawie stanu przycisku
				switch(((Button)event.getSource()).getText()) {
					
					//Zosta³ naciœniêty, jako wa¿ny
					case "X":
						sums.add(value);
					break;
					
					//Zosta³ oznaczony jako niewa¿ny
					case "O":
						sums.remove(value);
						dontCares.add(value);
					break;
					
					//Zosta³ odznaczony ca³kowicie
					case " ":
						dontCares.remove(value);
					break;
				}
					
			}
		};
		StagePreparation();
	}

	//Scenariusze zape³nienia tabeli s¹ statyczne, tworznone tutaj
	public void StagePreparation()
	{
		ButtonForKarnaught button;
		
		//Przygotowujemy zape³nienie tabeli
		switch (common.variableNumber) {
		
		case 2:
			
			//Nazwy Zmiennych
			//!A
			gridPane.add(new Label("!"+common.variables[0]), 1, 0);
			//A
			gridPane.add(new Label(common.variables[0]), 2, 0);
			//!B
			gridPane.add(new Label("!"+common.variables[1]), 0, 1);
			//B
			gridPane.add(new Label(common.variables[1]), 0, 2);
			
			numberOfButtons=4;
			
			//0
			button=new ButtonForKarnaught(0);
			buttons[0]=button;
			gridPane.add(button, 1, 1);
			
			//2
			button=new ButtonForKarnaught(2);
			buttons[1]=button;
			gridPane.add(button, 2, 1);
			
			//1
			button=new ButtonForKarnaught(1);
			buttons[2]=button;
			gridPane.add(button, 1, 2);
			
			//3
			button=new ButtonForKarnaught(3);
			buttons[3]=button;
			gridPane.add(button, 2, 2);
			break;
			
		case 3:
			
			//Nazwy Zmiennych
			//!C
			gridPane.add(new Label("!"+common.variables[2]), 0, 1);
			//C
			gridPane.add(new Label(common.variables[2]), 0, 2);
			//!A!B
			gridPane.add(new Label("!"+common.variables[0]+"!"+common.variables[1]), 1, 0);
			//!AB
			gridPane.add(new Label("!"+common.variables[0]+common.variables[1]), 2, 0);
			//AB
			gridPane.add(new Label(common.variables[0]+common.variables[1]), 3, 0);
			//A!B
			gridPane.add(new Label(common.variables[0]+"!"+common.variables[1]), 4, 0);
			
			
			numberOfButtons=8;
			
			//0
			button=new ButtonForKarnaught(0);
			buttons[0]=button;
			gridPane.add(button, 1, 1);
			
			//2
			button=new ButtonForKarnaught(2);
			buttons[1]=button;
			gridPane.add(button, 2, 1);
			
			//1
			button=new ButtonForKarnaught(1);
			buttons[2]=button;
			gridPane.add(button, 1, 2);
			
			//3
			button=new ButtonForKarnaught(3);
			buttons[3]=button;
			gridPane.add(button, 2, 2);
			
			//6
			button=new ButtonForKarnaught(6);
			buttons[4]=button;
			gridPane.add(button, 3, 1);
			
			//4
			button=new ButtonForKarnaught(4);
			buttons[5]=button;
			gridPane.add(button, 4, 1);
			
			//7
			button=new ButtonForKarnaught(7);
			buttons[6]=button;
			gridPane.add(button, 3, 2);
			
			//5
			button=new ButtonForKarnaught(5);
			buttons[7]=button;
			gridPane.add(button, 4, 2);
			
			break;

		case 4:
			//Nazwy Zmiennych
			//!A!B
			gridPane.add(new Label("!"+common.variables[0]+"!"+common.variables[1]), 0, 1);
			//!AB
			gridPane.add(new Label("!"+common.variables[0]+common.variables[1]), 0, 2);
			//AB
			gridPane.add(new Label(common.variables[0]+common.variables[1]), 0, 3);
			//A!B
			gridPane.add(new Label(common.variables[0]+"!"+common.variables[1]), 0, 4);
			
			//!C!D
			gridPane.add(new Label("!"+common.variables[2]+"!"+common.variables[3]), 1, 0);
			//!CD
			gridPane.add(new Label("!"+common.variables[2]+common.variables[3]), 2, 0);
			//CD
			gridPane.add(new Label(common.variables[2]+common.variables[3]), 3, 0);
			//C!D
			gridPane.add(new Label(common.variables[2]+"!"+common.variables[3]), 4, 0);
			
			numberOfButtons=16;
			
			//0
			button=new ButtonForKarnaught(0);
			buttons[0]=button;
			gridPane.add(button, 1, 1);
			
			//1
			button=new ButtonForKarnaught(1);
			buttons[1]=button;
			gridPane.add(button, 2, 1);
			
			//3
			button=new ButtonForKarnaught(3);
			buttons[2]=button;
			gridPane.add(button, 3, 1);
			
			//2
			button=new ButtonForKarnaught(2);
			buttons[3]=button;
			gridPane.add(button, 4, 1);
			
			//4
			button=new ButtonForKarnaught(4);
			buttons[4]=button;
			gridPane.add(button, 1, 2);
			
			//5
			button=new ButtonForKarnaught(5);
			buttons[5]=button;
			gridPane.add(button, 2, 2);
			
			//7
			button=new ButtonForKarnaught(7);
			buttons[6]=button;
			gridPane.add(button, 3, 2);
			
			//6
			button=new ButtonForKarnaught(6);
			buttons[7]=button;
			gridPane.add(button, 4, 2);
			
			//12
			button=new ButtonForKarnaught(12);
			buttons[8]=button;
			gridPane.add(button, 1, 3);
			
			//13
			button=new ButtonForKarnaught(13);
			buttons[9]=button;
			gridPane.add(button, 2, 3);
			
			//15
			button=new ButtonForKarnaught(15);
			buttons[10]=button;
			gridPane.add(button, 3, 3);
			
			//14
			button=new ButtonForKarnaught(14);
			buttons[11]=button;
			gridPane.add(button, 4, 3);
			
			//8
			button=new ButtonForKarnaught(8);
			buttons[12]=button;
			gridPane.add(button, 1, 4);
			
			//9
			button=new ButtonForKarnaught(9);
			buttons[13]=button;
			gridPane.add(button, 2, 4);
			
			//11
			button=new ButtonForKarnaught(11);
			buttons[14]=button;
			gridPane.add(button, 3, 4);
			
			//10
			button=new ButtonForKarnaught(10);
			buttons[15]=button;
			gridPane.add(button, 4, 4);
			
			break;	
		}
		
		//Ustawiamy listenery na klikniêcia w przyciski oraz wielkoœæ przycisków
		for(int i=0;i<numberOfButtons;i++) {
			buttons[i].setOnAction(onButtonPressed);
			buttons[i].setMinHeight(30.0);
			buttons[i].setMinWidth(30.0);
		}
		
		//Przygotowywujemy przycisk do rozwi¹zania
		solve=new Button("Rozwi¹¿");
		solve.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				//Obrabiamy zbiory wyników i podajemy stringi do solvera
				String sumsString="";
				for (Integer sum : sums) {
					sumsString+=sum.toString()+",";
				}
				String dontCaresString="";
				for (Integer dc : dontCares) {
					dontCaresString+=dc.toString()+",";
				}
				common.solve(sumsString, dontCaresString);
			}
		});
		
		common.stage.setTitle("Karnaughta");
		gridPane.setAlignment(Pos.CENTER);
		common.root.getChildren().add(gridPane);
		common.root.getChildren().add(solve);
		solve.setAlignment(Pos.BOTTOM_RIGHT);
		common.stage.setScene(new Scene(common.root, 200, 200));
		common.stage.show();
	}
}
