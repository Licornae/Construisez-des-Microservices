package com.ecommerce.micrommerce.web.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

//@JsonFilter("monFiltreDynamique")
@Entity
public class Product {
    @Id
    private int id;
    @Size(min = 3, max = 25)
    private String nom;
    @DecimalMin(value = "0.1", message = "Le prix doit être supérieur à 0€")
    private double prix;

    //information que nous ne souhaitons pas exposer
    private double prixAchat;

    public Product() {
    }

    public Product(int id, String nom, double prix, double prixAchat) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.prixAchat = prixAchat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setPrixAchat(double prixAchat){
        this.prixAchat = prixAchat;
    }

    public double getPrixAchat(){
        return prixAchat;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                '}';
    }
}
