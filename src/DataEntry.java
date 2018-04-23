import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class DataEntry {

    private String keyValue;
    private Map<String, Integer> wordList = new HashMap<>();
    private int characterCount;

    private int wordsInUpperCase = 0;
    
    void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    String getKeyValue() {
        return keyValue;
    }

    Integer getSmsLenght(){
        int i = 0;
        for (Map.Entry<String, Integer> entry : wordList.entrySet()){
            i += entry.getValue();
        }
        return i;
    }

    Integer  get(String key){
        return wordList.getOrDefault(key, 0);
    }

    void add(String key, Integer value){
        wordList.put(key,value);
    }

    public boolean containsWord(String word) {
    	return wordList.containsKey(word);
    }
    
    public ArrayList<String> getWordList() {
        return new ArrayList<String>(wordList.keySet());
    }

    public int getCharacterCount() {
        return characterCount;
    }

    void setcharacterCount(int characterCount) {
        this.characterCount = characterCount;
    }

	public int getWordsInUpperCase() {
		return wordsInUpperCase;
	}

	public void setWordsInUpperCase(int wordsInUpperCase) {
		this.wordsInUpperCase = wordsInUpperCase;
	}
    
    
}
