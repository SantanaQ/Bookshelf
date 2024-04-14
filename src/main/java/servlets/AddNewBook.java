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

	private boolean inputCorrect = true;
	
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
			Connection con = DatabaseConnection.initializeDatabase();
			
			inputCorrect = checkISBN(isbn) && checkPrice(pr) && checkCategories(kategorien);
			//TODO: Inputs auf Richtigkeit prüfen und gute Fehlermeldungen
			
			DatabaseStatements.addBook(con, isbn, titel, autor, beschreibung, preis, coverStream);
			DatabaseStatements.addCategory(con, kategorien);
			DatabaseStatements.addBookcategories(con, isbn, kategorien);
			
			
            PrintWriter out = response.getWriter();
            if(inputCorrect) {
                File file = new File("C:\\Users\\Anwender\\git\\bookshelf\\src\\main\\webapp\\SuccessfullyAdded.html");
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                	   String line;
                	   while ((line = br.readLine()) != null) {
                	       out.println(line);
                	   }
                	}
            }else {
            	//Weiterleitung zur Fehlermeldungsseite
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
			errorMessage = "Die Eingabe der Kategorien hat nicht das richtige Format. Folgendes Format wird akzeptiert: Kategorie1,Kategorie2,Kategorie3";
			return errorMessage;
		}
		return errorMessage;
	}

}
