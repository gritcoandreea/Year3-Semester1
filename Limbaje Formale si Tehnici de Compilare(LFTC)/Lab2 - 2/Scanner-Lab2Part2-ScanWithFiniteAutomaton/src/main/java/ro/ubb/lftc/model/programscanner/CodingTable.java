package ro.ubb.lftc.model.programscanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodingTable {
	private String filename;
	private Map<String, Integer> codes;

	public CodingTable(String filename){
		this.filename = filename;
		codes = new HashMap<>();
		this.readFromFile();
	}

	public Integer getValueForCode(String code) {
		return codes.get(code);
	}

	/**
	 * Read the codes from a given file and saves them in the map
	 */
	private void readFromFile(){
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokensVal = line.split(" ");
				Integer value = Integer.valueOf(tokensVal[0]);
				String identifier = tokensVal[1];

				//add extra words
				for (int i = 2; i < tokensVal.length; i++) {
					if (!tokensVal[i].equals(" ")) {
						identifier += " ";
						identifier += tokensVal[i];
					}
				}
				//add to map
				codes.put(identifier, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, Integer> getCodes() {
		return codes;
	}

	public boolean hasCode(String code) {
		return codes.containsKey(code);
	}
}
