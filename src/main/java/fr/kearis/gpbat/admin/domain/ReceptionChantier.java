package fr.kearis.gpbat.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ReceptionChantier.
 */
@Entity
@Table(name = "reception_chantier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "receptionchantier")
public class ReceptionChantier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_reception")
    private String dateReception;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateReception() {
        return dateReception;
    }

    public ReceptionChantier dateReception(String dateReception) {
        this.dateReception = dateReception;
        return this;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReceptionChantier receptionChantier = (ReceptionChantier) o;
        if(receptionChantier.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, receptionChantier.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReceptionChantier{" +
            "id=" + id +
            ", dateReception='" + dateReception + "'" +
            '}';
    }
}
