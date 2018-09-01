import java.util.ArrayList;

public class Implicant {
	
	//String reprezentuj¹cy stan implikantu, mo¿e zawieraæ 0, 1 lub -
	public String value;

    //Zbiór zawieraj¹cy liczby w notacji dziesiêtnej, które pokrywa dany implikant
    public ArrayList<Integer> decimalNumbers= new ArrayList<Integer>();
    
	//Iloœæ wartoœci 1 w implikancie
    public int numberOfOnes=0;
    
    public Implicant(int length,int value) {
    	//Przekszta³camy podan¹ liczbê dziesiêtn¹ na notacjê binarn¹
    	this.value = Integer.toBinaryString(value);
    	
    	//Notacja binarna domyœlnie ignoruje zera z przodu danej liczby. Uzupe³niamy je, jeœli potrzeba
    	if(this.value.length()<length) {
    		String temporary="";
    		while(temporary.length()+this.value.length()<length) {
    			temporary+=(char)'0';
    		}
    		temporary+=this.value;
    		this.value=temporary;
    		
    	}
    	
    	//Dodajemy pierwsz¹ liczbê do zbioru liczb dziesiêtnych zawieraj¹cych siê w tym implikancie
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
		
		//Tworzymy wartoœæ implikantu przez porównanie, wstawianie '-' w miejscach ró¿nic
		String value="";
		for (int i = 0; i < this.value.length(); i++) {
            if (this.value.charAt(i) != secondOne.value.charAt(i)) {
                value+="-";
            } else {
                value+=this.value.charAt(i);
            }
        }
				
		//Tworzymy zbiór liczb dziesiêtnych zawartych w implikancie
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
	

	//Metoda zliczaj¹ca liczbê cyfr 1 w tekstowym wyra¿eniu implikantu
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
