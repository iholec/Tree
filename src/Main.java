import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<DataEntry> learningData = DataController.parseLearningData("data/SMSSpamCollection", "\n" ,"\\s");
        Statistic stats = new Statistic(learningData);
        stats.analyzeEntries();
        System.out.println(stats);
        //todo statistic übergeben
        //todo vergleichen
    }
}
