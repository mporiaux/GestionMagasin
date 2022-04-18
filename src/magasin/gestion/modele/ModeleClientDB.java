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
import magasin.metier.Produit;
import myconnections.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ModeleClientDB implements DAOClient {

    protected Connection dbConnect;

    /**
     * méthode permettant d'injecter la connexion à la DB venue de l'application
     * principale
     */
    public ModeleClientDB() {
        dbConnect = DBConnection.getConnection();
    }

    /**
     * création d'un client sur base des valeurs de son objet métier
     *
     * @param cl client à créer
     * @return client créé
     */
    @Override
    public Client create(Client cl) {

        String req1 = "insert into apiclient(nom,prenom,cp,localite,rue,num,tel) values(?,?,?,?,?,?,?)";
        String req2 = "select idclient from apiclient where nom=? and prenom=? and tel=?";
        try ( PreparedStatement pstm1 = dbConnect.prepareStatement(req1);  PreparedStatement pstm2 = dbConnect.prepareStatement(req2)) {
            pstm1.setString(1, cl.getNom());
            pstm1.setString(2, cl.getPrenom());
            pstm1.setInt(3, cl.getCp());
            pstm1.setString(4, cl.getLocalite());
            pstm1.setString(5, cl.getRue());
            pstm1.setString(6, cl.getNum());
            pstm1.setString(7, cl.getTel());
            int n = pstm1.executeUpdate();
            if (n == 0) {
               return null;
            }
            pstm2.setString(1, cl.getNom());
            pstm2.setString(2, cl.getPrenom());
            pstm2.setString(3, cl.getTel());
            ResultSet rs = pstm2.executeQuery();
            if (rs.next()) {
                int idclient = rs.getInt(1);
                cl.setIdclient(idclient);
                return cl;
            } else {
                throw new Exception("aucun client trouvé");
            }
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * récupération des données d'un client sur base de son identifiant
     *
     * @param client client recherché
     * @return client trouvé
     */
    @Override
    public Client read(Client client) {
          String req = "select * from apiclientcomfact where idclient = ?";
        try ( PreparedStatement pstm = dbConnect.prepareStatement(req);) {
            pstm.setInt(1, client.getIdclient());
             ResultSet rs = pstm.executeQuery() ;
                if (rs.next()) {
                    String nom = rs.getString("NOM");
                    String prenom = rs.getString("PRENOM");
                    int cp = rs.getInt("CP");
                    String localite = rs.getString("LOCALITE");
                    String rue = rs.getString("RUE");
                    String num = rs.getString("NUM");
                    String tel = rs.getString("TEL");
                    Client cl = new Client(client.getIdclient(), nom, prenom, cp, localite, rue, num, tel);
                    List<ComFact> lcf = new ArrayList<>();
                    if(rs.getInt("IDCOMMANDE")!=0){
                        do{
                            int idcommande = rs.getInt("IDCOMMANDE");
                            Integer numfact = rs.getInt("NUMFACT"); //integer plutôt que int car peut être null
                            LocalDate datecom = rs.getDate("DATECOMMANDE").toLocalDate();
                            LocalDate datefact = rs.getDate("DATEFACTURATION")==null ? null :rs.getDate("DATEFACTURATION").toLocalDate();
                            LocalDate datepay = rs.getDate("DATEPAYEMENT")==null? null :  rs.getDate("DATEPAYEMENT").toLocalDate();
                            char etat = rs.getString("ETAT").charAt(0);
                            BigDecimal montant = rs.getBigDecimal("MONTANT");
                            ComFact cf = new ComFact(idcommande,numfact,datecom,datefact,datepay,etat,montant,client);
                            lcf.add(cf);
                        }while(rs.next());

                    }
                    cl.setComFacts(lcf);
                    return cl;

                } else {
                    return null;
                }
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * mise à jour des données du client sur base de son identifiant
     *
     * @return Client
     * @param cl client à mettre à jour
     */
    @Override
    public Client update(Client cl) {
        String req = "update apiclient set nom=?,prenom=?,cp=?,localite=?,rue=?,num=?,tel=? where idclient= ?";
        try ( PreparedStatement pstm = dbConnect.prepareStatement(req)) {

            pstm.setInt(8, cl.getIdclient());
            pstm.setString(1, cl.getNom());
            pstm.setString(2, cl.getPrenom());
            pstm.setInt(3, cl.getCp());
            pstm.setString(4, cl.getLocalite());
            pstm.setString(5, cl.getRue());
            pstm.setString(6, cl.getNum());
            pstm.setString(7, cl.getTel());
            int n = pstm.executeUpdate();
            if (n == 0) {
                throw new Exception("aucun client mis à jour");
            }
            return read(cl);
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * effacement du client sur base de son identifiant
     *
     * @param cl client à effacer
     */
    @Override
    public boolean delete(Client cl) {

        String req = "delete from apiclient where idclient= ?";
        try ( PreparedStatement pstm = dbConnect.prepareStatement(req)) {

            pstm.setInt(1, cl.getIdclient());
            int n = pstm.executeUpdate();
            if (n == 0) return false;
            else return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Client> readAll() {
        String req = "select * from apiclient";
        List<Client> lc = new ArrayList<>();
        try ( PreparedStatement pstm = dbConnect.prepareStatement(req);  ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                int idclient = rs.getInt("IDCLIENT");
                String nom = rs.getString("NOM");
                String prenom = rs.getString("PRENOM");
                int cp = rs.getInt("CP");
                String localite = rs.getString("LOCALITE");
                String rue = rs.getString("RUE");
                String num = rs.getString("NUM");
                String tel = rs.getString("TEL");
                lc.add(new Client(idclient, nom, prenom, cp, localite, rue, num, tel));
            }
            if(lc.isEmpty()) return null;
            return lc;

        }
        catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Produit> produitsAchetes(Client clrech) {
        String req = "select * from PRODCLI where idclient =  ?";
        List<Produit> lp= new ArrayList<>();
        try ( PreparedStatement pstm = dbConnect.prepareStatement(req)) {
            pstm.setInt(1,clrech.getIdclient());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                int idproduit = rs.getInt("IDPRODUIT");
                String numprod = rs.getString("NUMPROD");
                String description = rs.getString("DESCRIPTION");
                lp.add(new Produit(idproduit, numprod, description));
            }
            return lp;

        }
        catch (Exception e){
            return null;
        }
    }


}
