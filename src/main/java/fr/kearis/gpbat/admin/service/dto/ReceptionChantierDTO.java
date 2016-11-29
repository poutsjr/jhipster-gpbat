package fr.kearis.gpbat.admin.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the ReceptionChantier entity.
 */
public class ReceptionChantierDTO implements Serializable {

    private Long id;

    private String dateReception;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDateReception() {
        return dateReception;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReceptionChantierDTO receptionChantierDTO = (ReceptionChantierDTO) o;

        if ( ! Objects.equals(id, receptionChantierDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReceptionChantierDTO{" +
            "id=" + id +
            ", dateReception='" + dateReception + "'" +
            '}';
    }
}
