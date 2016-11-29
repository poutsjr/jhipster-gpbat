package fr.kearis.gpbat.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AgenceClient.
 */
@Entity
@Table(name = "agence_client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "agenceclient")
public class AgenceClient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "secteur")
    private String secteur;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "chef_agence")
    private String chefAgence;

    @Column(name = "chef_service")
    private String chefService;

    @ManyToOne
    private Client client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public AgenceClient nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSecteur() {
        return secteur;
    }

    public AgenceClient secteur(String secteur) {
        this.secteur = secteur;
        return this;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public String getAdresse() {
        return adresse;
    }

    public AgenceClient adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getChefAgence() {
        return chefAgence;
    }

    public AgenceClient chefAgence(String chefAgence) {
        this.chefAgence = chefAgence;
        return this;
    }

    public void setChefAgence(String chefAgence) {
        this.chefAgence = chefAgence;
    }

    public String getChefService() {
        return chefService;
    }

    public AgenceClient chefService(String chefService) {
        this.chefService = chefService;
        return this;
    }

    public void setChefService(String chefService) {
        this.chefService = chefService;
    }

    public Client getClient() {
        return client;
    }

    public AgenceClient client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgenceClient agenceClient = (AgenceClient) o;
        if(agenceClient.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, agenceClient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AgenceClient{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", secteur='" + secteur + "'" +
            ", adresse='" + adresse + "'" +
            ", chefAgence='" + chefAgence + "'" +
            ", chefService='" + chefService + "'" +
            '}';
    }
}
