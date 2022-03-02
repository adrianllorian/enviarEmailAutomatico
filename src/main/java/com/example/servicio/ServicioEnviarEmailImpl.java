package com.example.servicio;
import static com.example.servicio.Constantes.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.modelo.Email;

@Service
public class ServicioEnviarEmailImpl implements ServicioEnviarEmail {
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Override
	public String enviarEmail(Email email) {
		if(controlExpresionRegularEmail(email) == false) {
			return ERROR_FORMATO_EMAIL;
			
		}
		else if(controlTamañoTextoCorto(email) == false) {
			return ERROR_TAMANO_TEXTO_CORTO;
			
		}
		
		else if(controlTamañoTextoLargo(email) == false) {
			return ERROR_TAMANO_TEXTO_LARGO;
		}
		
		else if(enviarEmailAlServidor(email) == false) {
			return ERROR_ENVIO;
		}
		
		else if(limiteDeEmails() == false) {
			return LIMITE_DE_EMAILS_DIARIOS;
		}
		
		else {
			return CORRECTO;
		}
		
	}
	
	/*Control de errores*/
	private boolean controlExpresionRegularEmail(Email email) {
		Pattern patron = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = patron.matcher(email.getDireccion());
		if(matcher.find() == true) {
			 return true;
		 }
		 else {
			 return false;
		 }		
	}
	
	private boolean controlTamañoTextoCorto(Email email) {
		if(email.getTexto().length() > 3) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	
	private boolean controlTamañoTextoLargo(Email email) {
		if(email.getTexto().length() < 3000) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/*Enviar email al servidor*/
	private boolean enviarEmailAlServidor(Email email) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		try {
		mimeMessage.setContent(email.getTexto(), "text/html"); /** Use this or below line **/
		helper.setText(email.getTexto(), true); // Use this or above line.
		helper.setTo("");
		helper.setSubject(email.getDireccion());
		helper.setFrom("");
		mailSender.send(mimeMessage);
		} catch (MessagingException ex) {
          return false;
        }
		return true;
	}	
	
	
	/*Limitar a 5 los emails diarios*/
	
	
	private static String obtenerFechaYHoraActual() {
		String formato = "yyyy-MM-dd";
		DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
		LocalDateTime ahora = LocalDateTime.now();
		return formateador.format(ahora);
	}
	
	
	private boolean limiteDeEmails() {
		List <String> dias = new ArrayList <String>();
		if(dias.size() < 5) {
			dias.add(obtenerFechaYHoraActual());
			return true;
	}
		else {
			if(!dias.get(0).equals(obtenerFechaYHoraActual())) {
				dias.clear();
				return true;
			}
			else {
				return false;
			}
		}
	}

}
