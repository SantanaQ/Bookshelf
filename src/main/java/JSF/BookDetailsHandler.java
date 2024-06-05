package JSF;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import objects.Buch;

@Named ("detailHandler")
@SessionScoped
public class BookDetailsHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Buch book;

	public Buch getBook() {
		return book;
	}

	public void setBook(Buch book) {
		this.book = book;
	}
	
	public void show(Buch book) throws IOException {
		this.book = book;
		FacesContext.getCurrentInstance().getExternalContext().redirect("bookdetails.xhtml");
	}
	
	

}
