import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

public class Solver {

    // sums+dontcares
    private Implicant[] implicants;
    private ArrayList<Integer> sumsList; 
    private ArrayList<Integer> dontCaresList;

    public ArrayList<String> petrickKey;

    private ArrayList<String>[] solution;
    private ArrayList<String> prime;
    private ArrayList<Implicant> finalImplicants;

    private int maximumLength;

    // constructor
    public Solver(String sums, String dontCares, int maximumLength) throws IllegalArgumentException {
    	
    	if(sums.isEmpty()) {
			//rzuæ wyj¹tek ¿e sumy puste
			throw new IllegalArgumentException("Pusty zbiór sum");
		}
    	
		//Obrobienie stringów do tablic liczb ca³kowitych
		sumsList=stringTranslator(sums);			
		dontCaresList=stringTranslator(dontCares);
		
		Collections.sort(sumsList);
		Collections.sort(dontCaresList);
		
		//Sprawdzenie zawartoœci tablic
		
		
		for (Integer integer : sumsList) {
			if(dontCaresList.contains(integer)) {
				throw new IllegalArgumentException("Element zbioru sum pokrywa siê z elementem zbioru niechcianych");
			}
		}
		
		
		//Obrobienie tablic, tak by utworzyæ tablicê obiektów implikantów
		
    	
		this.maximumLength=maximumLength;

		//Inicjalizacja zmiennych
        prime = new ArrayList<String>();
        petrickKey = new ArrayList<String>();

        
        // Tymczasowa tablica zawieraj¹ca obiekty implikantów zarówno dla sum jak i dla nie wa¿nych
        Implicant[] temp = new Implicant[sumsList.size() + dontCaresList.size()];
        
        int k = 0; // Index w tymczasowej tablicy, zarazem wielkoœæ tablicy implikantów
        ArrayList<Integer> tempSumsList=new ArrayList<Integer>();
        ArrayList<Integer> tempDontCaresList=new ArrayList<Integer>();
        for (int i = 0; i < sumsList.size(); i++) {
        	//ignorujemy wa¿ne które s¹ wiêksze jak maksymalna d³ugoœæ s³owa
        	if (Integer.toBinaryString(sumsList.get(i)).length() > maximumLength) {
                break;
            }
        	temp[k] = new Implicant(maximumLength,sumsList.get(i));
        	k++;
        	tempSumsList.add(sumsList.get(i));
		}
        for (int i = 0; i < dontCaresList.size(); i++) {
        	//ignorujemy nie wa¿ne które s¹ wiêksze jak maksymalna d³ugoœæ s³owa
        	if (Integer.toBinaryString(dontCaresList.get(i)).length() > maximumLength) {
                break;
            }
        	temp[k] = new Implicant(maximumLength,dontCaresList.get(i));
        	k++;
        	tempDontCaresList.add(dontCaresList.get(i));
		}
        this.sumsList=tempSumsList;
        this.dontCaresList=tempDontCaresList;
        

        //Przekszta³camy tymczasow¹ tablicê na tablicê implikantów
        implicants = new Implicant[k];
        for (int i = 0; i < k; i++)
        	implicants[i] = temp[i];

        // Tablicê sortujemy na podstawie iloœci wyst¹pienia cyfry 1 w implikantach
        Arrays.sort(implicants, new numbersOfOnesComparator());
    }

    // Pierwszy stopieñ rozwi¹zywania. Samodzielnie wo³a nastêpne funkcje
    public void solve() {
        // Tablica na nie-u¿yte jeszcze Implikanty
        ArrayList<Implicant> untaken = new ArrayList<>();

        // Pocz¹tkowe implikanty
        ArrayList<Implicant>[] list = group(implicants);
        // Przechowywanie wyników dzia³ania
        ArrayList<Implicant>[] results;

        //Pêtla bêdzie wykonywaæ siê tak d³ugo, póki wynik nie jest pusty
        boolean inserted;
        do {
            // Przechowywanie u¿ytych implikantów
            HashSet<String> taken = new HashSet<>();

            // Oczyszczamy listê wyników, deklarujemy jej wielkoœæ na podstawie pocz¹tkowych implikantów
            results = new ArrayList[list.length - 1];

            ArrayList<String> temp;
            inserted = false;

            // Pêtla by ka¿da grupa trafi³a do poni¿szych pêtli
            for (int i = 0; i < list.length - 1; i++) {

                results[i] = new ArrayList<>();
                // Zapisujemy u¿yte implikanty by unikn¹æ duplikatów
                temp = new ArrayList<>();

                // Sprawdzamy po kolei wszystkie elementy z pierwszych grup z elementami z nastêpnych
                for (int j = 0; j < list[i].size(); j++) {

                    // Pêtla po elementach z nastêpych grup
                    for (int k = 0; k < list[i + 1].size(); k++) {

                        // Sprawdzamy czy kombinacja jest mo¿liwa
                        if (combinationValidator(list[i].get(j), list[i + 1].get(k))) {
                            
                        	// Dodajemy implikanty do listy u¿ytych
                            taken.add(list[i].get(j).value);
                            taken.add(list[i + 1].get(k).value);


                            Implicant n = list[i].get(j).compare(list[i + 1].get(k));
                            
                            // Sprawdzamy czy taki wynik ju¿ przypadkiem istnieje. Jeœli tak, nie dodajemy go
                            if (!temp.contains(n.value)) {
                                results[i].add(n);
                                inserted = true;
                            }
                            temp.add(n.value);
                        }
                    }
                }
            }

            // Jeœli wynik nie jest pusty
            if (inserted) {
                for (int i = 0; i < list.length; i++) {
                    for (int j = 0; j < list[i].size(); j++) {
                        if (!taken.contains(list[i].get(j).value)) {
                        	
                            //Dodajemy nie u¿yte implikanty, do nieu¿ytych
                            untaken.add(list[i].get(j));
                        }
                    }
                }
                list = results;
            }
        } while (inserted && list.length > 1);

        // Kopiujemy wyniki wraz z nie wykorzystanymi implikantami do nowej tablicy
        finalImplicants = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list[i].size(); j++) {
                finalImplicants.add(list[i].get(j));
            }
        }
        for (int i = 0; i < untaken.size(); i++) {
            finalImplicants.add(untaken.get(i));
        }

        solveStepTwo();
    }

    // Drugi poziom rozwi¹zywania
    // Identyfikacja implikantów prostych nastêpnie dominuj¹cego w rzêdzie, a nastêpnie w kolumnie
    // Metoda jest rekurencyjna, powtarza siê do momentu wykorzystania wszystkich sum, nastêpnie wo³a moj¹ implementacje metody Petricka 
    private void solveStepTwo() {

        if (!identifyPrime()) {
            if (!rowDominance()) {
                if (!columnDominance()) {
                    // Jeœli ¿adna z poprzednich funkcji nie zwróci prawdy, przejdŸ do metody Pentricka
                    petrick();
                    return;
                }
            }
        }
        
        if (sumsList.size() != 0)
        	// Jeœli dalej istniej¹ sumy do wykorzystania, wywo³aj t¹ funkcje rekurencyjnie
        	solveStepTwo();
        
        else {
        	// Jeœli wszystkie sumy zosta³y wykorzystane, przejdŸ do utworzenia wyniku
            solution = new ArrayList[1];
            solution[0] = prime;
        }
    }

    // Metoda Pentricka
    void petrick() {

        HashSet<String>[] temp = new HashSet[sumsList.size()];
        
        // Konstruujemy tablelê do Metody Pentricka
        for (int i = 0; i < sumsList.size(); i++) {
        	temp[i] = new HashSet<>();
        	for (int j = 0; j < finalImplicants.size(); j++) {
                if (finalImplicants.get(j).decimalNumbers.contains(sumsList.get(i))) {
                    char t = (char) ('a' + j);
                    petrickKey.add(t + ": " + finalImplicants.get(j).value);
                    temp[i].add(t + "");
                }
		}
        	
        //Przemna¿amy wyniki metody Pentricka
        HashSet<String> finalResult = multiplyPentrick(temp, 0);

        // Odnajdujemy minimalne wartoœci w wynikach metody
        int min = -1;
        int count = 0;
        for (Iterator<String> t = finalResult.iterator(); t.hasNext();) {
            String m = t.next();
            if (min == -1 || m.length() < min) {
                min = m.length();
                count = 1;
            } else if (min == m.length()) {
                count++;
            }
        }

        // Dodajemy minimalne wyniki metody pentricka do wyników ostatecznych
        solution = new ArrayList[count];
        int k = 0;
        for (Iterator<String> t = finalResult.iterator(); t.hasNext();) {
            String c = t.next();
            if (c.length() == min) {
                solution[k] = new ArrayList<>();
                for (int l = 0; l < c.length(); l++) {
                    solution[k].add(finalImplicants.get((int) c.charAt(l) - 'a').value);
                }
                for (int l = 0; l < prime.size(); l++) {
                    solution[k].add(prime.get(l));
                }
                k++;
            }
        	}
        }
    }

    //T³umaczy teksty na tablicê liczb ca³kowitych
    private ArrayList<Integer> stringTranslator(String string){
    	//Jesli podany string nie ma zawartoœci, zwracamy pusty zbiór.
  	    if (string.trim().equals("")) {
  	    	return new ArrayList<>();

  	    }
  	        
  	  ArrayList<Integer> result=new ArrayList<>();
  	        
  	    
  	    for (String s : string.split(",")) {
  	    		result.add(Integer.parseInt(s));
  	    }
  	    	
  	    
  			
  	    return result;
  	}
    
	//wewnêtrzna klasa, s³u¿¹ca sortowaniu implikantów na podstawie iloœci 1 w nich zawartych
    private class numbersOfOnesComparator implements Comparator<Implicant> {
        @Override
        public int compare(Implicant a, Implicant b) {
            return a.numberOfOnes - b.numberOfOnes;
        }
    }
    
    // Przekszta³ca pe³n¹ tablicê implikantów, na ArrayList grup implikantów na podstawie iloœci cyfry 1
    private ArrayList<Implicant>[] group(Implicant[] implicants) {
        ArrayList<Implicant>[] groups = new ArrayList[implicants[implicants.length - 1].numberOfOnes + 1];

        for (int i = 0; i < groups.length; i++) {
            groups[i] = new ArrayList<>();
        }
        for (int i = 0; i < implicants.length; i++) {
            int k = implicants[i].numberOfOnes;
            groups[k].add(implicants[i]);
        }
        return groups;
    }

    //Validator sprawdzaj¹cy mo¿liwoœæ kombinowania implikantów
    boolean combinationValidator(Implicant i1, Implicant i2) {
        
    	int length= i1.value.length();
    	
        //Jeœli s¹ ró¿nej d³ugoœci, to z miejsca nie pasuj¹ do kombinacji
        if (length != i2.value.length())
            return false;

        int k = 0;
        for (int i = 0; i < length; i++) {
        	
        	// Jeœli '-' nie s¹ w tym samym miejscu koñczymy sprawdzanie
            if (i1.value.charAt(i) == '-' && i2.value.charAt(i) != '-' ||i1.value.charAt(i) != '-' && i2.value.charAt(i) == '-' )
                return false;
            
            //Jeœli znaki siê ró¿ni¹ zwiêkszamy licznik
            else if (i1.value.charAt(i) != i2.value.charAt(i))
                k++;
            
        }
        //Jeœli ¿aden ze znaków siê nie ró¿ni, koñczymy sprawdzanie
        if (k != 1)
            return false;
        
        
        return true;
    }
    
    // Sprawdza czy implikanty zawieraj¹ te same sumy
    boolean contains(Implicant i1, Implicant i2) {
        if (i1.decimalNumbers.size() <= i2.decimalNumbers.size()) {
            return false;
        }
        ArrayList<Integer> a = i1.decimalNumbers;
        ArrayList<Integer> b = i2.decimalNumbers;
        b.removeAll(dontCaresList);

        if (a.containsAll(b))
            return true;
        else
            return false;
    }
    
    // Identyfikuje implikanty proste i sprawdza je wzglêdem sum jakie reprezentuj¹
    private boolean identifyPrime() {
        ArrayList<Integer>[] cols = new ArrayList[sumsList.size()];
        
        for (int i = 0; i < sumsList.size(); i++) {
            cols[i] = new ArrayList();
            for (int j = 0; j < finalImplicants.size(); j++) {
                if (finalImplicants.get(j).decimalNumbers.contains(sumsList.get(i))) {
                    cols[i].add(j);
                }
            }
        }
        
        boolean flag = false;
        for (int i = 0; i < sumsList.toArray().length; i++) {
            if (cols[i].size() == 1) {
                flag = true;
                ArrayList<Integer> delete = finalImplicants.get(cols[i].get(0)).decimalNumbers;

                for (int j = 0; j < sumsList.size(); j++) {
                    if (delete.contains(sumsList.get(j))) {
                        dontCaresList.add(sumsList.get(j));
                        sumsList.remove(j);
                        j--;
                    }
                }
              
                prime.add(finalImplicants.get(cols[i].get(0)).value);
                finalImplicants.remove(cols[i].get(0).intValue());
                break;
            }
        }

        return flag;
    }
    
    //Identyfikuje zakryte rzêdy i usuwa je
    private boolean rowDominance() {
        boolean flag = false;
        for (int i = 0; i < finalImplicants.size() - 1; i++) {
            for (int j = i + 1; j < finalImplicants.size(); j++) {
                if (contains(finalImplicants.get(i), finalImplicants.get(j))) {
                    finalImplicants.remove(j);
                    j--;
                    flag = true;
                } else if (contains(finalImplicants.get(j), finalImplicants.get(i))) {
                    finalImplicants.remove(i);
                    i--;
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    //Identyfikuje zakryte kolumny i usuwa je
    private boolean columnDominance() {
        boolean flag = false;
        //Tworzy tabelê
        ArrayList<ArrayList<Integer>> cols = new ArrayList<>();
        for (int i = 0; i < sumsList.size(); i++) {
            cols.add(new ArrayList<Integer>());
            for (int j = 0; j < finalImplicants.size(); j++) {
                if (finalImplicants.get(j).decimalNumbers.contains(sumsList.get(i))) {
                    cols.get(i).add(j);
                }
            }
        }
        
        //Identyfikujemy i usuwamy kolumny
        for (int i = 0; i < cols.size() - 1; i++) {
            for (int j = i + 1; j < cols.size(); j++) {
                if (cols.get(j).containsAll(cols.get(i)) && cols.get(j).size() > cols.get(i).size()) {
                    cols.remove(j);
                    sumsList.remove(j);
                    j--;
                    flag = true;
                } else if (cols.get(i).containsAll(cols.get(j)) && cols.get(i).size() > cols.get(j).size()) {
                    cols.remove(i);
                    sumsList.remove(i);
                    i--;
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    // Mno¿y dwa gotowe terminy zmiennych, na przyk³ad (AB) * (AC) = ABC
    private String multiplyTerms(String s1, String s2) {
        HashSet<Character> r = new HashSet<>();
        for (int i = 0; i < s1.length(); i++)
            r.add(s1.charAt(i));
        for (int i = 0; i < s2.length(); i++)
            r.add(s2.charAt(i));
        String result = "";
        for (Iterator<Character> i = r.iterator(); i.hasNext();)
            result += i.next();
        return result;
    }
    
    //Mno¿y wyniki metody Pentricka
    //Metoda Rekurencyjna
    private HashSet<String> multiplyPentrick(HashSet<String>[] p, int k) {
        if (k >= p.length - 1) {
            return p[k];
        }

        HashSet<String> s = new HashSet<>();
        for (Iterator<String> t = p[k].iterator(); t.hasNext();) {
            String temp2 = t.next();
            for (Iterator<String> g = p[k + 1].iterator(); g.hasNext();) {
                String temp3 = g.next();
                s.add(multiplyTerms(temp2, temp3));
            }
        }
        p[k + 1] = s;
        return multiplyPentrick(p, k + 1);
    }

    //Konwertuje ostateczne wyniki na notacjê ze zmiennymi( np. -01- w !BC)
    private String symbolTranslator(String s) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '-') {
                continue;
            } else if (s.charAt(i) == '1') {
                r.append((char) ('A' + i));
                r.append(' ');
            } else {
            	r.append('!');
                r.append((char) ('A' + i));
                r.append(' ');
            }
        }
        if (r.toString().length() == 0) {
            r.append("1");
        }
        return r.toString();
    }

    //Metoda zwraca wynik pracy solvera
    public String returnResults() {
    	String result="";
        for (int i = 0; i < solution.length; i++) {
        	result+="\n(";
            for (int j = 0; j < solution[i].size(); j++) {
            	result+=symbolTranslator(solution[i].get(j));
                if (j != solution[i].size() - 1) {
                    result+=" +  ";
                }
            }
        	result+=")\n";
        }
        return result;
    }
    
    
}