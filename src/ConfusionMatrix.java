import java.util.ArrayList;
import java.util.Collections;

public class ConfusionMatrix {

	private ArrayList<String> labels = new ArrayList<>();

	private int confusionMatrix[][];

	ConfusionMatrix(ArrayList<String> labels) {
		Collections.sort(labels);
		this.labels = labels;
		confusionMatrix = new int[labels.size()][labels.size()];
	}

	public void setValue(String predictedLables, String realLables, int value) {
		confusionMatrix[labels.indexOf(predictedLables)][labels.indexOf(realLables)] = value;
	}

	void addToValue(String predictedLables, String realLables, int value) {
		confusionMatrix[labels.indexOf(predictedLables)][labels.indexOf(realLables)] += value;
	}

	public int getValue(String lable1, String lable2) {
		return confusionMatrix[labels.indexOf(lable1)][labels.indexOf(lable2)];
	}

	@Override
	public String toString() {
		String str = "";
		ArrayList<Integer> sizes = new ArrayList<>();

		for (String lable : labels) {
			sizes.add(lable.trim().length());
			str += "\t|" + lable.trim();
		}
		
		str += "\n------------------------------------------------------------";

		for (int i = 0; i < confusionMatrix.length; i++) {
			str += "\n" + labels.get(i).trim();
			for (int entry : confusionMatrix[i]) {
				str += "\t|" + entry;
			}
		}

		return str;
	}

}
