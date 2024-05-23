package JSF;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


public class Buch implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String isbn;
	private String titel;
	private String autor;
	private String Beschreibung;
	private List<String> kategorien;
	private BigDecimal preis;
	private InputStream cover;
	private String b64Cover;
	private int anzahl;
	
	public Buch(String isbn, String titel, String autor, String beschreibung, List<String> kategorien, BigDecimal preis,
			InputStream cover, String b64Cover) {
		this.isbn = isbn;
		this.titel = titel;
		this.autor = autor;
		Beschreibung = beschreibung;
		this.kategorien = kategorien;
		this.preis = preis;
		this.cover = cover;
		this.setB64Cover(b64Cover);
	}


	public Buch() {
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getBeschreibung() {
		return Beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		Beschreibung = beschreibung;
	}

	public List<String> getKategorien() {
		return kategorien;
	}

	public void setKategorien(List<String> kategorien) {
		this.kategorien = kategorien;
	}

	public BigDecimal getPreis() {
		return preis;
	}

	public void setPreis(BigDecimal preis) {
		this.preis = preis;
	}

	public InputStream getCover() {
		return cover;
	}

	public void setCover(InputStream cover) {
		this.cover = cover;
	}


	public String getB64Cover() {
		return b64Cover;
	}


	public void setB64Cover(String b64Cover) {
		this.b64Cover = b64Cover;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}

}