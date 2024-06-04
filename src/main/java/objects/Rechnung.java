package objects;

import java.io.Serializable;

public class Rechnung implements Serializable{

	private static final long serialVersionUID = 1L;

	private int bestellNr;
	private String vorname;
	private String nachname;
	private String adresse;
	private String plz;
	private String ort;
	
	public Rechnung(int bestellNr, String vorname, String nachname, String adresse, String plz, String ort) {
		this.bestellNr = bestellNr;
		this.vorname = vorname;
		this.nachname = nachname;
		this.adresse = adresse;
		this.plz = plz;
		this.ort = ort;
	}
	
	
	public int getBestellNr() {
		return bestellNr;
	}
	public void setBestellNr(int bestellNr) {
		this.bestellNr = bestellNr;
	}
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getPlz() {
		return plz;
	}
	public void setPlz(String plz) {
		this.plz = plz;
	}
	public String getOrt() {
		return ort;
	}
	public void setOrt(String ort) {
		this.ort = ort;
	}
}
