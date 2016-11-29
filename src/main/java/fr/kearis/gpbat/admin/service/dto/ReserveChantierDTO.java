package fr.kearis.gpbat.admin.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ReserveChantier entity.
 */
public class ReserveChantierDTO implements Serializable {

    private Long id;

    private String reserves;


    private Long chantierId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getReserves() {
        return reserves;
    }

    public void setReserves(String reserves) {
        this.reserves = reserves;
    }

    public Long getChantierId() {
        return chantierId;
    }

    public void setChantierId(Long chantierId) {
        this.chantierId = chantierId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReserveChantierDTO reserveChantierDTO = (ReserveChantierDTO) o;

        if ( ! Objects.equals(id, reserveChantierDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReserveChantierDTO{" +
            "id=" + id +
            ", reserves='" + reserves + "'" +
            '}';
    }
}
