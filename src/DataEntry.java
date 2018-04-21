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

    public Map<String, Integer> getWordList() {
        return wordList;
    }

    public int getCharacterCount() {
        return characterCount;
    }

    void setcharacterCount(int characterCount) {
        this.characterCount = characterCount;
    }
}
