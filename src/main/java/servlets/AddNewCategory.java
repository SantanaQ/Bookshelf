package servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.*;

/**
 * Servlet implementation class AddNewCategory
 */

@WebServlet("/AddNewCategory")
public class AddNewCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    Connection con;   
	private boolean inputCorrect = true;
	private String existingCategory = "";

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String kategorie = request.getParameter("newcategory");
		
		try {
			con = DatabaseConnection.initializeDatabase();
			inputCorrect = checkCategories(kategorie) && checkCategoriesContents(kategorie);
			PrintWriter out = response.getWriter();
			
			if(inputCorrect) {
				DatabaseStatements.addCategory(con, kategorie);
				
	            File file = new File("C:\\Users\\Anwender\\git\\bookshelf\\src\\main\\webapp\\SuccessfullyAdded.html");
	            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	            	   String line;
	            	   while ((line = br.readLine()) != null) {
	            	       out.println(line);
	            	   }
	            	}
			}
			else {
				out.println(reloadForm(kategorie));
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
			if(kategorieninDB.contains(kategorie)) {
				existingCategory = kategorie;
				return false;
			}
		}
		return true;
	}
	
	private String fehlermeldung(String kategorien) {
		String errorMessage = "";
		if(!checkCategories(kategorien)) {
			errorMessage = "Die Eingabe der Kategorien hat nicht das richtige Format. Folgendes Format wird akzeptiert: kategorie1,kategorie2,kategorie3,..."
					+ "Zudem müssen alle Buchstaben klein geschrieben werden (a-z).";
			return errorMessage;
		}
		if(!checkCategoriesContents(kategorien)) {
			errorMessage = "Folgende angegebene Kategorie ist in der Datenbank vorhanden: " + existingCategory;
			return errorMessage;
		}
		return errorMessage;
	}
	
	private String reloadForm(String kategorien) {
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
				+ "		<h2>Ein neues Buch hinzufügen:</h2>\r\n"
				+ "		<input class=\"formval\" type=\"text\" name=\"isbn\" required placeholder=\"ISBN\" minlength=\"13\" maxlength=\"17\">"
				+ "		<input class=\"formval\" type=\"text\" name=\"titel\" required placeholder=\"Titel\">"
				+ "		<input class=\"formval\" type=\"text\" name=\"autor\" required placeholder=\"Autor\">"
				+ "		<textarea class=\"beschreibung\" name=\"beschreibung\" rows=\"25\" cols=\"1\" required placeholder=\"Beschreibung des Buchs...\"></textarea>"
				+ "		<input class=\"formval\" type=\"text\" name=\"preis\" required placeholder=\"Preis (€)\">"
				+ "		<input class=\"formval\" type=\"text\" name=\"kategorie\" required placeholder=\"Kategorie(n)\">"
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
				+ "		<h2>Fehler: " + fehlermeldung(kategorien)
				+ "		<h2>Eine neue Kategorie hinzufügen:</h2>\r\n"
				+ "		<input class=\"formval\" type=\"text\" name=\"newcategory\" required placeholder=\"Kategoriename\" value="+ kategorien +">\r\n"
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
