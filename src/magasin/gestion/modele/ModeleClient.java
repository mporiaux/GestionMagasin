/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.modele;

import magasin.metier.Client;
import magasin.metier.Produit;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michel
 */
public class ModeleClient implements DAOClient {

    private List<Client> mesClients=new ArrayList<>();
     @Override
    public Client create(Client newcl) {
        if(mesClients.contains(newcl)) return null;
        mesClients.add(newcl);
        return newcl;
    }

    @Override
    public Client read(Client clrech) {
        int idrech=clrech.getIdclient();
        for (Client cl : mesClients) {
            if (cl.getIdclient()== idrech) {
                return cl;
            }
        }
        return null;
    }

    @Override
    public Client update(Client clrech) {
        Client cl = read(clrech);
        if (cl == null) {
            return null;
        }
        cl.setNom(clrech.getNom());
        cl.setPrenom(clrech.getPrenom());
        cl.setCp(clrech.getCp());
        cl.setLocalite(clrech.getLocalite());
        cl.setNum(clrech.getNum());
        cl.setRue(clrech.getTel());
        cl.setComFacts(clrech.getComFacts());
        cl.setTel(clrech.getTel());
        return cl;
    }
    
    @Override
    public boolean delete(Client clrech){
        Client cl = read(clrech);
        if(cl!=null && cl.getComFacts().isEmpty()) {
            mesClients.remove(cl);
            return true;
        }   
        else return false;
    }

    @Override
    public List<Client> readAll() {
        return mesClients;
    }

    @Override
    public List<Produit> produitsAchetes(Client cl) {
        return cl.produitsAchetes();
    }
}
