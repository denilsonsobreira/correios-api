package br.com.cep.correiros.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Serviço está em instalação.")
public class NotReadyException extends Exception{
}
