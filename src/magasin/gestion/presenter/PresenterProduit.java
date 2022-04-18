/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.presenter;

import magasin.gestion.modele.DAOProduit;
import magasin.gestion.vue.VueProduitInterface;
import magasin.metier.Produit;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Michel
 */
public class PresenterProduit {

    private DAOProduit mdp;
    private VueProduitInterface vuep;

    public PresenterProduit(DAOProduit mdp, VueProduitInterface vuep) {
        this.mdp = mdp;//injection de dépendance
        this.vuep = vuep;//injection de dépendance
    }

    public void gestion() {

        do {
            int ch = vuep.menu(new String[]{"ajout", "recherche unique", "modification", "suppression","voir tous", "fin"});
            switch (ch) {
                case 1:
                    ajout();
                    break;
                case 2:
                    recherche();
                    break;
                case 3:
                    modification();
                    break;
                case 4:
                    suppression();
                    break;
                case 5:
                    affAll();
                    break;
                case 6:
                    return;

            }
        } while (true);


    }

    protected void ajout() {

        Produit newpr = vuep.create();
        newpr = mdp.create(newpr);
        if (newpr == null) {
            vuep.displayMsg("erreur lors de la création du produit - doublon");
            return;
        }

        vuep.displayMsg("produit créé");
        vuep.display(newpr);

    }

    protected Produit recherche() {

        String nrechs = vuep.read();
        Produit pr = new Produit(nrechs,"",new BigDecimal(0),0,0);
        pr = mdp.read(pr);
        if (pr == null) {
            vuep.displayMsg("produit introuvable");
            return null;
        }
        vuep.display(pr);
        return pr;
    }

    protected void modification() {
        Produit pr = recherche();
        if (pr != null) {
            pr =  vuep.update(pr);
            mdp.update(pr);
        }
    }

    protected void suppression() {
        Produit pr = recherche();
        if (pr != null) {
            String rep;
            do {
                rep = vuep.getMsg("confirmez-vous la suppression (o/n) ? ");

            } while (!rep.equalsIgnoreCase("o") && !rep.equalsIgnoreCase("n"));

            if (rep.equalsIgnoreCase("o")) {
               if(mdp.delete(pr))  vuep.displayMsg("produit supprimé");
               else vuep.displayMsg("produit non supprimé");
            }

        }
    }

    protected Produit affAll() {
       List<Produit> lp = mdp.readAll();
       vuep.affAll(mdp.readAll());
       do{
         String chs=vuep.getMsg("numéro de l'élément choisi (0 pour aucun) :");
         int ch=Integer.parseInt(chs);
         if(ch==0)return null;
         if(ch>=1 && ch <= lp.size()) return lp.get(ch-1);
        } while(true);
    }

}
