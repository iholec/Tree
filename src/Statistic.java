import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.sqrt;

public class Statistic {

    public List<DataEntry> entries = new ArrayList<>();
    public Map<String, Integer> hamWordStatistic = new HashMap<>();
    public Map<String, Integer> spamWordStatistic = new HashMap<>();

    public double averageHamLength;
    public double averageSpamLength;

    public double hamCount;
    public double spamCount;
   
    public double spamWords;
    public double hamWords;

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
            } else if (entry.getKeyValue().equals("spam")) {
                spamSum += entry.getCharacterCount();
                spamCount++;
            }
        }
        averageHamLength = hamSum / hamCount;
        averageSpamLength = spamSum / spamCount;

        for (DataEntry entry : entries) {
            if (entry.getKeyValue().equals("ham")) {
                hamDeviation += (entry.getCharacterCount() - averageHamLength)* (entry.getCharacterCount() - averageHamLength);
            } else if (entry.getKeyValue().equals("spam")) {
                spamDeviation += (entry.getCharacterCount() - averageSpamLength)*(entry.getCharacterCount() - averageSpamLength);
            }
        }

        standardDeviationHamLength = sqrt(hamDeviation / averageHamLength - 1);
        standardDeviationSpamLength = sqrt(spamDeviation / averageSpamLength - 1);

        for (DataEntry entry : entries) {
            if (entry.getKeyValue().equals("ham")) {
                fillMap(hamWordStatistic, entry);
            } else if (entry.getKeyValue().equals("spam")) {
                fillMap(spamWordStatistic, entry);
            }
        }
        getWordCount(hamCount,hamWordStatistic);
        getWordCount(spamCount, spamWordStatistic);
    }

    private void fillMap(Map<String,Integer> map, DataEntry entry){
        Map<String, Integer> smsWords = entry.getWordList();
        for (Map.Entry<String, Integer> smsWord : smsWords.entrySet()) {
            int wordCount = entry.get(smsWord.getKey());
            if (map.containsKey(smsWord.getKey())) {
                wordCount += map.get(smsWord.getKey());
                map.put(smsWord.getKey(), wordCount);
            } else {
                map.put(smsWord.getKey(), wordCount);
            }
        }
    }

    private void getWordCount(double count, Map<String,Integer> map){
        for (Map.Entry<String, Integer> word : map.entrySet()) {
            count += word.getValue();
        }
    }

    public int getCompleteWordCount(){
    	int differentWords = 0;
        for (Map.Entry<String, Integer> word : hamWordStatistic.entrySet()) {
            differentWords ++;
        }
        for (Map.Entry<String, Integer> word : spamWordStatistic.entrySet()) {
            if(!hamWordStatistic.containsKey(word.getKey())){
                differentWords ++;
            }
        }
        return differentWords;
    }
    
    @Override
    public String toString() {
    	getCompleteWordCount();
        //printMaps();
    	String str = "";
    	str += "Total: "+ getCompleteWordCount() + "\n";
    	str += "Ham: "+ hamCount + "\n";
    	str += "Spam: "+ spamCount + "\n";
    	return str;
    }

    private void printMaps(){
        System.out.println("Ham Word Statistic");
        for (Map.Entry<String, Integer> word : hamWordStatistic.entrySet()) {
            System.out.println(word.getKey() + ": "  + word.getValue());
        }
        System.out.println("Spam Word Statistic");
        for (Map.Entry<String, Integer> word : spamWordStatistic.entrySet()) {
            System.out.println(word.getKey() + ": "  + word.getValue());
        }
    }

}
