package Items;

public class Upgrades {
    private boolean hasTearsOnce;     // Tears of a Fallen Angel: limited to 1 purchase/playthrough
    private boolean hasShovelUpgrade; // dig spike tiles with no damage
    private boolean hasBatTamer;      // all bat damage becomes a flat 0.5
    private boolean hasAirShoes;      // walk on water tiles, immune to heat damage
    private boolean hasStewshine;     // +1 max HP
    private boolean hasMikanMochi;    // +1 max HP
    private boolean hasKurosawaMacha; // +1 max HP
    private boolean hasChocoMintOnce; // Choco-mint Ice Cream: limited to 1 purchase/playthrough

    //------------------------
    // getters
    //------------------------
    public boolean hasTearsOnce() { return hasTearsOnce; }
    public boolean hasShovelUpgrade() { return hasShovelUpgrade; }
    public boolean hasBatTamer() { return hasBatTamer; }
    public boolean hasAirShoes() { return hasAirShoes; }
    public boolean hasStewshine() { return hasStewshine; }
    public boolean hasMikanMochi() { return hasMikanMochi; }
    public boolean hasKurosawaMacha() { return hasKurosawaMacha; }
    public boolean hasChocoMintOnce() { return hasChocoMintOnce; }

    //------------------------
    // setters
    //------------------------
    public void setHasTearsOnce(boolean value) { this.hasTearsOnce = value; }
    public void setHasShovelUpgrade(boolean value) { this.hasShovelUpgrade = value; }
    public void setHasBatTamer(boolean value) { this.hasBatTamer = value; }
    public void setHasAirShoes(boolean value) { this.hasAirShoes = value; }
    public void setHasStewshine(boolean value) { this.hasStewshine = value; }
    public void setHasMikanMochi(boolean value) { this.hasMikanMochi = value; }
    public void setHasKurosawaMacha(boolean value) { this.hasKurosawaMacha = value; }
    public void setHasChocoMintOnce(boolean value) { this.hasChocoMintOnce = value; }
}
