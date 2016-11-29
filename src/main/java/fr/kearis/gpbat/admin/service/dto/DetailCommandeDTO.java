package fr.kearis.gpbat.admin.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the DetailCommande entity.
 */
public class DetailCommandeDTO implements Serializable {

    private Long id;

    private Float quantite;

    private String localisation;

    private Integer ordreCommande;

    private String libelle;


    private Long simulationId;
    
    private Long commandeId;
    
    private Long bordereauId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Float getQuantite() {
        return quantite;
    }

    public void setQuantite(Float quantite) {
        this.quantite = quantite;
    }
    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
    public Integer getOrdreCommande() {
        return ordreCommande;
    }

    public void setOrdreCommande(Integer ordreCommande) {
        this.ordreCommande = ordreCommande;
    }
    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Long getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(Long simulationId) {
        this.simulationId = simulationId;
    }

    public Long getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(Long commandeId) {
        this.commandeId = commandeId;
    }

    public Long getBordereauId() {
        return bordereauId;
    }

    public void setBordereauId(Long bordereauId) {
        this.bordereauId = bordereauId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DetailCommandeDTO detailCommandeDTO = (DetailCommandeDTO) o;

        if ( ! Objects.equals(id, detailCommandeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DetailCommandeDTO{" +
            "id=" + id +
            ", quantite='" + quantite + "'" +
            ", localisation='" + localisation + "'" +
            ", ordreCommande='" + ordreCommande + "'" +
            ", libelle='" + libelle + "'" +
            '}';
    }
}
