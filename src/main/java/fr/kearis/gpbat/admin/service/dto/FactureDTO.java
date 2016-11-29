package fr.kearis.gpbat.admin.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Facture entity.
 */
public class FactureDTO implements Serializable {

    private Long id;

    private String numero;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FactureDTO factureDTO = (FactureDTO) o;

        if ( ! Objects.equals(id, factureDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FactureDTO{" +
            "id=" + id +
            ", numero='" + numero + "'" +
            '}';
    }
}
