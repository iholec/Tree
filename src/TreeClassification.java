import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

//todo: aufraeumenTreeClassification
public class TreeClassification extends Classification {

	HashMap<DataEntry, String> data = new HashMap<DataEntry, String>();

	ArrayList<String> attributes = new ArrayList<>();

	HashMap<String, Integer> attributeCount = new HashMap<>();

	@Override
	String findClassifyer(DataEntry entry) {
		return buildDecisionTrees(entry.attributes[0], data);
	}

	private String buildDecisionTrees(String attribute, HashMap<DataEntry, String> data) {
		//TODO filter data auf falsche baum zweige
		FrequenzyTable fT = new FrequenzyTable("wordcount", "wordcount", data);

		//Entropy
		double entropyS = 0;
		for (Integer count : fT.classifyerCount.values()) {
			if (count >= 0) {
				entropyS -= ((double) count) / ((double) fT.classifyerSum) * Math.log((double) count) / ((double) fT.classifyerSum) / Math.log(2);
			}
		}
		System.out.println("Entropy: " + entropyS);

		//Gain
		double gain = entropyS;
		for (String sf : fT.valueCount.keySet()) {
			ArrayList<Integer> values = new ArrayList<>();
			for (String classifyer : fT.classifyerCount.keySet()) {
				values.add(fT.frequencyTable.get(classifyer).get(sf));
			}
			gain -= ((double) fT.valueCount.get(sf)) / ((double) fT.classifyerSum) * attributeValueEntropy(values, fT.valueCount.get(sf));
		}

		System.out.println("Gain: " + gain);

		//TODO remove entrys from data
		//Find next attribute

		//Abbruchbedingungen
		String result = "";
		if ((result = checkSingleClassifyer(data)) == null) {
			return buildDecisionTrees(attribute, data);
		} else if (attribute == null) {
			//Häufigsten classifyer finden
			HashMap<String, Integer> classifyerCount = new HashMap<>();

			for (String classifyer : data.values()) {
				if (!classifyerCount.containsKey(classifyer)) {
					classifyerCount.put(classifyer, 0);
				}
				classifyerCount.put(classifyer, classifyerCount.get(classifyer) + 1);
			}
			int maxCount = 0;
			for (String countKey : classifyerCount.keySet())) {
				if(classifyerCount.get(countKey) > maxCount) {
					result = countKey;
				}
			}
		} else {
			return result;
		}

	}

	private String checkSingleClassifyer(HashMap<DataEntry, String> data) {
		HashSet<String> classifyers = new HashSet<>(data.values());

		if (classifyers.size() == 1) {
			return classifyers.toArray(new String[classifyers.size()])[0];
		}

		return null;
	}

	private double attributeValueEntropy(ArrayList<Integer> values, double sum) {
		double entropy = 0;
		for (Integer count : values) {
			if (count >= 0) {
				entropy -= ((double) count) / ((double) sum) * Math.log((double) count) / ((double) sum) / Math.log(2);
			}
		}
		return entropy;
	}

	private void buildDataTable(ArrayList<DataEntry> trainingsData) {
		HashSet<String> attributes = new HashSet<>();
		for (DataEntry dataEntry : trainingsData) {
			attributes.addAll(dataEntry.getAttributeList());
			data.put(dataEntry, dataEntry.getKeyValue());
		}
		this.attributes = new ArrayList<>(attributes);
	}

	@Override
	void init(ArrayList<DataEntry> trainingsData) {
		buildDataTable(trainingsData);
	}

}
