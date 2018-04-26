import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class contains and manages all DataBag objects T is the type of the key value
 */

class DataController {

	static ArrayList<SMSEntry> parseLearningData(String path, String entryDelimiter, String valueDelimiter) {
		return parseLearningData(path, entryDelimiter, valueDelimiter, -1);
	}

	private static void analyzeString(String smsText, SMSEntry entry) {
		//System.out.println(smsText);
		entry.setcharacterCount(smsText.length());

		entry.setWordsInUpperCase(countUppercase(smsText));

		smsText = smsText.toLowerCase(); //let's ignore all caps
		smsText = replaceVariableThingsThatAreActuallyTheSame(smsText);
		//Replace . , : ,
		smsText = replacePunctuationMarks(smsText);
		
		smsText = smsText.toLowerCase();
		
		String[] smsWords = smsText.split("\\s+");
		
		entry.setWordCount(smsWords.length);
		
		//System.out.println("Words: "+smsWords.length);
		for (String smsWord : smsWords) {
			//System.out.print(smsWord + " ");
			if (entry.containsWord(smsWord)) {
				entry.add(smsWord, entry.get(smsWord) + 1);
			} else {
				boolean spellcheck = false;
				for (String word : entry.getWordList()) {
					if (StringCompare.compare(smsWord, word) > StringCompare.PROBABILITY) {
						//System.out.println(smsWord + " | " + word + " : " + StringCompare.compare(smsWord, word));
						entry.add(word, entry.get(word) + 1);
						spellcheck = true;
						break;
					}
				}
				if (!spellcheck) {
					entry.add(smsWord, 1);
				}
			}
		}

		//System.out.println("\n" + entry.getWordList());
		//System.out.println(smsWords.length + " | " + entry.getWordList().keySet().size());

	}

	private static int countUppercase(String smsText) {
		int count = 0;

		String temp = smsText = replacePunctuationMarks(smsText);

		String[] smsWords = temp.split("\\s+");
		for (String smsWord : smsWords) {
			boolean hasLetters = false;
			for (Character letter : smsWord.toCharArray()) {
				if (Character.isAlphabetic(letter)) {
					hasLetters = true;
					break;
				}
			}

			if (hasLetters) {
				boolean hasLower = false;
				for (Character letter : smsWord.toCharArray()) {
					if (Character.isLowerCase(letter)) {
						hasLower = true;
						break;
					}
				}
				if (!hasLower) {
					count++;
				}
			}
		}
		return count;
	}

	private static String replacePunctuationMarks(String smsText) {
		smsText = smsText.replace("\\.", " ");
		smsText = smsText.replace(",", " ");
		smsText = smsText.replace("!", " ");
		smsText = smsText.replace("?", " ");
		smsText = smsText.replace(":", " ");
		smsText = smsText.replace(";", " ");
		smsText = smsText.replace(".", " ");
		smsText = smsText.replace("(", " ");
		smsText = smsText.replace(")", " ");
		smsText = smsText.replace("[", " ");
		smsText = smsText.replace("]", " ");
		smsText = smsText.replace(" '", " ");
		smsText = smsText.replace("' ", " ");
		smsText = smsText.replace("\"", " ");
		smsText = smsText.replace("<", " ");
		smsText = smsText.replace(">", " ");
		smsText = smsText.replace(" @", " ");
		smsText = smsText.replace("&", "and");
		return smsText;
	}

	private static String replaceVariableThingsThatAreActuallyTheSame(String smsText) {
		//todo:  preisangaben durch Wort ersetzten
		smsText = smsText.replaceAll("(euros?|pounds?|dollars?|[€$£p]) ?\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})?", " #priceReplacement ");
		smsText = smsText.replaceAll("\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})? ?(euros?|pounds?|dollars?|[€$£p])", " #priceReplacement ");
		//System.out.println("Replacing hidden telephone numbers of sms : \n" + smsText);
		smsText = smsText.replaceAll("[+]*\\d{2,}([xX]{3,}|[*]{3,}[0-9]*)", " #numberReplacementHidden ");
		//System.out.println(smsText);
		//System.out.println("Replacing telephone numbers of sms : \n" + smsText);
		smsText = smsText.replaceAll("[+]*\\d{5,}", " #numberReplacement ");//5 is minimum phone number length
		//System.out.println(smsText);
		//System.out.println("Replacing weird stars of sms : \n" + smsText);
		smsText = smsText.replaceAll("[*]{5,}", " #numberReplacement ");//5 is minimum phone number length/more than 5 stars without stuff always spam
		//#System.out.println(smsText);
		//System.out.println("Replacing emails of sms : \n" + smsText);
		smsText = smsText.replaceAll("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])", " #emailReplacement "); //this is fine
		//System.out.println(smsText);
		//System.out.println("Replacing http sites of sms : \n" + smsText);
		smsText = smsText.replaceAll("\\. ", "."); //Fix misspelled urls
		smsText = smsText.replaceAll("[A-Za-z]*https?[:]*//[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", " #websiteReplacementHttp "); //ALWAYS Spam
		//System.out.println(smsText);
		//System.out.println("Replacing web sites of sms : \n" + smsText);
		smsText = smsText.replaceAll("[A-Za-z/:]*www.[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", " #websiteReplacementNormal ");
		//System.out.println(smsText);
		//Except for 1 ALWAYS Spam, would also filter http -> needs to be done afterwards
		return smsText;
	}

	private static ArrayList<SMSEntry> parseLearningData(String path, String entryDelimiter, String valueDelimiter, int indexOfKey) {

		//todo: only split after first whitespace

		ArrayList<SMSEntry> data = new ArrayList<>();

		try {
			BufferedReader bfr = new BufferedReader(new FileReader(path));
			int character;
			StringBuilder line = new StringBuilder();
			boolean stop = false;

			while (!stop) {
				stop = true;
				String endBuffer = "";
				while ((character = bfr.read()) != -1) {
					stop = false;
					endBuffer += "" + (char) character;

					if (!entryDelimiter.startsWith(endBuffer)) {
						line.append(endBuffer);
						endBuffer = "";
					}

					if (entryDelimiter.equals(endBuffer)) {
						break;
					}
				}

				if (line.toString().split(valueDelimiter, 2).length > 1) {

					if (indexOfKey < 0) {
						indexOfKey = line.toString().split(valueDelimiter, 2).length - 1;
					}

					SMSEntry dataEntry = new SMSEntry();
					for (int i = 0; i < line.toString().split(valueDelimiter, 2).length; i++) {
						if (i == indexOfKey) {
							analyzeString(line.toString().split(valueDelimiter, 2)[i].trim(), dataEntry);
						} else {
							dataEntry.setKeyValue(line.toString().split(valueDelimiter, 2)[i].trim());
							//System.out.println(dataEntry.getKeyValue() + " ");
						}
					}
					data.add(dataEntry);
				}

				line = new StringBuilder();

			}
			bfr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(data.size() + " lines of data loaded.");

		return data;
	}

}
