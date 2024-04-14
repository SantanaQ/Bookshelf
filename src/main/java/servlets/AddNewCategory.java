package servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

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
       

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String kategorie = request.getParameter("newcategory");
		
		try {
			Connection con = DatabaseConnection.initializeDatabase();
			DatabaseStatements.addCategory(con, kategorie);
			
            PrintWriter out = response.getWriter();
            File file = new File("C:\\Users\\Anwender\\git\\bookshelf\\src\\main\\webapp\\SuccessfullyAdded.html");
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
		}
		
		
	}

}
