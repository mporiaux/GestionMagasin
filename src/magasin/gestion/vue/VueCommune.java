/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.vue;

import magasin.metier.Client;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Michel
 */
public class VueCommune implements VueCommuneInterface{
    
      private Scanner sc = new Scanner(System.in);
    @Override
    public int menu(String[] options) {
        do {
            StringBuilder sb = new StringBuilder(50);
            int i =0;
            for(String option : options) sb.append((++i)+"."+option+"\n");
            sb.append("choix:");
            System.out.println(sb.toString());
            String chs=sc.nextLine();
            int ch = Integer.parseInt(chs); //TODO gérer codages invalides
            if (ch >= 1 && ch <= options.length) {
                return ch;
            }
            System.out.println("choix invalide");
        } while (true);
    }

    @Override
    public void displayMsg(String msg) {
        System.out.println(msg);
    }

    @Override
    public String getMsg(String invite) {
        displayMsg(invite);
        String msg = sc.nextLine();
        return msg;
    }



}
