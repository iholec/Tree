import java.util.List;
import java.util.Map;

public class Statistic {

    public List<DataEntry> entries;
    public Map<String, Integer> hamWordStatistic;
    public Map<String, Integer> spamWordStatistic;

    public int averageHamLength;
    public int averageSpamLength;

    public int hamCount;
    public int spamCount;

    public int standardDeviationHamLength;
    public int standardDeviationSpamLength;

    public void analyzeEntries() {
        int hamSum = 0;
        int spamSum = 0;

        int hamDeviation = 0;
        int spamDeviation = 0;

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
                hamDeviation += (entry.getCharacterCount() - averageHamLength) ^ 2;
            } else if (entry.getKeyValue().equals("spam")) {
                spamDeviation += (entry.getCharacterCount() - averageSpamLength) ^ 2;
            }
        }

        standardDeviationHamLength = hamDeviation / averageHamLength - 1;
        standardDeviationSpamLength = spamDeviation / averageSpamLength - 1;

        for (DataEntry entry : entries) {
            if (entry.getKeyValue().equals("ham")) {
                fillMap(hamWordStatistic, entry);
            } else if (entry.getKeyValue().equals("spam")) {
                fillMap(spamWordStatistic, entry);
            }
        }
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

}
