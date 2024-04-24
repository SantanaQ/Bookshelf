package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
			stmt.setBlob(6, buch.getCover());
			stmt.executeUpdate();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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
	
	public List<String> getKCategories(){
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
