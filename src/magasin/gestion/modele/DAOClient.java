/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.modele;

import magasin.metier.Client;
import magasin.metier.Produit;

import java.util.List;

/**
 *
 * @author Michel
 */
public interface DAOClient extends DAO<Client>{
    List<Produit> produitsAchetes(Client clrech);

}
