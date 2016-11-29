package fr.kearis.gpbat.admin.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the TypeCommande entity.
 */
public class TypeCommandeDTO implements Serializable {

    private Long id;

    private String nom;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TypeCommandeDTO typeCommandeDTO = (TypeCommandeDTO) o;

        if ( ! Objects.equals(id, typeCommandeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TypeCommandeDTO{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            '}';
    }
}
