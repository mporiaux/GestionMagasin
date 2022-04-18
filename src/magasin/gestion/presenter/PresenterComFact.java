/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.presenter;

import magasin.gestion.modele.DAOComFact;
import magasin.gestion.vue.VueComFactInterface;
import magasin.metier.Client;
import magasin.metier.ComFact;
import magasin.metier.Ligne;
import magasin.metier.Produit;

import java.math.BigDecimal;

/**
 *
 * @author Michel
 */
public class PresenterComFact {

    private DAOComFact mdc;
    private VueComFactInterface vuecf;
    private PresenterClient pc;
    private PresenterProduit pp;


    public PresenterComFact(DAOComFact mdc, VueComFactInterface vuecf) {
        this.mdc = mdc; //injection de dépendance
        this.vuecf = vuecf;//injection de dépendance
    }

    public void setPc(PresenterClient pc) {
        this.pc = pc;
    }

    public void setPp(PresenterProduit pp) {
        this.pp = pp;
    }

    public void gestion() {

        do {
            int ch = vuecf.menu(new String[]{"ajout","recherche","modification","suppression","ajout ligne","suppression ligne","affichage complet","fin"});
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
                    addLigne();
                    break;
                case 6:
                    suppLigne();
                    break;
                case 7:
                    affAll();  
                    break;
               case 8:
                    return;
            }
        } while (true);

    }

    protected void ajout() {
        Client cl=pc.recherche();
        if(cl==null) return ;
        ComFact cf = new ComFact(cl);
        ComFact newcf = mdc.create(cf);
        if (newcf == null) {
            vuecf.displayMsg("erreur lors de la création de la commande");
            return ;
        }
        vuecf.displayMsg("commande créée");
        vuecf.display(newcf);
        return;
    }

    protected ComFact recherche() {

        int nrech = vuecf.read();
        ComFact cf = new ComFact(nrech, null, null, ' ', new BigDecimal(0), null);
        cf = mdc.read(cf);
        if (cf == null) {
            vuecf.displayMsg("commande introuvable");
            return null;
        }
        vuecf.display(cf);
        return cf;
    }

    protected void modification() {
        ComFact cf = recherche();
        if (cf != null) {
            cf = vuecf.update(cf);
            mdc.update(cf);
            vuecf.displayMsg("n° de facture = "+cf.getNumfact());
        }
    }

    protected void suppression() {
        ComFact cl = recherche();
        if (cl != null) {
            String rep;
            do {
                rep = vuecf.getMsg("confirmez-vous la suppression (o/n) ? ");

            } while (!rep.equalsIgnoreCase("o") && !rep.equalsIgnoreCase("n"));

            if (rep.equalsIgnoreCase("o")) {
                if (mdc.delete(cl)) {
                    vuecf.displayMsg("commande supprimée");
                } else {
                    vuecf.displayMsg("commande non supprimée");
                }
            }

        }
    }

   private void addLigne() {

       ComFact cf = recherche();
       if (cf == null) return;
       Produit p = pp.affAll();
       if (p == null) return;
       String qs = vuecf.getMsg("quantité : ");
       int q = Integer.parseInt(qs);
       Ligne l = new Ligne(p, q, p.getPhtva());
       boolean res = mdc.addLigne(cf,l);
       if(res)vuecf.displayMsg("ligne ajoutée");
       else vuecf.displayMsg("ligne pas ajoutée");
   }

    private void suppLigne() {
        ComFact cf = recherche();
        if (cf == null) return;
        Produit p = pp.affAll();
        if (p == null) return;
        Ligne l = new Ligne(p, 0, p.getPhtva());
        boolean res = mdc.suppLigne(cf,l);
        if(res)vuecf.displayMsg("ligne supprimée");
        else vuecf.displayMsg("ligne pas supprimée");
    }
   
    protected void affAll() {
        vuecf.affAll(mdc.readAll());
    }

}
