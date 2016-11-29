package fr.kearis.gpbat.admin.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Commande entity.
 */
public class CommandeDTO implements Serializable {

    private Long id;

    private LocalDate dateEdition;

    private LocalDate dateReception;

    private String referenceMarche;

    private Long montantHt;

    private Float typeTva;

    private String etatFacturation;


    private Long factureId;
    
    private Long commandeId;
    
    private Long partenaireId;
    
    private Long typecommandeId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getDateEdition() {
        return dateEdition;
    }

    public void setDateEdition(LocalDate dateEdition) {
        this.dateEdition = dateEdition;
    }
    public LocalDate getDateReception() {
        return dateReception;
    }

    public void setDateReception(LocalDate dateReception) {
        this.dateReception = dateReception;
    }
    public String getReferenceMarche() {
        return referenceMarche;
    }

    public void setReferenceMarche(String referenceMarche) {
        this.referenceMarche = referenceMarche;
    }
    public Long getMontantHt() {
        return montantHt;
    }

    public void setMontantHt(Long montantHt) {
        this.montantHt = montantHt;
    }
    public Float getTypeTva() {
        return typeTva;
    }

    public void setTypeTva(Float typeTva) {
        this.typeTva = typeTva;
    }
    public String getEtatFacturation() {
        return etatFacturation;
    }

    public void setEtatFacturation(String etatFacturation) {
        this.etatFacturation = etatFacturation;
    }

    public Long getFactureId() {
        return factureId;
    }

    public void setFactureId(Long factureId) {
        this.factureId = factureId;
    }

    public Long getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(Long commandeId) {
        this.commandeId = commandeId;
    }

    public Long getPartenaireId() {
        return partenaireId;
    }

    public void setPartenaireId(Long partenaireId) {
        this.partenaireId = partenaireId;
    }

    public Long getTypecommandeId() {
        return typecommandeId;
    }

    public void setTypecommandeId(Long typeCommandeId) {
        this.typecommandeId = typeCommandeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommandeDTO commandeDTO = (CommandeDTO) o;

        if ( ! Objects.equals(id, commandeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CommandeDTO{" +
            "id=" + id +
            ", dateEdition='" + dateEdition + "'" +
            ", dateReception='" + dateReception + "'" +
            ", referenceMarche='" + referenceMarche + "'" +
            ", montantHt='" + montantHt + "'" +
            ", typeTva='" + typeTva + "'" +
            ", etatFacturation='" + etatFacturation + "'" +
            '}';
    }
}
