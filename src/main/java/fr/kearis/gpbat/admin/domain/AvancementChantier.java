package fr.kearis.gpbat.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AvancementChantier.
 */
@Entity
@Table(name = "avancement_chantier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "avancementchantier")
public class AvancementChantier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "avancement_pourcentage")
    private Integer avancementPourcentage;

    @Column(name = "avancement_etat")
    private Float avancementEtat;

    @ManyToOne
    private Chantier chantier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAvancementPourcentage() {
        return avancementPourcentage;
    }

    public AvancementChantier avancementPourcentage(Integer avancementPourcentage) {
        this.avancementPourcentage = avancementPourcentage;
        return this;
    }

    public void setAvancementPourcentage(Integer avancementPourcentage) {
        this.avancementPourcentage = avancementPourcentage;
    }

    public Float getAvancementEtat() {
        return avancementEtat;
    }

    public AvancementChantier avancementEtat(Float avancementEtat) {
        this.avancementEtat = avancementEtat;
        return this;
    }

    public void setAvancementEtat(Float avancementEtat) {
        this.avancementEtat = avancementEtat;
    }

    public Chantier getChantier() {
        return chantier;
    }

    public AvancementChantier chantier(Chantier chantier) {
        this.chantier = chantier;
        return this;
    }

    public void setChantier(Chantier chantier) {
        this.chantier = chantier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AvancementChantier avancementChantier = (AvancementChantier) o;
        if(avancementChantier.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, avancementChantier.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AvancementChantier{" +
            "id=" + id +
            ", avancementPourcentage='" + avancementPourcentage + "'" +
            ", avancementEtat='" + avancementEtat + "'" +
            '}';
    }
}
