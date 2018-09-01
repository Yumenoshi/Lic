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
			//rzu� wyj�tek �e sumy puste
			throw new IllegalArgumentException("Pusty zbi�r sum");
		}
    	
		//Obrobienie string�w do tablic liczb ca�kowitych
		sumsList=stringTranslator(sums);			
		dontCaresList=stringTranslator(dontCares);
		
		Collections.sort(sumsList);
		Collections.sort(dontCaresList);
		
		//Sprawdzenie zawarto�ci tablic
		
		
		for (Integer integer : sumsList) {
			if(dontCaresList.contains(integer)) {
				throw new IllegalArgumentException("Element zbioru sum pokrywa si� z elementem zbioru niechcianych");
			}
		}
		
		
		//Obrobienie tablic, tak by utworzy� tablic� obiekt�w implikant�w
		
    	
		this.maximumLength=maximumLength;

		//Inicjalizacja zmiennych
        prime = new ArrayList<String>();
        petrickKey = new ArrayList<String>();

        
        // Tymczasowa tablica zawieraj�ca obiekty implikant�w zar�wno dla sum jak i dla nie wa�nych
        Implicant[] temp = new Implicant[sumsList.size() + dontCaresList.size()];
        
        int k = 0; // Index w tymczasowej tablicy, zarazem wielko�� tablicy implikant�w
        ArrayList<Integer> tempSumsList=new ArrayList<Integer>();
        ArrayList<Integer> tempDontCaresList=new ArrayList<Integer>();
        for (int i = 0; i < sumsList.size(); i++) {
        	//ignorujemy wa�ne kt�re s� wi�ksze jak maksymalna d�ugo�� s�owa
        	if (Integer.toBinaryString(sumsList.get(i)).length() > maximumLength) {
                break;
            }
        	temp[k] = new Implicant(maximumLength,sumsList.get(i));
        	k++;
        	tempSumsList.add(sumsList.get(i));
		}
        for (int i = 0; i < dontCaresList.size(); i++) {
        	//ignorujemy nie wa�ne kt�re s� wi�ksze jak maksymalna d�ugo�� s�owa
        	if (Integer.toBinaryString(dontCaresList.get(i)).length() > maximumLength) {
                break;
            }
        	temp[k] = new Implicant(maximumLength,dontCaresList.get(i));
        	k++;
        	tempDontCaresList.add(dontCaresList.get(i));
		}
        this.sumsList=tempSumsList;
        this.dontCaresList=tempDontCaresList;
        

        //Przekszta�camy tymczasow� tablic� na tablic� implikant�w
        implicants = new Implicant[k];
        for (int i = 0; i < k; i++)
        	implicants[i] = temp[i];

        // Tablic� sortujemy na podstawie ilo�ci wyst�pienia cyfry 1 w implikantach
        Arrays.sort(implicants, new numbersOfOnesComparator());
    }

    // Pierwszy stopie� rozwi�zywania. Samodzielnie wo�a nast�pne funkcje
    public void solve() {
        // Tablica na nie-u�yte jeszcze Implikanty
        ArrayList<Implicant> untaken = new ArrayList<>();

        // Pocz�tkowe implikanty
        ArrayList<Implicant>[] list = group(implicants);
        // Przechowywanie wynik�w dzia�ania
        ArrayList<Implicant>[] results;

        //P�tla b�dzie wykonywa� si� tak d�ugo, p�ki wynik nie jest pusty
        boolean inserted;
        do {
            // Przechowywanie u�ytych implikant�w
            HashSet<String> taken = new HashSet<>();

            // Oczyszczamy list� wynik�w, deklarujemy jej wielko�� na podstawie pocz�tkowych implikant�w
            results = new ArrayList[list.length - 1];

            ArrayList<String> temp;
            inserted = false;

            // P�tla by ka�da grupa trafi�a do poni�szych p�tli
            for (int i = 0; i < list.length - 1; i++) {

                results[i] = new ArrayList<>();
                // Zapisujemy u�yte implikanty by unikn�� duplikat�w
                temp = new ArrayList<>();

                // Sprawdzamy po kolei wszystkie elementy z pierwszych grup z elementami z nast�pnych
                for (int j = 0; j < list[i].size(); j++) {

                    // P�tla po elementach z nast�pych grup
                    for (int k = 0; k < list[i + 1].size(); k++) {

                        // Sprawdzamy czy kombinacja jest mo�liwa
                        if (combinationValidator(list[i].get(j), list[i + 1].get(k))) {
                            
                        	// Dodajemy implikanty do listy u�ytych
                            taken.add(list[i].get(j).value);
                            taken.add(list[i + 1].get(k).value);


                            Implicant n = list[i].get(j).compare(list[i + 1].get(k));
                            
                            // Sprawdzamy czy taki wynik ju� przypadkiem istnieje. Je�li tak, nie dodajemy go
                            if (!temp.contains(n.value)) {
                                results[i].add(n);
                                inserted = true;
                            }
                            temp.add(n.value);
                        }
                    }
                }
            }

            // Je�li wynik nie jest pusty
            if (inserted) {
                for (int i = 0; i < list.length; i++) {
                    for (int j = 0; j < list[i].size(); j++) {
                        if (!taken.contains(list[i].get(j).value)) {
                        	
                            //Dodajemy nie u�yte implikanty, do nieu�ytych
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

    // Drugi poziom rozwi�zywania
    // Identyfikacja implikant�w prostych nast�pnie dominuj�cego w rz�dzie, a nast�pnie w kolumnie
    // Metoda jest rekurencyjna, powtarza si� do momentu wykorzystania wszystkich sum, nast�pnie wo�a moj� implementacje metody Petricka 
    private void solveStepTwo() {

        if (!identifyPrime()) {
            if (!rowDominance()) {
                if (!columnDominance()) {
                    // Je�li �adna z poprzednich funkcji nie zwr�ci prawdy, przejd� do metody Pentricka
                    petrick();
                    return;
                }
            }
        }
        
        if (sumsList.size() != 0)
        	// Je�li dalej istniej� sumy do wykorzystania, wywo�aj t� funkcje rekurencyjnie
        	solveStepTwo();
        
        else {
        	// Je�li wszystkie sumy zosta�y wykorzystane, przejd� do utworzenia wyniku
            solution = new ArrayList[1];
            solution[0] = prime;
        }
    }

    // Metoda Pentricka
    void petrick() {

        HashSet<String>[] temp = new HashSet[sumsList.size()];
        
        // Konstruujemy tablel� do Metody Pentricka
        for (int i = 0; i < sumsList.size(); i++) {
        	temp[i] = new HashSet<>();
        	for (int j = 0; j < finalImplicants.size(); j++) {
                if (finalImplicants.get(j).decimalNumbers.contains(sumsList.get(i))) {
                    char t = (char) ('a' + j);
                    petrickKey.add(t + ": " + finalImplicants.get(j).value);
                    temp[i].add(t + "");
                }
		}
        	
        //Przemna�amy wyniki metody Pentricka
        HashSet<String> finalResult = multiplyPentrick(temp, 0);

        // Odnajdujemy minimalne warto�ci w wynikach metody
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

        // Dodajemy minimalne wyniki metody pentricka do wynik�w ostatecznych
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

    //T�umaczy teksty na tablic� liczb ca�kowitych
    private ArrayList<Integer> stringTranslator(String string){
    	//Jesli podany string nie ma zawarto�ci, zwracamy pusty zbi�r.
  	    if (string.trim().equals("")) {
  	    	return new ArrayList<>();

  	    }
  	        
  	  ArrayList<Integer> result=new ArrayList<>();
  	        
  	    
  	    for (String s : string.split(",")) {
  	    		result.add(Integer.parseInt(s));
  	    }
  	    	
  	    
  			
  	    return result;
  	}
    
	//wewn�trzna klasa, s�u��ca sortowaniu implikant�w na podstawie ilo�ci 1 w nich zawartych
    private class numbersOfOnesComparator implements Comparator<Implicant> {
        @Override
        public int compare(Implicant a, Implicant b) {
            return a.numberOfOnes - b.numberOfOnes;
        }
    }
    
    // Przekszta�ca pe�n� tablic� implikant�w, na ArrayList grup implikant�w na podstawie ilo�ci cyfry 1
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

    //Validator sprawdzaj�cy mo�liwo�� kombinowania implikant�w
    boolean combinationValidator(Implicant i1, Implicant i2) {
        
    	int length= i1.value.length();
    	
        //Je�li s� r�nej d�ugo�ci, to z miejsca nie pasuj� do kombinacji
        if (length != i2.value.length())
            return false;

        int k = 0;
        for (int i = 0; i < length; i++) {
        	
        	// Je�li '-' nie s� w tym samym miejscu ko�czymy sprawdzanie
            if (i1.value.charAt(i) == '-' && i2.value.charAt(i) != '-' ||i1.value.charAt(i) != '-' && i2.value.charAt(i) == '-' )
                return false;
            
            //Je�li znaki si� r�ni� zwi�kszamy licznik
            else if (i1.value.charAt(i) != i2.value.charAt(i))
                k++;
            
        }
        //Je�li �aden ze znak�w si� nie r�ni, ko�czymy sprawdzanie
        if (k != 1)
            return false;
        
        
        return true;
    }
    
    // Sprawdza czy implikanty zawieraj� te same sumy
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
    
    // Identyfikuje implikanty proste i sprawdza je wzgl�dem sum jakie reprezentuj�
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
    
    //Identyfikuje zakryte rz�dy i usuwa je
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
        //Tworzy tabel�
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

    // Mno�y dwa gotowe terminy zmiennych, na przyk�ad (AB) * (AC) = ABC
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
    
    //Mno�y wyniki metody Pentricka
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

    //Konwertuje ostateczne wyniki na notacj� ze zmiennymi( np. -01- w !BC)
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