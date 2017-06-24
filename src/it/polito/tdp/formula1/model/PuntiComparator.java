package it.polito.tdp.formula1.model;

import java.util.Comparator;

public class PuntiComparator implements Comparator<Driver> {

	@Override
	public int compare(Driver d1, Driver d2) {
		return - (d1.getPunti() - d2.getPunti());
	}

}
