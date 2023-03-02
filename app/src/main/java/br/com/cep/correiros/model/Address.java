package br.com.cep.correiros.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {
    @Id
    private String zipcode;
    private String street;
    private String district;
    private String city;
    private String state;
}
