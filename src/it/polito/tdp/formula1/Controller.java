package it.polito.tdp.formula1;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.formula1.model.Circuit;
import it.polito.tdp.formula1.model.Driver;
import it.polito.tdp.formula1.model.Model;
import it.polito.tdp.formula1.model.Race;
import it.polito.tdp.formula1.model.Season;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Circuit> boxCircuit;

    @FXML
    private ComboBox<Driver> boxDriver;

    @FXML
    private ComboBox<Season> boxSeason;

    @FXML
    private TextArea txtResult;

	private Model model;
	
	@FXML
    void doPopola(ActionEvent event) {
		boxCircuit.getItems().clear();
		boxDriver.getItems().clear();
		
		Season s = boxSeason.getValue();
		if(s == null){
			txtResult.appendText("ERRORE: selezionare una stagione.\n");
			return;
		}
		List <Circuit> circuiti = model.getCircuitiFromSeason(s);
		Collections.sort(circuiti);
		boxCircuit.getItems().addAll(circuiti);
    }
	
	@FXML
    void doInfoGara(ActionEvent event) {
		txtResult.clear();
		
		Season s = boxSeason.getValue();
		if(s == null){
			txtResult.appendText("ERRORE: selezionare una stagione.\n");
			return;
		}
		Circuit c = boxCircuit.getValue();
		if(c == null){
			txtResult.appendText("ERRORE: selezionare un circuito.\n");
			return;
		}
		
		Race race = model.getRaceInfo(c, s);
		txtResult.appendText("Informazioni relative alla RACE della stagione " + s.getYear().getValue() + " disputata nel circuito "+ c.getName() + " :\n" );
		txtResult.appendText("Race id: " + race.getRaceId() + "\n");
		txtResult.appendText("Season: " + race.getYear().getValue() + "\n");
		txtResult.appendText("Round: " + race.getRound() + "\n");
		txtResult.appendText("Circuit id: " + race.getCircuitId() + "\n");
		txtResult.appendText("Race name: " + race.getName() + "\n");
		txtResult.appendText("Date: " + race.getDate()+ "\n");
		txtResult.appendText("Time: " + race.getTime() + "\n");
		txtResult.appendText("Season: " + race.getYear().getValue() + "\n");
		txtResult.appendText("Url: " + race.getUrl() + "\n");
		
		txtResult.appendText("\nLista dei piloti che hanno partecipato alla suddetta race: \n");
		List <Driver> piloti = model.getDriversOfRace(race);
		Collections.sort(piloti);
		for( Driver d : piloti){
			txtResult.appendText(d.toString() + "\n");
		}
		
		boxDriver.getItems().addAll(piloti);

    }

    @FXML
    void doFantaGara(ActionEvent event) {
    	
    	txtResult.clear();
    
    	Season s = boxSeason.getValue();
		if(s == null){
			txtResult.appendText("ERRORE: selezionare una stagione.\n");
			return;
		}
		Circuit c = boxCircuit.getValue();
		if(c == null){
			txtResult.appendText("ERRORE: selezionare un circuito.\n");
			return;
		}
    	
		Driver driver = boxDriver.getValue();
    	if( driver == null){
    		txtResult.appendText("ERRORE: selezionare un pilota\n" );
    		return;
    	}
    	
    	txtResult.appendText("CLASSIFICA simulazione del pilota " + driver.getForename() +" " + driver.getSurname() + " nel circuito " + c.getName() + " :\n");
    	List <Driver> classifica = model.simula(driver,c);
    	for( Driver d : classifica){
    		txtResult.appendText("Anno " + d.getDriverId() + " : "  + d.getPunti() + " punti\n");
    	}
    	
  
    }

    

    @FXML
    void initialize() {
        assert boxCircuit != null : "fx:id=\"boxCircuit\" was not injected: check your FXML file 'Formula1.fxml'.";
        assert boxDriver != null : "fx:id=\"boxDriver\" was not injected: check your FXML file 'Formula1.fxml'.";
        assert boxSeason != null : "fx:id=\"boxSeason\" was not injected: check your FXML file 'Formula1.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Formula1.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model ;
		
		//Inizializzo tendina stagioni
		boxSeason.getItems().addAll(model.getAllSeasons());
	}
}
