import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ValueForTable {
	private SimpleIntegerProperty[] values;
	private SimpleIntegerProperty name;
	private SimpleStringProperty state;
	public ValueForTable(int name ,int ilosczmiennych, int[] values) {
		this.name=new SimpleIntegerProperty(name);
		this.values=new SimpleIntegerProperty[ilosczmiennych];
		this.state= new SimpleStringProperty("");
		for(int i=0;i<ilosczmiennych;i++)
		{
			this.values[i]=new SimpleIntegerProperty(values[i]);
		}
	}

	public void setStan(String v) {
		//Zarz¹dza logik¹ dzia³ania stateu
		switch (this.state.get()) {
		case "":
			this.state.set("Wybrane");
			break;
		case "Wybrane":
			this.state.set("Bez Znaczenia");
			break;
		case "Bez Znaczenia":
			this.state.set("");
			break;

		default:
			this.state.set("");
			break;
		}
		
	}
	
	public void setVariable0(int v)
	{
		this.values[0].set(v);
	}
	public void setVariable1(int v)
	{
		this.values[1].set(v);
	}
	public void setVariable2(int v)
	{
		this.values[2].set(v);
	}
	public void setVariable3(int v)
	{
		this.values[3].set(v);
	}
	public void setNazwa(int v)
	{
		this.name.set(v);
	}
	
	public int getVariable0()
	{
		return this.values[0].get();
	}
	public int getVariable1()
	{
		return this.values[1].get();
	}
	public int getVariable2()
	{
		return this.values[2].get();
	}
	public int getVariable3()
	{
		return this.values[3].get();
	}
	public int getNazwa()
	{
		return this.name.get();
	}
	public String getStan() {
		return this.state.get();
	}
	
}
