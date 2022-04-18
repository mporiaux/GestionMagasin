/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.modele;

import java.util.List;

/**
 *
 * @author Michel
 */
public interface DAO<T> {

    T create(T newObj);

    boolean delete(T ObjRech);

    T read(T objRech);

    T update(T objRech);

    List<T> readAll();
}
