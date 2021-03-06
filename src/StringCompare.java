import java.util.ArrayList;

public class StringCompare {

	public static final double PROBABILITY = 0.85;

	/**
	 * Returns the distance between the two words
	 * 
	 * @param words
	 *            to compare
	 * @return number between 0 and 1. 0 = same word, 1 = no similarities
	 */
	public static double compare(String string1, String string2) {

		ArrayList<Character> s1 = new ArrayList<>();

		for (Character character : string1.toCharArray()) {
			s1.add(character);
		}

		ArrayList<Character> s2 = new ArrayList<>();

		for (Character character : string2.toCharArray()) {
			s2.add(character);
		}

		ArrayList<Character> sameLetters = new ArrayList<>();

		ArrayList<Character> temp1 = new ArrayList<>(s1);
		ArrayList<Character> temp2 = new ArrayList<>(s2);

		for (int i = temp1.size() - 1; i >= 0; i--) {
			for (int c = temp2.size() - 1; c >= 0; c--) {
				if (temp1.get(i).equals(temp2.get(c))) {
					sameLetters.add(temp1.get(i));
					temp1.remove(i);
					temp2.remove(c);
					break;
				}
			}
		}

		ArrayList<Character> combinedetters = new ArrayList<>(s2);

		temp1 = new ArrayList<>(s1);
		temp2 = new ArrayList<>(s2);

		for (int i = temp1.size() - 1; i >= 0; i--) {
			boolean letterFound = false;
			for (int c = temp2.size() - 1; c >= 0; c--) {
				if (temp1.get(i).equals(temp2.get(c))) {
					letterFound = true;
					temp1.remove(i);
					temp2.remove(c);
					break;
				}
			}
			if (!letterFound) {
				combinedetters.add(temp1.get(i));
			}
		}

		//		System.out.println(sameLetters);
		//		System.out.println(combinedetters);

		if ((double)((double) sameLetters.size() / (double) combinedetters.size()) == 1) {
			if(!string1.equals(string2)) {
				return 0;
			}
		}

		return (double) (double) sameLetters.size() / (double) combinedetters.size();
	}

}
