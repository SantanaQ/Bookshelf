package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
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
 * Servlet implementation class ProductGroups
 */
@WebServlet("/ProductGroups")
public class ProductGroups extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductGroups() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TemplateEngine engine = ThymeleafConfig.getTemplateEngine();
        WebContext context = new WebContext(request, response, request.getServletContext());
        response.setCharacterEncoding("UTF-8");
        
        DatabaseStatements dbstatements = new DatabaseStatements();
        List<Buch> buecher = dbstatements.getBooks("Kategorien");
        List<String> kategorien = dbstatements.getCategories();
        context.setVariable("buecher", buecher);
        context.setVariable("kategorien", kategorien);
        engine.process("index.html", context, response.getWriter());
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TemplateEngine engine = ThymeleafConfig.getTemplateEngine();
        WebContext context = new WebContext(request, response, request.getServletContext());
        response.setCharacterEncoding("UTF-8");
        String kategorie = request.getParameter("kategorien");
        
        DatabaseStatements dbstatements = new DatabaseStatements();
        List<Buch> buecher = dbstatements.getBooks(kategorie);
        List<String> kategorien = dbstatements.getCategories();
        context.setVariable("buecher", buecher);
        context.setVariable("kategorien", kategorien);
        engine.process("index.html", context, response.getWriter());
	}

}
