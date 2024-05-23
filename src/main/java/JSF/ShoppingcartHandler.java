package JSF;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
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
	private Item book = new Item();
	
	@PostConstruct
	public void init(){
		DatabaseStatements dbstatements = new DatabaseStatements();
		books = new ArrayList<>();
		List<Buch> buecher = dbstatements.getBooks("Roman");
		for(Buch b : buecher) {
			numberItems++;
			total += b.getPreis().doubleValue();
			Item i = new Item(b, 1);
			book = i;
			System.out.println(book.getBuch().getTitel());
			books.add(i);
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

	public void addNew(String isbn) {
		DatabaseStatements dbstatements = new DatabaseStatements();
		Buch buch = dbstatements.getBook(isbn);
		Item item = new Item(buch , 1);
		numberItems += 1;
		addTotal(buch.getPreis().doubleValue());
		books.add(item);
	}
	
	public void delete(Item item) {
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
		subTotal(price);
		numberItems -= amount;
		books.remove(remove);
	}

	public void addAmount(Item item) {
		numberItems++;
		addTotal(item.getBuch().getPreis().doubleValue());
		DataHelper hlp = new DataHelper();
		books.get(hlp.findVal(books, item)).setAnzahl(item.getAnzahl() + 1);
		
	}
	
	public void reduceAmount(Item item) {
		if(item.getAnzahl() == 1) {
			return;
		}
		numberItems--;
		subTotal(item.getBuch().getPreis().doubleValue());
		DataHelper hlp = new DataHelper();
		books.get(hlp.findVal(books, item)).setAnzahl(item.getAnzahl() - 1);
	}
	
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	private void addTotal(double price) {
		total += price;
		total = round(total, 2);
	}
	
	private void subTotal(double price) {
		total -= price;
		total = round(total, 2);	
	}

}