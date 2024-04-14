package servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import database.*;

/**
 * Servlet implementation class AddNewBook
 */
@WebServlet("/AddNewBook")
@MultipartConfig
public class AddNewBook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Connection con;
	private boolean inputCorrect = true;
	private String notExistingCategory = "";
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String isbn = request.getParameter("isbn");
		String titel = request.getParameter("titel");
		String autor = request.getParameter("autor");
		String beschreibung = request.getParameter("beschreibung");
		String pr = request.getParameter("preis");
		BigDecimal preis = new BigDecimal(pr);
		String kategorien = request.getParameter("kategorie");
		Part cover = request.getPart("titelbild");
		InputStream coverStream = cover.getInputStream();
		
	
		
		try {
			con = DatabaseConnection.initializeDatabase();
			
			inputCorrect = checkISBN(isbn) && checkPrice(pr) && checkCategories(kategorien) && checkCategoriesContents(kategorien);
			//TODO: Inputs auf Richtigkeit prüfen und gute Fehlermeldungen
		
            PrintWriter out = response.getWriter();
            if(inputCorrect) {
    			DatabaseStatements.addBook(con, isbn, titel, autor, beschreibung, preis, coverStream);
    			DatabaseStatements.addBookcategories(con, isbn, kategorien);
                File file = new File("C:\\Users\\Anwender\\git\\bookshelf\\src\\main\\webapp\\SuccessfullyAdded.html");
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                	   String line;
                	   while ((line = br.readLine()) != null) {
                	       out.println(line);
                	   }
                	}
            }else {
            	out.println(reloadForm(isbn, titel, autor, beschreibung, pr, kategorien));
            }

            
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (coverStream != null) {
                try {
                    coverStream.close();
                } catch (IOException io) {
                	io.printStackTrace();
                }
            }
		}
		
	}
	
	private boolean checkISBN(String isbn) {
		String regex = "(?=[0-9X]{10}$|(?=(?:[0-9]+[-]){3})[-0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[-]){4})[-0-9]{17}$)(?:97[89][-]?)?[0-9]{1,5}[-]?[0-9]+[-]?[0-9]+[-]?[0-9X]$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(isbn);
		return matcher.matches();
	}
	
	private boolean checkPrice(String preis) {
		String regex = "\\d{1,5}\\.\\d{2}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(preis);
		return matcher.matches();
	}
	
	private boolean checkCategories(String kategorien) {
		String regex = "^[a-z]+(,[a-z]+)*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(kategorien);
		return matcher.matches();
	}
	
	private boolean checkCategoriesContents(String kategorien) {
		List<String> kategorieninDB = DatabaseStatements.getKategorien(con);
		List<String> buchkategorien = Arrays.asList(kategorien.split(","));
		for(String kategorie : buchkategorien) {
			if(!kategorieninDB.contains(kategorie)) {
				notExistingCategory = kategorie;
				return false;
			}
		}
		return true;
	}
	
	
	private String fehlermeldung(String isbn, String preis, String kategorien) {
		String errorMessage = "";
		if(!checkISBN(isbn)) {
			errorMessage = "Die ISBN entspricht nicht dem richtigen Format. Folgende Formate wird akzeptiert: XXX-X-XXX-XXXXX-X bzw. X-XXX-XXXXX-X.";
			return errorMessage;
		}
		if(!checkPrice(preis)) {
			errorMessage = "Der Preis hat nicht das richtige Format. Akzeptiert wird folgendes Format: XX.XX z.B. 10.50";
			return errorMessage;
		}
		if(!checkCategories(kategorien)) {
			errorMessage = "Die Eingabe der Kategorien hat nicht das richtige Format. Folgendes Format wird akzeptiert: Kategorie1,Kategorie2,Kategorie3,...";
			return errorMessage;
		}
		if(!checkCategoriesContents(kategorien)) {
			errorMessage = "Folgende angegebene Kategorie ist in der Datenbank nicht vorhanden: " + notExistingCategory;
			return errorMessage;
		}
		return errorMessage;
	}
	
	private String reloadForm(String isbn, String titel, String autor, String beschreibung, String preis, String kategorien) {
		String html = 
				"<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "<meta charset=\"UTF-8\">\r\n"
				+ "<link rel=\"stylesheet\" href=\"styles/AddBookForm.css\">\r\n"
				+ "<link rel=\"stylesheet\" href=\"styles/general.css\">\r\n"
				+ "<link rel=\"stylesheet\" href=\"styles/header.css\">\r\n"
				+ "\r\n"
				+ "<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\r\n"
				+ "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\r\n"
				+ "    <link href=\"https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap\" rel=\"stylesheet\">\r\n"
				+ "\r\n"
				+ "<title>Neues Buch hinzufügen</title>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "  <!--Anfang Header-->\r\n"
				+ "  <div class=\"headerr-onlylogo\">\r\n"
				+ "    <div class=\"items-onlylogo\">\r\n"
				+ "      <div class=\"logo-box\">\r\n"
				+ "        <a href=\"AddBookForm.html\">\r\n"
				+ "          <img class=\"logo\" src=\"https://github.com/SantanaQ/Internet-Technologien/blob/main/images/logo.png?raw=true\" alt=\"logo\">\r\n"
				+ "        </a>\r\n"
				+ "      </div>\r\n"
				+ "    </div>\r\n"
				+ "    <div class=\"rightt\"></div>\r\n"
				+ "  </div>\r\n"
				+ "  <!--Ende Header-->\r\n"
				+ "\r\n"
				+ "<div class=\"main\">\r\n"
				+ "<form action=\"./AddNewBook\" method=\"post\" enctype=\"multipart/form-data\">\r\n"
				+ "	<div class=\"form-content\">\r\n"
				+ "     <h1 style=\"color: red\">Fehler: " + fehlermeldung(isbn, preis, kategorien) + "</h1>\r\n"
				+ "		<h2>Ein neues Buch hinzufügen:</h2>\r\n"
				+ "		<input class=\"formval\" type=\"text\" name=\"isbn\" required placeholder=\"ISBN\" minlength=\"17\" maxlength=\"17\" value=\"" + isbn + "\">\r\n"
				+ "		<input class=\"formval\" type=\"text\" name=\"titel\" required placeholder=\"Titel\" value=\"" + titel + "\">\r\n"
				+ "		<input class=\"formval\" type=\"text\" name=\"autor\" required placeholder=\"Autor\" value\"" + autor + "\">\r\n"
				+ "		<textarea class=\"beschreibung\" name=\"beschreibung\" rows=\"25\" cols=\"1\" required placeholder=\"Beschreibung des Buchs...\">" + beschreibung + "</textarea>\r\n"
				+ "		<input class=\"formval\" type=\"text\" name=\"preis\" required placeholder=\"Preis (€)\" value=\"" + preis + "\">\r\n"
				+ "		<input class=\"formval\" type=\"text\" name=\"kategorie\" required placeholder=\"Kategorie(n)\" value=\"" + kategorien +"\">\r\n"
				+ "		<!-- image -->\r\n"
				+ "		<div class=\"titelbild-box\">\r\n"
				+ "			<p class=\"titelbild-text\">Buchcover hochladen:</p>\r\n"
				+ "			<input class=\"formval-file\" type=\"file\" name=\"titelbild\" required placeholder=\"Titelbild\">\r\n"
				+ "		</div>\r\n"
				+ "		<div class=\"submit-box\">\r\n"
				+ "			<input class=\"submit\" type=\"submit\" value=\"Buch hinzufügen\">\r\n"
				+ "		</div>\r\n"
				+ "	</div>\r\n"
				+ "</form>\r\n"
				+ "<form action=\"./AddNewCategory\" method=\"post\">\r\n"
				+ "	<div class=\"form-content\">\r\n"
				+ "		<h2>Eine neue Kategorie hinzufügen:</h2>\r\n"
				+ "		<input class=\"formval\" type=\"text\" name=\"newcategory\" required placeholder=\"Kategoriename\">\r\n"
				+ "		<div class=\"submit-box\">\r\n"
				+ "			<input class=\"submit\" type=\"submit\" value=\"Kategorie hinzufügen\">\r\n"
				+ "		</div>\r\n"
				+ "	</div>\r\n"
				+ "</form>\r\n"
				+ "</div>\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "</body>\r\n"
				+ "</html>";
				return html;
	}
	

}
