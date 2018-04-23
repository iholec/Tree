import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static java.lang.Math.sqrt;

public class Statistic {

	public List<DataEntry> entries = new ArrayList<>();
	public Map<String, Integer> hamWordStatistic = new HashMap<>();
	public Map<String, Integer> spamWordStatistic = new HashMap<>();

	public double averageHamLength;
	public double averageSpamLength;

	public int hamCount = 0;
	public int spamCount = 0;

	public int uniqueWordCount = 0;

	public int spamWords;
	public int hamWords;

	public double standardDeviationHamLength;
	public double standardDeviationSpamLength;

	public Statistic(List<DataEntry> data) {
		entries = data;
	}

	public void analyzeEntries() {
		double hamSum = 0;
		double spamSum = 0;

		double hamDeviation = 0;
		double spamDeviation = 0;

		for (DataEntry entry : entries) {
			if (entry.getKeyValue().equals("ham")) {
				hamSum += entry.getCharacterCount();
				hamCount++;
				//Spellcheck
				for (String newWord : entry.getWordList().keySet()) {
					if (hamWordStatistic.containsKey(newWord)) {
						hamWordStatistic.put(newWord, hamWordStatistic.get(newWord) + entry.get(newWord));
					} else {
						boolean spellcheck = false;
						for (String keyWord : hamWordStatistic.keySet()) {
							if (StringCompare.compare(newWord, keyWord) > StringCompare.PROBABILITY) {
								//System.out.println(smsWord + " | " + word + " : " + StringCompare.compare(smsWord, word));
								hamWordStatistic.put(keyWord, hamWordStatistic.get(keyWord) + entry.get(newWord));
								spellcheck = true;
								break;
							}
						}
						if (!spellcheck) {
							hamWordStatistic.put(newWord, entry.get(newWord));
						}
					}
				}
			} else if (entry.getKeyValue().equals("spam")) {
				spamSum += entry.getCharacterCount();
				spamCount++;
				//Spellcheck
				for (String newWord : entry.getWordList().keySet()) {
					if (spamWordStatistic.containsKey(newWord)) {
						spamWordStatistic.put(newWord, spamWordStatistic.get(newWord) + entry.get(newWord));
					} else {
						boolean spellcheck = false;
						for (String keyWord : spamWordStatistic.keySet()) {
							if (StringCompare.compare(newWord, keyWord) > StringCompare.PROBABILITY) {
								//System.out.println(smsWord + " | " + word + " : " + StringCompare.compare(smsWord, word));
								spamWordStatistic.put(keyWord, spamWordStatistic.get(keyWord) + entry.get(newWord));
								spellcheck = true;
								break;
							}
						}
						if (!spellcheck) {
							spamWordStatistic.put(newWord, entry.get(newWord));
						}
					}
				}
			}
		}

		averageHamLength = (double) hamSum / (double) hamCount;
		averageSpamLength = (double) spamSum / (double) spamCount;

		for (DataEntry entry : entries) {
			if (entry.getKeyValue().equals("ham")) {
				hamDeviation += (entry.getCharacterCount() - averageHamLength) * (entry.getCharacterCount() - averageHamLength);
			} else if (entry.getKeyValue().equals("spam")) {
				spamDeviation += (entry.getCharacterCount() - averageSpamLength) * (entry.getCharacterCount() - averageSpamLength);
			}
		}

		standardDeviationHamLength = sqrt(hamDeviation / averageHamLength - 1);
		standardDeviationSpamLength = sqrt(spamDeviation / averageSpamLength - 1);

		hamCount = getWordCount(hamWordStatistic);
		spamCount = getWordCount(spamWordStatistic);
	}

	private int getWordCount(Map<String, Integer> map) {
		int count = 0;
		for (Integer wordCount : map.values()) {
			count += wordCount;
		}
		return count;
	}

	public int getUniqueWordCount() {
		if (uniqueWordCount == 0) {
			HashSet<String> differentWords = new HashSet<>();
			differentWords.addAll(hamWordStatistic.keySet());
			differentWords.addAll(spamWordStatistic.keySet());
			uniqueWordCount = differentWords.size();
		}
		return uniqueWordCount;
	}

	@Override
	public String toString() {
		getUniqueWordCount();
		//printMaps();
		String str = "";
		str += "Total: " + getUniqueWordCount() + "\n";
		str += "Ham: " + hamCount + "\n";
		str += "Spam: " + spamCount + "\n";
		return str;
	}

	private void printMaps() {
		System.out.println("Ham Word Statistic");
		for (Map.Entry<String, Integer> word : hamWordStatistic.entrySet()) {
			System.out.println(word.getKey() + ": " + word.getValue());
		}
		System.out.println("Spam Word Statistic");
		for (Map.Entry<String, Integer> word : spamWordStatistic.entrySet()) {
			System.out.println(word.getKey() + ": " + word.getValue());
		}
	}

}
