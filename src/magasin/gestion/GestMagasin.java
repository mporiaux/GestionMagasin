/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.gestion;

import magasin.gestion.modele.*;
import magasin.gestion.presenter.PresenterClient;
import magasin.gestion.presenter.PresenterComFact;
import magasin.gestion.presenter.PresenterProduit;
import magasin.gestion.vue.*;

import java.time.LocalDate;
import java.util.Scanner;

/**
 *
 * @author Michel
 */
public class GestMagasin {
    public static LocalDate maintenant = LocalDate.now();
    private static Scanner sc = new Scanner(System.in);

    private PresenterClient pc;
    private PresenterComFact pcf;
    private PresenterProduit pp;



    private  void changementDeDateActuelle(){
        System.out.println("nouvelle date : ");
        int j = sc.nextInt();
        int m = sc.nextInt();
        int a = sc.nextInt();
        sc.skip("\n");
        maintenant= LocalDate.of(a,m,j);
        System.out.println("changement de date effectué");
    }

    public void gestion(String modeVue,String modeData) {
        
        ///////////////////////////////////////////////////////////////////////////////
        //ATTENTION  à l'ordre il faut avoir instancié un élément avant de le passer en 
        //paramètre des setters des éléments suivants
        //cela concerne en particulier les presenters
        ///////////////////////////////////////////////////////////////////////////////


        VueClientInterface vuec;
        VueProduitInterface vuepr;
        VueComFactInterface vuecf;
        VueCommuneInterface vcm;

        DAOClient mdc;
        DAOComFact mdcf;
        DAOProduit mdpr;

        if (modeVue.equals("console")){
            vuec = new VueClient();
            vuecf=new VueComFact();
            vuepr = new VueProduit();
            vcm=new VueCommune();

        }
        else
        {
            vuec=new VueClientGraph();
            vuecf=new VueComFactGraph();
            vuepr=new VueProduitGraph();
            vcm=new VueCommuneGraph();
        }

        if(modeData.equals("db")){
            mdc= new ModeleClientDB();
            mdcf = new ModeleComFactDB();
            mdpr = new ModeleProduitDB();
        }
        else {
            mdc= new ModeleClient();
            mdcf = new ModeleComFact();
            mdpr = new ModeleProduit();
        }


        pp = new PresenterProduit(mdpr, vuepr);
        pc = new PresenterClient(mdc, vuec);
        pcf = new PresenterComFact(mdcf, vuecf);

        pcf.setPc(pc);
        pcf.setPp(pp);

        do {

            int ch = vcm.menu(new String[]{"Clients","Commandes","Produits","changement de date","fin"});

            switch (ch) {
                case 1:
                    pc.gestion();
                    break;
                case 2:
                    pcf.gestion();
                    break;
                case 3:
                    pp.gestion();
                    break;
                case 4:
                   changementDeDateActuelle();
                    break;
                case 5:
                    System.exit(0);
                default:
                    System.out.println("choix invalide");
            }
        } while (true);
    }

    public static void main(String[] args) {
        String  modeVue = args[0];
        String modeData= args[1];
        GestMagasin gm = new GestMagasin();
        gm.gestion(modeVue,modeData);
    }

}
