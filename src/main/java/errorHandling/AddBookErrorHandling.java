package errorHandling;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.DatabaseStatements;

public class AddBookErrorHandling {
	
	static String notExistingCategory;

	public static boolean checkISBN(String isbn) {
		String regex = "(?=[0-9X]{10}$|(?=(?:[0-9]+[-]){3})[-0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[-]){4})[-0-9]{17}$)(?:97[89][-]?)?[0-9]{1,5}[-]?[0-9]+[-]?[0-9]+[-]?[0-9X]$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(isbn);
		return matcher.matches();
	}
	
	public static boolean checkPrice(String preis) {
		String regex = "\\d{1,5}\\.\\d{2}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(preis);
		return matcher.matches();
	}
	
	public static boolean checkCategories(String kategorien) {
		String regex = "^[a-z]+(,[a-z]+)*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(kategorien);
		return matcher.matches();
	}
	
	public static boolean checkCategoriesContents(String kategorien) {
		List<String> kategorieninDB = DatabaseStatements.getKategorien();
		List<String> buchkategorien = Arrays.asList(kategorien.split(","));
		for(String kategorie : buchkategorien) {
			if(!kategorieninDB.contains(kategorie)) {
				notExistingCategory = kategorie;
				return false;
			}
		}
		return true;
	}
	
	public static boolean checkIfIsbnExists(String isbn) {
		List <String> allISBNs = DatabaseStatements.getAllISBN();
		if(allISBNs.contains(isbn)) {
			return false;
		}
		return true;
	}
	
	
	public static String fehlermeldung(String isbn, String preis, String kategorien) {
		String errorMessage = "";
		if(!checkIfIsbnExists(isbn)) {
			errorMessage = "Es existiert bereits ein Buch mit angegebener ISBN.";
		}
		if(!checkISBN(isbn)) {
			errorMessage = "Die ISBN entspricht nicht dem richtigen Format. Folgende Formate werden akzeptiert: XXX-X-XXX-XXXXX-X bzw. X-XXX-XXXXX-X.";
			return errorMessage;
		}
		if(!checkPrice(preis)) {
			errorMessage = "Der Preis hat nicht das richtige Format. Akzeptiert wird folgendes Format: XX.XX z.B. 10.50";
			return errorMessage;
		}
		if(!checkCategories(kategorien)) {
			errorMessage = "Die Eingabe der Kategorien hat nicht das richtige Format. Folgendes Format wird akzeptiert: kategorie1,kategorie2,kategorie3,..."
					+ "Zudem müssen alle Buchstaben klein geschrieben werden (a-z).";
			return errorMessage;
		}
		if(!checkCategoriesContents(kategorien)) {
			errorMessage = "Folgende angegebene Kategorie ist in der Datenbank nicht vorhanden: " + notExistingCategory;
			return errorMessage;
		}
		return errorMessage;
	}
	
	
	
	
}
