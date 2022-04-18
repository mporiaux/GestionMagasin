/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.vue;

import magasin.metier.Client;
import magasin.metier.ComFact;
import magasin.metier.Ligne;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Michel
 */
public class VueComFactGraph extends VueCommuneGraph implements VueComFactInterface {

    @Override
    public ComFact create() {
        //cette méthode a été développée uniquement pour satisfaire l'iplémentation de l'interface, elle n'est pas utilisée par le presenter
        return null;
    }

    @Override
    public void display(ComFact cf) {

       int i=0;
        StringBuffer sb= new StringBuffer(200);
        sb.append(cf+"\n");
        for (Ligne l :cf.getLignes()) {
           sb.append((++i)+"."+l+"\n");
        }
        displayMsg(sb.toString());
    }

    
    @Override
    public ComFact update(ComFact cf) {
     
        do {
            String ch = getMsg("1.changement d'état\n2.fin");
            switch (ch) {
                case "1":
                    if(cf.getEtat()=='c') {
                        cf.facturer();
                        displayMsg("commande facturée");
                    }
                    else  if(cf.getEtat()=='f'){
                        cf.payer();
                        displayMsg("facture payée");
                    }
                    else displayMsg("facture déjà payée");
                    break;
                case "2":
                    return cf;
                default:
                    displayMsg("choix invalide");
            }

        } while (true);
    }
    
    @Override
    public Integer read() {
        String ns = getMsg("numéro de commande : ");
        int n = Integer.parseInt(ns);
        return n;
    }
    
    
    @Override
    public void affAll(List<ComFact> lcf) {
        int i =0;
        StringBuffer sb= new StringBuffer(200);

        for(ComFact cf:lcf) sb.append((++i)+"."+cf+"\n");
        displayMsg(sb.toString());
    }

}
