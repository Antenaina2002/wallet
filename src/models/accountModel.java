package models;

public class accountModel {
    int id;
    private String user;
    private String RIB;
    private int wallet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public String getRIB() {
        return RIB;
    }

    public int getWallet() {
        return wallet;
    }

    public accountModel(int id, String user, String RIB, int wallet) {
        this.id = id;
        this.user = user;
        this.RIB = RIB;
        this.wallet = wallet;
    }
}
