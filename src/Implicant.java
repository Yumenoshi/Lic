import java.util.ArrayList;

public class Implicant {
	
	//String reprezentuj�cy stan implikantu, mo�e zawiera� 0, 1 lub -
	public String value;

    //Zbi�r zawieraj�cy liczby w notacji dziesi�tnej, kt�re pokrywa dany implikant
    public ArrayList<Integer> decimalNumbers= new ArrayList<Integer>();
    
	//Ilo�� warto�ci 1 w implikancie
    public int numberOfOnes=0;
    
    public Implicant(int length,int value) {
    	//Przekszta�camy podan� liczb� dziesi�tn� na notacj� binarn�
    	this.value = Integer.toBinaryString(value);
    	
    	//Notacja binarna domy�lnie ignoruje zera z przodu danej liczby. Uzupe�niamy je, je�li potrzeba
    	if(this.value.length()<length) {
    		String temporary="";
    		while(temporary.length()+this.value.length()<length) {
    			temporary+=(char)'0';
    		}
    		temporary+=this.value;
    		this.value=temporary;
    		
    	}
    	
    	//Dodajemy pierwsz� liczb� do zbioru liczb dziesi�tnych zawieraj�cych si� w tym implikancie
    	decimalNumbers.add(value);
    	
    	this.numberOfOnes=countOnes(this.value);
    	
    	
    }
    
    //Dodatkowy, prywatny konstruktor, na potrzeby metody compare
    private Implicant(String implicant,ArrayList<Integer> numbers) {
    	
    	this.value=implicant;
    	
    	this.numberOfOnes=countOnes(this.value);
    	
    	this.decimalNumbers=numbers;
    }
    
    
    //Kombinuje dwa implikanty i zwraca nowy jako wynik
	public Implicant compare(Implicant secondOne) {
		
		//Tworzymy warto�� implikantu przez por�wnanie, wstawianie '-' w miejscach r�nic
		String value="";
		for (int i = 0; i < this.value.length(); i++) {
            if (this.value.charAt(i) != secondOne.value.charAt(i)) {
                value+="-";
            } else {
                value+=this.value.charAt(i);
            }
        }
				
		//Tworzymy zbi�r liczb dziesi�tnych zawartych w implikancie
		ArrayList<Integer> numbers = new ArrayList<Integer>();;
        for (Integer integer : this.decimalNumbers) {
			numbers.add(integer);
		}
        for (Integer integer : secondOne.decimalNumbers) {
			numbers.add(integer);
		}
		
        //Zwracamy nowo utworzony implikant
		return new Implicant(value, numbers);
	}
	

	//Metoda zliczaj�ca liczb� cyfr 1 w tekstowym wyra�eniu implikantu
    private int countOnes(String value) {
    	int number=0;

    	for (char s : value.toCharArray()) {
    		if (s=='1') {
            	number++;
            }
		}
    	return number;	
    }
}
