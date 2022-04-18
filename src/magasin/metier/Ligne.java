/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.metier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 *
 * @author Michel
 */
public class Ligne {

    protected Produit produit;
    protected int quantite;
    protected BigDecimal prixAchat;

    public Ligne() {
    }
    
    

    public Ligne(Produit produit, int quantite, BigDecimal prixAchat) {
      
        this.produit = produit;
        this.quantite = quantite;
        this.prixAchat = prixAchat;
        prixAchat.setScale(2, RoundingMode.HALF_UP);
    }

 
    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(BigDecimal prixAchat) {
        this.prixAchat = prixAchat;
    }

    @Override
    public String toString() {
        return "Ligne{" + "produit=" + produit.getIdproduit()+" "+" "+produit.getNumprod()+" "+produit.getDescription() + ", quantite=" + quantite + ", prixAchat=" + prixAchat + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ligne ligne = (Ligne) o;
        return produit.equals(ligne.produit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produit);
    }
}
