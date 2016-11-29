package fr.kearis.gpbat.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import fr.kearis.gpbat.admin.domain.enumeration.TypeDiagnostic;

/**
 * A DiagnosticChantier.
 */
@Entity
@Table(name = "diagnostic_chantier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "diagnosticchantier")
public class DiagnosticChantier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_diagnostic")
    private TypeDiagnostic typeDiagnostic;

    @Column(name = "date_diagnostic")
    private LocalDate dateDiagnostic;

    @Column(name = "reference_diagnostic")
    private String referenceDiagnostic;

    @ManyToOne
    private Chantier chantier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeDiagnostic getTypeDiagnostic() {
        return typeDiagnostic;
    }

    public DiagnosticChantier typeDiagnostic(TypeDiagnostic typeDiagnostic) {
        this.typeDiagnostic = typeDiagnostic;
        return this;
    }

    public void setTypeDiagnostic(TypeDiagnostic typeDiagnostic) {
        this.typeDiagnostic = typeDiagnostic;
    }

    public LocalDate getDateDiagnostic() {
        return dateDiagnostic;
    }

    public DiagnosticChantier dateDiagnostic(LocalDate dateDiagnostic) {
        this.dateDiagnostic = dateDiagnostic;
        return this;
    }

    public void setDateDiagnostic(LocalDate dateDiagnostic) {
        this.dateDiagnostic = dateDiagnostic;
    }

    public String getReferenceDiagnostic() {
        return referenceDiagnostic;
    }

    public DiagnosticChantier referenceDiagnostic(String referenceDiagnostic) {
        this.referenceDiagnostic = referenceDiagnostic;
        return this;
    }

    public void setReferenceDiagnostic(String referenceDiagnostic) {
        this.referenceDiagnostic = referenceDiagnostic;
    }

    public Chantier getChantier() {
        return chantier;
    }

    public DiagnosticChantier chantier(Chantier chantier) {
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
        DiagnosticChantier diagnosticChantier = (DiagnosticChantier) o;
        if(diagnosticChantier.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, diagnosticChantier.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DiagnosticChantier{" +
            "id=" + id +
            ", typeDiagnostic='" + typeDiagnostic + "'" +
            ", dateDiagnostic='" + dateDiagnostic + "'" +
            ", referenceDiagnostic='" + referenceDiagnostic + "'" +
            '}';
    }
}
