/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.vue;

import magasin.metier.Produit;

import java.math.BigDecimal;
import java.util.List;
import java.util.PrimitiveIterator;

/**
 *
 * @author Michel
 */
public class VueProduit extends VueCommune implements VueProduitInterface{


    @Override
    public Produit create() {
        String numprod = getMsg("code produit : ");
        String description = getMsg("description: ");
        String phtvas = getMsg("prix hors tva : ");
        BigDecimal phtva = new BigDecimal(phtvas);
        String stocks = getMsg("stock :");
        int stock = Integer.parseInt(stocks);
        String stockMins = getMsg("stock Minimum :");
        int stockMin = Integer.parseInt(stockMins);
        Produit newpr = new Produit(numprod, description, phtva, stock,stockMin);
        return newpr;
    }

 
    @Override
    public void display(Produit pr) {
        displayMsg(pr.toString());
    }


    @Override
    public Produit update(Produit pr) {

        do {
            String ch = getMsg("1.changement de prix\n2.réapprovisionner\n3.fin");
            switch (ch) {
                case "1":
                    String phtvas = getMsg("prix hors tva : ");
                    BigDecimal phtva = new BigDecimal(phtvas);
                    pr.setPhtva(phtva);
                    break;
                case "2":
                    int q = pr.quantiteACommander();
                    System.out.println("indication , il faufrait recommander : "+q);
                    String qs = getMsg("quantité à commander :");
                     q = Integer.parseInt(qs);
                    pr.reapprovisionner(q);
                    break;
                case "3":
                    return pr;
                default:
                    displayMsg("choix invalide");
            }

        } while (true);
    }

    
    @Override
    public String read() {
        String numprod = getMsg("numéro de produit :");
        return numprod;
    }

    @Override
    public void affAll(List<Produit> lpr) {
        int i =0;
        for(Produit p :lpr){
            displayMsg((++i)+"."+p.toString());
        }
    }
}
