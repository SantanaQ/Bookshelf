package JSF;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import database.DatabaseStatements;
import objects.Kunde;


@Named("loginHandler")
@SessionScoped
public class LoginHandler implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;
	private String passwort;
	@Inject
	private ShoppingcartHandler cartHandler;
	private Kunde kunde;

	public LoginHandler() {
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
	
	public void login() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", true);
		setKunde();
		if(!cartHandler.isCheckout()) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("ProductGroups");
		} else
			FacesContext.getCurrentInstance().getExternalContext().redirect("checkout.xhtml");
	}
	

	public ShoppingcartHandler getCartHandler() {
		return cartHandler;
	}

	public void setCartHandler(ShoppingcartHandler cartHandler) {
		this.cartHandler = cartHandler;
	}

	public Kunde getKunde() {
		return kunde;
	}

	public void setKunde() {
		DatabaseStatements stmts = new DatabaseStatements();
		Kunde k = stmts.getKunde(email);
		this.kunde = k;
	}
	
	public String includeHeader() {
		if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user") != null) {
			return "header-logged-in.xhtml";
		}else
			return "header.xhtml";
	}
	
	public void logout() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", null);
		kunde = null;
		FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
	}
	
	
}
