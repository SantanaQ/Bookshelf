package JSF;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import database.DatabaseStatements;
import objects.Buch;

@Named("cartHandler")
@SessionScoped
public class ShoppingcartHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private double total = 0;
	private int numberItems = 0;
	private List<Buch> books;
	
	public List<Buch> fill(){
		DatabaseStatements dbstatements = new DatabaseStatements();
		books = dbstatements.getBooks("Krimi");
		for(Buch b : books) {
			b.setAnzahl(1);
		}
		return books;
	}
	
	
	public List<Buch> getBooks() {
		return books;
	}
	
	public double getTotal() {
		return total;
	}
	
	public int getNumberItems() {
		return numberItems;
	}
	/*
	public void addNew(String isbn) {
		DatabaseStatements dbstatements = new DatabaseStatements();
		Buch buch = dbstatements.getBook(isbn);
		buch.setAnzahl(1);
		numberItems += 1;
		total += buch.getPreis().doubleValue();
		books.add(buch);
	}
	
	public void delete(Buch buch) {
		double price = 0;
		double amount = 0;
		for (Buch b : books) {
			if(b.equals(buch)) {
				price = b.getAnzahl() * b.getPreis().doubleValue();
				amount = b.getAnzahl();
				break;
			}
		}
		total -= price;
		numberItems -= amount;
		books.remove(buch);
	}
	*/
	public void addAmount() {
		
	}
	
	public void reduceAmount() {
		
	}
	

}