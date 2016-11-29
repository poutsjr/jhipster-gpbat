package fr.kearis.gpbat.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ReserveChantier.
 */
@Entity
@Table(name = "reserve_chantier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reservechantier")
public class ReserveChantier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "reserves")
    private String reserves;

    @ManyToOne
    private Chantier chantier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReserves() {
        return reserves;
    }

    public ReserveChantier reserves(String reserves) {
        this.reserves = reserves;
        return this;
    }

    public void setReserves(String reserves) {
        this.reserves = reserves;
    }

    public Chantier getChantier() {
        return chantier;
    }

    public ReserveChantier chantier(Chantier chantier) {
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
        ReserveChantier reserveChantier = (ReserveChantier) o;
        if(reserveChantier.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reserveChantier.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReserveChantier{" +
            "id=" + id +
            ", reserves='" + reserves + "'" +
            '}';
    }
}
