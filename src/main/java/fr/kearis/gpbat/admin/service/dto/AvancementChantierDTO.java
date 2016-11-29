package fr.kearis.gpbat.admin.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the AvancementChantier entity.
 */
public class AvancementChantierDTO implements Serializable {

    private Long id;

    private Integer avancementPourcentage;

    private Float avancementEtat;


    private Long chantierId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getAvancementPourcentage() {
        return avancementPourcentage;
    }

    public void setAvancementPourcentage(Integer avancementPourcentage) {
        this.avancementPourcentage = avancementPourcentage;
    }
    public Float getAvancementEtat() {
        return avancementEtat;
    }

    public void setAvancementEtat(Float avancementEtat) {
        this.avancementEtat = avancementEtat;
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

        AvancementChantierDTO avancementChantierDTO = (AvancementChantierDTO) o;

        if ( ! Objects.equals(id, avancementChantierDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AvancementChantierDTO{" +
            "id=" + id +
            ", avancementPourcentage='" + avancementPourcentage + "'" +
            ", avancementEtat='" + avancementEtat + "'" +
            '}';
    }
}
