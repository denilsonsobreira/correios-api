package br.com.cep.correiros.service;

import br.com.cep.correiros.CorreirosApplication;
import br.com.cep.correiros.excpetion.NoContentException;
import br.com.cep.correiros.excpetion.NotReadyException;
import br.com.cep.correiros.model.Address;
import br.com.cep.correiros.model.AddressStatus;
import br.com.cep.correiros.model.Status;
import br.com.cep.correiros.repository.AddressRepository;
import br.com.cep.correiros.repository.AddressStatusRepository;
import br.com.cep.correiros.repository.SetupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CorreiosService {

    private static Logger logger = LoggerFactory.getLogger(CorreiosService.class);

    private AddressRepository addressRepository;
    private AddressStatusRepository addressStatusRepository;

    @Value("${setup.on.startup}")
    private boolean setupOnStartup;
    private SetupRepository setupRepository;
    public CorreiosService(AddressRepository addressRepository, AddressStatusRepository addressStatusRepository, SetupRepository setupRepository) {
        this.addressRepository = addressRepository;
        this.addressStatusRepository = addressStatusRepository;
        this.setupRepository = setupRepository;
    }

    public Status getStatus() {
        return this.addressStatusRepository.findById(AddressStatus.DEFAULT_ID).
                orElse(AddressStatus.builder().status(Status.NEED_SETUP).build())
                .getStatus();
    }

    public Address getAddressByZipCode(String zipcode) throws NoContentException, NotReadyException{
        if( !this.getStatus().equals(Status.READY) )
            throw new NotReadyException();

        return addressRepository.findById(zipcode).orElseThrow(NoContentException::new);
    }

    public void setup() throws Exception{
        if(this.getStatus().equals(Status.NEED_SETUP)) {

            try {
                this.saveStatus(Status.SETUP_RUNNING);
                this.addressRepository.saveAll(this.setupRepository.getFromOriginal());
            }catch (Exception e) {
                this.saveStatus(Status.NEED_SETUP);
                throw e;
            }

            this.saveStatus(Status.READY);
        }
    }

    @EventListener(ApplicationStartedEvent.class)
    public void setupOnStartup() {
        if(!setupOnStartup) return;
        try {
            this.setup();
        }catch (Exception e) {
            logger.error("setupOnStartup() -> error: " + e);
            CorreirosApplication.close(999);
        }
    }

    private void saveStatus(Status status) {
        this.addressStatusRepository.save(AddressStatus.builder()
                .id(AddressStatus.DEFAULT_ID)
                .status(status)
                .build());
    }
}
