package magasin.metier;

import magasin.gestion.GestMagasin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * classe métier de gestion d'une commande-facture
 *
 * @author Michel Poriaux
 * @version 1.0
 * @see Client
 * @see Ligne
 */
public class ComFact {
    private static int numComAct = 0;
    private static int numFactAct=0;

    /**
     * identifiant unique-numéro de commande
     */
    protected int idcommande;
    /**
     * numéro de facture - unique
     */
    protected Integer numfact;

    /**
     * date de la commande
     */
    protected LocalDate datecom;

    /**
     * date de la facture
     */
    protected LocalDate dateFacturation;
    /**
     * date du payement
     */
    protected LocalDate datePayement;
    /**
     * état de la facture :c:en commande-f:facturée-p:payée
     */
    protected char etat;
    /**
     * montant total de la commande
     */
    protected BigDecimal montant;
    /**
     * client de la commande
     */
    protected Client client;
    
    /**
     * liste des lignes de commande
     */
    protected List<Ligne> lignes = new ArrayList<>();

    /**
     * constructeur par défaut
     */
    public ComFact() {
    }


    /**
     * constructeur paramétré
     *
     * @param idcommande numéro de commande
     * @param numfact numéro de facture
     * @param datecom date de commande
     * @param etat état de la commande C,F,P
     * @param montant montant total de la commande
     */
    public ComFact(int idcommande, Integer numfact, LocalDate datecom, char etat, BigDecimal montant, Client client) {
        this.idcommande = idcommande;
        this.numfact = numfact;
        this.datecom = datecom;
        this.etat = etat;
        this.montant = montant.setScale(2, RoundingMode.HALF_UP);
        this.client = client;
    }

    public ComFact(Client client) {
        this.idcommande = ++numComAct;
        this.numfact = 0;
        this.datecom = GestMagasin.maintenant;
        this.etat = 'c';
        this.montant = BigDecimal.ZERO;
        this.client = client;
        client.addComFact(this);
    }

    public ComFact(int idcommande, Integer numfact, LocalDate datecom, LocalDate dateFacturation, LocalDate datePayement, char etat, BigDecimal montant, Client client) {
        this.idcommande = idcommande;
        this.numfact = numfact;
        this.datecom = datecom;
        this.dateFacturation = dateFacturation;
        this.datePayement = datePayement;
        this.etat = etat;
        this.montant = montant;
        this.client = client;
    }

    /**
     * getter client de la commande
     * @return client de la commande
     */
    public Client getClient() {
        return client;
    }

    /**
     * setter client de la commande
     * @param client client de la commande
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * getter date de commande
     *
     * @return date de la commande
     */
    public LocalDate getDatecom() {
        return datecom;
    }

    /**
     * setter date de commande
     *
     * @param datecom date de la commande
     */
    public void setDatecom(LocalDate datecom) {
        this.datecom = datecom;
    }


    /**
     * getter date de commande
     *
     * @return date de facturation
     */
    public LocalDate getDateFacturation() {
        return dateFacturation;
    }

    /**
     * setter date de commande
     *
     * @param dateFacturation date de facturation
     */
    public void setDateFacturation(LocalDate dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    /**
     * getter date de payement
     *
     * @return date de payement
     */
    public LocalDate getDatePayement() {
        return datePayement;
    }

    /**
     * setter date de payement
     *
     * @param datePayement date de payement
     */
    public void setDatePayement(LocalDate datePayement) {
        this.datePayement= datePayement;
    }

    /**
     * getter état de la commande
     *
     * @return état de la commande (C:en commande,F:facturée,P:payée)
     */
    public char getEtat() {
        return etat;
    }

    /**
     * setter état de la commande
     *
     * @param etat état de la commande (C:en commande,F:facturée,P:payée)
     */
    public void setEtat(char etat) {
        this.etat = etat;
    }

    /**
     * getter montant
     *
     * @return montant total de la commande
     */
    public BigDecimal getMontant() {
        return montant;
    }

    /**
     * setter du montant
     *
     * @param montant montant total de la commande
     */
    public void setMontant(BigDecimal montant) {
        this.montant = montant.setScale(2,RoundingMode.HALF_UP);
    }

    /**
     * getter numéro de commande
     *
     * @return numéro de la commande
     */
    public int getIdcommande() {
        return idcommande;
    }

    /**
     * setter numéro de commande
     *
     * @param idcommande numéro de la commande
     */
    public void setIdcommande(int idcommande) {
        this.idcommande = idcommande;
    }

    /**
     * getter numéro de facture
     *
     * @return nuléro de la facture
     */
    public Integer getNumfact() {
        return numfact;
    }

    /**
     * setter numéro de facture
     *
     * @param numfact numéro de facture
     */
    public void setNumfact(Integer numfact) {
        this.numfact = numfact;
    }

    /**
     * getter lignes de commandes
     *
     * @return listes des lignes de commande
     */
    public List<Ligne> getLignes() {
        return lignes;
    }

    /**
     * setter lignes
     *
     * @param lignes liste des lignes de commande
     */
    public void setLignes(List<Ligne> lignes) {
        this.lignes = lignes;
    }

    @Override
    public String toString() {
        return "ComFact{" +
                "idcommande=" + idcommande +
                ", numfact=" + numfact +
                ", datecom=" + datecom +
                ", dateFacturation=" + dateFacturation +
                ", datePayement=" + datePayement +
                ", etat=" + etat +
                ", montant=" + montant +
                ", client=" + client +
             '}';
    }

    /**
     * méthode toString
     *
     * @return informations complètes
     */


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.idcommande;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComFact other = (ComFact) obj;
        if (this.idcommande != other.idcommande) {
            return false;
        }
        return true;
    }

    public boolean estEnRetard(){
        return GestMagasin.maintenant.isAfter(dateFacturation.plusDays(15)) ;
    }

    public boolean addLigne(Ligne l) {
        if(lignes.contains(l)) return false;
        lignes.add(l);
        montant=montant.add(l.prixAchat.multiply(new BigDecimal(l.quantite)));
        return true;
    }



    public boolean supLigne(Ligne l){
          if(lignes.remove(l))  {
              montant = montant.subtract(l.prixAchat.multiply(new BigDecimal(l.quantite)));
              return true;
          }
          return false;
    }

    public void facturer(){
        etat='f';
        dateFacturation=GestMagasin.maintenant;
        numfact=++numFactAct;
    }

    public void payer(){
        etat='p';
        datePayement=GestMagasin.maintenant;
    }

    public List<Produit> produitsAchetes(){
        List<Produit> lp = new ArrayList<>();
        for(Ligne l: lignes){
            lp.add(l.getProduit());
        }
        return lp;
    }
}
