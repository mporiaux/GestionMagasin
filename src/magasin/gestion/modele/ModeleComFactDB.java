package magasin.gestion.modele;

/**
 * classe de mappage poo-relationnel client
 *
 * @author Michel Poriaux
 * @version 1.0
 * @see Client
 */

import magasin.metier.Client;
import magasin.metier.ComFact;
import magasin.metier.Ligne;
import magasin.metier.Produit;
import myconnections.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ModeleComFactDB implements DAOComFact {

    protected Connection dbConnect;

    /**
     * méthode permettant d'injecter la connexion à la DB venue de l'application
     * principale
     */
    public ModeleComFactDB() {
        dbConnect = DBConnection.getConnection();
    }

    /**
     * création d'un client sur base des valeurs de son objet métier
     *
     * @param obj commande à créer
     * @return client créé
     */
    @Override
    public ComFact create(ComFact obj) {

        String req1 = "insert into apicomfact(datecommande,etat,montant,idclient) values(?,?,?,?)";
        String req2 = "select MAX(idcommande) from apicomfact where idclient=?";
        try ( PreparedStatement pstm1 = dbConnect.prepareStatement(req1);  PreparedStatement pstm2 = dbConnect.prepareStatement(req2)) {
            pstm1.setDate(1, Date.valueOf(obj.getDatecom()));
            pstm1.setString(2, "" + obj.getEtat());
            pstm1.setBigDecimal(3, obj.getMontant());
            pstm1.setInt(4, obj.getClient().getIdclient());
            int n = pstm1.executeUpdate();
            if (n == 0) {
                return null;
            }
            pstm2.setInt(1, obj.getClient().getIdclient());
            ResultSet rs2 = pstm2.executeQuery();
            if (rs2.next()) {
                int idcommande = rs2.getInt(1);
                obj.setIdcommande(idcommande);
                return obj;
            } else {
                return null;
            }
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * récupération des données d'une commande sur base de son identifiant
     *
     * @param cfRech commande recherchée
     * @return client trouvé
     */
    @Override
    public ComFact read(ComFact cfRech) {


      /*  String req="select * from( select * from API_CLIENT CL join API_COMFACT CO on CL.IDCLIENT=CO.IDCLIENT  where CO.NUMCOMMANDE=141 )" +
                " A LEFT join (select * from  API_LIGNE LG  join API_PRODUIT  PR on LG.IDPRODUIT=PR.IDPRODUIT) B on (A.NUMCOMMANDE = B.IDCOMFACT);\n";*/
        String req = "select * from apicomfactclientprod where idcommande = ?";
        int idcf = cfRech.getIdcommande();
        try (PreparedStatement pstm = dbConnect.prepareStatement(req);) {
            pstm.setInt(1, idcf);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                Integer numfact = rs.getInt("NUMFACT");
                LocalDate datecom = rs.getDate("DATECOMMANDE").toLocalDate();
                LocalDate datefact = rs.getDate("DATEFACTURATION")==null ? null :rs.getDate("DATEFACTURATION").toLocalDate();
                LocalDate datepay = rs.getDate("DATEPAYEMENT")==null? null :  rs.getDate("DATEPAYEMENT").toLocalDate();
                char etat = rs.getString("ETAT").charAt(0);
                BigDecimal montant = rs.getBigDecimal("MONTANT").setScale(2);
                int idclient = rs.getInt("IDCLIENT");
                String nom = rs.getString("NOM");
                String prenom = rs.getString("PRENOM");
                int cp = rs.getInt("CP");
                String localite = rs.getString("LOCALITE");
                String rue = rs.getString("RUE");
                String num = rs.getString("NUM");
                String tel = rs.getString("TEL");
                Client cl = new Client(idclient, nom, prenom, cp, localite, rue, num, tel);
                ComFact cf = new ComFact(idcf, numfact, datecom,datefact,datepay, etat, montant, cl);
                int idpr= rs.getInt("IDPRODUIT");

                List<Ligne> llg = new ArrayList<>();
                if (idpr != 0) {
                 do {
                        int quant = rs.getInt("QUANTITE");
                        BigDecimal pa = rs.getBigDecimal("PRIXACHAT").setScale(2);
                        idpr = rs.getInt("IDPRODUIT");
                        String numprod = rs.getString("NUMPROD");
                        String description = rs.getString("DESCRIPTION");
                         Produit pr = new Produit(idpr, numprod, description);
                        Ligne lg = new Ligne(pr, quant, pa);
                        llg.add(lg);
                    } while(rs.next());

                }
                cf.setLignes(llg);
                return cf;
            } else {
               return null;
            }
        }
        catch (Exception e){
            return null;
        }
    }


        /**
     * mise à jour des données d'une commande sur base de son identifiant
     *
     * @return CommFact
     * @param cf comFact à mettre à jour
     */
    @Override
    public ComFact update(ComFact cf){
        String req1 = "update apicomfact set numfact= ? ,etat=?,montant=?,datefacturation=?,datepayement=? where idcommande = ?";
        String req2 = "select MAX(NUMFACT) from APICOMFACT";
         try ( PreparedStatement pstm = dbConnect.prepareStatement(req1);PreparedStatement pstm2 = dbConnect.prepareStatement(req2) ) {
          if(cf.getEtat()=='f'){
              ResultSet rs = pstm2.executeQuery();
              rs.next();
              int numfact= rs.getInt(1)+1;
              cf.setNumfact(numfact);
          }
            pstm.setInt(6, cf.getIdcommande());
            pstm.setInt(1, cf.getNumfact());
            pstm.setString(2, "" + cf.getEtat());
            pstm.setBigDecimal(3, cf.getMontant());
            pstm.setDate(4,cf.getDateFacturation()==null?null:Date.valueOf(cf.getDateFacturation()));
            pstm.setDate(5,cf.getDatePayement()==null?null:Date.valueOf(cf.getDatePayement()));
            int n = pstm.executeUpdate();
            if (n == 0) {
                return null;
            }    
                    
            return read(cf);
        }
         catch (Exception e){
             return null;
         }
    }


    @Override
    public boolean addLigne(ComFact cf,Ligne l) {
        /* String req1= "insert into apiligne(idcommande,idproduit,quantite,prixachat) values(?,?,?,?)";
         String req2="select * from apiproduit where idproduit = ? for update  wait 10";
         String req3= "update apicomfact set montant = montant + ? where idcommande = ?";
         String req4="update apiproduit set stock=stock - ? where idproduit = ?";

         try ( PreparedStatement pstm = dbConnect.prepareStatement(req1);
               PreparedStatement pstm2=dbConnect.prepareStatement(req2);
               PreparedStatement pstm3=dbConnect.prepareStatement(req3);
               PreparedStatement pstm4=dbConnect.prepareStatement(req4);) {
            dbConnect.setAutoCommit(false);
            Produit pr = l.getProduit();
            int idproduit= pr.getIdproduit();
            pstm4.setInt(1,l.getQuantite());
            pstm4.setInt(2,idproduit);
            pstm2.setInt(1,idproduit);
            pstm2.executeQuery();//verrouillage du record produit accédé
            pstm4.executeUpdate();//mise à jour du record produit accédé
             BigDecimal valeur = l.getPrixAchat().multiply(new BigDecimal(l.getQuantite()));
             valeur= valeur.setScale(2,BigDecimal.ROUND_HALF_UP);
             pstm3.setBigDecimal(1,valeur);
             pstm3.setInt(2,cf.getIdcommande());
             pstm3.executeUpdate();
            pstm.setInt(1, cf.getIdcommande());
            pstm.setInt(2, idproduit);
            pstm.setInt(3, l.getQuantite());
            pstm.setBigDecimal(4,l.getPrixAchat());
            int n = pstm.executeUpdate();

            dbConnect.commit();//libération du record produit
            if (n == 0) {
                try{
                    dbConnect.rollback(); //libération du reord produit
                }
                catch (Exception e){}
                dbConnect.setAutoCommit(true);
                return false;
            } else {
               dbConnect.setAutoCommit(true);
               return true;

            }
        }
        catch(Exception e){

            try{
                dbConnect.rollback();//libération du record produit
                dbConnect.setAutoCommit(true);
            }
            catch (Exception e2){   }

             return false;
        }*/
//version sans mise à jour du stock de produits
        String req1 = "insert into apiligne(idcommande,idproduit,quantite,prixachat) values(?,?,?,?)";
        String req3 = "update apicomfact set montant = montant + ? where idcommande = ?";

        try (PreparedStatement pstm = dbConnect.prepareStatement(req1);
             PreparedStatement pstm3 = dbConnect.prepareStatement(req3)) {
            Produit pr = l.getProduit();
            int idproduit = pr.getIdproduit();
            BigDecimal valeur = l.getPrixAchat().multiply(new BigDecimal(l.getQuantite()));
            valeur = valeur.setScale(2, BigDecimal.ROUND_HALF_UP);
            pstm3.setBigDecimal(1, valeur);
            pstm3.setInt(2, cf.getIdcommande());
            pstm3.executeUpdate();
            pstm.setInt(1, cf.getIdcommande());
            pstm.setInt(2, idproduit);
            pstm.setInt(3, l.getQuantite());
            pstm.setBigDecimal(4, l.getPrixAchat());
            pstm.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean suppLigne(ComFact cf, Ligne l) {
    /*    String req1= "delete from apiligne where idcommande = ? and idproduit = ?";
        String req2="select * from apiproduit where idproduit = ? for update  wait 10";
        String req3= "update apicomfact set montant = montant - ? where idcommande = ?";
        String req4="update apiproduit set stock=stock + ? where idproduit = ?";
            String req5="select * from apiligne where idcommande = ? and idproduit = ? ";

        try ( PreparedStatement pstm = dbConnect.prepareStatement(req1);
              PreparedStatement pstm2=dbConnect.prepareStatement(req2);
              PreparedStatement pstm3=dbConnect.prepareStatement(req3);
              PreparedStatement pstm4=dbConnect.prepareStatement(req4);
               PreparedStatement pstm5=dbConnect.prepareStatement(req5);
              ) {
            Produit pr = l.getProduit();
            int idproduit = pr.getIdproduit();
            pstm5.setInt(1, cf.getIdcommande());
            pstm5.setInt(2, idproduit);
            ResultSet rs = pstm2.executeQuery();
            if (rs.next()) {
                dbConnect.setAutoCommit(false);


                pstm4.setInt(1, l.getQuantite());
                pstm4.setInt(2, idproduit);
                pstm2.setInt(1, idproduit);
                pstm2.executeQuery();//verrouillage du record produit accédé
                pstm4.executeUpdate();//mise à jour du record produit accédé
                BigDecimal valeur = l.getPrixAchat().multiply(new BigDecimal(l.getQuantite()));
                valeur = valeur.setScale(2, BigDecimal.ROUND_HALF_UP);
                pstm3.setBigDecimal(1, valeur);
                pstm3.setInt(2, cf.getIdcommande());
                pstm3.executeUpdate();
                pstm.setInt(1, cf.getIdcommande());
                pstm.setInt(2, idproduit);
                int n = pstm.executeUpdate();

                dbConnect.commit();//libération du record produit
                if (n == 0) {
                    try {
                        dbConnect.rollback(); //libération du record produit
                    } catch (Exception e) {
                    }
                    dbConnect.setAutoCommit(true);
                    return false;
                } else {
                    dbConnect.setAutoCommit(true);
                    return true;

                }
            }
            else return false;
        }

        catch(Exception e){

            try{
                dbConnect.rollback();//libération du record produit
                dbConnect.setAutoCommit(true);
            }
            catch (Exception e2){   }

            return false;
        }

      */
        //version sans gestion du stock de produits

        String req1= "delete from apiligne where idcommande = ? and idproduit = ?";
        String req2="select * from apiligne where idcommande = ? and idproduit = ? ";
        String req3= "update apicomfact set montant = montant - ? where idcommande = ?";

        try ( PreparedStatement pstm = dbConnect.prepareStatement(req1);
              PreparedStatement pstm2 = dbConnect.prepareStatement(req2);
              PreparedStatement pstm3=dbConnect.prepareStatement(req3))
              {
            Produit pr = l.getProduit();
            int idproduit= pr.getIdproduit();
            pstm2.setInt(1,cf.getIdcommande());
            pstm2.setInt(2,idproduit);
            ResultSet rs = pstm2.executeQuery();
            if(rs.next()){
                BigDecimal valeur = rs.getBigDecimal("prixachat").multiply(new BigDecimal(rs.getInt("quantite")));
                valeur= valeur.setScale(2,BigDecimal.ROUND_HALF_UP);
                pstm3.setBigDecimal(1,valeur);
                pstm3.setInt(2,cf.getIdcommande());
                pstm3.executeUpdate();
                pstm.setInt(1, cf.getIdcommande());
                pstm.setInt(2, idproduit);
                pstm.executeUpdate();
                return true;
            }
            return false;
            }

        catch(Exception e){
             return false;
        }
    }


    /**
     * effacement de la commande sur base de son identifiant
     *
     * @param obj client à effacer
     */
    @Override
    public boolean delete(ComFact obj) {

        String req = "delete from apicomfact where numcommande= ?";
        try ( PreparedStatement pstm = dbConnect.prepareStatement(req)) {

            pstm.setInt(1, obj.getIdcommande());
            int n = pstm.executeUpdate();
            if (n == 0) {
                return false;
            } else {
                return true;
            }
        }
       catch(Exception e){
            return false;
       }
    }

    @Override
    public List<ComFact> readAll() {
        String req = "select * from apicomfact";
        List<ComFact> lcf = new ArrayList<>();
        try ( PreparedStatement pstm = dbConnect.prepareStatement(req);) {

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                int numcom = rs.getInt("IDCOMMANDE");
                Integer numfact = rs.getInt("NUMFACT");
                LocalDate datecom = rs.getDate("DATECOMMANDE").toLocalDate();
                LocalDate datefact = rs.getDate("DATEFACTURATION")==null ? null :rs.getDate("DATEFACTURATION").toLocalDate();
                LocalDate datepay = rs.getDate("DATEPAYEMENT")==null? null :  rs.getDate("DATEPAYEMENT").toLocalDate();
                char etat = rs.getString("ETAT").charAt(0);
                BigDecimal montant = rs.getBigDecimal("MONTANT");
                ComFact cf = new ComFact(numcom, numfact, datecom,datefact,datepay, etat, montant,null);
                lcf.add(cf);
            }
            if (lcf.isEmpty()) return null;
            return lcf;

        }
        catch(Exception e){
            return null;
        }
    }
}
