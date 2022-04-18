package magasin.gestion.modele;

/**
 * classe de mappage poo-relationnel client
 *
 * @author Michel Poriaux
 * @version 1.0
 * @see Client
 */

import magasin.metier.Produit;
import myconnections.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ModeleProduitDB implements DAOProduit {

    protected Connection dbConnect;

    /**
     * méthode permettant d'injecter la connexion à la DB venue de l'application
     * principale
     */
    public ModeleProduitDB() {
        dbConnect = DBConnection.getConnection();
    }

    /**
     * création d'un client sur base des valeurs de son objet métier
     *
     * @param prd produit à créer
     * @return produit créé
     */
    @Override
    public Produit create(Produit prd) {

        String req1 = "insert into apiproduit(numprod,description,phtva,stock,stockmin) values(?,?,?,?,?)";
        String req2 = "select idproduit from apiproduit where numprod=?";
         try ( PreparedStatement pstm1 = dbConnect.prepareStatement(req1);PreparedStatement pstm2 = dbConnect.prepareStatement(req2)) {
            pstm1.setString(1, prd.getNumprod());
            pstm1.setString(2, prd.getDescription());
            pstm1.setBigDecimal(3, prd.getPhtva());
            pstm1.setInt(4, prd.getStock());
            pstm1.setInt(5,prd.getStockMin());
            int n = pstm1.executeUpdate();
            pstm2.setString(1, prd.getNumprod());
            ResultSet rs = pstm2.executeQuery();
             if (rs.next()) {
                 int idprod = rs.getInt(1);
                 prd.setIdproduit(idprod);
                 return prd;
             } else {
                 return null;
             }
          
        }
         catch(Exception e){
               return null;
         }
    }

    /**
     * récupération des données d'un client sur base de son identifiant
     *
     * @param prRech   produit recherché
     * @return client trouvé
     */
    @Override
    public Produit read(Produit prRech) {
        String req = "select * from apiproduit where NUMPROD = ?";
        try ( PreparedStatement pstm = dbConnect.prepareStatement(req);) {
            String numprod = prRech.getNumprod();
            pstm.setString(1, numprod);
            ResultSet rs = pstm.executeQuery();
                if (rs.next()) {
                    int idpr = rs.getInt("IDPRODUIT");
                    String description = rs.getString("DESCRIPTION");
                    BigDecimal phtva = rs.getBigDecimal("PHTVA");
                    int stock = rs.getInt("STOCK");
                    int stockMin = rs.getInt("STOCKMIN");
                    Produit pr = new Produit(idpr,numprod,description, phtva, stock,stockMin);
                    return pr;

                } else {
                    return null;
                }

        }
        catch(Exception e){
            return null;
        }
    }

    /**
     * mise à jour des données du client sur base de son identifiant
     *
     * @return produit
     * @param prd produit à mettre à jour
     */
    @Override
    public Produit update(Produit prd){
         String req = "update apiproduit set description=?,phtva=?,stock=?,stockmin=? where idproduit = ?";
        try ( PreparedStatement pstm = dbConnect.prepareStatement(req)) {

            pstm.setInt(5, prd.getIdproduit());
            pstm.setString(1, prd.getDescription());
            pstm.setBigDecimal(2, prd.getPhtva());
            pstm.setInt(3, prd.getStock());
            pstm.setInt(4, prd.getStockMin());
            int n = pstm.executeUpdate();
            if (n == 0) {
               return null;
            }
            return read(prd);
        }
       catch (Exception e){
            return null;
       }
    }

    /**
     * effacement du client sur base de son identifiant
     *
     * @param obj produit à effacer
     */
    @Override
    public boolean delete(Produit obj) {

        String req = "delete from apiproduit where idproduit = ?";
        try ( PreparedStatement pstm = dbConnect.prepareStatement(req)) {

            pstm.setInt(1, obj.getIdproduit());
            int n = pstm.executeUpdate();
            if (n == 0) return false;
            else return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Produit> readAll(){
        String req = "select * from apiproduit";
        List<Produit> lc = new ArrayList<>();
        try ( PreparedStatement pstm = dbConnect.prepareStatement(req);  ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                int idproduit = rs.getInt("IDPRODUIT");
                String numprod = rs.getString("NUMPROD");
                String description = rs.getString("DESCRIPTION");
                BigDecimal phtva = rs.getBigDecimal("PHTVA");
                int stock = rs.getInt("STOCK");
                int stockmin = rs.getInt("STOCKMIN");
                lc.add(new Produit(idproduit,numprod,description,phtva, stock,stockmin));
            }
            return lc;

        }
        catch (Exception e){
            return null;
        }
    }
}
