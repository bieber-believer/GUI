package LivingThings;

/**
 * This class is a representation of an Idol in the group Aquors. It contains the name of the idol, dungeon of the idol, the
 * number of times the idol has been rescued and whether the idol has been recused at least once.
 *
 * Idol
 */

public class Idol {
    private String name;
    private String dungeonName;
    private boolean isRescuedOnce;
    private int rescueCount;

    /**
     * Constructs an Idol object with the given name and dungeon name.
     * A newly created idol starts as not yet rescued, with a rescue count of 0.
     *
     * @param name name of idol
     * @param dungeonName name of dungeon associated with the idol
     */
    public Idol(String name, String dungeonName){
        this.name = name;
        this.dungeonName = dungeonName;
        this.isRescuedOnce = false;
        this.rescueCount = 0;
    }

    /**
     * Marks this idol as rescued. Increments the rescue count by 1, and if this
     * is the idol's first rescue, updates isRescuedOnce to true.
     */
    public void rescue(){
        if(!this.isRescuedOnce)
            this.isRescuedOnce = true;
        this.rescueCount++;
    }

    /**
     * Returns the name of the idol
     *
     * @return name of idol
     */
    public String getName(){
        return this.name;
    }

    /**
     * Returns the name of the idol's dungeon
     *
     * @return name of the dungeon
     */
    public String getDungeonName(){
        return this.dungeonName;
    }

    /**
     * Checks if an idol has been rescued at least one
     *
     * @return true if the idol has been rescued once, false otherwise
     */
    public boolean isRescuedOnce(){
        return this.isRescuedOnce;
    }

    /**
     * Returns the amount of times an idol has been rescued
     *
     * @return number of times an idol has been rescued
     */
    public int getRescueCount(){
        return this.rescueCount;
    }

    /**
     * Increments the rescue count by 1
     */
    public void addRescueCount(){
        this.rescueCount++;
    }

    public void setRescuedOnce(boolean rescued){
        this.isRescuedOnce = rescued;
    }

    public void setRescueCount(int rescueCount){
        this.rescueCount = rescueCount;
    }
}
