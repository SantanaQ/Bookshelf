package objects;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Buch {
	
	private String isbn;
	private String titel;
	private String autor;
	private String Beschreibung;
	private List<String> kategorien;
	private BigDecimal preis;
	private InputStream cover;
	
	public Buch(String isbn, String titel, String autor, String beschreibung, List<String> kategorien, BigDecimal preis,
			InputStream cover) {
		this.isbn = isbn;
		this.titel = titel;
		this.autor = autor;
		Beschreibung = beschreibung;
		this.kategorien = kategorien;
		this.preis = preis;
		this.cover = cover;
	}
	
	public static List<String> CategoryStringToList(String kategorien){
		List<String> categories = Arrays.asList(kategorien.split(","));
		return categories;
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

}
