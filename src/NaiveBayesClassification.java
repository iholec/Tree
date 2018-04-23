import java.util.*;
import java.util.concurrent.TimeUnit;


//todo: aufraeumenTreeClassification
public class NaiveBayesClassification extends Classification {

	public NaiveBayesClassification(int k) {
		super(k);
	}

	public NaiveBayesClassification(int k, int dataBags) {
		super(k, dataBags);
	}

	@Override
	public void run() {
		for (int i = 0; i < dataBags; i++) {

			// Alle bags werden zum classifizieren verwendet
			ArrayList<DataEntry> trainingsData = new ArrayList<>();
			for (DataBag dataBag : dataBagList) {
				trainingsData.addAll(dataBag);
			}

			double P_Spam = statistic.spamCount/statistic.entries.size();
			double P_Ham = statistic.hamCount/statistic.entries.size();

			for (DataEntry entry : statistic.entries) {
				double spamProbability = 1;
				double hamProbability = 1;
				for (Map.Entry<String, Integer> word : entry.getWordList().entrySet()) {
					double wordSpamOccurrences;
					double spamFrequency = 1;
					if(statistic.spamWordStatistic.containsKey(word.getKey())){
						wordSpamOccurrences = statistic.spamWordStatistic.get(word.getKey());
						spamFrequency = (wordSpamOccurrences + 1)/(statistic.spamCount + statistic.differentWords); //Laplace Soothing
					}
					spamProbability = spamProbability * spamFrequency;

					double wordHamOccurrences;
					double hamFrequency = 1;
					if(statistic.spamWordStatistic.containsKey(word.getKey())){
						wordHamOccurrences = statistic.spamWordStatistic.get(word.getKey());
						hamFrequency = (wordHamOccurrences + 1)/(statistic.hamCount + statistic.differentWords); //Laplace Soothing
					}
					hamProbability = hamProbability * hamFrequency;
				}
				double isSpam = spamProbability * P_Spam;
				double isHam = hamProbability * P_Ham;

				if(isHam >= isSpam){//im Zweifelsfall ist es Spam
					System.out.println("Entry: " + entry + "\n was classified as Ham");
				}else{
					System.out.println("Entry: " + entry + "\n was classified as Spam");
				}


			}
			}
		calculateTime();
		}
	}
