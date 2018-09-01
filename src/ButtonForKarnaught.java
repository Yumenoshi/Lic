
import javafx.scene.control.Button;


public class ButtonForKarnaught extends Button {
	
	//Przechowywanie wartoœci dziesiêtnej przycisku
	public int value;
	
	public ButtonForKarnaught(int value) {
		super(" ");
		this.value=value;
	}
	
	public int OnClick() {
		switch(this.getText()) {
		//Zaznaczenie pustego jako wa¿nego
		case " ":
			super.setText("X");
			break;
			
		//Zaznaczenie wa¿nego jako niewa¿nego
		case "X":
			super.setText("O");
			break;
			
		//Odznaczenie niewa¿nego
		case "O":
			super.setText(" ");
			break;
		}
		return value;
	}
	
}
