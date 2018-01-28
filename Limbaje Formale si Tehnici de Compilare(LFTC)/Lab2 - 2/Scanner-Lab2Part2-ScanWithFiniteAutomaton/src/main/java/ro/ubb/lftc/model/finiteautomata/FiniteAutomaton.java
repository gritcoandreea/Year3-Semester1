package ro.ubb.lftc.model.finiteautomata;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Set<String> states;
 * Set<String> alphabet;
 * String initialState;
 * Set<String> finalStates;
 * Set<Transition> transitions;
 */
public class FiniteAutomaton {
	private final String fileName;
	private Set<String> states;
	private Set<String> alphabet;
	private String initialState;
	private Set<String> finalStates;
	private Set<Transition> transitions;

	public FiniteAutomaton(String fileName) {
		states = new LinkedHashSet<>();
		alphabet = new LinkedHashSet<>();
		finalStates = new LinkedHashSet<>();
		transitions = new LinkedHashSet<>();
		this.fileName = fileName;
	}

	/**
	 * @return true if the automaton is deterministic or not
	 */
	public boolean isDeterministic() {
		for (String state : states) {
			Map<String, String> nextStates = new HashMap<>();
			for (Transition t : transitions) {
				if (t.getFirstState().equals(state)) {
					for (String symbol : t.getSymbols()) {
						if (nextStates.containsKey(symbol)) {
							return false;
						}
						nextStates.put(symbol, t.getSecondState());
					}
				}
			}
		}
		return true;
	}

	/**
	 * Verifies if the sequence is accepted by the automaton. (works for deterministic automatons)
	 *
	 * @param sequence - String sequence of characters
	 * @return the longest prefix that is accepted by the automaton or the same string given if it is all correct
	 */
	public String getLongestPrefixForSequence(String sequence) {
		String acceptedPrefix = "";
		String acceptedCompleteSequence = "";
		String currentState = initialState;
		String currentSymbol;
		String nextState;

		for (int i = 0; i < sequence.length(); i++) {
			//iterate through the sequence
			currentSymbol = String.valueOf(sequence.charAt(i));
			acceptedCompleteSequence += currentSymbol;
			try {
				nextState = findNextState(currentState, currentSymbol);
			} catch (RuntimeException e) {
				//if this transition does not exist
				return acceptedPrefix;
			}
			if (finalStates.contains(nextState)) {
				acceptedPrefix += acceptedCompleteSequence;
				acceptedCompleteSequence = "";
			}
			currentState = nextState;
		}
		return acceptedPrefix;
	}

	public boolean isAccepted(String s){
		return s.equals(this.getLongestPrefixForSequence(s));
	}

	private String findNextState(String initialState, String currentSymbol){
		for (Transition t : transitions) {
			if (t.getFirstState().equals(initialState) && t.getSymbols().contains(currentSymbol)) {
				return t.getSecondState();
			}
		}
		throw new RuntimeException("It does not exist another state");
	}


	public void readAutomaton(){
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.equals("INITIAL:")) {
					line = br.readLine();//read next line
					if(line.equals("")){
						throw  new RuntimeException("The automaton has to have an initial state!");
					}
					this.initialState = line;
					this.states.add(line);
				}

				if (line.equals("FINAL:")) {
					line = br.readLine();//read next line
					List<String> finals = Arrays.stream(line.split("\\s")).filter(s -> !s.equals("")).map
							(String::trim).collect(Collectors.toList());
					if(finals.size()==0){
						System.out.println("The automaton has to have at least a final state!");
					}
					for (String s : finals) {
						this.finalStates.add(s);
						this.states.add(s);
					}
				}

				if (line.equals("TRANSITIONS:")) {
					String transition;
					while ((transition = br.readLine()) != null) {
						List<String> trans = Arrays.stream(transition.split(" ")).filter(s -> !s.equals(""))
								.map(String::trim).collect(Collectors.toList());
						String state1 = trans.get(0);
						String state2 = trans.get(1);
						Transition t = new Transition(state1, state2);
						this.states.add(state1);
						this.states.add(state2);
						//read symbols
						for (int i = 2; i < trans.size(); i++) {
							String symbol = trans.get(i);
							t.addSymbol(symbol);
							this.alphabet.add(symbol);
						}
						this.transitions.add(t);
					}
					if(transitions.size()==0){
						throw  new RuntimeException("The automaton has to have transitions!");
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Problems in reading the program :", e);
		}
	}
	@Override
	public String toString() {
		String str = "Finite automata: \n";
		str += getStringStates();
		str += getStringAlphabet();
		str += getStringInitialState();
		str += getStringFinalStates();
		str += getStringTransitions();
		return str;
	}

	private String getStringTransitions() {
		String str = "Transitions: ";
		for (Transition s : transitions) {
			str += "\n";
			str += s.toString();
		}
		str += "\n";
		return str;
	}

	private String getStringFinalStates() {
		String str = "Final states: ";
		for (String s : finalStates) {
			str += s;
			str += " ";
		}
		str += "\n";
		return str;
	}

	private String getStringInitialState() {
		String str = "Initial State: ";
		str += initialState;
		str += "\n";
		return str;
	}

	private String getStringAlphabet() {
		String str = "Alphabet: ";
		for (String s : alphabet) {
			str += s;
			str += " ";
		}
		str += "\n";
		return str;
	}

	private String getStringStates() {
		String str = "States: ";
		for (String s : states) {
			str += s;
			str += " ";
		}
		str += "\n";
		return str;
	}
}
