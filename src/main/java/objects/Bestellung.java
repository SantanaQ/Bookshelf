package objects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Bestellung implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Kunde kunde;
	private List<Item> books;
	private Date datum;
	private String zahlungsmethode;

	public Bestellung(Kunde kunde, List<Item> books, Date datum, String zahlungsmethode) {
		this.kunde = kunde;
		this.books = books;
		this.datum = datum;
		this.zahlungsmethode = zahlungsmethode;
	}
	
	public Kunde getKunde() {
		return kunde;
	}
	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}
	public List<Item> getBooks() {
		return books;
	}
	public void setBooks(List<Item> books) {
		this.books = books;
	}
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public String getZahlungsmethode() {
		return zahlungsmethode;
	}
	public void setZahlungsmethode(String zahlungsmethode) {
		this.zahlungsmethode = zahlungsmethode;
	}

}
