package LivingThings;

public class Lailaps {
    private int row;
    private int col;
    private float hp;
    private float maxHp;

    /**
     * Creates Lailaps at the given position
     *
     *  @param row starting row
     *  @param col starting column
     */
    public Lailaps(int row, int col) {
        this.row = row;
        this.col = col;

        this.hp = 4.0f;
        this.maxHp = 4.0f;
    }

    /**
     * Returns Lailaps' current row position
     *
     *  @return current row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns Lailaps' current column position
     *
     *  @return current column
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Returns Lailaps' current HP
     *
     * @return current HP
     */
    public float getHp() {
        return this.hp;
    }

    /**
     * Returns Lailaps' max HP
     *
     * @return max HP
     */
    public float getMaxHp() {
        return this.maxHp;
    }

    /**
     * Sets Lailaps' position
     *
     * @param row new row position
     * @param col new column position
     */
    public void setPosition(int row, int col){
        this.row = row;
        this.col = col;
    }

    /**
     * Reduces Lailaps' HP depending on damage
     *
     * @param damage amount of damage taken
     */
    public void takeDamage(float damage){
        this.hp -= damage;
        if(this.hp < 0)
            this.hp = 0; // to make sure hp is not gonna be a - value
    }

    /**
     * Checks if Lailaps is still alive
     *
     * @return true if HP is above 0, false if not
     */
    public boolean isAlive(){
        return this.hp > 0;
    }
}
