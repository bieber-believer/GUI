package Items;

import Game.OverallStats;
import LivingThings.Idol;
import LivingThings.Player;

import java.util.ArrayList;

public class Shop {

    /**
     * Describes one thing for sale
     */
    public static class ShopItem{
        private String name;
        private int price;
        private String description;
        private String unlockedBy; // idol name or null if always avail
        private boolean purchaseOnce;

        public ShopItem(String name, int price, String description, String unlockedBy, boolean purchaseOnce){
            this.name = name;
            this.price = price;
            this.description = description;
            this.unlockedBy = unlockedBy;
            this.purchaseOnce = purchaseOnce;
        }

        //------------------------
        // getters
        //------------------------

        /**
         * Gets the name of the item
         *
         * @return name of item
         */
        public String getName() {
            return name;
        }

        /**
         * Gets price of the item
         *
         * @return price of item
         */
        public int getPrice() {
            return price;
        }

        public String getDescription() {
            return description;
        }

        public String getUnlockedBy() {
            return unlockedBy;
        }

        public boolean isPurchaseOnce() {
            return purchaseOnce;
        }
    }

    public enum PurchaseResult{
        SUCCESS, YOU_BROKE, ALREADY_PURCHASED
    }

    //the full list of everything hanamaru can sell
    private static final ShopItem[] CATALOG = {
            new ShopItem("Tears of a Fallen Angel", 30, "Heals Yohane .5 HP", null, true),
            new ShopItem("Noppo Bread", 100, "Heals Yohane .5 HP", null, false),
            new ShopItem("Shovel Upgrade", 300, "Dig spike walls with no damage", "Kanan", true),
            new ShopItem("Bat Tamer", 400, "Bat damage becomes a flat .5 HP", "Riko", true),
            new ShopItem("Air Shoes", 500, "Walk over water, immune to heat damage", "You", true),
            new ShopItem("Stewshine", 1000, "+1 Max HP permanently", "Mari", true),
            new ShopItem("Mikan Mochi", 1000, "+1 Max HP permanently", "Chika", true),
            new ShopItem("Kurosawa Macha", 1000, "+1 Max HP permanently", "Dia", true),
            new ShopItem("Choco-Mint Ice Cream", 2000, "Saves from a fatal hit, heals to full", "Ruby", true)
    };

    public static ArrayList<ShopItem> getAvailableItems(OverallStats overallStats){
        ArrayList<ShopItem> avail = new ArrayList<>();
        for(ShopItem item : CATALOG){
            if(item.getUnlockedBy() == null || isIdolRescued(overallStats, item.getUnlockedBy()))
                avail.add(item);
        }
        return avail;
    }

    private static boolean isIdolRescued(OverallStats overallStats, String idolName){
        for(Idol idol : overallStats.getAqours()){
            if(idol.getName().equals(idolName) && idol.isRescuedOnce())
                return true;
        }
        return false;
    }

    /**
     * Checks whether a one-time-purchase item has already been bought
     * this playthrough.
     */
    public static boolean isAlreadyPurchased(ShopItem item, Player player){
        switch(item.getName()){
            case "Tears of a Fallen Angel": return player.getUpgrades().hasTearsOnce();
            case "Choco-Mint Ice Cream":    return player.getUpgrades().hasChocoMintOnce();
            case "Shovel Upgrade":          return player.getUpgrades().hasShovelUpgrade();
            case "Bat Tamer":               return player.getUpgrades().hasBatTamer();
            case "Air Shoes":                return player.getUpgrades().hasAirShoes();
            case "Stewshine":               return player.getUpgrades().hasStewshine();
            case "Mikan Mochi":             return player.getUpgrades().hasMikanMochi();
            case "Kurosawa Macha":          return player.getUpgrades().hasKurosawaMacha();
            default: return false; // Noppo Bread: no purchase limit
        }
    }

    /**
     * Buy the given item for the player. Checks purchase limit and gold
     * before buying.
     *
     * @param item item being bought
     * @param player player buying it
     * @param overallStats used to track gold spent
     *
     * @return SUCCESS, YOU_BROKE, or ALREADY_PURCHASED
     */
    public static PurchaseResult purchase(ShopItem item, Player player, OverallStats overallStats){
        if(item.isPurchaseOnce() && isAlreadyPurchased(item, player)){
            return PurchaseResult.ALREADY_PURCHASED;
        }
        if(player.getGold() < item.getPrice()){
            return PurchaseResult.YOU_BROKE;
        }

        player.addGold(-item.getPrice()); // spend the gold
        overallStats.addGoldSpent(item.getPrice());
        applyEffect(item, player);

        return PurchaseResult.SUCCESS;
    }

    /**
     * Gives the player whatever this item does — either adds it
     * to their inventory (Tears, Noppo Bread, Choco-mint) or change the
     * matching Upgrades flag (everything else).
     */
    private static void applyEffect(ShopItem item, Player player){
        switch(item.getName()){
            case "Tears of a Fallen Angel":
                player.addItem(new Item("Tears of a Fallen Angel"));
                player.getUpgrades().setHasTearsOnce(true);
                break;
            case "Noppo Bread":
                player.addItem(new Item("Noppo Bread"));
                break;
            case "Choco-Mint Ice Cream":
                player.addItem(new Item("Choco-mint Ice Cream"));
                player.getUpgrades().setHasChocoMintOnce(true);
                break;
            case "Shovel Upgrade":
                player.getUpgrades().setHasShovelUpgrade(true);
                break;
            case "Bat Tamer":
                player.getUpgrades().setHasBatTamer(true);
                break;
            case "Air Shoes":
                player.getUpgrades().setHasAirShoes(true);
                break;
            case "Stewshine":
                player.getUpgrades().setHasStewshine(true);
                player.increaseMaxHP(1f);
                break;
            case "Mikan Mochi":
                player.getUpgrades().setHasMikanMochi(true);
                player.increaseMaxHP(1f);
                break;
            case "Kurosawa Macha":
                player.getUpgrades().setHasKurosawaMacha(true);
                player.increaseMaxHP(1f);
                break;
        }
    }
}
