package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import database.DatabaseStatements;
import objects.Buch;
import thymeleaf.ThymeleafConfig;

/**
 * Servlet implementation class BookDetails
 */
@WebServlet("/BookDetails")
public class BookDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TemplateEngine engine = ThymeleafConfig.getTemplateEngine();
        WebContext context = new WebContext(request, response, request.getServletContext());
        response.setCharacterEncoding("UTF-8");
        
        DatabaseStatements dbstatements = new DatabaseStatements();
        String isbn = request.getParameter("isbn");
        Buch buch = dbstatements.getBuch(isbn);
        context.setVariable("titel", buch.getTitel());
        context.setVariable("autor", buch.getAutor());
        context.setVariable("preis", buch.getPreis());
        context.setVariable("kategorien", buch.getKategorien());
        context.setVariable("isbn", buch.getIsbn());
        context.setVariable("beschreibung", buch.getBeschreibung());
        engine.process("bookdetails.html", context, response.getWriter());
	}

}
