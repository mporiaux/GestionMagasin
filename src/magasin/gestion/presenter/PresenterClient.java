/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.presenter;

import magasin.gestion.modele.DAOClient;
import magasin.gestion.vue.VueClientInterface;
import magasin.metier.Client;
import magasin.metier.ComFact;

import java.util.List;

/**
 *
 * @author Michel
 */
public class   PresenterClient {

    private DAOClient mdc;
    private VueClientInterface vuec;


    public PresenterClient(DAOClient mdc, VueClientInterface vuec) {
        this.mdc = mdc; //injection de dépendance
        this.vuec = vuec;//injection de dépendance

    }

    public void gestion() {

        do {
            int ch = vuec.menu(new String[]{"ajout", "recherche unique", "modification", "suppression","voir tous","détail des commandes","fin"});
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
                case 6:detailCom();
                    break;
                case 7 :  return;
            }
        } while (true);

    }

    private void detailCom() {
        Client cl = recherche();

            if (cl != null) {
                do {
                    List l=null;
                    int ch = vuec.menu(new String[]{"commandes en cours", "factures non payees", "factures en retard", "factures payees", "produits achetés", "fin"});
                    switch (ch) {
                        case 1:
                             l=cl.commandesEnCours();
                            break;
                        case 2:
                            l = cl.facturesNonPayees();
                            break;
                        case 3:
                            l= cl.facturesRetard();
                            break;
                        case 4:
                            l=cl.facturesPayees();
                            break;
                        case 5:
                            l=mdc.produitsAchetes(cl);
                            break;
                        case 6:
                            return;
                    }
                    if(l==null) {
                        vuec.displayMsg("une erreur s'est produite");
                        continue;
                    }
                    if(l.isEmpty()) vuec.displayMsg("aucun élément à afficher");
                    else vuec.affLobj(l);
                }

                while (true) ;
            }
   }


    protected void ajout() {

        Client newcl = vuec.create();
        newcl = mdc.create(newcl);
        if (newcl == null) {
            vuec.displayMsg("erreur lors de la création du client - doublon");
            return;
        }

        vuec.displayMsg("cient créé");
        vuec.display(newcl);

    }

    protected Client recherche() {

        int nrech = vuec.read();
        Client cl = new Client(nrech,"","",null,"","","","");
        cl = mdc.read(cl);
        if (cl == null) {
            vuec.displayMsg("client introuvable");
            return null;
        }
        vuec.display(cl);
        return cl;
    }

    protected void modification() {
        Client cl = recherche();
        if (cl != null) {
            cl =  vuec.update(cl);
            mdc.update(cl);
        }
    }

    protected void suppression() {
        Client cl = recherche();
        if (cl != null) {
            String rep;
            do {
                rep = vuec.getMsg("confirmez-vous la suppression (o/n) ? ");

            } while (!rep.equalsIgnoreCase("o") && !rep.equalsIgnoreCase("n"));

            if (rep.equalsIgnoreCase("o")) {
               if( mdc.delete(cl))vuec.displayMsg("client supprimé");
               else vuec.displayMsg("client non supprimé");
            }
        }
    }
    


    protected void affAll() {
        vuec.affAll(mdc.readAll());
    }
}
