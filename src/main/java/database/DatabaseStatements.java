package database;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import objects.Bestellung;
import objects.Buch;
import objects.Item;
import objects.Kreditkarte;
import objects.Kunde;
import objects.Rechnung;


public class DatabaseStatements {
	
	private static Connection con;

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
	
	public Buch getBook(String isbn) {
		Buch buch = null;
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt;
			ResultSet rs;
			stmt = con.prepareStatement("SELECT * FROM Buch WHERE Buch.ISBN = ?");
			stmt.setString(1, isbn);	
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
				String from = " FROM Buch ORDER BY Titel";
				stmt = con.prepareStatement(select + from);
			}else {
				String select = "SELECT Buch.ISBN, Buch.Titel, Buch.Autor, Buch.Beschreibung, Buch.Preis, Buch.Titelbild";
				String from = " FROM Buch, Buchkategorien, Kategorie";
				String where = " WHERE Buch.ISBN = Buchkategorien.ISBN"
						+ " AND Buchkategorien.KategorieNr = Kategorie.KategorieNr"
						+ " AND Kategorie.Kategoriename = ?";
				stmt = con.prepareStatement(select + from + where);
				stmt.setString(1, kategorie);
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
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Kategorie ORDER BY Kategoriename");
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
	
	public void addKunde(Kunde kunde) {
		Date kundenGeburtstag = new Date(kunde.getGeburtsdatum().getTime() );
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Kunde "
					+ "(Vorname, Nachname, Geburtsdatum, Adresse, PLZ, Ort, eMail, Passwort)  "
					+ "VALUES(?,?,?,?,?,?,?,?)");
			stmt.setString(1, kunde.getVorname());
			stmt.setString(2, kunde.getNachname());
			stmt.setDate(3, kundenGeburtstag);
			stmt.setString(4, kunde.getAdresse());
			stmt.setString(5, kunde.getPlz());
			stmt.setString(6, kunde.getOrt());
			stmt.setString(7, kunde.getEmail());
			stmt.setString(8, kunde.getPasswort());
			stmt.executeUpdate();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Kunde getKunde(String email) {
		Kunde kunde = null;
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Kunde WHERE eMail = ?");
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				String vorname = rs.getString("Vorname");
				String nachname = rs.getString("Nachname");
				Date geburtsdatum = rs.getDate("Geburtsdatum");
				String adresse = rs.getString("Adresse");
				String plz = rs.getString("PLZ");
				String ort = rs.getString("Ort");
				String eMail = rs.getString("eMail");
				String passwort = rs.getString("passwort");
				kunde = new Kunde(vorname, nachname, (java.util.Date) geburtsdatum, adresse, plz, ort, eMail, passwort);
			}
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kunde;
	}
	
	public int getKundenNr(Kunde kunde) {
		int kundenNr = -1;
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Kunde WHERE eMail = ?");
			stmt.setString(1, kunde.getEmail());
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				kundenNr = rs.getInt("KundenNr");
			}
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kundenNr;
	}
	
	public int getBezahlmethodenNr(Bestellung bestellung) {
		int bezahlmethodenNr = -1;
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Bezahlmethode WHERE Bezeichnung = ?");
			stmt.setString(1, bestellung.getZahlungsmethode());
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				bezahlmethodenNr = rs.getInt("BezahlmethodenNr");
			}
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bezahlmethodenNr;
	}
	
	public int getBezahlmethodenNr(String bezahlmethode) {
		int bezahlmethodenNr = -1;
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Bezahlmethode WHERE Bezeichnung = ?");
			stmt.setString(1, bezahlmethode);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				bezahlmethodenNr = rs.getInt("BezahlmethodenNr");
			}
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bezahlmethodenNr;
	}
	
	public void addBestellung(Bestellung bestellung) {
		int kundenNr = getKundenNr(bestellung.getKunde());
		int bezahlmethodenNr = getBezahlmethodenNr(bestellung);
		int bestellNr = setBestellNr();
		Date sqlDate = new Date(bestellung.getDatum().getTime());
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Bestellung (BestellNr, Datum, KundenNr, BezahlmethodenNr) VALUES (?,?,?,?)");
			stmt.setInt(1, bestellNr);
			stmt.setDate(2,sqlDate);
			stmt.setInt(3,kundenNr);
			stmt.setInt(4,bezahlmethodenNr);
			stmt.executeUpdate();
			addBuchBestellung(bestellung, bestellNr);
			con.close();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int setBestellNr() {
		int bestellNr = -1;
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Bestellung ORDER BY BestellNr DESC LIMIT 1");
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				bestellNr = rs.getInt("BestellNr") + 1;
			}else
				bestellNr = 1;
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bestellNr;
	}
	
	private void addBuchBestellung(Bestellung bestellung, int bestellNr) {
		try {
			con = DatabaseConnection.initializeDatabase();
			for(Item i : bestellung.getBooks()) {
				PreparedStatement stmt = con.prepareStatement("INSERT INTO Buchbestellung (ISBN, BestellNr, Anzahl) VALUES (?,?,?)");
				stmt.setString(1, i.getBuch().getIsbn());
				stmt.setInt(2, bestellNr);
				stmt.setInt(3, i.getAnzahl());
				stmt.executeUpdate();
			}
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addRechnung(Rechnung rechnung) {
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Rechnung (BestellNr, Vorname, Nachname, Adresse, PLZ, Ort) VALUES (?,?,?,?,?,?)");
			stmt.setInt(1, rechnung.getBestellNr());
			stmt.setString(2, rechnung.getVorname());
			stmt.setString(3, rechnung.getNachname());
			stmt.setString(4, rechnung.getAdresse());
			stmt.setString(5, rechnung.getPlz());
			stmt.setString(6, rechnung.getOrt());
			stmt.executeUpdate();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean kreditkarteVorhanden(String nummer) {
		boolean vorhanden = false;
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Kreditkarte WHERE KreditkartenNr = ?");
			stmt.setString(1, nummer);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				vorhanden = true;
			}
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vorhanden;
	}
	
	public void addKreditkarte(Kreditkarte karte) {
		Date sqlDate = new Date(karte.getGueltigkeit().getTime());
		try {
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Kreditkarte (KreditkartenNr, Anbieter, Gueltigkeit, Inhaber, BezahlmethodenNr) VALUES (?,?,?,?,?)");
			stmt.setString(1, karte.getKartenNr());
			stmt.setString(2, karte.getAnbieter());
			stmt.setDate(3, sqlDate);
			stmt.setString(4, karte.getInhaber());
			stmt.setInt(5, getBezahlmethodenNr("Kreditkarte"));
			stmt.executeUpdate();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
