import java.util.Comparator;

public class EuklidianDistanceComparator implements Comparator<String>{
	
	private String toCompare = null;
	
	EuklidianDistanceComparator(String entry) {
		toCompare = entry;
	}


	@Override
	public int compare(String o1, String o2) {
		return 0;
	}
}

/**
 * Zum berechnen des nächsten Nachbarn (siehe Folien)
 */
	/*@Override
	public int compare(DataEntry test1, DataEntry test2) {

		double dist1 = 0.0d;
		double dist2 = 0.0d;

		double summ = 0;
		for(int i = 0; i < test1.size(); i++) {
			summ += Math.pow(test1.get(i) - toCompare.get(i), 2);
		}

		dist1 = Math.sqrt( summ);

		summ = 0;
		for(int i = 0; i < test2.size(); i++) {
			summ += Math.pow(test2.get(i) - toCompare.get(i), 2);
		}
		dist2 = Math.sqrt( summ);

		if (dist1 > dist2) {
		return 1;
		}
		if (dist1 < dist2) {
		return -1;
		}
		return 0;

	}*/