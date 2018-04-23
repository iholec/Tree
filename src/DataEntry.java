import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class DataEntry {

    private String keyValue;
    private Map<String, Integer> wordList = new HashMap<>();
    private int characterCount;

    void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    String getKeyValue() {
        return keyValue;
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
}
