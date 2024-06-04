package objects;

import java.io.Serializable;
import java.util.Date;

public class Kreditkarte implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String kartenNr;
	private String anbieter;
	private Date gueltigkeit;
	private String inhaber;
	
	public Kreditkarte(String kartenNr, String anbieter, Date gueltigkeit, String inhaber) {
		super();
		this.kartenNr = kartenNr;
		this.anbieter = anbieter;
		this.gueltigkeit = gueltigkeit;
		this.inhaber = inhaber;
	}
	
	
	public String getKartenNr() {
		return kartenNr;
	}
	public void setKartenNr(String kartenNr) {
		this.kartenNr = kartenNr;
	}
	public String getAnbieter() {
		return anbieter;
	}
	public void setAnbieter(String anbieter) {
		this.anbieter = anbieter;
	}
	public Date getGueltigkeit() {
		return gueltigkeit;
	}
	public void setGueltigkeit(Date gueltigkeit) {
		this.gueltigkeit = gueltigkeit;
	}
	public String getInhaber() {
		return inhaber;
	}
	public void setInhaber(String inhaber) {
		this.inhaber = inhaber;
	}
	
	

}
