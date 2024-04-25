package errorhandling;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regexCheck {
	
	
	public static void main(String[] args) {
		String title1 = "Knife - Knife Meditations After an Attempted Murder";
		String title2 = "KnifeKnifeMeditationsAfteranAttemptedMurder";
		regexCheck check = new regexCheck();
		System.out.println(check.checkTitle(title1));
		System.out.println(check.checkTitle(title2));
	}

	public boolean checkTitle(String titel) {
		String regex = "[a-zA-Z ,.:;_!-]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(titel);

		return matcher.matches();
	}

}
