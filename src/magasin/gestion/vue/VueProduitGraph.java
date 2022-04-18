/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.vue;

import magasin.metier.Client;
import magasin.metier.Produit;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Michel
 */
public class VueProduitGraph extends VueCommuneGraph implements VueProduitInterface {


    @Override
    public Produit create() {
        JTextField tfnumprod = new JTextField();
        JTextField tfdescr = new JTextField();
        JTextField tfphtva = new JTextField();
        JTextField tfstock = new JTextField();
        JTextField tfstockMin = new JTextField();

        Object[] message = {
                "numéro de produit: ", tfnumprod,
                "description:", tfdescr,
                "PHTVA:", tfphtva,
                "stock:", tfstock,
                "stockMin:", tfstockMin,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "nouveau produit", JOptionPane.DEFAULT_OPTION);
        String numprod = tfnumprod.getText().toString();
        String descr = tfdescr.getText().toString();
        String phtvas = tfphtva.getText().toString();
        String stocks = tfstock.getText().toString();
        String stockMins = tfstockMin.getText().toString();
        BigDecimal phtva = new BigDecimal(phtvas);
        int stock = Integer.parseInt(stocks);
        int stockMin = Integer.parseInt(stockMins);
        Produit newpr = new Produit(numprod, descr, phtva, stock,stockMin);
        return newpr;
    }

 
    @Override
    public void display(Produit pr) {
        displayMsg(pr.toString());
    }


    @Override
    public Produit update(Produit pr) {

        do {
            String ch = getMsg("1.changement de prix\n2.réapprovisiooner\n3.fin");
            switch (ch) {
                case "1":
                    String phtvas = getMsg("prix hors tva : ");
                    BigDecimal phtva = new BigDecimal(phtvas);
                    pr.setPhtva(phtva);
                    break;
                case "2":
                    int q = pr.quantiteACommander();
                   displayMsg("indication , il faufrait recommander : "+q);
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
    public void affAll(List<Produit> lp) {
        int i =0;
        StringBuffer sb= new StringBuffer(200);

        for(Produit p:lp) sb.append((++i)+"."+p+"\n");
        displayMsg(sb.toString());
    }

}
