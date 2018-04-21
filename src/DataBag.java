import java.util.ArrayList;

/**
 * One DataBag holds a variable amount of LearningData objects depending on the
 * Data object
 */

class DataBag extends ArrayList<DataEntry> {

	void addData(DataEntry learningData) {
		this.add(learningData);
	}

}
