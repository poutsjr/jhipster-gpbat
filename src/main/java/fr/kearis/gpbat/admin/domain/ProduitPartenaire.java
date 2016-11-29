package fr.kearis.gpbat.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import fr.kearis.gpbat.admin.domain.enumeration.UniteMetrique;

/**
 * A ProduitPartenaire.
 */
@Entity
@Table(name = "produit_partenaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "produitpartenaire")
public class ProduitPartenaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "article")
    private String article;

    @Column(name = "libelle")
    private String libelle;

    @Enumerated(EnumType.STRING)
    @Column(name = "unite")
    private UniteMetrique unite;

    @Column(name = "prix")
    private Float prix;

    @ManyToOne
    private Bordereau bordereau;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticle() {
        return article;
    }

    public ProduitPartenaire article(String article) {
        this.article = article;
        return this;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getLibelle() {
        return libelle;
    }

    public ProduitPartenaire libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public UniteMetrique getUnite() {
        return unite;
    }

    public ProduitPartenaire unite(UniteMetrique unite) {
        this.unite = unite;
        return this;
    }

    public void setUnite(UniteMetrique unite) {
        this.unite = unite;
    }

    public Float getPrix() {
        return prix;
    }

    public ProduitPartenaire prix(Float prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public Bordereau getBordereau() {
        return bordereau;
    }

    public ProduitPartenaire bordereau(Bordereau bordereau) {
        this.bordereau = bordereau;
        return this;
    }

    public void setBordereau(Bordereau bordereau) {
        this.bordereau = bordereau;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProduitPartenaire produitPartenaire = (ProduitPartenaire) o;
        if(produitPartenaire.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, produitPartenaire.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProduitPartenaire{" +
            "id=" + id +
            ", article='" + article + "'" +
            ", libelle='" + libelle + "'" +
            ", unite='" + unite + "'" +
            ", prix='" + prix + "'" +
            '}';
    }
}
