import java.util.Map;

//todo: aufraeumenTreeClassification
public class NaiveBayesClassification extends Classification {

	@Override
	String findClassifyer(DataEntry entry) {
		double P_Spam = statistic.spamCount / statistic.entries.size();
		double P_Ham = statistic.hamCount / statistic.entries.size();

		double spamProbability = 1;
		double hamProbability = 1;
		for (Map.Entry<String, Integer> word : entry.getWordList().entrySet()) {
			double wordSpamOccurrences;
			double spamFrequency = 1;
			if (statistic.spamWordStatistic.containsKey(word.getKey())) {
				wordSpamOccurrences = statistic.spamWordStatistic.get(word.getKey());
				spamFrequency = (wordSpamOccurrences + 1) / (statistic.spamCount + statistic.differentWords); //Laplace Soothing
			}
			spamProbability = spamProbability * spamFrequency;

			double wordHamOccurrences;
			double hamFrequency = 1;
			if (statistic.spamWordStatistic.containsKey(word.getKey())) {
				wordHamOccurrences = statistic.spamWordStatistic.get(word.getKey());
				hamFrequency = (wordHamOccurrences + 1) / (statistic.hamCount + statistic.differentWords); //Laplace Soothing
			}
			hamProbability = hamProbability * hamFrequency;
		}
		double isSpam = spamProbability * P_Spam;
		double isHam = hamProbability * P_Ham;

		if (isHam >= isSpam) {//im Zweifelsfall ist es Spam
			//System.out.println("Entry: " + entry + "\n was classified as Ham");
			return "ham";
		} else {
			//System.out.println("Entry: " + entry + "\n was classified as Spam");
			return "spam";
		}
	}
}
