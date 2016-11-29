package fr.kearis.gpbat.admin.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fr.kearis.gpbat.admin.domain.enumeration.TypeDiagnostic;

/**
 * A DTO for the DiagnosticChantier entity.
 */
public class DiagnosticChantierDTO implements Serializable {

    private Long id;

    private TypeDiagnostic typeDiagnostic;

    private LocalDate dateDiagnostic;

    private String referenceDiagnostic;


    private Long chantierId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public TypeDiagnostic getTypeDiagnostic() {
        return typeDiagnostic;
    }

    public void setTypeDiagnostic(TypeDiagnostic typeDiagnostic) {
        this.typeDiagnostic = typeDiagnostic;
    }
    public LocalDate getDateDiagnostic() {
        return dateDiagnostic;
    }

    public void setDateDiagnostic(LocalDate dateDiagnostic) {
        this.dateDiagnostic = dateDiagnostic;
    }
    public String getReferenceDiagnostic() {
        return referenceDiagnostic;
    }

    public void setReferenceDiagnostic(String referenceDiagnostic) {
        this.referenceDiagnostic = referenceDiagnostic;
    }

    public Long getChantierId() {
        return chantierId;
    }

    public void setChantierId(Long chantierId) {
        this.chantierId = chantierId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DiagnosticChantierDTO diagnosticChantierDTO = (DiagnosticChantierDTO) o;

        if ( ! Objects.equals(id, diagnosticChantierDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DiagnosticChantierDTO{" +
            "id=" + id +
            ", typeDiagnostic='" + typeDiagnostic + "'" +
            ", dateDiagnostic='" + dateDiagnostic + "'" +
            ", referenceDiagnostic='" + referenceDiagnostic + "'" +
            '}';
    }
}
