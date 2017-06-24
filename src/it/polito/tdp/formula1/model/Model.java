package it.polito.tdp.formula1.model;


import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.formula1.db.F1DAO;

public class Model {
	
	private F1DAO dao ;
	private List <Season> stagioni;
	private List<Circuit> circuiti;
	private List <Driver> piloti;
	private Simulatore sim;
	
	
	public Model(){
		 dao = new F1DAO();	
		 
	}


	public List <Season> getAllSeasons(){
		if(stagioni == null){
			stagioni = dao.getAllSeasons();
		}
		return stagioni;
	}
	
	public List<Circuit> getCircuitiFromSeason(Season s){
		circuiti =  dao.getCircuitsOfSeason(s);
		return circuiti;
	}
	
	public Race getRaceInfo( Circuit c, Season s ){
		return dao.getRaceInfo(c, s);
	}
	
	public List<Driver> getDriversOfRace(Race r){
		 piloti = dao.getDriversFromRace(r);
		return piloti;
		
	}


	public List<Driver> simula(Driver driver, Circuit c) {
		this.sim = new Simulatore( this, driver, c);
		sim.preparaSimulazione();
		sim.run();
		List <Driver> classifica = sim.getClassifica();
		return classifica;
	}
	
	
}
