package errorhandling;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.DatabaseStatements;

public class AddBookErrorHandling {
	
	private boolean isbnNEW;
	private boolean isbnOK;
	private boolean priceOK;
	private boolean titleOK;
	private boolean authorOK;
	private boolean categoriesExist;
	private boolean categoriesSelected;
	
	public AddBookErrorHandling() {
		this.isbnNEW = true;
		this.isbnOK = true;
		this.priceOK = true;
		this.titleOK = true;
		this.authorOK = true;
		this.categoriesExist = true;
		this.categoriesSelected = true;
	}
	

	public boolean checkIfCategorySelected(String[] categoryParam) {
		if( categoryParam == null  && checkIfCategoriesinDB()) {
			categoriesSelected = false;
			return false;
		}
		return true;
	}
	
	public boolean checkIfCategoriesinDB() {
		DatabaseStatements dbstatements = new DatabaseStatements();
		List<String> categoriesInDB = dbstatements.getCategories();
		if(categoriesInDB.isEmpty()) {
			categoriesExist = false;
			return false;
		}
		return true;
	}
	
	public boolean checkIfIsbnExists(String isbn) {
		DatabaseStatements dbstatements = new DatabaseStatements();
		List <String> allISBNs = dbstatements.getAllISBN();
		if(allISBNs.contains(isbn)) {
			isbnNEW = false;
			return false;
		}
		return true;
	}
	
	public boolean checkISBN(String isbn) {
		String regex = "(?=[0-9X]{10}$|(?=(?:[0-9]+[-]){3})[-0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[-]){4})[-0-9]{17}$)(?:97[89][-]?)?[0-9]{1,5}[-]?[0-9]+[-]?[0-9]+[-]?[0-9X]$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(isbn);
		if(!matcher.matches()) {
			isbnOK = false;
			return false;
		}
		return true;
	}
	
	public boolean checkPrice(String preis) {
		String regex = "\\d{1,5}\\.\\d{2}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(preis);
		if(!matcher.matches()) {
			priceOK = false;
			return false;	
		}
		return true;
	}
	
	public boolean checkTitle(String titel) {
		String regex = "[A-Z0-9][a-zA-Z0-9 ,.:;_?!&-]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(titel);
		if(!matcher.matches()) {
			titleOK = false;
			return false;	
		}

		return true;
	}
	
	public boolean checkAuthor(String autor) {
		String regex = "[a-zA-Z .]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(autor);
		if(!matcher.matches()) {
			authorOK = false;
			return false;
		}
		return true;
	}
	
	
	public String fehlermeldung() {
		String errorMessage = "";
		if(!categoriesSelected) {
			errorMessage += "Fehler: Es ist keine Kategorie für das Buch ausgewählt. </br>";
		}
		if(!categoriesExist) {
			errorMessage += "Fehler: Buch kann nicht hinzugefügt werden, Datenbank enthält noch keine Kategorien. </br>";
		}
		if(!isbnNEW) {
			errorMessage += "Fehler: Es existiert bereits ein Buch mit angegebener ISBN. <br/>";
		}
		if(!isbnOK) {
			errorMessage += "Fehler: Die ISBN entspricht nicht dem richtigen Format. Folgende Formate werden akzeptiert: </br> 978-X-XXX-XXXXX-X bzw. X-XXX-XXXXX-X. <br/>";
		}
		if(!priceOK) {
			errorMessage += "Fehler: Der Preis hat nicht das richtige Format. Akzeptiert wird folgendes Format: XX.XX z.B. 10.50 <br/>";
		}
		if(!titleOK) {
			errorMessage += "Fehler: Der Titel muss mit einem Großbuchstaben oder einer Zahl beginnen und erlaubt nur folgende Sonderzeichen: , . : ; - _ ? & ! <br/>";
		}
		if(!authorOK) {
			errorMessage += "Fehler: Der Autor darf keine Sonderzeichen, außer '.' enthalten. <br/>";
		}
		
		return errorMessage;
	}
	
	
	
	
}
