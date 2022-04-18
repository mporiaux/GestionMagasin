/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.modele;

import magasin.metier.Client;
import magasin.metier.ComFact;
import magasin.metier.Ligne;
import magasin.metier.Produit;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michel
 */
public class ModeleComFact implements DAOComFact {

    private List<ComFact> mesComFacts = new ArrayList<>();

    @Override
    public ComFact create(ComFact newcf) {
        mesComFacts.add(newcf);
        return newcf;
    }

    @Override
    public ComFact read(ComFact cfrech) {
        int p = mesComFacts.indexOf(cfrech);
        if(p<0) return null;
        return mesComFacts.get(p);
    }

    @Override
    public ComFact update(ComFact cfrech) {
        ComFact cf = read(cfrech);
        if (cf == null) {
            return null;
        }
        cf.setNumfact(cfrech.getNumfact());
        cf.setEtat(cfrech.getEtat());
        cf.setMontant(cfrech.getMontant());
        cf.setDatecom(cfrech.getDatecom());
        cf.setDateFacturation(cfrech.getDateFacturation());
        cf.setDatePayement(cfrech.getDatePayement());
        cf.setLignes(cfrech.getLignes());
        return cf;
    }

    @Override
    public boolean delete(ComFact cfrech) {
        ComFact cf = read(cfrech);
        if (cf != null && cf.getLignes().isEmpty()) {
            mesComFacts.remove(cf);
            Client cl = cf.getClient();
            cl.supComFact(cf);
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean addLigne(ComFact cf,Ligne l) {
        Produit p = l.getProduit();
        int q = l.getQuantite();
        if (p.retirer(q)) {
            boolean res = cf.addLigne(l);
            if (res) {
                update(cf);
                return true;
            } else {
                p.reapprovisionner(q);
                return false;
            }
        } else return false;
    }

    @Override
    public boolean suppLigne(ComFact cf, Ligne l) {
       return  cf.supLigne(l);
    }


    @Override
    public List<ComFact> readAll() {
        return mesComFacts;
    }


}
