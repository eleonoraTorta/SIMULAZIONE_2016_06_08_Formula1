package it.polito.tdp.formula1.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import it.polito.tdp.formula1.db.F1DAO;

public class Simulatore {
	
	private Model model;
	private F1DAO dao;
	
	// variabili della simulazione
	private Driver driver;
	private Circuit circuit;
	private int numPiloti;
	private int totlaps;
	private List <Driver> piloti;
	
	//Lista degli eventi
	private PriorityQueue <Evento> queue ;
	
	public Simulatore(Model model, Driver driver, Circuit circuit) {
		super();
		this.model = model;
		this.dao = new F1DAO();
		
		this.driver = driver;
		this.circuit = circuit;
		this.numPiloti =0;
		this.piloti = new ArrayList <Driver>();

		this.queue= new PriorityQueue<Evento>();
	}
	
	public void preparaSimulazione(){
		// Preparo N eventi = numero di races disputate dal driver nel circuit in questione = numero di piloti nella simulazione
		List <Integer> races = dao.getRaceIdFromCircuitAndDriver(circuit, driver);
		if( races.size() ==0){
			System.out.println("NON sono disponibili i dati sul databse per tale simulazione\n");
			return;
		}
		// Quanti sonoi piloti totali?
		numPiloti = races.size();
		// Quanti sono i laps totali?
		this.totlaps = dao.getLapsForRaceAndDriver(races.get(0), driver).size();
		
		for( int i = 0; i< numPiloti ; i++){
		//	int idYear = dao.getYearOfRace(races.get(i));
			// Individuo il driver , che ha come id l'id della race
			Driver driver = new Driver ( races.get(i), null, 0, null, null,null,null,null, null);
			System.out.println(driver.getDriverId() + "\n");
			piloti.add(driver);
			Evento e = new Evento (0, driver,  0);
			queue.add(e);
		}
		
	}
	
	public void run(){
		
		int lapCorrente = 0;
		int count = 0;
	
		while(!queue.isEmpty()){
				
			Evento e = queue.poll();
			System.out.println("Analizzo evento : " + e.toString());
		
			if(e.getLap() == lapCorrente && e.getLap() != 0){
				count ++;
				System.out.println("Driver non eliminato e non primo: "+ e.getDriver().getDriverId() + " " + e.getLap() + " " + e.getTime() + "\n");
				e.getDriver().setPosizioneLap(count);
			}
			else if(e.getLap() > lapCorrente  && e.getLap()!= 0){
				lapCorrente = e.getLap();
				count =1;
				System.out.println("Driver taglia traguardo per PRIMO: "+ e.getDriver().getDriverId() + " " + e.getLap() + " " + e.getTime() + "\n");
				e.getDriver().setPosizioneLap(count);
				
			}
			else if (e.getLap() < lapCorrente && e.getLap()!=0){
				e.getDriver().setEliminato();
				System.out.println("ELIMINATO: "+ e.getDriver().getDriverId() + " " + e.getLap() + " " + e.getTime() + "\n");
			}
			
			
			// Considerazioni per eventuale evento scatenato
			if( e.getLap() < totlaps  && e.getDriver().isEliminato() == false){
				// lap time prossimo lap
				int millisecondi = dao.getMillisecondsOfLap(e.getDriver().getDriverId(), driver.getDriverId(), e.getLap() +1);
				// quando ritagliera traguardo?
				int time = e.getTime() + millisecondi;
				//Schedulo nuovo evento
				Evento next = new Evento (time, e.getDriver(), e.getLap() +1);
				queue.add(next);
				System.out.println("Schedulo nuovo evento: " + next.toString());
			}
		}		
	
	}
	
	public List <Driver> getClassifica(){
		List <Driver> classifica = new ArrayList <Driver>();
		classifica.addAll(piloti);
		for( Driver d : classifica){
		System.out.println(d.getDriverId());
		}
		for( Driver d : piloti){
			if( d.isEliminato()){
				System.out.println("Eliminato: " + d.getDriverId() + "\n");
				classifica.remove(d);
			}
			else{
				int idAnno = dao.getYearOfRace(d.getDriverId());
				d.setDriverId(idAnno);
			}
		}
		Collections.sort(classifica, new PuntiComparator());
		return classifica;

	}
	
	

}
