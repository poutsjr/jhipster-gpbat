package fr.kearis.gpbat.admin.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fr.kearis.gpbat.admin.domain.enumeration.UniteMetrique;

/**
 * A DTO for the ProduitPartenaire entity.
 */
public class ProduitPartenaireDTO implements Serializable {

    private Long id;

    private String article;

    private String libelle;

    private UniteMetrique unite;

    private Float prix;


    private Long bordereauId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    public UniteMetrique getUnite() {
        return unite;
    }

    public void setUnite(UniteMetrique unite) {
        this.unite = unite;
    }
    public Float getPrix() {
        return prix;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public Long getBordereauId() {
        return bordereauId;
    }

    public void setBordereauId(Long bordereauId) {
        this.bordereauId = bordereauId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProduitPartenaireDTO produitPartenaireDTO = (ProduitPartenaireDTO) o;

        if ( ! Objects.equals(id, produitPartenaireDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProduitPartenaireDTO{" +
            "id=" + id +
            ", article='" + article + "'" +
            ", libelle='" + libelle + "'" +
            ", unite='" + unite + "'" +
            ", prix='" + prix + "'" +
            '}';
    }
}
