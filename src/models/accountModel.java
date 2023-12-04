package models;

public class accountModel {
    int id;
    private String RIB;
    private int wallet;

    public int getId() {
        return id;
    }

    public String getAuthorName() {
        return RIB;
    }

    public int getAuthorSex() {
        return wallet;
    }

    public accountModel(int id, String RIB, int wallet) {
        this.id = id;
        this.RIB = RIB;
        this.wallet = wallet;
    }
}
