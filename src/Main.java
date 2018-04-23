import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {

		ArrayList<DataEntry> learningData = DataController.parseLearningData("data/SMSSpamCollection", "\n", "\\s");
		NaiveBayesClassification algorithm = new NaiveBayesClassification(1);

//		ArrayList<DataEntry> testData100 = new ArrayList<>();
//		for (int i = 0; i < 100; i++) {
//			testData100.add(learningData.get(i % learningData.size()));
//		}
//
//		algorithm.learn(testData100);
//		algorithm.classify();

		//Statistic stats = new Statistic(learningData);
		//stats.analyzeEntries();
		//System.out.println(stats);

		//todo statistic übergeben
		//todo vergleichen
	}
}
