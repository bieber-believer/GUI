package Game;

import LivingThings.Idol;

import java.util.ArrayList;

public class OverallStats {
    private ArrayList<Idol> aqours; // the aqours ppl
    private int numSiren; // # of times siren was killed
    private int gamesLost; // # of times player got pwned
    private int goldSpent; // total gold spent

    private int gold; // the gold carried over to the new game
    private int noppoBreadCount; // the noppo bread carried over to new game
    private int tearsOfAngelCount;

    /**
     * Creates an OverallStats object. Instansitaties the Aquors array list and initializes other values to 0;
     */
    public OverallStats(){
        aqours = new ArrayList<>();

        aqours.add(new Idol("Chika", "Yasudaya Ryokan"));
        aqours.add(new Idol("Riko", "Numazu Deep Sea Aquarium"));
        aqours.add(new Idol("You", "Izu-Mito Sea Paradise"));
        aqours.add(new Idol("Hanamaru", "Shougetsu Confectionary"));
        aqours.add(new Idol("Ruby", "Nagahama Castle Ruins"));
        aqours.add(new Idol("Dia", "Numazugoyotei"));
        aqours.add(new Idol("Kanan", "Uchiura Bay Pier"));
        aqours.add(new Idol("Mari", "Awashima Marine Park"));

        numSiren = 0;
        gamesLost = 0;
        goldSpent = 0;
    }

    //------------------------
    // getters
    //------------------------
    /**
     * Return the list of idol in aquors
     *
     * @return ArrayList of idols
     */
    public ArrayList<Idol> getAqours() {
        return aqours;
    }

    /**
     * Returns the amount of times the siren was defeated
     *
     * @return number of times siren was deafeated
     */
    public int getNumSiren() {
        return numSiren;
    }


    /**
     * Returns the number of games lost
     *
     * @return number of games lost
     */
    public int getGamesLost() {
        return gamesLost;
    }

    /**
     * Returns the gold spent in Hanamaru's shop
     *
     * @return total gold spent
     */
    public int getGoldSpent() {
        return goldSpent;
    }

    public int getGold() {
        return gold;
    }

    public int getNoppoBreadCount() {
        return noppoBreadCount;
    }

    public int getTearsOfAngelCount() {
        return tearsOfAngelCount;
    }

    //------------------------
    // setters
    //------------------------

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setNoppoBreadCount(int noppoBreadCount) {
        this.noppoBreadCount = noppoBreadCount;
    }

    public void setTearsOfAngelCount(int tearsOfAngelCount) {
        this.tearsOfAngelCount = tearsOfAngelCount;
    }

    /**
     * Adds the given amount to the total gold spent
     *
     * @param amount amount of gold to add
     */
    public void addGoldSpent(int amount) {
        goldSpent += amount;
    }

    /**
     * Increments the number of games lost by 1
     */
    public void addGamesLost() {
        this.gamesLost++;
    }

    /**
     * Increments the number of times the siren was deafeated by 1
     */
    public void addNumSiren() {
        numSiren++;
    }
}
