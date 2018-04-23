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
		return buildDecisionTrees();
	}

	private String buildDecisionTrees() {
		//TODO filter data auf falsche baum zweige
		FrequenzyTable fT = new FrequenzyTable("wordcount", "wordcount",data);

		//Entropy
		double entropy = 0;
		for (Integer count : fT.classifyerCount.values()) {
			entropy -= ((double)count)/((double)fT.classifyerSumm) * Math.log((double)count)/((double)fT.classifyerSumm)/Math.log(2);
		}
		System.out.println("Entropy: "+entropy);
		
		for (String dataBag : fT.valueCount.keySet()) {
			
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
	}

}
