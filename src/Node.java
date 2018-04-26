import java.util.ArrayList;
import java.util.HashMap;

public class Node {

	String attribute = "";

	ArrayList<Node> nodes = new ArrayList<>();

	FrequenzyTable frequenzyTable = null;

	public Node(String attribute, HashMap<DataEntry, String> data) {
		this.attribute = attribute;
		getEntropy(data);
	}

	public void addNode(Node n) {
		nodes.add(n);
	}

	public double getEntropy(HashMap<DataEntry, String> data) {
		double entropy = 0;
		frequenzyTable = new FrequenzyTable(attribute, attribute, data);
		for (Integer count : frequenzyTable.classifyerCount.values()) {
			if (count > 0) {
				entropy -= ((double) count) / ((double) frequenzyTable.classifyerSum) * Math.log((double) count / (double) frequenzyTable.classifyerSum) / Math.log(2);
			}
		}
		return entropy;
	}

	public double getInformationGain(double rootEntropy) {
		//Gain
		double informationGain = 0;

		informationGain = rootEntropy;
		for (Integer attributeValue : frequenzyTable.valueCount.keySet()) {
			ArrayList<Integer> values = new ArrayList<>();
			for (String classifyer : frequenzyTable.classifyerCount.keySet()) {
				values.add(frequenzyTable.frequencyTable.get(classifyer).get(attributeValue));
			}
			if (frequenzyTable.valueCount.get(attributeValue) > 0) {
//				System.out.println("S" + attributeValue + ": " + frequenzyTable.valueCount.get(attributeValue));
//				System.out.println("S: " + frequenzyTable.classifyerSum);
				informationGain -= ((double) frequenzyTable.valueCount.get(attributeValue) / (double) frequenzyTable.classifyerSum) * attributeValueEntropy(values, frequenzyTable.valueSum);
			}
		}

		System.out.println(attribute + " IGain: " + informationGain);

		return informationGain;
	}

	private double attributeValueEntropy(ArrayList<Integer> values, double sum) {
		double entropy = 0;
		for (Integer count : values) {
			if (count > 0) {
				entropy -= ((double) count / (double) sum) * Math.log((double) count / (double) sum) / Math.log(2);
			}
		}
		return entropy;
	}

	@Override
	public String toString() {
		String str = attribute;
		for (Node node : nodes) {
			str += "+" + node.toString() + "\n";
		}
		return str;
	}

}
