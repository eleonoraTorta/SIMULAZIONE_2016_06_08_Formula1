package it.polito.tdp.formula1.model;

public class Evento  implements Comparable <Evento>{
	
	private int time;
	private Driver driver;
	private int lap;
//	private int posizione;

	
	public Evento(int time, Driver driver, int lap) {
		super();
		this.time = time;
		this.driver = driver;
		this.lap = lap;
//		this.posizione = posizione;
	
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getLap() {
		return lap;
	}

	public void setLap(int lap) {
		this.lap = lap;
	}

	

	public Driver getDriver() {
		return driver;
	}

	public void setdriver(Driver driver) {
		this.driver = driver;
	}
	
	

//	public int getPosizione() {
//		return posizione;
//	}
//
//	public void setPosizione(int posizione) {
//		this.posizione = posizione;
//	}

	@Override
	public int compareTo(Evento o) {
		return this.time - o.getTime();
	}
	
	public String toString(){
		return this.getTime() + " " + this.getDriver().getDriverId() + " " + this.getLap() + "\n";
	}
	
	
	
	

}
