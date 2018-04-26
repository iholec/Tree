import java.util.LinkedList;

public class Main {

	public static void main(String[] args) {

		//ArrayList<DataEntry> learningData = DataController.parseLearningData("data/SMSSpamCollection", "\n", "\\s");

		//NaiveBayesClassification algorithm = new NaiveBayesClassification();
		//TreeClassification algorithm = new TreeClassification();
		//
		//		algorithm.learn(learningData);

		DataTable<String, String, Integer> testTable = new DataTable<>();

		for (int i = 1; i <= 5; i++) {
			LinkedList<Integer> temp = new LinkedList<>();
			for (int c = 0; c < i; c++) {
				temp.add(i);
			}
			testTable.addRow("" + i, temp);
		}
		
		System.out.println(testTable);

		//algorithm.init(testData);

		//		ArrayList<DataEntry> testData1000 = new ArrayList<>();
		//		for (int i = 0; i < 1000; i++) {
		//			testData1000.add(learningData.get(i % learningData.size()));
		//		}
		//		ArrayList<DataEntry> testData10000 = new ArrayList<>();
		//		for (int i = 0; i < 10000; i++) {
		//			testData10000.add(learningData.get(i % learningData.size()));
		//		}
		//		ArrayList<DataEntry> testData100 = new ArrayList<>();
		//		for (int i = 0; i < 100; i++) {
		//			testData100.add(learningData.get(i % learningData.size()));
		//		}
		//
		//		algorithm.classify(testData100);

		//		Statistic stats = new Statistic(learningData);
		//		stats.analyzeEntries();
		//		System.out.println(stats);

		//todo statistic übergeben
		//todo vergleichen
	}
}