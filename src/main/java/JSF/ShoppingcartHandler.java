package JSF;

import java.io.Serializable;
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
		for(Item b : books) {
			if(b.getBuch().getIsbn().equals(newISBN)) {
				return;
			}
		}
		if(newISBN != null) {
			addNew();
			setNewISBN(null);	
		}
		
	}
	
	public List<Item> getBooks() {
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
		books.add(book);
	}
	
	public String delete(Item item) {
		double price = 0;
		double amount = 0;
		Item remove = null;
		for (Item i : books) {
			if(i.equals(item)) {
				remove = i;
				price = i.getAnzahl() * i.getBuch().getPreis().doubleValue();
				amount = i.getAnzahl();
				break;
			}
		}
		total -= price;
		numberItems -= amount;
		books.remove(remove);
		return "/shoppingcart.xhtml?isbn=null";
	}

	public void addAmount(Item item) {
		numberItems++;
		total += item.getBuch().getPreis().doubleValue();
		DataHelper hlp = new DataHelper();
		books.get(hlp.findVal(books, item)).setAnzahl(item.getAnzahl() + 1);
		
	}
	
	public void reduceAmount(Item item) {
		if(item.getAnzahl() == 1) {
			return;
		}
		numberItems--;
		total -= item.getBuch().getPreis().doubleValue();
		DataHelper hlp = new DataHelper();
		books.get(hlp.findVal(books, item)).setAnzahl(item.getAnzahl() - 1);
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