package fr.kearis.gpbat.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Commande.
 */
@Entity
@Table(name = "commande")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "commande")
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_edition")
    private LocalDate dateEdition;

    @Column(name = "date_reception")
    private LocalDate dateReception;

    @Column(name = "reference_marche")
    private String referenceMarche;

    @Column(name = "montant_ht")
    private Long montantHt;

    @Column(name = "type_tva")
    private Float typeTva;

    @Column(name = "etat_facturation")
    private String etatFacturation;

    @OneToOne
    @JoinColumn(unique = true)
    private Facture facture;

    @OneToMany(mappedBy = "commande")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DetailCommande> details = new HashSet<>();

    @OneToMany(mappedBy = "commande")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Chantier> chantiers = new HashSet<>();

    @ManyToOne
    private Commande commande;

    @OneToMany(mappedBy = "commande")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Commande> references = new HashSet<>();

    @ManyToOne
    private Partenaire partenaire;

    @ManyToOne
    private TypeCommande typecommande;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateEdition() {
        return dateEdition;
    }

    public Commande dateEdition(LocalDate dateEdition) {
        this.dateEdition = dateEdition;
        return this;
    }

    public void setDateEdition(LocalDate dateEdition) {
        this.dateEdition = dateEdition;
    }

    public LocalDate getDateReception() {
        return dateReception;
    }

    public Commande dateReception(LocalDate dateReception) {
        this.dateReception = dateReception;
        return this;
    }

    public void setDateReception(LocalDate dateReception) {
        this.dateReception = dateReception;
    }

    public String getReferenceMarche() {
        return referenceMarche;
    }

    public Commande referenceMarche(String referenceMarche) {
        this.referenceMarche = referenceMarche;
        return this;
    }

    public void setReferenceMarche(String referenceMarche) {
        this.referenceMarche = referenceMarche;
    }

    public Long getMontantHt() {
        return montantHt;
    }

    public Commande montantHt(Long montantHt) {
        this.montantHt = montantHt;
        return this;
    }

    public void setMontantHt(Long montantHt) {
        this.montantHt = montantHt;
    }

    public Float getTypeTva() {
        return typeTva;
    }

    public Commande typeTva(Float typeTva) {
        this.typeTva = typeTva;
        return this;
    }

    public void setTypeTva(Float typeTva) {
        this.typeTva = typeTva;
    }

    public String getEtatFacturation() {
        return etatFacturation;
    }

    public Commande etatFacturation(String etatFacturation) {
        this.etatFacturation = etatFacturation;
        return this;
    }

    public void setEtatFacturation(String etatFacturation) {
        this.etatFacturation = etatFacturation;
    }

    public Facture getFacture() {
        return facture;
    }

    public Commande facture(Facture facture) {
        this.facture = facture;
        return this;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public Set<DetailCommande> getDetails() {
        return details;
    }

    public Commande details(Set<DetailCommande> detailCommandes) {
        this.details = detailCommandes;
        return this;
    }

    public Commande addDetail(DetailCommande detailCommande) {
        details.add(detailCommande);
        detailCommande.setCommande(this);
        return this;
    }

    public Commande removeDetail(DetailCommande detailCommande) {
        details.remove(detailCommande);
        detailCommande.setCommande(null);
        return this;
    }

    public void setDetails(Set<DetailCommande> detailCommandes) {
        this.details = detailCommandes;
    }

    public Set<Chantier> getChantiers() {
        return chantiers;
    }

    public Commande chantiers(Set<Chantier> chantiers) {
        this.chantiers = chantiers;
        return this;
    }

    public Commande addChantier(Chantier chantier) {
        chantiers.add(chantier);
        chantier.setCommande(this);
        return this;
    }

    public Commande removeChantier(Chantier chantier) {
        chantiers.remove(chantier);
        chantier.setCommande(null);
        return this;
    }

    public void setChantiers(Set<Chantier> chantiers) {
        this.chantiers = chantiers;
    }

    public Commande getCommande() {
        return commande;
    }

    public Commande commande(Commande commande) {
        this.commande = commande;
        return this;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Set<Commande> getReferences() {
        return references;
    }

    public Commande references(Set<Commande> commandes) {
        this.references = commandes;
        return this;
    }

    public Commande addReference(Commande commande) {
        references.add(commande);
        commande.setCommande(this);
        return this;
    }

    public Commande removeReference(Commande commande) {
        references.remove(commande);
        commande.setCommande(null);
        return this;
    }

    public void setReferences(Set<Commande> commandes) {
        this.references = commandes;
    }

    public Partenaire getPartenaire() {
        return partenaire;
    }

    public Commande partenaire(Partenaire partenaire) {
        this.partenaire = partenaire;
        return this;
    }

    public void setPartenaire(Partenaire partenaire) {
        this.partenaire = partenaire;
    }

    public TypeCommande getTypecommande() {
        return typecommande;
    }

    public Commande typecommande(TypeCommande typeCommande) {
        this.typecommande = typeCommande;
        return this;
    }

    public void setTypecommande(TypeCommande typeCommande) {
        this.typecommande = typeCommande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Commande commande = (Commande) o;
        if(commande.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, commande.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Commande{" +
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
