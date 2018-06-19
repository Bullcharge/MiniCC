package bit.minicc;
import java.util.ArrayList;;

public class SymbolTable {
	private ArrayList<SymbolEntry> symbolList;
	
	public SymbolTable() {
		this.symbolList = new ArrayList<SymbolEntry>();
	}
	
	public void insertSymbol(SymbolEntry se) {
		this.symbolList.add(se);
	}
	
	public SymbolEntry insertSymbol(String name) {
		SymbolEntry se = new SymbolEntry();
		se.name = name;
		return se;
	}
	
	public SymbolEntry findByName(String name) {
		SymbolEntry se = null;
		for(SymbolEntry e: symbolList) {
			se = e;
			break;
		}
		
		return se;
	}
	
	public void print()
	{
		System.out.println("---------------------------------------------------------------");
		System.out.println("index\t\tname\t\tkind");
		int i =1;
		for(SymbolEntry e: symbolList) {
			System.out.println("" + i + "\t\t" + e.name + "\t\t" + e.kind);
			i++;
		}
		System.out.println("----------------------------------------------------------------");
	}
	
	public ArrayList<SymbolEntry> getSymbolList(){
		return symbolList;
	}
	
	public void setSymbolList(ArrayList<SymbolEntry> symbolList) {
		this.symbolList = symbolList;
	}
}
