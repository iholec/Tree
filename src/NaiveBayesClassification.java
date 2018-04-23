import java.util.ArrayList;
import java.util.Map;

//todo: aufraeumenTreeClassification
public class NaiveBayesClassification extends Classification {

	@Override
	String findClassifyer(DataEntry entry) {
		double P_Spam = statistic.spamCount / statistic.entries.size();
		double P_Ham = statistic.hamCount / statistic.entries.size();

		double spamProbability = 1;
		double hamProbability = 1;
		for (String word : entry.getWordList()) {

			double wordSpamOccurrences;
			double spamFrequency = 1;
			if (statistic.spamWordStatistic.containsKey(word)) {
				wordSpamOccurrences = statistic.spamWordStatistic.get(word);
				spamFrequency = (wordSpamOccurrences + 1) / (statistic.spamWords + statistic.uniqueWordCount); //Laplace Soothing
				spamFrequency = spamFrequency * entry.get(word);
			}
			spamProbability = spamProbability * spamFrequency;

			double wordHamOccurrences;
			double hamFrequency = 1;
			if (statistic.hamWordStatistic.containsKey(word)) {
				wordHamOccurrences = statistic.hamWordStatistic.get(word);
				hamFrequency = (wordHamOccurrences + 1) / (statistic.hamWords + statistic.uniqueWordCount); //Laplace Soothing
				hamFrequency = hamFrequency * entry.get(word);
			}
			hamProbability = hamProbability * hamFrequency;

		}
		double isSpam = spamProbability * P_Spam;
		double isHam = hamProbability * P_Ham;

		if (isHam > isSpam) {//im Zweifelsfall ist es Ham
			//System.out.println("Entry: " + entry + "\n was classified as Ham");
			return "ham";
		} else {
			//System.out.println("Entry: " + entry + "\n was classified as Spam");
			return "spam";
		}
	}

	@Override
	void init(ArrayList<DataEntry> trainingsData) {
		// TODO Auto-generated method stub
		
	}
}
