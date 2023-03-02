package br.com.cep.correiros.repository;

import br.com.cep.correiros.model.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, String> {
}
