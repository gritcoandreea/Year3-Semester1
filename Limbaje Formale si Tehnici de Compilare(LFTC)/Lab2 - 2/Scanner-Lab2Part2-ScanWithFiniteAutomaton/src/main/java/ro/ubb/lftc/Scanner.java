package ro.ubb.lftc;

import ro.ubb.lftc.model.finiteautomata.FiniteAutomaton;
import ro.ubb.lftc.model.programscanner.CodingTable;
import ro.ubb.lftc.model.programscanner.ProgramInternalForm;
import ro.ubb.lftc.model.programscanner.SymbolTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scanner {
	private static final String CONSTANT = "constant";
	private static final String IDENTIFIER = "identifier";
	private ProgramInternalForm programInternalForm;
	private SymbolTable symbolTable;
	private CodingTable codingTable;
	private FiniteAutomaton integerFiniteAutomaton;
	private FiniteAutomaton identifierFiniteAutomaton;
	private FiniteAutomaton tokenAutomaton;

	private Scanner() {
		codingTable = new CodingTable("src/main/resources/codes.txt");
		symbolTable = new SymbolTable();
		programInternalForm = new ProgramInternalForm();
		//read the automatons
		integerFiniteAutomaton = new FiniteAutomaton("src/main/resources/integer.txt");
		integerFiniteAutomaton.readAutomaton();
		identifierFiniteAutomaton = new FiniteAutomaton("src/main/resources/identifier.txt");
		identifierFiniteAutomaton.readAutomaton();
		tokenAutomaton = new FiniteAutomaton("src/main/resources/keyword.txt");
		tokenAutomaton.readAutomaton();
	}

	/**
	 * It fills the programInternalForm and the symbolTable with the
	 * associated data
	 *
	 * @param filename - the name of the file with the program we scan
	 
	 */
    private void scan(String filename){
		verifyAutomatons();
		symbolTable = new SymbolTable();
		programInternalForm = new ProgramInternalForm();

		String[] tokensVal = getProgramFromFile(filename);

		int i = 0;
		while (i < tokensVal.length) {
			verifySingleTokens(tokensVal[i]);
			i++;
		}
	}

	private void verifyAutomatons() {
		if(!identifierFiniteAutomaton.isDeterministic()){
			throw new RuntimeException("The finite automaton for IDENTIFIER is not deterministic!");

		}
		if(!integerFiniteAutomaton.isDeterministic()){
			throw new RuntimeException("The finite automaton for INTEGER is not deterministic!");
		}
		if(!tokenAutomaton.isDeterministic()){
			throw new RuntimeException("The finite automaton for TOKENS is not deterministic!");
		}
	}

	/**
	 * Verifies tokens composed of a single word - simple keywords, constants, identifiers
	 * Exits the function if an empty string is found
	 *
	 * @param token - the token to be checked
	 * @throws RuntimeException - if it cannot identify a type for this token.
	 *                         It allerts that there is a mistake in the written program from the file, that does
	 *                         not respect the alphabet of this language
	 */
	private void verifySingleTokens(String token){
		//verify normal codes
		if (token.equals("")) {
			return;
		}
		if (codingTable.hasCode(token)) {
			System.out.println("Keyword: " + token);
			programInternalForm.addValues(codingTable.getValueForCode(token), null);
			return;
		}
		if (isConstantToken(token)) {
			System.out.println("Constant: " + token);
			symbolTable.addValue(token);
			programInternalForm.addValues(codingTable.getValueForCode(CONSTANT), symbolTable.getPosition(token));
			return;
		}
		if (isIdentifierToken(token)) {
			System.out.println("Identifier: " + token);
			symbolTable.addValue(token);
			programInternalForm.addValues(codingTable.getValueForCode(IDENTIFIER), symbolTable.getPosition(token));
			return;
		}

		throw new RuntimeException("Couldn't find a category for string '" + token + "'");
	}

	/**
	 * @param s - the string to verify
	 * @return true if is an identifier (contains only letters and is not a keyword),
	 * false otherwise
	 */
	private boolean isIdentifierToken(String s){
		return identifierFiniteAutomaton.isAccepted(s);
	}

	/**
	 * @param s - the string to verify
	 * @return true if s is a valid number, false otherwise
	 */
	private boolean isConstantToken(String s) {
		return integerFiniteAutomaton.isAccepted(s);
	}

	/**
	 * Reads and splits the text in an array of string which were separated by space
	 *
	 * @param filename - the file from which it reads the program
	 * @return an array of possible tokens
	 * @throws RuntimeException in case of an IOException (cannot find or open the file)
	 */
	private String[] getProgramFromFile(String filename) {
		String program = "";
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				program += line;
				program += " ";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getTokens(program);
	}

	private String[] getTokens(String program) {
		List<String> tokens= new ArrayList<>();
		while (!program.isEmpty()){
			program=program.trim();
			String nextToken = tokenAutomaton.getLongestPrefixForSequence(program);
			if(!nextToken.isEmpty()){
				tokens.add(nextToken);
				program=program.substring(nextToken.length());
			}
			program=program.trim();
		}

		return tokens.toArray(new String[0]);
	}

	private ProgramInternalForm getProgramInternalForm() {
		return programInternalForm;
	}

	private SymbolTable getSymbolTable() {
		return symbolTable;
	}

	public static void main(String[] args){

		Scanner scanner = new Scanner();
		scanner.scan("src/main/resources/scanner_ex1.txt");
		System.out.println("\n");
		System.out.println(scanner.getProgramInternalForm().toString());
		System.out.println(scanner.getSymbolTable().toString());
	}
}
