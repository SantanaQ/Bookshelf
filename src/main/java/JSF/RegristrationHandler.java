package JSF;

import java.io.Serializable;
import java.sql.Date;

import javax.inject.Named;

import database.DatabaseStatements;
import objects.Kunde;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;

@Named("regHandler")
@RequestScoped
public class RegristrationHandler implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String vorname;
	private String nachname;
	private Date geburtsdatum;
	private String adresse;
	private String plz;
	private String ort;
	private String email;
	private String passwort;
	
	
	public void register() {
		Kunde neuerKunde = new Kunde(vorname, nachname, geburtsdatum, adresse, plz, ort, email, passwort);
		DatabaseStatements stmts = new DatabaseStatements();
		stmts.addKunde(neuerKunde);
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


	public Date getGeburtsdatum() {
		return geburtsdatum;
	}


	public void setGeburtsdatum(Date geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPasswort() {
		return passwort;
	}


	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}




	
	
}
