package objects;

import java.util.Date;


public class Kunde {

	private String vorname;
	private String nachname;
	private Date geburtsdatum;
	private String adresse;
	private String plz;
	private String ort;
	private String email;
	private String passwort;
	
	
	/*        String date=request.getParameter("dob");
        System.out.println("dateString:"+date);
        Date utilDate=new SimpleDateFormat("yyyy-mm-dd").parse(date); */
	
	public Kunde() {
	}
	
	public Kunde(String vorname, String nachname, Date geburtsdatum, String adresse, String plz, String ort,
						String email, String passwort) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.geburtsdatum = geburtsdatum;
		this.adresse = adresse;
		this.plz = plz;
		this.ort = ort;
		this.email = email;
		this.passwort = passwort;
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
