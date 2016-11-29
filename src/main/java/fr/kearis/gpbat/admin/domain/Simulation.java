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

import fr.kearis.gpbat.admin.domain.enumeration.EtatSimulation;

/**
 * A Simulation.
 */
@Entity
@Table(name = "simulation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "simulation")
public class Simulation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_simulation")
    private LocalDate dateSimulation;

    @Column(name = "remarques")
    private String remarques;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private EtatSimulation etat;

    @OneToMany(mappedBy = "simulation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DetailCommande> details = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateSimulation() {
        return dateSimulation;
    }

    public Simulation dateSimulation(LocalDate dateSimulation) {
        this.dateSimulation = dateSimulation;
        return this;
    }

    public void setDateSimulation(LocalDate dateSimulation) {
        this.dateSimulation = dateSimulation;
    }

    public String getRemarques() {
        return remarques;
    }

    public Simulation remarques(String remarques) {
        this.remarques = remarques;
        return this;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

    public EtatSimulation getEtat() {
        return etat;
    }

    public Simulation etat(EtatSimulation etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(EtatSimulation etat) {
        this.etat = etat;
    }

    public Set<DetailCommande> getDetails() {
        return details;
    }

    public Simulation details(Set<DetailCommande> detailCommandes) {
        this.details = detailCommandes;
        return this;
    }

    public Simulation addDetail(DetailCommande detailCommande) {
        details.add(detailCommande);
        detailCommande.setSimulation(this);
        return this;
    }

    public Simulation removeDetail(DetailCommande detailCommande) {
        details.remove(detailCommande);
        detailCommande.setSimulation(null);
        return this;
    }

    public void setDetails(Set<DetailCommande> detailCommandes) {
        this.details = detailCommandes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Simulation simulation = (Simulation) o;
        if(simulation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, simulation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Simulation{" +
            "id=" + id +
            ", dateSimulation='" + dateSimulation + "'" +
            ", remarques='" + remarques + "'" +
            ", etat='" + etat + "'" +
            '}';
    }
}
