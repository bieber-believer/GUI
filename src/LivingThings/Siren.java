package LivingThings;

public class Siren extends Entity{
    private boolean isReleased;

    public Siren(String name, int row, int col, int goldDrop, boolean movesDiagonal, float atkDamage){
        super(name, row, col, goldDrop, movesDiagonal, atkDamage);
    }

    public boolean isReleased() {
        return isReleased;
    }

    public void spawnBat(){

    }

    public void move(){

    }
}
