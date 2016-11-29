package fr.kearis.gpbat.admin.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Chantier entity.
 */
public class ChantierDTO implements Serializable {

    private Long id;

    private String reference;

    private String adresse;

    private LocalDate dateDebut;

    private LocalDate dateDemandeTravaux;


    private Long receptionId;
    
    private Long piloteId;
    
    private Long commandeId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    public LocalDate getDateDemandeTravaux() {
        return dateDemandeTravaux;
    }

    public void setDateDemandeTravaux(LocalDate dateDemandeTravaux) {
        this.dateDemandeTravaux = dateDemandeTravaux;
    }

    public Long getReceptionId() {
        return receptionId;
    }

    public void setReceptionId(Long receptionChantierId) {
        this.receptionId = receptionChantierId;
    }

    public Long getPiloteId() {
        return piloteId;
    }

    public void setPiloteId(Long utilisateurId) {
        this.piloteId = utilisateurId;
    }

    public Long getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(Long commandeId) {
        this.commandeId = commandeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChantierDTO chantierDTO = (ChantierDTO) o;

        if ( ! Objects.equals(id, chantierDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChantierDTO{" +
            "id=" + id +
            ", reference='" + reference + "'" +
            ", adresse='" + adresse + "'" +
            ", dateDebut='" + dateDebut + "'" +
            ", dateDemandeTravaux='" + dateDemandeTravaux + "'" +
            '}';
    }
}
