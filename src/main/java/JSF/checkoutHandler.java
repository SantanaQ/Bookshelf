package JSF;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import database.DatabaseStatements;
import objects.Bestellung;


@Named("checkoutHandler")
@SessionScoped
public class checkoutHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String vorname;
	private String nachname;
	private String adresse;
	private String plz;
	private String ort;
	private String zahlungsmethode;
	
	private boolean rechnungszahlung = false;
	private boolean kartenzahlung = false;
	private String kreditkartenAnbieter;
	private String kartenInhaber;
	private String kartennummer;
	private Date gueltigkeit;
	private String pruefnummer;
	
	private String bestellNr;
	
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
	

	public void toPayment() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("checkout-payment.xhtml");
	}

	public void toConfirm() throws IOException {
		if(zahlungsmethode == null  &&  !zahlungsmethode.equals("Rechnung") && !zahlungsmethode.equals("Kreditkarte")) {
			return;
		}
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
		// TODO: Bestellung in DB eintragen
		DatabaseStatements stmts = new DatabaseStatements();
		Date today = new Date();
		Bestellung bestellung = new Bestellung(loginHandler.getKunde(), cartHandler.getBooks(), today, zahlungsmethode);
		stmts.addBestellung(bestellung);
		
		// Warenkorb clearen
		cartHandler.setBooks(new ArrayList<>());
		cartHandler.setNumberItems(0);
		cartHandler.setTotal(0);
		FacesContext.getCurrentInstance().getExternalContext().redirect("confirmation2.xhtml");
	}




}
