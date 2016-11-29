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
 * A Chantier.
 */
@Entity
@Table(name = "chantier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "chantier")
public class Chantier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_demande_travaux")
    private LocalDate dateDemandeTravaux;

    @OneToOne
    @JoinColumn(unique = true)
    private ReceptionChantier reception;

    @OneToMany(mappedBy = "chantier")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AvancementChantier> avancements = new HashSet<>();

    @OneToMany(mappedBy = "chantier")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReserveChantier> reserves = new HashSet<>();

    @OneToMany(mappedBy = "chantier")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "chantier")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DiagnosticChantier> diagnostics = new HashSet<>();

    @ManyToOne
    private Utilisateur pilote;

    @ManyToOne
    private Commande commande;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public Chantier reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAdresse() {
        return adresse;
    }

    public Chantier adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public Chantier dateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateDemandeTravaux() {
        return dateDemandeTravaux;
    }

    public Chantier dateDemandeTravaux(LocalDate dateDemandeTravaux) {
        this.dateDemandeTravaux = dateDemandeTravaux;
        return this;
    }

    public void setDateDemandeTravaux(LocalDate dateDemandeTravaux) {
        this.dateDemandeTravaux = dateDemandeTravaux;
    }

    public ReceptionChantier getReception() {
        return reception;
    }

    public Chantier reception(ReceptionChantier receptionChantier) {
        this.reception = receptionChantier;
        return this;
    }

    public void setReception(ReceptionChantier receptionChantier) {
        this.reception = receptionChantier;
    }

    public Set<AvancementChantier> getAvancements() {
        return avancements;
    }

    public Chantier avancements(Set<AvancementChantier> avancementChantiers) {
        this.avancements = avancementChantiers;
        return this;
    }

    public Chantier addAvancement(AvancementChantier avancementChantier) {
        avancements.add(avancementChantier);
        avancementChantier.setChantier(this);
        return this;
    }

    public Chantier removeAvancement(AvancementChantier avancementChantier) {
        avancements.remove(avancementChantier);
        avancementChantier.setChantier(null);
        return this;
    }

    public void setAvancements(Set<AvancementChantier> avancementChantiers) {
        this.avancements = avancementChantiers;
    }

    public Set<ReserveChantier> getReserves() {
        return reserves;
    }

    public Chantier reserves(Set<ReserveChantier> reserveChantiers) {
        this.reserves = reserveChantiers;
        return this;
    }

    public Chantier addReserve(ReserveChantier reserveChantier) {
        reserves.add(reserveChantier);
        reserveChantier.setChantier(this);
        return this;
    }

    public Chantier removeReserve(ReserveChantier reserveChantier) {
        reserves.remove(reserveChantier);
        reserveChantier.setChantier(null);
        return this;
    }

    public void setReserves(Set<ReserveChantier> reserveChantiers) {
        this.reserves = reserveChantiers;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public Chantier clients(Set<Client> clients) {
        this.clients = clients;
        return this;
    }

    public Chantier addClient(Client client) {
        clients.add(client);
        client.setChantier(this);
        return this;
    }

    public Chantier removeClient(Client client) {
        clients.remove(client);
        client.setChantier(null);
        return this;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Set<DiagnosticChantier> getDiagnostics() {
        return diagnostics;
    }

    public Chantier diagnostics(Set<DiagnosticChantier> diagnosticChantiers) {
        this.diagnostics = diagnosticChantiers;
        return this;
    }

    public Chantier addDiagnostic(DiagnosticChantier diagnosticChantier) {
        diagnostics.add(diagnosticChantier);
        diagnosticChantier.setChantier(this);
        return this;
    }

    public Chantier removeDiagnostic(DiagnosticChantier diagnosticChantier) {
        diagnostics.remove(diagnosticChantier);
        diagnosticChantier.setChantier(null);
        return this;
    }

    public void setDiagnostics(Set<DiagnosticChantier> diagnosticChantiers) {
        this.diagnostics = diagnosticChantiers;
    }

    public Utilisateur getPilote() {
        return pilote;
    }

    public Chantier pilote(Utilisateur utilisateur) {
        this.pilote = utilisateur;
        return this;
    }

    public void setPilote(Utilisateur utilisateur) {
        this.pilote = utilisateur;
    }

    public Commande getCommande() {
        return commande;
    }

    public Chantier commande(Commande commande) {
        this.commande = commande;
        return this;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Chantier chantier = (Chantier) o;
        if(chantier.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, chantier.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Chantier{" +
            "id=" + id +
            ", reference='" + reference + "'" +
            ", adresse='" + adresse + "'" +
            ", dateDebut='" + dateDebut + "'" +
            ", dateDemandeTravaux='" + dateDemandeTravaux + "'" +
            '}';
    }
}
