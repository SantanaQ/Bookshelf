package errorhandling;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.DatabaseStatements;

public class AddCategoryErrorHandling {
	
	private String existingCategory;
	private boolean categoryOK;
	private boolean contentOK;
	
	public AddCategoryErrorHandling() {
		this.categoryOK = true;
		this.contentOK = true;
	}

	public boolean checkCategory(String kategorie) {
		String regex = "^[a-z]+$"; //
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
		List<String> kategorieninDB = dbstatements.getKCategories();
		if(kategorieninDB.contains(kategorie)) {
			existingCategory = kategorie;
			contentOK = false;
			return false;
		}
		return true;
	}
	
	
	public  String fehlermeldung() {
		String errorMessage = "";
		if(!categoryOK) {
			errorMessage = "Die Eingabe der Kategorie hat nicht das richtige Format. Es sind nur Kleinbuchstaben erlaubt (a-z).";
		}
		if(!contentOK) {
			errorMessage = "Folgende angegebene Kategorie ist in der Datenbank bereits vorhanden: " + existingCategory;
		}
		return errorMessage;
	}
	
}
