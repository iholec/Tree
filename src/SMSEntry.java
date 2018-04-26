import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class SMSEntry extends DataEntry{
	
	HashMap<String,Integer> data = new HashMap<>();
	
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
        for (Map.Entry<String, Integer> entry : data.entrySet()){
            i += entry.getValue();
        }
        return i;
    }

    Integer  get(String key){
        return data.getOrDefault(key, 0);
    }

    void add(String key, Integer value){
    	if(key.toLowerCase().startsWith("#priceReplacement".toLowerCase())) {
    		prices ++;
    	} else if(key.toLowerCase().startsWith("#numberReplacement".toLowerCase())) {
    		phonenumbers ++;
    	} else if(key.toLowerCase().startsWith("#websiteReplacement".toLowerCase())) {
    		websites ++;
    	} else if(key.toLowerCase().startsWith("#emailReplacement".toLowerCase())) {
    		emails ++;
    	}
    	
        data.put(key,value);
    }

    public boolean containsWord(String word) {
    	return data.containsKey(word);
    }
    
    public ArrayList<String> getWordList() {
        return new ArrayList<String>(data.keySet());
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
//		attributeMap.put(attributeLabels[0], wordCount);
//		attributeMap.put(attributeLabels[1], getWordsInUpperCase());
//		attributeMap.put(attributeLabels[2], getCharacterCount());
//		attributeMap.put(attributeLabels[3], phonenumbers);
//		attributeMap.put(attributeLabels[4], emails);
//		attributeMap.put(attributeLabels[5], websites);
//		attributeMap.put(attributeLabels[6], prices);
		return attributeMap;
	}

	public Collection<? extends String> getAttributeList() {
		return null;
	}
    
}
