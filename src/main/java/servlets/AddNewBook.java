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
			
			//TODO: Inputs auf Richtigkeit prüfen und gute Fehlermeldungen
			
			DatabaseStatements.addBook(con, isbn, titel, autor, beschreibung, preis, coverStream);
			//TODO: Fix diese adds, weil Tabellen nicht gefüllt werden
			DatabaseStatements.addCategory(con, kategorien);
			DatabaseStatements.addBookcategories(con, isbn, kategorien);
			
			
            PrintWriter out = response.getWriter();
            File file = new File("C:\\Users\\flobo\\git\\bookshelf\\src\\main\\webapp\\SuccessfullyAdded.html");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            	   String line;
            	   while ((line = br.readLine()) != null) {
            	       out.println(line);
            	   }
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
	
	/*private String checkISBN(String isbn) {
		String match1 = ""
	}*/

}
