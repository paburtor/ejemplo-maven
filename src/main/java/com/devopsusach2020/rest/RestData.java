package com.devopsusach2020.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.devopsusach2020.model.Pais;
import com.devopsusach2020.model.Mundial;
import com.google.gson.Gson;

@RestController
@RequestMapping(path = "/rest/mscovid")
public class RestData {
	
	private static final Logger LOGGER = Logger.getLogger("devops.subnivel.Control");

	
	@GetMapping(path = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Pais getData(@RequestParam(name = "msg") String message){
		
		LOGGER.log(Level.INFO, "Proceso exitoso de prueba");
		
		Pais response = new Pais();
		response.setMensaje("Mensaje Recibido: " + message);
		return response;
	}
	
	
	@GetMapping(path = "/estadoPais", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Pais getTotalPais(@RequestParam(name = "pais") String message){
		RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<String> call= restTemplate.getForEntity("https://api.covid19api.com/live/country/" + message ,String.class);

	    Pais response = new Pais();
	    int confirmed=0;
	    int death=0;
	    int recovered=0;
	    String resp=call.getBody();
	    
	    LOGGER.log(Level.INFO, "Consulta por pais");
	    
	    if(resp!=null)
            {
		Gson gson = new Gson();
		Pais[] estados = gson.fromJson(resp.toLowerCase(), Pais[].class);

	        for(Pais estado : estados) 
		{
        		response.setDate(estado.getDate());
	        	response.setActive(estado.getActive());
        		confirmed += estado.getConfirmed();
        		death += estado.getDeaths();
	        	recovered += estado.getRecovered();
        	}
	}
        
    	response.setConfirmed(confirmed);
    	response.setDeaths(death);
    	response.setRecovered(recovered);
    	response.setCountry(message);
    	response.setMensaje("ok");

		return response;		
	}
	

	@GetMapping(path = "/estadoMundial", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Mundial getTotalMundial(){
		
		LOGGER.log(Level.INFO, "Consulta mundial");
		
		RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<String> call= restTemplate.getForEntity("https://api.covid19api.com/world/total" ,String.class);
	    Mundial response = new Mundial();
		Gson gson = new Gson();
		Mundial estado = new Mundial();
		String resp=call.getBody();

		if(resp!=null)
		{
		    estado = gson.fromJson(resp.toLowerCase(), Mundial.class);
		}

        	response.setTotalConfirmed(estado.getTotalConfirmed());
		response.setTotalDeaths(estado.getTotalDeaths());
		response.setTotalRecovered(estado.getTotalRecovered());
		

		return response;		
	}
}	
