package LivingThings;

public class Entity {
    // for all entities
    private int row, col;
    private float hp, maxHP;
    private boolean isAdjacent; // to check if either bat or the player is adjacent to each other

    // for evil enemies
    private String name;
    private int goldDrop; // num of gold they drop
    private boolean movesDiagonal; // whether the enemy can move diagonally
    private float atkDamage;


    //constructor for player;
    public Entity(float hp, float maxHP){
        this.row = 0;
        this.col = 0;
        this.hp = hp;
        this.maxHP = maxHP;
    }

    //constructor for enemies
    public Entity(String name, int row, int col, int goldDrop, boolean movesDiagonal, float atkDamage){
        this.name = name;
        this.row = row;
        this.col = col;
        this.goldDrop = goldDrop;
        this.movesDiagonal = movesDiagonal;
        this.atkDamage = atkDamage;
    }

    /**
     * Entity takes damage and reduces their HP by the given amount
     *
     * @param atkDamage amount of damage entity receives
     */
    public void takeDamage(float atkDamage) {
        this.hp -= atkDamage;
        if(this.hp < 0) this.hp = 0; // to make sure hp is not gon be a negative value
    }

    /**
     * Heals entity by the given amount
     *
     * @param restored amount of hp restored
     */
    public void heal(float restored){
        this.hp += restored;
        if(this.hp > this.maxHP) this.hp = this.maxHP; // to make sure no exceed max hp
    }

    //------------------------
    // getters
    //------------------------

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public float getHp() {
        return hp;
    }

    public float getMaxHP() {
        return maxHP;
    }

    public String getName() {
        return name;
    }

    public float getAtkDamage() {
        return atkDamage;
    }

    public int getGoldDrop() {
        return goldDrop;
    }

    public boolean isAdjacent() {
        return isAdjacent;
    }

    public boolean isMovesDiagonal() {
        return movesDiagonal;
    }

    //------------------------
    // setters
    //------------------------

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public void setMaxHP(float maxHP) {
        this.maxHP = maxHP;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAtkDamage(float atkDamage) {
        this.atkDamage = atkDamage;
    }

    public void setGoldDrop(int goldDrop) {
        this.goldDrop = goldDrop;
    }

    public void setAdjacent(boolean adjacent) {
        isAdjacent = adjacent;
    }

    public void setMovesDiagonal(boolean movesDiagonal) {
        this.movesDiagonal = movesDiagonal;
    }
}
