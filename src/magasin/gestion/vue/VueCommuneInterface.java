package magasin.gestion.vue;

public interface VueCommuneInterface {
    int menu(String[] options);

    void displayMsg(String msg);

    String getMsg(String invite);
}
