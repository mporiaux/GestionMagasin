/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.modele;

import magasin.metier.ComFact;
import magasin.metier.Ligne;


/**
 *
 * @author Michel
 */
public interface DAOComFact extends DAO<ComFact> {

    boolean addLigne(ComFact cf, Ligne l);
    boolean suppLigne(ComFact cf,Ligne l);
}
