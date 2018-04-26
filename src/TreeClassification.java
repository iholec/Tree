import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

//todo: aufraeumenTreeClassification
public class TreeClassification extends Classification {

	HashMap<DataEntry, String> data = new HashMap<DataEntry, String>();

	ArrayList<String> attributes = new ArrayList<>();

	HashMap<String, Integer> attributeCount = new HashMap<>();

	Node root = null;

	@Override
	String findClassifyer(DataEntry entry) {
		return null;
	}

	/**
	 * Finds the best classifyer
	 * 
	 * @param root
	 *            the possible attributes for the IGain
	 * @param data
	 *            The data to build the Tree on
	 * @return The best fitting classifyer
	 */
	private String buildDecisionTrees(Node root, HashMap<DataEntry, String> data) {

		System.out.println("Node: " + root);

		//Abbruchbedingungen
		if (checkSingleClassifyer(data) != null) {
			return checkSingleClassifyer(data); //buildDecisionTrees(attributes, data);
		} else if (data.isEmpty() || (root == null)) { //TODO testen
			//Häufigsten classifyer finden
			HashMap<String, Integer> classifyerCount = new HashMap<>();

			for (String classifyer : data.values()) {
				if (!classifyerCount.containsKey(classifyer)) {
					classifyerCount.put(classifyer, 0);
				}
				classifyerCount.put(classifyer, classifyerCount.get(classifyer) + 1);
			}
			int maxCount = 0;
			String result = "";
			for (String countKey : classifyerCount.keySet()) {
				if (classifyerCount.get(countKey) > maxCount) {
					maxCount = classifyerCount.get(countKey);
					result = countKey;
				}
			}

			return result;

		} else {
			Node nextLeaf = null;
			double maxGain = 0;

			//Entropy
			double entropyRoot = 0;

			for (Integer attributeValue : root.frequenzyTable.attributeValueSet) {

				System.out.println(root.attribute + " - " + attributeValue + ": ");

				double gain = 0;

				HashMap<DataEntry, String> nextData = new HashMap<>(data);

				DataEntry[] dataKeys = nextData.keySet().toArray(new DataEntry[nextData.keySet().size()]);

				for (int i = dataKeys.length - 1; i >= 0; i--) {
					if (dataKeys[i].getAttributes().get(root.attribute) != attributeValue) {
						nextData.remove(dataKeys[i]);
					}
				}

				entropyRoot = root.getEntropy(nextData);

				//Alle Attribute --> bester gain
				for (String attribute : attributes) {
					Node node = new Node(attribute, nextData);
					gain = node.getInformationGain(entropyRoot);

					if (maxGain < gain) {
						maxGain = gain;
						nextLeaf = node;
					}
				}

				return buildDecisionTrees(nextLeaf, nextData);
			}
		}
		return null; //TODD ist so nicht gut (wegen rekursiv und so)

	}

	private String checkSingleClassifyer(HashMap<DataEntry, String> data) {
		HashSet<String> classifyers = new HashSet<>(data.values());

		if (classifyers.size() == 1) {
			return classifyers.toArray(new String[classifyers.size()])[0];
		}

		return null;
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

		System.out.println(root);
		System.out.println("");
	}

	void startDecisionTree() {
		//first entropy
		double entropy = 0;

		HashMap<String, Integer> classifyerCount = new HashMap<>();
		for (DataEntry dataEntry : data.keySet()) {
			if (!classifyerCount.containsKey(dataEntry.getKeyValue())) {
				classifyerCount.put(dataEntry.getKeyValue(), 0);
			}
			classifyerCount.put(dataEntry.getKeyValue(), classifyerCount.get(dataEntry.getKeyValue()) + 1);
		}

		int sum = 0;

		for (Integer count : classifyerCount.values()) {
			sum += count;
		}

		for (String classifyer : classifyerCount.keySet()) {
			if (classifyerCount.get(classifyer) >= 0) {
				entropy -= ((double) classifyerCount.get(classifyer)) / ((double) sum) * Math.log((double) classifyerCount.get(classifyer) / (double) sum) / Math.log(2);
			}
		}

		double maxGain = 0;
		for (String attribute : attributes) {
			Node n = new Node(attribute, data);
			double gain = n.getInformationGain(entropy);
			if (gain > maxGain) {
				root = n;
				maxGain = gain;
			}

		}

		System.out.println();

		//Start tree
		buildDecisionTrees(root, data);
	}

}
