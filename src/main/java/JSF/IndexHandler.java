package JSF;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import database.DatabaseStatements;
import objects.Buch;

@Named("indexHandler")
@SessionScoped
public class IndexHandler implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private List<Buch> books = init();
	private List<String> kategorien = getKategorien();
	private String kategorieheadline = "Durchsuchen";
	private final String standardHeadline = "Durchsuchen";
	
	public void goToCategory(String kategorie) throws IOException {
		setBooks(kategorie);
		if(kategorie == null || kategorie.isEmpty() || kategorie.equals("")) {
			setKategorieheadline(standardHeadline);
		}else
			setKategorieheadline(kategorie);
		FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
	}
	
	public void goToDetails(String isbn) throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("bookdetails.xhtml");
	}
	
	public List<Buch> init(){
		DatabaseStatements stmts = new DatabaseStatements();
		return stmts.getBooks("");
	}
	
	public List<Buch> getBooks() {
		return books;
	}
	
	public void setBooks(String kategorie) throws IOException {
		DatabaseStatements stmts = new DatabaseStatements();
		this.books = stmts.getBooks(kategorie);
	}
	
	public List<String> getKategorien() {
		DatabaseStatements stmts = new DatabaseStatements();
		return stmts.getCategories();
	}
	
	public void setKategorien() {
		DatabaseStatements stmts = new DatabaseStatements();
		this.kategorien = stmts.getCategories();
	}
	
	public String getKategorieheadline() {
		return kategorieheadline;
	}
	
	public void setKategorieheadline(String kategorieheadline) {
		this.kategorieheadline = kategorieheadline;
	}

	

}
