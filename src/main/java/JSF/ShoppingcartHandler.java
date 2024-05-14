package JSF;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import database.DatabaseStatements;
import objects.Buch;

@Named("shoppingcartHandler")
@SessionScoped
public class ShoppingcartHandler implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	private double total;
	private int numberItems;
	private List<Buch> books = new ArrayList<>();
	
	
	
	
	public List<Buch> getBooks() {
		return books;
	}
	
	
	public double getTotal() {
		return total;
	}
	
	public int getNumberItems() {
		return numberItems;
	}
	
	public void addNew(String isbn) {
		DatabaseStatements dbstatements = new DatabaseStatements();
		Buch buch = dbstatements.getBook(isbn);
		buch.setAnzahl(1);
		books.add(buch);
	}
	
	public void delete(String isbn) {
		double price = 0;
		double amount = 0;
		Buch deleted = null;
		for (Buch b : books) {
			if(b.getIsbn().equals(isbn)) {
				price = b.getAnzahl() * b.getPreis().doubleValue();
				amount = b.getAnzahl();
				deleted = b;
				break;
			}
		}
		total -= price;
		numberItems -= amount;
		books.remove(deleted);
	}
	
	public void addAmount() {
		
	}
	
	public void reduceAmount() {
		
	}
	

}