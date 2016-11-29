package fr.kearis.gpbat.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import fr.kearis.gpbat.admin.domain.enumeration.UniteMetrique;

/**
 * A Bordereau.
 */
@Entity
@Table(name = "bordereau")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bordereau")
public class Bordereau implements Serializable {

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
    private CorpsEtat corpsEtat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticle() {
        return article;
    }

    public Bordereau article(String article) {
        this.article = article;
        return this;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getLibelle() {
        return libelle;
    }

    public Bordereau libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public UniteMetrique getUnite() {
        return unite;
    }

    public Bordereau unite(UniteMetrique unite) {
        this.unite = unite;
        return this;
    }

    public void setUnite(UniteMetrique unite) {
        this.unite = unite;
    }

    public Float getPrix() {
        return prix;
    }

    public Bordereau prix(Float prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public CorpsEtat getCorpsEtat() {
        return corpsEtat;
    }

    public Bordereau corpsEtat(CorpsEtat corpsEtat) {
        this.corpsEtat = corpsEtat;
        return this;
    }

    public void setCorpsEtat(CorpsEtat corpsEtat) {
        this.corpsEtat = corpsEtat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bordereau bordereau = (Bordereau) o;
        if(bordereau.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bordereau.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bordereau{" +
            "id=" + id +
            ", article='" + article + "'" +
            ", libelle='" + libelle + "'" +
            ", unite='" + unite + "'" +
            ", prix='" + prix + "'" +
            '}';
    }
}
