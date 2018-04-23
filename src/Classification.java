import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class Classification implements Runnable {

	private Thread th;
	private Date start;

	private ArrayList<DataBag> dataBagList = new ArrayList<>();
	private HashMap<String, Integer> correctLearningDataValues = new HashMap<>();

	private ConfusionMatrix confusionMatrix;
	private Statistic statistic = null;

	private int dataBags = 10;
	private int k = 7;

	Classification(int k) {
		this.k = k;
		th = new Thread(this);
	}

	Classification(int k, int dataBags) {
		this(k);
		this.dataBags = dataBags;
	}

	/**
	 * Zum hinzufügen von Trainingsdaten
	 * 
	 * @param learningData
	 */
	private void addLearningData(ArrayList<DataEntry> learningData) {

		Collections.shuffle(learningData);
		
		statistic = new Statistic(learningData);
		statistic.analyzeEntries();

		int bagSize = learningData.size() / dataBags;

		if ((learningData.size() % dataBags) > 0) {
			bagSize++;
		}

		DataBag bag = null;
		for (int i = 0; i < learningData.size(); i++) {
			if ((i % bagSize) == 0) {
				bag = new DataBag();
				dataBagList.add(bag);
			}
			bag.addData(learningData.get(i));
		}

		System.out.println(dataBagList.size() + " data bags @ " + bagSize + " created.");
	}

	/**
	 * Zusammenfassung von add Data und start learn
	 * 
	 * @param learningData
	 */
	public void learn(ArrayList<DataEntry> learningData) {
		addLearningData(learningData);
		learn();
	}

	/**
	 * Erstellt zu den daten eine confusion matrix
	 * 
	 * @param dataBagList2
	 */
	private void createConfusionMatrix(ArrayList<DataBag> dataBagList2) {
		// Übersicht über richtig falsch; 2D array mit größe == möglichen
		// lösungen
		HashSet<String> uniqueKeys = new HashSet<>();

		for (DataBag entry : dataBagList2) {
			for (DataEntry learningDataEntry : entry) {
				uniqueKeys.add(learningDataEntry.getKeyValue());
			}
		}

		confusionMatrix = new ConfusionMatrix(new ArrayList<>(uniqueKeys));
	}

	public void classify() {
		System.out.println("Start classifying " + statistic.entries.size() + " lines of data now!");
		start = new Date(); // Zum messen der Zeit
		th.start();
	}

	@Override
	public void run() {
		for (int i = 0; i < dataBags; i++) {

			// Alle bags werden zum classifizieren verwendet
			ArrayList<DataEntry> trainingsData = new ArrayList<>();
			for (DataBag dataBag : dataBagList) {
				trainingsData.addAll(dataBag);
			}

			// find kNN
			for (DataEntry entry : statistic.entries) {
				//EuklidianDistanceComparator comperator = new EuklidianDistanceComparator(entry);
				//Collections.sort(trainingsData, comperator);

				// Zahlen der meisten vorkommsisse
				HashMap<String, Integer> classifyer = new HashMap<String, Integer>();
				for (int c = 0; c < k; c++) {
					if (!classifyer.containsKey(trainingsData.get(c).getKeyValue())) {
						classifyer.put(trainingsData.get(c).getKeyValue(), 0);
					}
					classifyer.put(trainingsData.get(c).getKeyValue(), classifyer.get(trainingsData.get(c).getKeyValue()) + 1);
				}

				// Find best fit
				int biggestValue = 0;
				String bestClassifyer = "";
				for (String key : classifyer.keySet()) {
					if (biggestValue < classifyer.get(key)) {
						biggestValue = classifyer.get(key);
						bestClassifyer = key;
					}
				}

				// System.out.println("Entry: " + entry + "\n was classified as
				// " + bestClassifyer);
			}
		}

		calculateTime();

	}

	private void calculateTime() {
		long millis = new Date().getTime() - start.getTime();
		String runTime = String.format("%d min, %d sec, %d milli sec", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)), millis % 1000);
		System.out.println("Time for classifying " + statistic.entries.size() + " lines of data: " + runTime);
	}

	/**
	 * Startet den Lernprozess
	 */
	private void learn() {
		createConfusionMatrix(dataBagList);

		ArrayList<HashMap<String, Integer>> confusionMatratzen = new ArrayList<>();

		ArrayList<Double> accuracyList = new ArrayList<>();

		// Count all possible Classifier
		for (DataBag dataBag : dataBagList) {
			for (DataEntry learningDataEntry : dataBag) {
				if (!correctLearningDataValues.containsKey(learningDataEntry.getKeyValue())) {
					correctLearningDataValues.put(learningDataEntry.getKeyValue(), 0);
				}
				correctLearningDataValues.put(learningDataEntry.getKeyValue(), correctLearningDataValues.get(learningDataEntry.getKeyValue()) + 1);
			}
		}
		System.out.println(correctLearningDataValues);

		// Training durch k-fold cross Validation

		System.out.println("Start learning now!");

		for (int i = 0; i < dataBags; i++) {
			HashMap<String, Integer> confusionM = new HashMap<String, Integer>();

			// Bag der als Test verwendet werden soll
			DataBag forTest = dataBagList.get(i);

			// Alle andern bags werden zum training verwendet
			ArrayList<DataEntry> trainingsData = new ArrayList<DataEntry>();
			for (int c = 0; c < dataBagList.size(); c++) {
				if (c != i) trainingsData.addAll(dataBagList.get(c));
			}

			// Testen
			double accuracy = 0;
			for (DataEntry entry : forTest) {
				//EuklidianDistanceComparator comperator = new EuklidianDistanceComparator(entry);
				//Collections.sort(trainingsData, comperator);

				// Zahlen der meisten vorkommsisse
				HashMap<String, Integer> classifyer = new HashMap<String, Integer>();
				for (int c = 0; c < k; c++) {
					if (!classifyer.containsKey(trainingsData.get(c).getKeyValue())) {
						classifyer.put(trainingsData.get(c).getKeyValue(), 0);
					}
					classifyer.put(trainingsData.get(c).getKeyValue(), classifyer.get(trainingsData.get(c).getKeyValue()) + 1);
				}

				// Find best fit
				int biggestValue = 0;
				String bestClassifyer = "";
				for (String key : classifyer.keySet()) {
					if (biggestValue < classifyer.get(key)) {
						biggestValue = classifyer.get(key);
						bestClassifyer = key;
					}
				}

				if (!confusionM.containsKey(bestClassifyer)) {
					confusionM.put(bestClassifyer, 0);
				}
				confusionM.put(bestClassifyer, confusionM.get(bestClassifyer) + 1);
				confusionMatrix.addToValue(bestClassifyer, entry.getKeyValue(), 1);
				if (entry.getKeyValue().equals(bestClassifyer)) {
					accuracy += 1;
				}
				// System.out.println("Entry: " + entry + "\n was classified as
				// " + bestClassifyer);
			}
			accuracy = accuracy / forTest.size();
			System.out.println("Accuracy: " + accuracy);
			accuracyList.add(accuracy);

			confusionMatratzen.add(confusionM);
			System.out.println(confusionM);
		}

		// TODO Confusion Matrix aus einzelnen werten berechnen (siehe Folien)

		double totalAccuracy = 0;
		for (Double acc : accuracyList) {
			totalAccuracy += acc;
		}

		totalAccuracy = totalAccuracy / accuracyList.size();

		System.out.println("Total accuracy: " + totalAccuracy);

		// for (String predicted : confusionMatrix.labels) {
		// for (String real : confusionMatrix.labels) {
		// confusionMatrix.setValue(predicted, real,
		// confusionMatrix.getValue(predicted, real) /
		// confusionMatratzen.size());
		// }
		// }

		System.out.println(confusionMatrix);

	}

}
