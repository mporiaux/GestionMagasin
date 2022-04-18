/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion.vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

/**
 *
 * @author Michel
 */
public class VueCommuneGraph implements VueCommuneInterface {
    
      private Scanner sc = new Scanner(System.in);
    @Override
    public int menu(String[] options) {
        int ch;
        JTextField choiceField = new JTextField(5);
        StringBuilder sb = new StringBuilder(50);
        int i =0;
        for(String option : options) sb.append((++i)+"."+option+"\n");
        sb.append("choix:");
        do {
            ch = Integer.parseInt(JOptionPane.showInputDialog(sb.toString()));//TODO gérer encodages invalides
            if (ch >= 1 && ch <= options.length) {
                    return ch;
                }
       }
        while (true);
    }

    @Override
    public void displayMsg(String msg) {
       JOptionPane.showConfirmDialog(null, msg,"information", JOptionPane.DEFAULT_OPTION);
    }

    @Override
    public String getMsg(String invite) {
        return JOptionPane.showInputDialog(null,invite,"question",JOptionPane.DEFAULT_OPTION);
    }
}
