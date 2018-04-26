import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class DataEntry {
		
    protected String keyValue;
    private Map<String, Integer> data = new HashMap<>();
     
    void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    String getKeyValue() {
        return keyValue;
    }

    Integer  get(String key){
        return data.getOrDefault(key, 0);
    }

    void add(String key, Integer value){
            data.put(key,value);
    }

    public boolean containsWord(String word) {
    	return data.containsKey(word);
    }
    
    public ArrayList<String> getData() {
        return new ArrayList<String>(data.keySet());
    }
    
	public HashMap<String, Integer> getAttributes() {
		HashMap<String, Integer> attributeMap = new HashMap<String, Integer>();
		return attributeMap;
	}

	public Collection<? extends String> getAttributeList() {
		return null;
	}
    
}
