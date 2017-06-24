package it.polito.tdp.formula1.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		Model model = new Model();
		List <Season> stagioni = model.getAllSeasons();
		Season s = stagioni.get(15);
		System.out.println(model.getCircuitiFromSeason(s));
//		Season t = stagioni.get(22);
//		System.out.println(model.getCircuitiFromSeason(t));
		
		List <Circuit> circuiti = model.getCircuitiFromSeason(s); 
		Circuit c = circuiti.get(3);
		Race r = model.getRaceInfo(c, s);
		
		System.out.println(model.getDriversOfRace(r));
		
		
	}

}
