package it.polito.tdp.formula1.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.formula1.model.Circuit;
import it.polito.tdp.formula1.model.Driver;
import it.polito.tdp.formula1.model.LapTime;
import it.polito.tdp.formula1.model.Race;
import it.polito.tdp.formula1.model.Season;


public class F1DAO {

	public List<Season> getAllSeasons() {
		
		String sql = "SELECT year, url FROM seasons ORDER BY year" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Season> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(new Season(Year.of(rs.getInt("year")), rs.getString("url"))) ;
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
public List<Circuit> getCircuitsOfSeason(Season s) {
		
		String sql = "SELECT DISTINCT c.circuitId as id, c.circuitRef as ref, c.name as name, c.location as location, c.country as country " + 
				"FROM circuits as c, races as r " + 
				"WHERE r.circuitId = c.circuitId " + 
				"AND r.year = ?" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, s.getYear().getValue());
			
			ResultSet rs = st.executeQuery() ;
			
			List<Circuit> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(new Circuit(rs.getInt("id"), rs.getString("ref"), rs.getString("name"), rs.getString("location"), rs.getString("country"))) ;
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	

public Race getRaceInfo(Circuit c, Season s ) {
	
	String sql = "SELECT * " + 
			"FROM races as r " + 
			"WHERE circuitId = ? " + 
			"AND year = ?" ;
	
	Race race = null;
	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		st.setInt(1,c.getCircuitId());
		st.setInt(2, s.getYear().getValue());
		
		ResultSet rs = st.executeQuery() ;
		
		
		if(rs.next()) {
			LocalTime time = null;
			int id = rs.getInt("raceId");
			Year year = Year.of(rs.getInt("year"));
			int round = rs.getInt("round");
			int circuitId = rs.getInt("circuitId");
			String name = rs.getString("name");
			LocalDate date = rs.getDate("date").toLocalDate();
			if(rs.getTime("time") != null){
				time = rs.getTime("time").toLocalTime();
			}
			
			String url = rs.getString("url");
			race = new Race(id,year,round,circuitId,name,date,time,url);
			
		}
		
		conn.close();
		return race ;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null ;
	}
}

public List<Driver> getDriversFromRace(Race r) {
	
	String sql = "SELECT DISTINCT d.driverId as id, d.driverRef as ref, d.number as n, d.code as code, d.forename as forename , d.surname as surname, d.dob as dob, d.nationality as nat, d.url as url " + 
			"FROM races as gara, drivers as d, results as ris " + 
			"WHERE gara.raceId = ? " + 
			"AND ris.raceId = gara.raceId " + 
			"AND d.driverId = ris.driverId" ;
	
	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		st.setInt(1, r.getRaceId());
		
		ResultSet rs = st.executeQuery() ;
		
		List<Driver> list = new ArrayList<>() ;
		while(rs.next()) {
			
			int id =  rs.getInt("id");
			String ref = rs.getString("ref");
			int number =  rs.getInt("n");
			String code = rs.getString("code");
			String forename = rs.getString("forename");
			String surname = rs.getString("surname");
			LocalDate dob = rs.getDate("dob").toLocalDate();
			String nationality = rs.getString("nat");
			String url = rs.getString("url");
			
			Driver d = new Driver(id,ref,number,code,forename,surname,dob,nationality,url);

			list.add(d) ;
		}
		
		conn.close();
		return list ;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null ;
	}
}

public List<Integer> getRaceIdFromCircuitAndDriver(Circuit c, Driver d) {
	
	String sql = "SELECT  * " + 
			"FROM results as res " + 
			"WHERE res.driverId = ? AND res.position IS NOT NULL " + 
			"AND res.raceId IN (SELECT r.raceId  FROM races as r WHERE  r.circuitId = ?)" ;
	
	List<Integer> list = new ArrayList<>() ;
	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		st.setInt(1, d.getDriverId());
		st.setInt(2, c.getCircuitId());
		
		ResultSet rs = st.executeQuery() ;
		
		
		while(rs.next()) {
			
			int id = rs.getInt("raceId");

			list.add(id) ;
		}
		
		conn.close();
		return list ;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null ;
	}
}

public List<LapTime> getLapsForRaceAndDriver( int raceid, Driver d) {
	
	String sql = "SELECT * " + 
			"FROM laptimes " + 
			"WHERE raceId = ? and driverId = ?" ;
	
	List<LapTime> list = new ArrayList<>() ;
	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		st.setInt(1, raceid);
		st.setInt(2, d.getDriverId());
		
		ResultSet rs = st.executeQuery() ;
		
		
		while(rs.next()) {
			
			int raceId = rs.getInt("raceId");
			int driverid = rs.getInt("driverId");
			int lap = rs.getInt("lap");
			String time = rs.getString("time");
			int millisecondi = rs.getInt("milliseconds");
			
			LapTime laptime = new LapTime(raceId, driverid,lap,time,millisecondi);

			list.add(laptime) ;
		}
		
		conn.close();
		return list ;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null ;
	}
}

public int getYearOfRace(int idRace){
	String sql = "SELECT races.year " + 
			"FROM races " + 
			"WHERE races.raceId = ?" ;
	
	int id = 0;
	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		st.setInt(1,idRace);
		
		
		ResultSet rs = st.executeQuery() ;
		
		if(rs.next()) {
	
			 id = rs.getInt("year");
		
		}
		
		conn.close();
		return id;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return 0 ;
	}
}

public int getMillisecondsOfLap(int raceId, int driverId,int lap){
	String sql = "SELECT milliseconds " + 
			"FROM laptimes " + 
			"WHERE raceId = ? AND driverId = ? AND lap =?" ;
	
	int millisecondi = 0;
	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		st.setInt(1,raceId);
		st.setInt(2,driverId);
		st.setInt(3,lap);
		
		
		ResultSet rs = st.executeQuery() ;
		
		if(rs.next()) {
	
			 millisecondi = rs.getInt("milliseconds");
		
		}
		
		conn.close();
		return millisecondi;
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return 0 ;
	}
	}

	public static void main(String[] args) {
		F1DAO dao = new F1DAO() ;
		
		List<Season> seasons = dao.getAllSeasons() ;
		System.out.println(seasons);
		
//		Season s = new Season(Year.of(2009), "http://en.wikipedia.org/wiki/2009_Formula_One_season");
		
		List<Circuit> circuiti= dao.getCircuitsOfSeason(seasons.get(10));
		System.out.println(circuiti);
		
		System.out.println(dao.getRaceInfo(circuiti.get(3), seasons.get(0)));
		
	
	}
	
}
