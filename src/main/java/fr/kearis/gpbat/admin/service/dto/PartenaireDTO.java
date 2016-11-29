package fr.kearis.gpbat.admin.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Partenaire entity.
 */
public class PartenaireDTO implements Serializable {

    private Long id;

    private String raisonSociale;

    private String responsable;

    private String contact;

    private String adresse;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }
    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PartenaireDTO partenaireDTO = (PartenaireDTO) o;

        if ( ! Objects.equals(id, partenaireDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PartenaireDTO{" +
            "id=" + id +
            ", raisonSociale='" + raisonSociale + "'" +
            ", responsable='" + responsable + "'" +
            ", contact='" + contact + "'" +
            ", adresse='" + adresse + "'" +
            '}';
    }
}
