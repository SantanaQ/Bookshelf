package database;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DatabaseStatements {
	

	public static void addBook(Connection con, String isbn, String titel, 
			String autor, String beschreibung, BigDecimal preis, InputStream cover) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("INSERT INTO Buch VALUES (?,?,?,?,?,?)");
			stmt.setString(1, isbn);
			stmt.setString(2, titel);
			stmt.setString(3, autor);
			stmt.setString(4, beschreibung);
			stmt.setBigDecimal(5, preis);
			stmt.setBlob(6, cover);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addCategory(Connection con, String kategorien) {
		kategorien.replaceAll(", ", ",");
		List<String> categoriesList = Arrays.asList(kategorien.split(","));
		//System.out.println(categoriesList);
		HashSet<String> hashSetCategories = new HashSet<>(categoriesList);
		System.out.println(hashSetCategories);
		List<String> kategorienInDB = getKategorien(con);
		PreparedStatement stmt = null;
		try {
			for(String s : hashSetCategories) {
				
				if(!kategorienInDB.contains(s)) {
					stmt = con.prepareStatement("INSERT INTO Kategorie (Kategoriename) VALUES(?)");
					stmt.setString(1, s);
					stmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static List<String> getKategorien(Connection con){
		List<String> kategorien = new ArrayList<>();
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Kategorie");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				kategorien.add(rs.getString("Kategoriename"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kategorien;
	}
	
	
	
	public static void addBookcategories(Connection con, String isbn, String kategorien) {
		List<Integer> katNrList = getBuchkategorienNr(con, kategorien);
		try {
			for(int i : katNrList) {
				PreparedStatement stmt = con.prepareStatement("INSERT INTO Buchkategorien VALUES(?,?)");
				stmt.setString(1, isbn);
				stmt.setInt(2, i);
				stmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static List<Integer> getBuchkategorienNr(Connection con, String kategorien){
		kategorien.replaceAll(", ", ",");
		List<String> categoriesList = Arrays.asList(kategorien.split(","));
		HashSet<String> hashSetCategories = new HashSet<>(categoriesList);
		List<Integer> categoryIndex = new ArrayList<>();
		
		try {
			for(String s : hashSetCategories) {
				PreparedStatement stmt = con.prepareStatement("SELECT * FROM Kategorie WHERE Kategoriename = '" + s + "'");
				ResultSet rs = stmt.executeQuery();
				if(rs.next()) {
					categoryIndex.add(rs.getInt("KategorieNr"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categoryIndex;
	}
	
}
