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
import helpers.DataHelper;
import objects.Buch;

/**
 * Servlet implementation class AddNewBook
 */
@WebServlet("/AddNewBook")
@MultipartConfig
public class AddNewBook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private boolean inputCorrect;

	//
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

		List<String> kategorien = null;
		String[] buchkats = request.getParameterValues("kategorien");
		
		Part cover = request.getPart("titelbild"); 
		InputStream coverStream = cover.getInputStream();
		DataHelper hlp = new DataHelper();
		String b64Cover = hlp.streamToB64Convert(coverStream);
		
		response.setCharacterEncoding("UTF-8");//
		PrintWriter out = response.getWriter();		
		AddBookErrorHandling errors = new AddBookErrorHandling();
		inputCorrect = 	   errors.checkISBN(isbn) 
						& errors.checkPrice(pr) 
						& errors.checkIfIsbnExists(isbn)
						& errors.checkAuthor(autor)
						& errors.checkTitle(titel)
						& errors.checkIfCategoriesinDB()
						& errors.checkIfCategorySelected(request.getParameterValues("kategorien"));
				
		DatabaseStatements dbstatements = new DatabaseStatements();

		if (inputCorrect) {
			kategorien = Arrays.asList(request.getParameterValues("kategorien"));
			BigDecimal preis = new BigDecimal(pr);
			Buch buch = new Buch(isbn, titel, autor, beschreibung, kategorien, preis, coverStream, b64Cover);
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
			out.println(reloadForm(errors, isbn, titel, autor, beschreibung, pr, buchkats));
		}
		if (coverStream != null) {
			try {
				coverStream.close();
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}
	
//
	
	private String reloadForm(AddBookErrorHandling errors, String isbn, String titel, 
			String autor, String beschreibung, String preis, String[] buchkategorien) {
		DatabaseStatements dbstatements = new DatabaseStatements();//
		List<String> kategorien = dbstatements.getCategories();
		String css = getStyle();
		String html = 
				"<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">\r\n"
				+ css
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
				DataHelper helper = new DataHelper();
				for(int i = 0 ; i < kategorien.size(); i++) {
					if(buchkategorien != null && helper.containsVal(buchkategorien, kategorien.get(i)))  {
						html += "<div class=\"check\">"
								+ "<input type=\"checkbox\"  id=\"kat"+ i +"\" name=\"kategorien\" value=\""+ kategorien.get(i) + "\" checked />\r\n"
								+ "			<label for=\"kat" + i + "\">"+kategorien.get(i) +"</label>"
								+ "</div>";
					} else
						html += "<div class=\"check\">"
								+ "<input type=\"checkbox\"  id=\"kat"+ i +"\" name=\"kategorien\" value=\""+ kategorien.get(i) + "\" />\r\n"
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
	
	public static String getStyle() {
		String css = 
				"<style>"
				+ "body{\r\n"
				+ "	padding-bottom: 200px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".main{\r\n"
				+ "	margin: auto;\r\n"
				+ "	width: 50%;\r\n"
				+ "	display: flex;\r\n"
				+ "	flex-direction: column;\r\n"
				+ "	margin-top: 20px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".form-content{\r\n"
				+ "	display: flex;\r\n"
				+ "	flex-direction: column;\r\n"
				+ "	background-color: white;\r\n"
				+ "	box-shadow: 0px 2px 4px rgba(65, 65, 65, 0.3);\r\n"
				+ "	padding: 10px;\r\n"
				+ "	margin-bottom: 20px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ ".formval{\r\n"
				+ "	margin-top: 10px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "\r\n"
				+ ".beschreibung{\r\n"
				+ "	margin-top: 10px;\r\n"
				+ "	height: 400px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".titelbild-box{\r\n"
				+ "	margin-top: 10px;\r\n"
				+ "	display: flex;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".titelbild-text{\r\n"
				+ "	margin: 0;\r\n"
				+ "	margin-right: 10px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".formval-file{\r\n"
				+ "	margin: 0;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".submit-box{\r\n"
				+ "	display: flex;\r\n"
				+ "	justify-content: end;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".submit{\r\n"
				+ "	margin-top: 10px;\r\n"
				+ "	width: 150px;\r\n"
				+ "	height: 50px;\r\n"
				+ "	border: 1px solid black;\r\n"
				+ "	background-color: white;\r\n"
				+ "	cursor: pointer;\r\n"
				+ "	font-weight: bold;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".submit:hover{\r\n"
				+ "	background-color: rgb(0, 81, 255);\r\n"
				+ "	border-color: rgb(0, 81, 255);\r\n"
				+ "	color: white;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".kategorien-box{\r\n"
				+ "	display: grid;\r\n"
				+ "	grid-template-columns: 1fr 1fr 1fr 1fr; \r\n"
				+ "}\r\n"
				+ "body{\r\n"
				+ "  background-color: rgb(248, 248, 248);\r\n"
				+ "  font-family: 'Roboto';\r\n"
				+ "  margin: 0;\r\n"
				+ "  padding-bottom: 700px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "button{\r\n"
				+ "  cursor: pointer;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "a{\r\n"
				+ "  color: black;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "a:link { \r\n"
				+ "  text-decoration: none; \r\n"
				+ "} \r\n"
				+ "a:visited { \r\n"
				+ "  text-decoration: none; \r\n"
				+ "} \r\n"
				+ "a:hover { \r\n"
				+ "  text-decoration: none; \r\n"
				+ "} \r\n"
				+ "a:active { \r\n"
				+ "  text-decoration: none; \r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".roboto-thin {\r\n"
				+ "  font-family: \"Roboto\", sans-serif;\r\n"
				+ "  font-weight: 100;\r\n"
				+ "  font-style: normal;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".roboto-thin-italic {\r\n"
				+ "  font-family: \"Roboto\", sans-serif;\r\n"
				+ "  font-weight: 100;\r\n"
				+ "  font-style: italic;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".roboto-light {\r\n"
				+ "  font-family: \"Roboto\", sans-serif;\r\n"
				+ "  font-weight: 300;\r\n"
				+ "  font-style: normal;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".roboto-light-italic {\r\n"
				+ "  font-family: \"Roboto\", sans-serif;\r\n"
				+ "  font-weight: 300;\r\n"
				+ "  font-style: italic;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".roboto-regular {\r\n"
				+ "  font-family: \"Roboto\", sans-serif;\r\n"
				+ "  font-weight: 400;\r\n"
				+ "  font-style: normal;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".roboto-regular-italic {\r\n"
				+ "  font-family: \"Roboto\", sans-serif;\r\n"
				+ "  font-weight: 400;\r\n"
				+ "  font-style: italic;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".roboto-medium {\r\n"
				+ "  font-family: \"Roboto\", sans-serif;\r\n"
				+ "  font-weight: 500;\r\n"
				+ "  font-style: normal;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".roboto-medium-italic {\r\n"
				+ "  font-family: \"Roboto\", sans-serif;\r\n"
				+ "  font-weight: 500;\r\n"
				+ "  font-style: italic;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".roboto-bold {\r\n"
				+ "  font-family: \"Roboto\", sans-serif;\r\n"
				+ "  font-weight: 700;\r\n"
				+ "  font-style: normal;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".roboto-bold-italic {\r\n"
				+ "  font-family: \"Roboto\", sans-serif;\r\n"
				+ "  font-weight: 700;\r\n"
				+ "  font-style: italic;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".roboto-black {\r\n"
				+ "  font-family: \"Roboto\", sans-serif;\r\n"
				+ "  font-weight: 900;\r\n"
				+ "  font-style: normal;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".roboto-black-italic {\r\n"
				+ "  font-family: \"Roboto\", sans-serif;\r\n"
				+ "  font-weight: 900;\r\n"
				+ "  font-style: italic;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".headerr{\r\n"
				+ "  background-color: rgb(255, 255, 255);\r\n"
				+ "  width: 100%;\r\n"
				+ "  height: 110px;\r\n"
				+ "  border-color: rgb(86, 105, 146);\r\n"
				+ "  box-shadow: 0px 2px 4px rgba(65, 65, 65, 0.3);\r\n"
				+ "  display: flex;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".headerr-onlylogo{\r\n"
				+ "  background-color: rgb(255, 255, 255);\r\n"
				+ "  width: 100%;\r\n"
				+ "  height: 70px;\r\n"
				+ "  border-color: rgb(86, 105, 146);\r\n"
				+ "  box-shadow: 0px 2px 4px rgba(65, 65, 65, 0.3);\r\n"
				+ "  display: flex;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".leftt{\r\n"
				+ "  width: 20%;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".rightt{\r\n"
				+ "  width: 20%;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".items{\r\n"
				+ "  margin: auto;\r\n"
				+ "  width: 60%;\r\n"
				+ "  padding: 10px;\r\n"
				+ "  float: right;\r\n"
				+ "  display: flex;\r\n"
				+ "  justify-content: space-between;\r\n"
				+ "  align-items: center;\r\n"
				+ "  align-self: center;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".items-onlylogo{\r\n"
				+ "  width: 40%;\r\n"
				+ "  padding-left:10px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "/*\r\n"
				+ "button{\r\n"
				+ "  border: 1px solid;\r\n"
				+ "  width: 100px;\r\n"
				+ "  height: 50px;\r\n"
				+ "  border-radius: 15px;\r\n"
				+ "  float: right;\r\n"
				+ "  margin: 10px;\r\n"
				+ "  font-weight: bold;\r\n"
				+ "  text-shadow: 2px 2px 10px #5f5f5f;\r\n"
				+ "}\r\n"
				+ "*/\r\n"
				+ "\r\n"
				+ "\r\n"
				+ ".logo-box{\r\n"
				+ "  margin-top: 5px;\r\n"
				+ "  width: 30%;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".logo-box img{\r\n"
				+ "  max-width: 100%;\r\n"
				+ "  max-height: 80%;\r\n"
				+ "  object-fit: contain;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".home{\r\n"
				+ "  float: left;\r\n"
				+ "  background-color: white;\r\n"
				+ "  border: 2px solid;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".search{\r\n"
				+ "  margin-left: 5%;\r\n"
				+ "  width: 50%;\r\n"
				+ "  display:flex;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".searchbar{\r\n"
				+ "  height: 46px;\r\n"
				+ "  width: 65%;\r\n"
				+ "  border-top-left-radius: 20px;\r\n"
				+ "  border-bottom-left-radius: 20px;\r\n"
				+ "  border: 1px solid rgb(180, 180, 180);\r\n"
				+ "  padding-left: 30px;\r\n"
				+ "  margin: 0;\r\n"
				+ "  font-size: 18px;\r\n"
				+ "  outline: none;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "@media only screen and (min-width: 800px){\r\n"
				+ "  .searchbar{\r\n"
				+ "    display: none;\r\n"
				+ "  }\r\n"
				+ "\r\n"
				+ "  .searchbutton{\r\n"
				+ "    display: none;\r\n"
				+ "  }\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "@media only screen and (min-width: 1200px){\r\n"
				+ "  .searchbar{\r\n"
				+ "    width: 45%;\r\n"
				+ "    display: flex;\r\n"
				+ "  }\r\n"
				+ "\r\n"
				+ "  .searchbutton{\r\n"
				+ "    display: inline-block;\r\n"
				+ "  }\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "@media only screen and (min-width: 1850px){\r\n"
				+ "  .searchbar{\r\n"
				+ "    width: 65%;\r\n"
				+ "  }\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".searchbar:hover{\r\n"
				+ "  background-color: white;\r\n"
				+ "  border-color: black;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".searchbar:focus{\r\n"
				+ "  background-color: white;\r\n"
				+ "  border-color: black;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".searchbutton{\r\n"
				+ "  margin: 0;\r\n"
				+ "  border-radius: 20px;\r\n"
				+ "  border-top-left-radius: 0px;\r\n"
				+ "  border-bottom-left-radius: 0px;\r\n"
				+ "  border: 1px solid rgb(180, 180, 180);\r\n"
				+ "  border-left: 0;\r\n"
				+ "  background-color: whitesmoke;\r\n"
				+ "  width: 50px;\r\n"
				+ "  height: 50px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".searchbutton:hover{\r\n"
				+ "  background-color: white;\r\n"
				+ "  border-color: black;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".buttons{\r\n"
				+ "  display:flex;\r\n"
				+ "  width: 30%;\r\n"
				+ "  justify-content: end;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".signup{\r\n"
				+ "  background-color: rgb(0, 81, 255);\r\n"
				+ "  color: white;\r\n"
				+ "  border: 1px solid rgb(0, 81, 255);;\r\n"
				+ "  width: 100px;\r\n"
				+ "  height: 50px;\r\n"
				+ "  border-radius: 15px;\r\n"
				+ "  float: right;\r\n"
				+ "  margin: 10px;\r\n"
				+ "  font-weight: bold;\r\n"
				+ "  text-shadow: 2px 2px 10px #5f5f5f;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".signup:hover{\r\n"
				+ "  background-color: white;\r\n"
				+ "  color: rgb(0, 81, 255);\r\n"
				+ "  text-shadow: none;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".login{\r\n"
				+ "  background-color: whitesmoke;\r\n"
				+ "  border-color: whitesmoke;\r\n"
				+ "  border: 1px solid;\r\n"
				+ "  width: 100px;\r\n"
				+ "  height: 50px;\r\n"
				+ "  border-radius: 15px;\r\n"
				+ "  float: right;\r\n"
				+ "  margin: 10px;\r\n"
				+ "  font-weight: bold;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ ".login:hover{\r\n"
				+ "  background-color: white;\r\n"
				+ "  border-color: black;\r\n"
				+ "  text-shadow: 0px 3px 5px #5f5f5f;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "\r\n"
				+ ".warenkorb{\r\n"
				+ "  width: 40px;\r\n"
				+ "  height: 40px;\r\n"
				+ "}\r\n"
				+ "\r\n"
				+ "\r\n"
				+ ".warenkorb-button{\r\n"
				+ "  width: 60px;\r\n"
				+ "  height: 60px;\r\n"
				+ "  border-radius: 50px;\r\n"
				+ "  background-color: white;\r\n"
				+ "  border: none;\r\n"
				+ "  margin: 5px;\r\n"
				+ "}"
				+ "</style>";
		return css;
	}
	

}
