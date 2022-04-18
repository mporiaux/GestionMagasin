package magasin.gestion.vue;

import magasin.metier.Client;

import java.util.List;

public interface VueClientInterface extends VueInterface<Client,Integer>  {
      void affLobj(List lobj);
}
