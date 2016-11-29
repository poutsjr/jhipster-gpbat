package fr.kearis.gpbat.admin.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the AgenceClient entity.
 */
public class AgenceClientDTO implements Serializable {

    private Long id;

    private String nom;

    private String secteur;

    private String adresse;

    private String chefAgence;

    private String chefService;


    private Long clientId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getChefAgence() {
        return chefAgence;
    }

    public void setChefAgence(String chefAgence) {
        this.chefAgence = chefAgence;
    }
    public String getChefService() {
        return chefService;
    }

    public void setChefService(String chefService) {
        this.chefService = chefService;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AgenceClientDTO agenceClientDTO = (AgenceClientDTO) o;

        if ( ! Objects.equals(id, agenceClientDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AgenceClientDTO{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", secteur='" + secteur + "'" +
            ", adresse='" + adresse + "'" +
            ", chefAgence='" + chefAgence + "'" +
            ", chefService='" + chefService + "'" +
            '}';
    }
}
