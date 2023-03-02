package br.com.cep.correiros.controller;

import br.com.cep.correiros.excpetion.NoContentException;
import br.com.cep.correiros.excpetion.NotReadyException;
import br.com.cep.correiros.model.Address;
import br.com.cep.correiros.model.Status;
import br.com.cep.correiros.service.CorreiosService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorreiosController {

    private CorreiosService correiosService;
    public CorreiosController(CorreiosService correiosService) {
        this.correiosService = correiosService;
    }

    @GetMapping("/status")
    public String getStatus() {
        return "Status: " + this.correiosService.getStatus();
    }

    @GetMapping("/zipcode/{zipcode}")
    public Address getAddressByZipCode(@PathVariable("zipcode") String zipCode) throws NoContentException,NotReadyException {

        return this.correiosService.getAddressByZipCode(zipCode);
    }
}
