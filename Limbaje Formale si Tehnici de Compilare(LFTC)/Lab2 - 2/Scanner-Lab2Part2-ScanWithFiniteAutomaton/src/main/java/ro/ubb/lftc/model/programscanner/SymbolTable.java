package ro.ubb.lftc.model.programscanner;

import java.util.Hashtable;

public class SymbolTable {
	private int currentPosition;
	private Hashtable<String, Integer> table;

	public SymbolTable() {
		table = new Hashtable<>();
		currentPosition = 0;
	}

	/**
	 * Adds a constant or an identifier in the symbol table, if it does't already exist
	 *
	 * @param value - String
	 */
	public void addValue(String value) {
		if (!table.containsKey(value)) {
			currentPosition++;
			table.put(value, currentPosition);
		}
	}

	@Override
	public String toString() {
		String s = "SymbolTable: \n";
		for (String key : table.keySet()) {
			s += key;
			s += " , ";
			s += table.get(key);
			s += "\n";
		}
		return s;
	}

	public Integer getPosition(String code) {
		return table.get(code);
	}
}
