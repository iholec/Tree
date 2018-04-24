import java.util.ArrayList;
import java.util.Map;

//todo: aufraeumenTreeClassification
public class NaiveBayesClassification extends Classification {

	public static long factorial(int number) {
		long result = 1;

		for (int factor = 2; factor <= number; factor++) {
			result *= factor;
		}

		return result;
	}

	@Override
	String findClassifyer(DataEntry entry) {
		double P_Spam = Math.log((double)statistic.spamCount / (double)statistic.entries.size());
		double P_Ham = Math.log((double)statistic.hamCount /(double) statistic.entries.size());

		double spamProbability = 1;
		double hamProbability = 1;

		for (String word : entry.getWordList()) {

			double wordSpamOccurrences;
			double spamFrequency = 1;
			if (statistic.spamWordStatistic.containsKey(word)) {
				wordSpamOccurrences = (double)statistic.spamWordStatistic.get(word);
				spamFrequency = Math.log((wordSpamOccurrences + 1) / ((double)statistic.spamWords + (double)statistic.uniqueWordCount)); //Laplace Smoothing
				for(int i = 1; i < entry.get(word); i++) {
					spamFrequency = spamFrequency * spamFrequency;
				}
			}
			spamProbability = spamProbability * spamFrequency;

			double wordHamOccurrences;
			double hamFrequency = 1;
			if (statistic.hamWordStatistic.containsKey(word)) {
				wordHamOccurrences = (double)statistic.hamWordStatistic.get(word);
				hamFrequency = Math.log((wordHamOccurrences + 1) / ((double)statistic.hamWords + (double)statistic.uniqueWordCount)); //Laplace Smoothing
				for(int i = 1; i < entry.get(word); i++){
					hamFrequency = hamFrequency * hamFrequency;
				}
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
