package JSF;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import database.DatabaseStatements;
import helpers.DataHelper;
import objects.Buch;

@Named("cartHandler")
@SessionScoped
public class ShoppingcartHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private double total = 0;
	private int numberItems = 0;
	private List<Item> books = new ArrayList<>();
	private Item book;
	private String newISBN;

	public void init() {
		// wenn Buch bereits vorhanden, keine Initialisierung
		for(Item b : books) {
			if(b.getBuch().getIsbn().equals(newISBN)) {
				return;
			}
		}
		// Nur wenn URL Parameter vorhanden, füge Buch neu hinzu
		if(newISBN != null) {
			addNew();
			setNewISBN(null);	
		}
	}
	
	public List<Item> getBooks() {
		if(total < 0.1) {
			total = 0;
		}
		return books;
	}
	
	public double getTotal() {
		return total;
	}
	
	public int getNumberItems() {
		return numberItems;
	}
	
	public void addNew() {
		numberItems += 1;
		total += book.getBuch().getPreis().doubleValue();
		book.setBuchSumme(book.getBuch().getPreis().doubleValue());
		books.add(book);
	}
	
	public String delete(Item item) {
		double price = 0;
		double amount = 0;
		for (Item i : books) {
			if(i.equals(item)) {
				price = i.getAnzahl() * i.getBuch().getPreis().doubleValue();
				amount = i.getAnzahl();
				break;
			}
		}
		total -= price;
		numberItems -= amount;
		books.remove(item);
		return "/shoppingcart.xhtml?isbn=null";
	}

	public void addAmount(Item item) {
		numberItems++;
		total += item.getBuch().getPreis().doubleValue();
		item.setAnzahl(item.getAnzahl()+1);
		item.setBuchSumme(item.getBuchSumme() + item.getBuch().getPreis().doubleValue());
	}
	
	public void reduceAmount(Item item) {
		if(item.getAnzahl() == 1) {
			return;
		}
		numberItems--;
		total -= item.getBuch().getPreis().doubleValue();
		item.setAnzahl(item.getAnzahl()-1);
		item.setBuchSumme(item.getBuchSumme() - item.getBuch().getPreis().doubleValue());
	}

	public String getNewISBN() {
		return newISBN;
	}

	public void setNewISBN(String newISBN) {
		this.newISBN = newISBN;
		DatabaseStatements dbstatements = new DatabaseStatements();
		Buch b = dbstatements.getBook(newISBN);
		book = new Item(b, 1);
		init();
	}

}