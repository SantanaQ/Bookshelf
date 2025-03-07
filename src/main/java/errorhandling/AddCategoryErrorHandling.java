package errorhandling;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.DatabaseStatements;

public class AddCategoryErrorHandling {
	
	private boolean categoryOK;
	private boolean contentOK;
	
	public AddCategoryErrorHandling() {
		this.categoryOK = true;
		this.contentOK = true;
	}

	public boolean checkCategory(String kategorie) {
		String regex = "^[A-Z][a-z]+$"; //
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(kategorie);
		if(!matcher.matches()) {
			categoryOK = false;
			return false;
		}

		return true;
	}
	
	
	public boolean checkCategoryContents(String kategorie) {
		DatabaseStatements dbstatements = new DatabaseStatements();
		List<String> kategorieninDB = dbstatements.getCategories();
		if(kategorieninDB.contains(kategorie)) {
			contentOK = false;
			return false;
		}
		return true;
	}
	
	
	public  String fehlermeldung() {
		String errorMessage = "";
		if(!categoryOK) {
			errorMessage = "Fehler: Die Eingabe der Kategorie hat nicht das richtige Format. "
					+ "Der Name einer Kategorie muss mit einem Großbuchstaben beginnen und darf danach nur Kleinbuchstaben "
					+ "ohne weitere Leerzeichen oder Sonderzeichen enthalten.";
		}
		if(!contentOK) {
			errorMessage = "Fehler: Die angegebene Kategorie ist in der Datenbank bereits vorhanden.";
		}
		return errorMessage;
	}
	
}
