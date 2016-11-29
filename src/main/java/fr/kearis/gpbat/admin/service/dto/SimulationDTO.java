package fr.kearis.gpbat.admin.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fr.kearis.gpbat.admin.domain.enumeration.EtatSimulation;

/**
 * A DTO for the Simulation entity.
 */
public class SimulationDTO implements Serializable {

    private Long id;

    private LocalDate dateSimulation;

    private String remarques;

    private EtatSimulation etat;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getDateSimulation() {
        return dateSimulation;
    }

    public void setDateSimulation(LocalDate dateSimulation) {
        this.dateSimulation = dateSimulation;
    }
    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }
    public EtatSimulation getEtat() {
        return etat;
    }

    public void setEtat(EtatSimulation etat) {
        this.etat = etat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SimulationDTO simulationDTO = (SimulationDTO) o;

        if ( ! Objects.equals(id, simulationDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SimulationDTO{" +
            "id=" + id +
            ", dateSimulation='" + dateSimulation + "'" +
            ", remarques='" + remarques + "'" +
            ", etat='" + etat + "'" +
            '}';
    }
}
