package com.example.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.modelo.Email;
import com.example.servicio.ServicioEnviarEmail;

@RestController
public class RutasEmail {
	
	@Autowired
	private ServicioEnviarEmail enviarEmail;
	
	@PostMapping(value="/enviarEmail", consumes=MediaType.APPLICATION_JSON_VALUE)
	public String recibirEmail( @RequestBody Email email) {
		return enviarEmail.enviarEmail(email);
	}

	@GetMapping(value="/pruebaEnviarEmail")
	public String prueba() {
		System.out.println("Funciona");
		return "Funciona";
	}
	
} 
