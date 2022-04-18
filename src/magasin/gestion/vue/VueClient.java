/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.vue;


import magasin.metier.Client;
import magasin.metier.ComFact;

import java.util.List;

/**
 *
 * @author Michel
 */
public class VueClient extends VueCommune implements VueClientInterface{


    @Override
    public Client create() {
        String nom = getMsg("nom : ");
        String prenom = getMsg("prénom : ");
        Integer cp = Integer.parseInt(getMsg("CP : "));
        String loc = getMsg("localité :");
        String rue = getMsg("rue : ");
        String num = getMsg("numéro : ");
        String tel = getMsg("téléphone : ");
        Client newcl = new Client(nom, prenom, cp, loc, rue, num, tel);
        return newcl;
    }

  
    @Override
    public void display(Client cl) {
        displayMsg(cl.toString());
        if (!cl.getComFacts().isEmpty()) {
            String rep;
            do {
                rep = getMsg("Afficher ses commandes (o/n) ");
            } while (!rep.equalsIgnoreCase("o") && !rep.equalsIgnoreCase("n"));

            if (rep.equalsIgnoreCase("o")) {
                for (ComFact cf : cl.getComFacts()) {
                    displayMsg(cf.toString());
                }
            }
        }
    }

 
    @Override
    public Client update(Client cl) {
       
        do {
            String ch = getMsg("1.changement de téléphone\n2.fin");
            switch (ch) {
                case "1":
                    String ntel = getMsg("nouveau numéro de téléphone :");
                    cl.setTel(ntel);//on devrait tester que cela ne crée pas de doublons nom-prénom-tel
                    break;
                case "2":
                    return cl;
                default:
                    displayMsg("choix invalide");
            }

        } while (true);
    }


    @Override
    public Integer read() {
        String ns = getMsg("numéro de client : ");
        int n = Integer.parseInt(ns);
        return n;
    }

    @Override
    public void affAll(List<Client> lcl) {
        int i =0;
        for(Client cl:lcl){
            displayMsg((++i)+"."+cl.toString());
        }
    }


    @Override
    public void affLobj(List lobj) {
        int i =0;
        for(Object o:lobj){
            displayMsg((++i)+"."+o.toString());
        }
    }
}
