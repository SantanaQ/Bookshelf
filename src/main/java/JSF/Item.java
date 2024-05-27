package JSF;

import java.io.Serializable;

import objects.Buch;

public class Item implements Serializable{

	private static final long serialVersionUID = 1L;
	private Buch buch;
	private int anzahl;
	private double buchSumme = 0;
	
	public Item() {
	}

	public Item(Buch buch, int anzahl) {
		this.buch = buch;
		this.anzahl = anzahl;
	}
	
	public Buch getBuch() {
		return buch;
	}

	public void setBuch(Buch buch) {
		this.buch = buch;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}

	public double getBuchSumme() {
		return buchSumme;
	}

	public void setBuchSumme(double buchSumme) {
		this.buchSumme = buchSumme;
	}


}
