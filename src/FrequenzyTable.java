import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FrequenzyTable {

	String name = "";

	HashMap<String, HashMap<Integer, Integer>> frequencyTable = new HashMap<String, HashMap<Integer, Integer>>();

	HashSet<Integer> attributeValueSet = new HashSet<Integer>();

	HashMap<Integer, Integer> valueCount = new HashMap<Integer, Integer>();
	HashMap<String, Integer> classifyerCount = new HashMap<String, Integer>();

	public int classifyerSum = 0;

	public int valueSum = 0;

	public FrequenzyTable(String name, String attribute, HashMap<DataEntry, String> dataClassifyer) {
		this.name = name;
		generateFrequenzyTable(attribute, dataClassifyer);
	}

	public int getAttributeValueCount(Integer value) {
		return valueCount.get(value);
	}

	public int getClassifyerCount(String classifyer) {
		return classifyerCount.get(classifyer);
	}

	public List<String> getClassifyerList() {
		return new ArrayList<>(frequencyTable.keySet());
	}

	public HashSet<Integer> getAttributeValueSet() {
		return attributeValueSet;
	}

	private void generateFrequenzyTable(String attribute, HashMap<DataEntry, String> data) {

		for (DataEntry dataEntry : data.keySet()) {

			if(!frequencyTable.containsKey(data.get(dataEntry))) {
				frequencyTable.put(data.get(dataEntry), new HashMap<>());	
			}
			
			HashMap<Integer, Integer> count = frequencyTable.get(data.get(dataEntry));
			
			int attributeValue = dataEntry.getAttributes().get(attribute);
			attributeValueSet.add(attributeValue);
			if (!count.containsKey(attributeValue)) {
				count.put(attributeValue, 0);
			}
			count.put(attributeValue, count.get(attributeValue)+1);

		}

		for (String key : frequencyTable.keySet()) {
			for (Integer attributeValue : attributeValueSet) {
				if (!frequencyTable.get(key).containsKey(attributeValue)) {
					frequencyTable.get(key).put(attributeValue, 0);
				}
			}
		}

		for (Integer string : attributeValueSet) {
			valueCount.put(string, 0);
		}

		for (String key : frequencyTable.keySet()) {
			int cCount = 0;

			for (Integer valueKey : frequencyTable.get(key).keySet()) {
				valueCount.put(valueKey, valueCount.get(valueKey) + frequencyTable.get(key).get(valueKey));
				cCount += frequencyTable.get(key).get(valueKey);
			}

			classifyerCount.put(key, cCount);

		}
		
		for (Integer count : classifyerCount.values()) {
			classifyerSum += count;
		}
		
		for (Integer count : valueCount.values()) {
			valueSum += count;
		}

	}

	@Override
	public String toString() {
		String str = name + ":\n";
		ArrayList<Integer> sizes = new ArrayList<>();

		ArrayList<String> classifyers = (ArrayList<String>) getClassifyerList();
		ArrayList<Integer> valueList = new ArrayList<>(attributeValueSet);

		Collections.sort(valueList);
		
		for (String lable : classifyers) {
			sizes.add(lable.trim().length());
			str += "\t|" + lable.trim();
		}
		str += "\t|";

		str += "\n--------------------------------------------------------------------------------------------------------------";

		for (int i = 0; i < valueList.size(); i++) {
			str += "\n" + valueList.get(i);
			for (String classifyer : classifyers) {
				str += "\t|" + frequencyTable.get(classifyer).get(valueList.get(i));
			}
			str += "\t|" + getAttributeValueCount(valueList.get(i));
		}

		str += "\n";
		for (String classifyer : classifyers) {
			str += "\t|" + getClassifyerCount(classifyer);
		}
		str += "\t|";

		return str;
	}

}
