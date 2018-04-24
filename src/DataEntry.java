import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class DataEntry {
	
	String[] attributes = { "wordcount", "capslock", "lettercount", "phonenumbers", "emails", "websites", "prices" };
	
    private String keyValue;
    private Map<String, Integer> wordList = new HashMap<>();
    private int characterCount;

    private int wordsInUpperCase = 0;
    
    int wordCount = 0;
    int phonenumbers = 0;
    int emails = 0;
    int websites = 0;
    int prices = 0;
    
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
    	if(key.startsWith("#priceReplacement")) {
    		prices ++;
    	} else if(key.startsWith("#numberReplacement")) {
    		phonenumbers ++;
    	} else if(key.startsWith("#websiteReplacement")) {
    		websites ++;
    	} else if(key.startsWith("#emailReplacement")) {
    		emails ++;
    	}
    	
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

	public void setWordCount(int size) {
		wordCount = size;		
	}
    
	public HashMap<String, Integer> getAttributes() {
		HashMap<String, Integer> attributeMap = new HashMap<String, Integer>();
		attributeMap.put(attributes[0], wordCount);
		attributeMap.put(attributes[1], getWordsInUpperCase());
		attributeMap.put(attributes[2], getCharacterCount());
		attributeMap.put(attributes[3], phonenumbers);
		attributeMap.put(attributes[4], emails);
		attributeMap.put(attributes[5], websites);
		attributeMap.put(attributes[6], prices);
		return attributeMap;
	}

	public Collection<? extends String> getAttributeList() {
		return Arrays.asList(attributes);
	}
    
}
