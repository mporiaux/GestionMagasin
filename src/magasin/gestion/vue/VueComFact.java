/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.vue;

import magasin.metier.ComFact;
import magasin.metier.Ligne;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Michel
 */
public class VueComFact extends VueCommune implements VueComFactInterface{

    private Scanner sc = new Scanner(System.in);


    @Override
    public ComFact create() {
        //cette méthode a été codée uniquement pour satisfaire l'implémentation de l'interface, elle n'est pas utilisée par le presenter
        return null;
    }

    @Override
    public void display(ComFact cf) {
       displayMsg(cf.toString());
       for (Ligne l :cf.getLignes()) {
                    displayMsg(l.toString());
       }
    }

    
    @Override
    public ComFact update(ComFact cf) {
     
        do {
            String ch = getMsg("1.changement d'état\n2.fin");
            switch (ch) {
                case "1":
                    if(cf.getEtat()=='c') {
                        cf.facturer();
                        System.out.println("commande facturée");
                    }
                    else  if(cf.getEtat()=='f'){
                        cf.payer();
                        System.out.println("facture payée");
                    }
                    else System.out.println("facture déjà payée");
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
        for(ComFact cf:lcf){
            displayMsg((++i)+"."+cf.toString());
        }
    }

}
