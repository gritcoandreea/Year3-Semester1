package ro.ubb.lftc.model.finiteautomata;

import java.util.LinkedHashSet;
import java.util.Set;

public class Transition {
	private String firstState;
	private String secondState;
	private Set<String> symbols;

	Transition(String state1, String state2) {
		this.firstState = state1;
		this.secondState = state2;
		symbols = new LinkedHashSet<>();
	}

	String getFirstState() {
		return firstState;
	}

	String getSecondState() {
		return secondState;
	}

	Set<String> getSymbols() {
		return symbols;
	}

	public void setSymbols(Set<String> symbols) {
		this.symbols = symbols;
	}

	void addSymbol(String symbol) {
		symbols.add(symbol);
	}

	@Override
	public String toString() {
		String s;
		s = firstState + " " + secondState + " {";
		for (String i : symbols) {
			s += i;
			s += " ";
		}
		s += "}";
		return s;
	}
}
