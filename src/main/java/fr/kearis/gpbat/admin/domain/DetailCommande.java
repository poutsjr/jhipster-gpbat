package fr.kearis.gpbat.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DetailCommande.
 */
@Entity
@Table(name = "detail_commande")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "detailcommande")
public class DetailCommande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "quantite")
    private Float quantite;

    @Column(name = "localisation")
    private String localisation;

    @Column(name = "ordre_commande")
    private Integer ordreCommande;

    @Column(name = "libelle")
    private String libelle;

    @ManyToOne
    private Simulation simulation;

    @ManyToOne
    private Commande commande;

    @ManyToOne
    private Bordereau bordereau;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getQuantite() {
        return quantite;
    }

    public DetailCommande quantite(Float quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Float quantite) {
        this.quantite = quantite;
    }

    public String getLocalisation() {
        return localisation;
    }

    public DetailCommande localisation(String localisation) {
        this.localisation = localisation;
        return this;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public Integer getOrdreCommande() {
        return ordreCommande;
    }

    public DetailCommande ordreCommande(Integer ordreCommande) {
        this.ordreCommande = ordreCommande;
        return this;
    }

    public void setOrdreCommande(Integer ordreCommande) {
        this.ordreCommande = ordreCommande;
    }

    public String getLibelle() {
        return libelle;
    }

    public DetailCommande libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public DetailCommande simulation(Simulation simulation) {
        this.simulation = simulation;
        return this;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public Commande getCommande() {
        return commande;
    }

    public DetailCommande commande(Commande commande) {
        this.commande = commande;
        return this;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Bordereau getBordereau() {
        return bordereau;
    }

    public DetailCommande bordereau(Bordereau bordereau) {
        this.bordereau = bordereau;
        return this;
    }

    public void setBordereau(Bordereau bordereau) {
        this.bordereau = bordereau;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DetailCommande detailCommande = (DetailCommande) o;
        if(detailCommande.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, detailCommande.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DetailCommande{" +
            "id=" + id +
            ", quantite='" + quantite + "'" +
            ", localisation='" + localisation + "'" +
            ", ordreCommande='" + ordreCommande + "'" +
            ", libelle='" + libelle + "'" +
            '}';
    }
}
