package ExpressionGrammar;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScanGrammar {
	String filename = "./grammar.txt";
	
	HashMap<String, List<String[]>> grammarDict;

	java.util.Scanner txtScanner;
	
	public ScanGrammar() {
		grammarDict = new HashMap<>();
		try {
			txtScanner = new java.util.Scanner(new File(filename));
			setGrammarDic();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setGrammarDic() {
		while (txtScanner.hasNextLine()) {
			String line = "";
			List<String[]> valueStr2DArray = new ArrayList<String[]>();
			
			line = txtScanner.nextLine();
			String[] lineStrArray = line.substring(line.indexOf(":") + 1, line.length()).split("_");

			for (int i = 0; i < lineStrArray.length; i++) { 
				valueStr2DArray.add(lineStrArray[i].split(" "));
			}
			grammarDict.put(line.substring(0, line.indexOf(":")), valueStr2DArray);
		}
	}
	
	public HashMap<String, List<String[]>> getGrammarDic() {
		return grammarDict;
	}
	
	public String toString() {
		String result = "";
		for (String key: grammarDict.keySet()) {
			result += key + ":";
			for (int i = 0; i < grammarDict.get(key).size(); i++) {
				for (int j = 0; j < grammarDict.get(key).get(i).length; j++) {
					result += " ";
					result += grammarDict.get(key).get(i)[j];
				}
				result += "|";
			}
			result += "\n";
		}
		return result;
	}
	
	public Set<String> first(String input) {
		Set<String> first_set = new HashSet<String>();
		
		add_first(input, first_set);
		return first_set;
	}
	
	private void add_first(String input, Set<String> first_set) {
		// it's non terminal
		if (grammarDict.containsKey(input)) {
			for (int i=0;i<grammarDict.get(input).size();i++) {
				add_first(grammarDict.get(input).get(i)[0], first_set);
			}
		} else { // it's terminal
			first_set.add(input);
		}
	}
	
	public Set<String> follow(String input) {
		Set<String> follow_set = new HashSet<String>();
		
		add_follow(input, follow_set);
		return follow_set;
	}

	private void add_follow(String input, Set<String> follow_set) {
		
		for (String key: grammarDict.keySet()) {
			for (int i= 0; i < grammarDict.get(key).size(); i++) {
				for (int j = 0; j < grammarDict.get(key).get(i).length; j++) {
					if (grammarDict.get(key).get(i)[j].equals(input)) {
						if (j < grammarDict.get(key).get(i).length - 1) {
							follow_set.addAll(first(grammarDict.get(key).get(i)[j + 1]));
							if (follow_set.contains("epsilon")) {
								follow_set.remove("epsilon");
								follow_set.addAll(follow(grammarDict.get(key).get(i)[j + 1]));
							}	
						} else if (!key.equals(input)){
							follow_set.addAll(follow(key));
						}
					}
				}
			}
		}
	}
	
}