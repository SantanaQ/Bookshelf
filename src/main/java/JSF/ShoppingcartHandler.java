package JSF;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import database.DatabaseStatements;//
import objects.Buch;
import objects.Item;

@Named("cartHandler")
@SessionScoped()
public class ShoppingcartHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private double total = 0;
	private int numberItems = 0;
	private List<Item> books = new ArrayList<>();
	private Item book;
	private String newISBN;
	private boolean checkout = false;
	private final double versandkosten = 3.99;
	private double gesamt;
	
	public void init() throws IOException {
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
	
	public void setBooks(List<Item> books) {
		this.books = books;
	}
	
	public List<Item> getBooks() {
		if(total < 0.1) {
			total = 0;
		}
		return books;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}
	
	public double getTotal() {
		return total;
	}
	
	public void setNumberItems(int numberItems) {
		this.numberItems = numberItems;
	}
	
	public int getNumberItems() {
		return numberItems;
	}
	
	public boolean isCheckout() {
		return checkout;
	}

	public void setCheckout(boolean checkout) {
		this.checkout = checkout;
	}

	public double getGesamt() {
		return gesamt;
	}

	public void setGesamt() {
		this.gesamt = total + versandkosten;
	}

	public double getVersandkosten() {
		return versandkosten;
	}
	
	public void addNew() {
		numberItems += 1;
		total += book.getBuch().getPreis().doubleValue();
		book.setBuchSumme(book.getBuch().getPreis().doubleValue());
		books.add(book);
		
	}
	
	public void delete(Item item) throws IOException {
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
		FacesContext.getCurrentInstance().getExternalContext().redirect("shoppingcart.xhtml");
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

	public void setNewISBN(String newISBN) throws IOException {
		this.newISBN = newISBN;
		DatabaseStatements dbstatements = new DatabaseStatements();
		Buch b = dbstatements.getBook(newISBN);
		if(b == null) {
			return;
		}
		book = new Item(b, 1);
		init();
	}
	
	public void checkout() throws IOException {
		if(!books.isEmpty()) {
			setCheckout(true);
			setGesamt();
			FacesContext.getCurrentInstance().getExternalContext().redirect("checkout.xhtml");
		}
	}

	public void clearCart() {
		setBooks(new ArrayList<>());
		setNumberItems(0);
		setTotal(0);
	}
	
	public void show(Buch buch) throws IOException {
		Item item = new Item(buch, 1);
		book = item;
		addFromStart();
		FacesContext.getCurrentInstance().getExternalContext().redirect("shoppingcart.xhtml");
	}
	
	private void addFromStart() {
		for(Item b: books) {
			if(b.getBuch().equals(book.getBuch())) {
				numberItems += 1;
				b.setAnzahl(b.getAnzahl()+1);
				total += book.getBuch().getPreis().doubleValue();
				book.setBuchSumme(book.getBuch().getPreis().doubleValue());
				return;
			}
		}
		numberItems += 1;
		total += book.getBuch().getPreis().doubleValue();
		book.setBuchSumme(book.getBuch().getPreis().doubleValue());
		books.add(book);
	}


}