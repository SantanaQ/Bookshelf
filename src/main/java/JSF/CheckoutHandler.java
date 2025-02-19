package JSF;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import database.DatabaseStatements;
import objects.Bestellung;
import objects.Kreditkarte;
import objects.Rechnung;


@Named("checkoutHandler")
@SessionScoped
public class CheckoutHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	//Lieferdaten
	private String vorname;
	private String nachname;
	private String adresse;
	private String plz;
	private String ort;
	private String zahlungsmethode;
	
	//Rechnungsdaten
	private String r_vorname;
	private String r_nachname;
	private String r_adresse;
	private String r_plz;
	private String r_ort;
	
	private boolean rechnungszahlung = false;
	private boolean kartenzahlung = false;
	
	//Kreditkarteninfos
	private String kreditkartenAnbieter;
	private String kartenInhaber;
	private String kartennummer;
	private Date gueltigkeit;
	private String pruefnummer;
	
	private int bestellNr;
	
	@Inject
	private LoginHandler loginHandler;
	@Inject
	private ShoppingcartHandler cartHandler;

	@PostConstruct
	public void init() {
		vorname = loginHandler.getKunde().getVorname();
		nachname = loginHandler.getKunde().getNachname();
		adresse = loginHandler.getKunde().getAdresse();
		plz = loginHandler.getKunde().getPlz();
		ort = loginHandler.getKunde().getOrt();
		r_vorname = vorname;
		r_nachname = nachname;
		r_adresse = adresse;
		r_plz = plz;
		r_ort = ort;
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

	public String getZahlungsmethode() {
		return zahlungsmethode;
	}

	public void setZahlungsmethode(String zahlungsmethode) {
		this.zahlungsmethode = zahlungsmethode;
		if(zahlungsmethode.equals("Kreditkarte")) {
			setKartenzahlung(true);
		}
	}

	public String getKreditkartenAnbieter() {
		return kreditkartenAnbieter;
	}

	public void setKreditkartenAnbieter(String kreditkartenAnbieter) {
		this.kreditkartenAnbieter = kreditkartenAnbieter;
	}

	public String getKartenInhaber() {
		return kartenInhaber;
	}

	public void setKartenInhaber(String kartenInhaber) {
		this.kartenInhaber = kartenInhaber;
	}

	public String getKartennummer() {
		return kartennummer;
	}

	public void setKartennummer(String kartennummer) {
		this.kartennummer = kartennummer;
	}

	public Date getGueltigkeit() {
		return gueltigkeit;
	}

	public void setGueltigkeit(Date gueltigkeit) {
		this.gueltigkeit = gueltigkeit;
	}

	public String getPruefnummer() {
		return pruefnummer;
	}

	public void setPruefnummer(String pruefnummer) {
		this.pruefnummer = pruefnummer;
	}

	public boolean isKartenzahlung() {
		return kartenzahlung;
	}

	public void setKartenzahlung(boolean kartenzahlung) {
		this.kartenzahlung = kartenzahlung;
	}
	
	public boolean isRechnungszahlung() {
		return rechnungszahlung;
	}

	public void setRechnungszahlung(boolean rechnungszahlung) {
		this.rechnungszahlung = rechnungszahlung;
	}
	
	public ShoppingcartHandler getCartHandler() {
		return cartHandler;
	}

	public void setCartHandler(ShoppingcartHandler cartHandler) {
		this.cartHandler = cartHandler;
	}
	
	public String getR_vorname() {
		return r_vorname;
	}

	public void setR_vorname(String r_vorname) {
		this.r_vorname = r_vorname;
	}

	public String getR_nachname() {
		return r_nachname;
	}

	public void setR_nachname(String r_nachname) {
		this.r_nachname = r_nachname;
	}

	public String getR_adresse() {
		return r_adresse;
	}

	public void setR_adresse(String r_adresse) {
		this.r_adresse = r_adresse;
	}

	public String getR_plz() {
		return r_plz;
	}

	public void setR_plz(String r_plz) {
		this.r_plz = r_plz;
	}

	public String getR_ort() {
		return r_ort;
	}

	public void setR_ort(String r_ort) {
		this.r_ort = r_ort;
	}


	public void toPayment() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("checkout-payment.xhtml");
	}

	public int getBestellNr() {
		return bestellNr;
	}
	
	public void setBestellNr(int bestellNr) {
		this.bestellNr = bestellNr;
	}
	
	public void toConfirm() throws IOException {
		if(zahlungsmethode == null  ||  (!zahlungsmethode.equals("Rechnung") && !zahlungsmethode.equals("Kreditkarte")) ) {
			return;
		}else
			FacesContext.getCurrentInstance().getExternalContext().redirect("confirmation1.xhtml");
	}
	
	public void mitRechnung() throws IOException {
		setZahlungsmethode("Rechnung");
		setKartenzahlung(false);
		setRechnungszahlung(true);
	}
	
	public void mitKarte() throws IOException {
		setZahlungsmethode("Kreditkarte");
		setKartenzahlung(true);
		setRechnungszahlung(false);
	}

	public void buyNow() throws IOException {	
		// Bestellung in DB eintragen
		DatabaseStatements stmts = new DatabaseStatements();
		Date today = new Date();
		Bestellung bestellung = new Bestellung(loginHandler.getKunde(), cartHandler.getBooks(), today, zahlungsmethode);
		int bestellNr = stmts.addBestellung(bestellung);

		// BestellNr festhalten
		setBestellNr(bestellNr);
		
		//Rechnung bzw. Kreditkarte in DB eintragen
		if(isRechnungszahlung()) {
			Rechnung rechnung = new Rechnung(bestellNr, r_vorname, r_nachname, r_adresse, r_plz, r_ort);
			stmts.addRechnung(rechnung);
		}else {
			if(!stmts.kreditkarteVorhanden(kartennummer)) {
				Kreditkarte kreditkarte = new Kreditkarte(kartennummer, kreditkartenAnbieter, gueltigkeit, kartenInhaber);
				stmts.addKreditkarte(kreditkarte);				
			}
		}
				
		// Warenkorb clearen
		cartHandler.clearCart();
		FacesContext.getCurrentInstance().getExternalContext().redirect("confirmation2.xhtml");
	}
	
	






}
