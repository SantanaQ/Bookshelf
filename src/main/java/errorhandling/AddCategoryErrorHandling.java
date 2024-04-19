package errorhandling;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.DatabaseStatements;

public class AddCategoryErrorHandling {
	
	private static String existingCategory;

	public static boolean checkCategory(String kategorie) {
		String regex = "^[a-z]+$"; //
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(kategorie);
		return matcher.matches();
	}
	
	
	public static boolean checkCategoryContents(String kategorie) {
		DatabaseStatements dbstatements = new DatabaseStatements();
		List<String> kategorieninDB = dbstatements.getKategorien();
		if(kategorieninDB.contains(kategorie)) {
			existingCategory = kategorie;
			return false;
		}
		return true;
	}
	
	
	public static String fehlermeldung(String kategorie) {
		String errorMessage = "";
		if(!checkCategory(kategorie)) {
			errorMessage = "Die Eingabe der Kategorie hat nicht das richtige Format. Es sind nur Kleinbuchstaben erlaubt (a-z).";
			return errorMessage;
		}
		if(!checkCategoryContents(kategorie)) {
			errorMessage = "Folgende angegebene Kategorie ist in der Datenbank bereits vorhanden: " + existingCategory;
			return errorMessage;
		}
		return errorMessage;
	}
	
}
