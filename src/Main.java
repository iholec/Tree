import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<DataEntry> learningData = DataController.parseLearningData(System.getProperty("user.dir")+"/data/SMSSpamCollection", "\n" ,"\\s");
        //todo statistic groß
        //todo statistic übergeben
        //todo vergleichen
    }
}
