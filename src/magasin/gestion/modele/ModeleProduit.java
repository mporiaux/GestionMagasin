/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.modele;

import magasin.metier.Produit;

import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author Michel
 */
public class ModeleProduit implements DAOProduit {

    private List<Produit> mesProduits=new ArrayList<>();
  
    @Override
    public Produit create(Produit newpr) {
        if(mesProduits.contains(newpr)) return null;
        mesProduits.add(newpr);
        return newpr;
    }

    @Override 
    public Produit read(Produit prech){ 
        String numprod = prech.getNumprod();
        for(Produit pr : mesProduits){
            if(pr.getNumprod().equals(numprod)) return pr;
        }
        return null;
    }

    @Override
    public Produit update(Produit prrech) {
        Produit pr = read(prrech);
        if (pr == null) {
            return null;
        }
        pr.setDescription(prrech.getDescription());
        pr.setPhtva(prrech.getPhtva());
        pr.setStock(prrech.getStock());
        pr.setStockMin(prrech.getStockMin());
        return pr;
    }
    
    @Override
    public boolean delete(Produit prrech){
        Produit pr = read(prrech);
        if(pr!=null) {
            mesProduits.remove(pr);
            return true;
        }   
        else return false;
    }

    @Override
    public List<Produit> readAll() {
        return mesProduits;
    }
}
