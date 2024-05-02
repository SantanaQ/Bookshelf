package database;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import helpers.DataHelper;
import objects.Buch;


public class DatabaseStatements {
	
	private static Connection con; //

	public void addBook(Buch buch) {
		PreparedStatement stmt = null;
		try {
			con = DatabaseConnection.initializeDatabase();
			stmt = con.prepareStatement("INSERT INTO Buch VALUES (?,?,?,?,?,?)");
			stmt.setString(1, buch.getIsbn());
			stmt.setString(2, buch.getTitel());
			stmt.setString(3, buch.getAutor());
			stmt.setString(4, buch.getBeschreibung());
			stmt.setBigDecimal(5, buch.getPreis());
			stmt.setString(6, buch.getB64Cover());
			stmt.executeUpdate();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Buch getBuch(String isbn) {
		Buch buch = null;
		try {
			
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt;
			ResultSet rs;
			
			stmt = con.prepareStatement("SELECT * FROM Buch WHERE Buch.ISBN = ?");
			stmt.setString(0, isbn);
			
			rs = stmt.executeQuery();
			rs.next();
			String isbn2 = rs.getString("ISBN");
			String titel = rs.getString("Titel");
			String autor = rs.getString("Autor");
			String beschreibung = rs.getString("Beschreibung");
			BigDecimal preis = rs.getBigDecimal("Preis"); 
			InputStream cover = null;
			String b64Cover = rs.getString("Titelbild");
			List<String> buchkategorien = getBookCategories(isbn);

			buch = new Buch(isbn2, titel, autor, beschreibung, buchkategorien, preis, cover, b64Cover);
			
			con.close();
			
		} catch (Exception e) {
		}
		
		return buch;
	}
	
	public List<Buch> getBooks(String kategorie){
		List<Buch> buecher = new ArrayList<>();
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt;
			ResultSet rs;
			if(kategorie.equals("") || kategorie == null || kategorie.equals("Kategorien")) { //Kategorien => submit value if all books selected
				String select = "SELECT *";
				String from = " FROM Buch";
				stmt = con.prepareStatement(select + from);
			}else {
				String select = "SELECT Buch.ISBN, Buch.Titel, Buch.Autor, Buch.Beschreibung, Buch.Preis, Buch.Titelbild";
				String from = " FROM Buch, Buchkategorien, Kategorie";
				String where = " WHERE Buch.ISBN = Buchkategorien.ISBN"
						+ " AND Buchkategorien.KategorieNr = Kategorie.KategorieNr"
						+ " AND Kategorie.Kategoriename = '" + kategorie + "'";
				stmt = con.prepareStatement(select + from + where);
			}
			rs = stmt.executeQuery();
			while(rs.next()) {
				String isbn = rs.getString("ISBN");
				String titel = rs.getString("Titel");
				String autor = rs.getString("Autor");
				String beschreibung = rs.getString("Beschreibung");
				BigDecimal preis = rs.getBigDecimal("Preis"); 
				InputStream cover = null;
				String b64Cover = rs.getString("Titelbild");
				List<String> buchkategorien = getBookCategories(isbn);

				Buch buch = new Buch(isbn, titel, autor, beschreibung, buchkategorien, preis, cover, b64Cover);
				buecher.add(buch);
			}
			con.close();
		} catch (Exception e) {
		}
		return buecher;
	}
	
	public List<String> getBookCategories(String isbn){
		List<String> buchkategorien = new ArrayList<>();
		String sql = "SELECT Kategorie.Kategoriename FROM Kategorie, Buchkategorien WHERE Kategorie.KategorieNr = Buchkategorien.KategorieNr AND Buchkategorien.ISBN = '" + isbn + "'";
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				buchkategorien.add(rs.getString("Kategoriename"));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buchkategorien;
	}
	
	public void addCategory(String kategorie) {
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Kategorie (Kategoriename) VALUES(?)");
			stmt.setString(1, kategorie);
			stmt.executeUpdate();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getCategories(){
		List<String> kategorien = new ArrayList<>();
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Kategorie");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				kategorien.add(rs.getString("Kategoriename"));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Collections.sort(kategorien);
		return kategorien;
	}
	
	
	
	public void addBookCategories(String isbn, List<String> kategorien) {
		List<Integer> katNrList = getBookCategoriesNr(kategorien);
		try {
			con = DatabaseConnection.initializeDatabase();
			for(int i : katNrList) {
				PreparedStatement stmt = con.prepareStatement("INSERT INTO Buchkategorien VALUES(?,?)");
				stmt.setString(1, isbn);
				stmt.setInt(2, i);
				stmt.executeUpdate();
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private List<Integer> getBookCategoriesNr(List<String> kategorien){
		List<Integer> categoryIndex = new ArrayList<>();
		try {
			con = DatabaseConnection.initializeDatabase();
			for(String s : kategorien) {
				PreparedStatement stmt = con.prepareStatement("SELECT * FROM Kategorie WHERE Kategoriename = '" + s + "'");
				ResultSet rs = stmt.executeQuery();
				if(rs.next()) {
					categoryIndex.add(rs.getInt("KategorieNr"));
				}
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return categoryIndex;
	}
	
	public List<String> getAllISBN(){
		List<String> isbns = new ArrayList<>();
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement("SELECT Buch.isbn FROM Buch");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				isbns.add(rs.getString("ISBN"));
			}
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isbns;
	}
	
}
