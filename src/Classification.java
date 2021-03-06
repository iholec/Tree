import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class Classification implements Runnable {

    private Thread th;
    private Date start;

    protected ArrayList<DataBag> dataBagList = new ArrayList<>();
    protected HashMap<String, Integer> correctLearningDataValues = new HashMap<>();

    protected ConfusionMatrix confusionMatrix;
    protected Statistic statistic = null;

    protected int dataBags = 10;
    
    protected ArrayList<SMSEntry> dataToClassify = new ArrayList<>();

    Classification() {
        th = new Thread(this);
    }

    Classification(int dataBags) {
    	this();
        this.dataBags = dataBags;
    }

    private void createFrequencyTable(){

    }

    /**
     * Zum hinzufügen von Trainingsdaten
     * @param learningData
     */
    private void setLearningData(ArrayList<SMSEntry> learningData) {

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
     * @param learningData
     */
    public void learn(ArrayList<SMSEntry> learningData) {
        setLearningData(learningData);
        learn();
    }

    /**
     * Erstellt zu den daten eine confusion matrix
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

    public void classify(ArrayList<SMSEntry> data) {
    	dataToClassify = data;
        System.out.println("Start classifying " + data.size() + " lines of data now!");
        start = new Date(); // Zum messen der Zeit
        th.start();
    }

//    protected void calculateTime(){
//        long millis = new Date().getTime() - start.getTime();
//        String runTime = String.format("%d min, %d sec, %d milli sec", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)), millis % 1000);
//        System.out.println("Time for classifying " + statistic.entries.size() + " lines of data: " + runTime);
//    }

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
                if (c != i)
                    trainingsData.addAll(dataBagList.get(c));
            }
            
            init(trainingsData);

            // Testen
            double accuracy = 0;
            for (DataEntry entry : forTest) {    

                String bestClassifyer = findClassifyer(entry);
                               
//            	//==================== dieser abschnitt ist kNN ==========================
//            	//EuklidianDistanceComparator comperator = new EuklidianDistanceComparator(entry);
//                //Collections.sort(trainingsData, comperator);
//            	// Zahlen der meisten vorkommsisse
//            	 HashMap<String, Integer> classifyer = new HashMap<String, Integer>();
//                for (int c = 0; c < k; c++) {
//                    if (!classifyer.containsKey(trainingsData.get(c).getKeyValue())) {
//                        classifyer.put(trainingsData.get(c).getKeyValue(), 0);
//                    }
//                    classifyer.put(trainingsData.get(c).getKeyValue(), classifyer.get(trainingsData.get(c).getKeyValue()) + 1);
//                }
//                
//
//                // Find best fit
//                int biggestValue = 0;
//                String bestClassifyer = "";
//                for (String key : classifyer.keySet()) {
//                    if (biggestValue < classifyer.get(key)) {
//                        biggestValue = classifyer.get(key);
//                        bestClassifyer = key;
//                    }
//                }
//                //======================================================================

                // classifyer setzten
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
    
	@Override
	public void run() {

		 // Alle andern bags werden zum training verwendet
        ArrayList<DataEntry> trainingsData = new ArrayList<>();
        for (DataBag dataEntrys : dataBagList) {
			trainingsData.addAll(dataEntrys);
		}
        
        init(trainingsData);
		
		// find kNN
		for (SMSEntry entry : dataToClassify) {
			String bestClassifyer = findClassifyer(entry);
			
			// System.out.println("Entry: " + entry + "\n was classified as
			// " + bestClassifyer);

		}

		long millis = new Date().getTime() - start.getTime();
		String runTime = String.format("%d min, %d sec, %d milli sec", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)), millis % 1000);
		System.out.println("Time for classifying " + dataToClassify.size() + " lines of data: " + runTime);

	}

	abstract void init(ArrayList<DataEntry> trainingsData);

	abstract String findClassifyer(DataEntry entry);

}
