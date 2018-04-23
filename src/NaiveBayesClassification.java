import java.util.Map;

//todo: aufraeumenTreeClassification
public class NaiveBayesClassification extends Classification {

	@Override
	String findClassifyer(DataEntry entry) {
		double P_Spam = (double)statistic.spamCount / (double)statistic.entries.size();
		double P_Ham = (double)statistic.hamCount /(double) statistic.entries.size();

		double spamProbability = 1;
		double hamProbability = 1;
		for (String word : entry.getWordList()) {

			double wordSpamOccurrences;
			double spamFrequency = 1;
			if (statistic.spamWordStatistic.containsKey(word)) {
				wordSpamOccurrences = (double)statistic.spamWordStatistic.get(word);
				spamFrequency = (wordSpamOccurrences + 1) / ((double)statistic.spamWords + (double)statistic.uniqueWordCount); //Laplace Soothing
				spamFrequency = spamFrequency * entry.get(word);
			}
			spamProbability = spamProbability * spamFrequency;

			double wordHamOccurrences;
			double hamFrequency = 1;
			if (statistic.hamWordStatistic.containsKey(word)) {
				wordHamOccurrences = (double)statistic.hamWordStatistic.get(word);
				hamFrequency = (wordHamOccurrences + 1) / ((double)statistic.hamWords + (double)statistic.uniqueWordCount); //Laplace Soothing
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
}
