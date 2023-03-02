package br.com.cep.correiros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AddressStatus {
    public static final int DEFAULT_ID = 1;
    @Id
    private int id;
    private Status status;
}
