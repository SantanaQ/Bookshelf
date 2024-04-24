package servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import database.*;
import errorhandling.AddBookErrorHandling;
import objects.Buch;

/**
 * Servlet implementation class AddNewBook
 */
@WebServlet("/AddNewBook")
@MultipartConfig
public class AddNewBook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private boolean inputCorrect;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		inputCorrect = true;
		out.println(reloadForm(null, "", "", "", "", "",  null ));
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String isbn = request.getParameter("isbn");
		String titel = request.getParameter("titel");
		String autor = request.getParameter("autor");
		String beschreibung = request.getParameter("beschreibung");
		String pr = request.getParameter("preis");
		List<String> kategorien = Arrays.asList(request.getParameterValues("kategorien"));
		Part cover = request.getPart("titelbild"); 
		InputStream coverStream = cover.getInputStream();
		
		response.setCharacterEncoding("UTF-8");//
		PrintWriter out = response.getWriter();
		
		AddBookErrorHandling errors = new AddBookErrorHandling();
		inputCorrect = 	   errors.checkISBN(isbn) 
						& errors.checkPrice(pr) 
						& errors.checkIfIsbnExists(isbn)
						& errors.checkAuthor(autor)
						& errors.checkTitle(titel);
				
		DatabaseStatements dbstatements = new DatabaseStatements();

		if (inputCorrect) {

			BigDecimal preis = new BigDecimal(pr);
			Buch buch = new Buch(isbn, titel, autor, beschreibung, kategorien, preis, coverStream);
			dbstatements.addBook(buch);
			dbstatements.addBookCategories(isbn, kategorien);
			ServletContext context = getServletContext();
			String filepath = context.getRealPath("/WEB-INF/html/SuccessfullyAdded.html");
			File file = new File(filepath);
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {
					out.println(line);
				}
			}
		} else {
			out.println(reloadForm(errors, isbn, titel, autor, beschreibung, pr, kategorien));
		}
		if (coverStream != null) {
			try {
				coverStream.close();
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}
	

	
	private String reloadForm(AddBookErrorHandling errors, String isbn, String titel, 
			String autor, String beschreibung, String preis, List<String> buchkategorien) {
		DatabaseStatements dbstatements = new DatabaseStatements();
		List<String> kategorien = dbstatements.getKCategories();
		String html = 
				"<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">\r\n"
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
				+ "<!--Anfang Header-->\r\n"
				+ "<div class=\"headerr-onlylogo\">\r\n"
				+ "  <div class=\"items-onlylogo\">\r\n"
				+ "    <div class=\"logo-box\">\r\n"
				+ "      <a href=\"#\">\r\n"
				+ "        <img class=\"logo\" src=\"https://github.com/SantanaQ/Internet-Technologien/blob/main/images/logo.png?raw=true\" alt=\"logo\">\r\n"
				+ "      </a>\r\n"
				+ "    </div>\r\n"
				+ "  </div>\r\n"
				+ "  <div class=\"rightt\"></div>\r\n"
				+ "</div>\r\n"
				+ "<!--Ende Header-->\r\n"
				+ "\r\n"
				+ "<div class=\"main\">\r\n"
				+ "<form action=\"./AddNewBook\" method=\"post\" enctype=\"multipart/form-data\">\r\n"
				+"	<div class=\"form-content\">\r\n";
				if(!inputCorrect) {
					html += "<p style=\"color:red\">"+ errors.fehlermeldung() +"</p>";
				}
				html
				+= "	<h2>Ein neues Buch hinzufügen:</h2>\r\n"
				+ "		<input class=\"formval\" type=\"text\" name=\"isbn\" required placeholder=\"ISBN\" minlength=\"13\" maxlength=\"17\" value=\"" + isbn + "\">\r\n"
				+ "		<input class=\"formval\" type=\"text\" name=\"titel\" required placeholder=\"Titel\" value=\"" + titel + "\">\r\n"
				+ "		<input class=\"formval\" type=\"text\" name=\"autor\" required placeholder=\"Autor\" value=\""+ autor + "\">\r\n"
				+ "		<textarea class=\"beschreibung\" name=\"beschreibung\" rows=\"25\" cols=\"1\" required placeholder=\"Beschreibung des Buchs...\">"+ beschreibung +"</textarea>\r\n"
				+ "		<input class=\"formval\" type=\"text\" name=\"preis\" required placeholder=\"Preis (€)\" value=\"" + preis + "\">\r\n"
				+"      <p>Kategorien:</p>\r\n"
				+ "		<div class=\"kategorien-box\">\r\n";

				for(int i = 0 ; i < kategorien.size(); i++) {
					if(buchkategorien != null && buchkategorien.contains(kategorien.get(i)))  {
						html += "<div class=\"check\">"
								+ "<input type=\"checkbox\"  id=\"kat"+ i +"\" name=\"kategorien\" value=\""+ kategorien.get(i) + "\" checked>\r\n"
								+ "			<label for=\"kat" + i + "\">"+kategorien.get(i) +"</label>"
								+ "</div>";
					} else
						html += "<div class=\"check\">"
								+ "<input type=\"checkbox\"  id=\"kat"+ i +"\" name=\"kategorien\" value=\""+ kategorien.get(i) + "\">\r\n"
								+ "			<label for=\"kat" + i + "\">"+kategorien.get(i) +"</label>"
								+ "</div>";
				}

				html += "		</div>\r\n"
				+ "		<!-- image -->\r\n"
				+ "		<div class=\"titelbild-box\">\r\n"
				+ "			<p class=\"titelbild-text\">Buchcover hochladen:</p>\r\n"
				+ "			<input class=\"formval-file\" type=\"file\" name=\"titelbild\" required placeholder=\"Titelbild\" accept=\".png,.jpeg,.webp\" >\r\n"
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
				+ "</body>\r\n"
				+ "</html>";
				return html;
	}
	

}
