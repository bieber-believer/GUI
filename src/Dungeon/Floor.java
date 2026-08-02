package Dungeon;

import Items.Item;
import LivingThings.Bat;
import LivingThings.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Floor {
    protected Tile[][] map; // floor layput
    protected String fileName;
    protected ArrayList<Bat> bats;

    protected int floorNumber; // the floor number player is currently on
    protected boolean escaped; // whether the player has escaped the floor

    protected String dungeonName; // name of the dungeon
    protected int dungeonNumber; // the number of dungeons the player had done/is doing (1,2,3)

    protected Player player; // the player
    private Random random;

    //------------------------
    // for bat movement
    //------------------------
    // moves in 4 directions (up,down, left, right)
    private static final int[] STRAIGHT_ROW_OFFSETS = {-1, 1, 0, 0};
    private static final int[] STRAIGHT_COL_OFFSETS = {0, 0, -1, 1};

    //moves in 8 directions, the 4 directions + diagonal
    private static final int[] ALL_ROW_OFFSETS = {-1, 1, 0, 0, -1, -1, 1, 1};
    private static final int[] ALL_COL_OFFSETS = {0, 0, -1, 1, -1, 1, -1, 1};

    public Floor(int dungeonNumber, String dungeonName, int floorNumber, String fileName){
        this.dungeonNumber = dungeonNumber;
        this.dungeonName = dungeonName;
        this.floorNumber = floorNumber;
        this.fileName = fileName;
        this.escaped = false;
        bats = new ArrayList<Bat>();
        random = new Random();
    }

    public void loadMap(Player player) throws IOException {
        this.player = player;

        // get grid size
        int rows = 12;
        int cols = 55;
        map = new Tile[rows][cols];

        InputStream stream = getClass().getResourceAsStream(this.fileName);

        if (stream == null) {
            System.out.println("File not found: " + this.fileName);
            return;
        }

        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String fileLine;
        while ((fileLine = reader.readLine()) != null) {
            lines.add(fileLine);
        }
        reader.close();


        for(int i = 0; i < rows; i++){
            String rowLine = lines.get(i); // entire line in a row

            for(int j = 0; j < cols; j++){
                char symbol = rowLine.charAt(j);

                //get player position and set it
                if(symbol == 'Y'){
                    player.setRow(i);
                    player.setCol(j);
                    symbol = '.'; // the tile player is on is just grass
                } else if (symbol == 'b') {
                    Bat bat = createBatForDungeon(i, j);
                    bats.add(bat);
                    symbol = '.'; // the tile under bat is also just grass
                }

                // build the tile object and store
                Tile tile = new Tile(symbol, i, j);
                tile.tileProperties();

                map[i][j] = tile;
            }
        }
    }

    private Bat createBatForDungeon(int row, int col){
        Bat bat;
        switch (dungeonNumber){
            case 1:
                bat =  new Bat(row, col, 5, false, 0.5f);
                break;
            case 2:
                bat = new Bat(row, col, 10, false, 1.0f);
                break;
            case 3:
            default:
                bat = new Bat(row, col, 15, true, 1.5f);
        }
        bat.loadImage();
        return bat;
    }

    public void movePlayer(int rowChange, int colChange){
        // where player would end up if they moved
        int newRow = player.getRow() + rowChange;
        int newCol = player.getCol() + colChange;

        if(!isInsideMap(newRow, newCol))
            return; // do nothing

        // get the tile in the target pos
        Tile target = map[newRow][newCol];

        // attacks the beat instead of walk so bat drops gold
        Bat bat = getBatAt(newRow, newCol);
        if(bat != null){
            bats.remove(bat);
            target.setSymbol('g');
            try {
                target.tileProperties(); // loads gold sprite + correct passable/diggable/damage for 'g'
            } catch (IOException e) {
                e.printStackTrace();
            }
            target.setGoldValue(bat.getGoldDrop()); // gold amount is separate, set it after
            return;
        }

        // treasure chest check
        if(target.getSymbol() == 'T'){
            boolean givesItem = random.nextBoolean(); // 50/50 coin flip: true or false

            if(givesItem){
                target.setSymbol('.');
                try {
                    target.tileProperties(); // grass sprite
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.addItem(new Item("Noppo Bread"));
            } else {
                int goldAmount = random.nextInt(91) + 10; // 91 possible values bc 10 to 100
                target.setSymbol('g');
                try {
                    target.tileProperties(); // gold sprite
                } catch (IOException e) {
                    e.printStackTrace();
                }
                target.setGoldValue(goldAmount);
            }
            return;
        }

        // player digs
        if(target.isDiggable){
            float digDamage = target.getDamage();
            target.setSymbol('.');
            try {
                target.tileProperties();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(digDamage > 0)
                player.takeDamage(digDamage, "Spike Wall");;

            return;
        }

        // check if not passable and not diggable
        if(!target.isPassable){
            return;
        }

        // if passes the checks, player can move
        player.setRow(newRow);
        player.setCol(newCol);

        // pick up gold and turn gold tile to grass
        if(target.getGoldValue() > 0){
            player.addGold(target.getGoldValue());
            target.setSymbol('.');
            try {
                target.tileProperties(); // switches sprite back to grass
            } catch (IOException e) {
                e.printStackTrace();
            }
            target.setGoldValue(0);
        }
    }

    public void checkHeatDamage(){
        Tile currentTile = map[player.getRow()][player.getCol()];
        if(currentTile.getDamage() > 0)
            player.takeDamage(currentTile.getDamage(), "Heat Tile");
    }

    public void moveBats(){
        for(Bat bat : bats){
            if(isAdjacentToPlayer(bat)){
                player.takeDamage(bat.getAtkDamage(), "Bat");
                continue;
            }

            // hi amber, this is a ternary operator
            // condition ? valueIfTure : valueIfFalse

            int[] rowOffsets = bat.isMovesDiagonal() ? ALL_ROW_OFFSETS : STRAIGHT_ROW_OFFSETS;
            int[] colOffsets = bat.isMovesDiagonal() ? ALL_COL_OFFSETS : STRAIGHT_COL_OFFSETS;

            // get random whole number from 0 to n-1 for offset movement
            int direction = random.nextInt(rowOffsets.length);
            int newRow = bat.getRow() + rowOffsets[direction];
            int newCol = bat.getCol() + colOffsets [direction];

            // only move if passable and water
            if(isInsideMap(newRow, newCol) && (map[newRow][newCol].isPassable() || map[newRow][newCol].getSymbol() == 'w')){
              bat.setRow(newRow);
              bat.setCol(newCol);
            }
        }
    }

    //------------------------
    // helper methods
    //------------------------
    private boolean isInsideMap(int row, int col){
        return row >= 0 && row < map.length && col >= 0 && col < map[0].length;
    }

    private Bat getBatAt(int row, int col){
        for(Bat bat : bats){
            if(bat.getRow() == row && bat.getCol() == col)
                return bat;
        }
        return null; // bat not found
    }

    private boolean isAdjacentToPlayer(Bat bat){
        int rowDiff = Math.abs(bat.getRow() - player.getRow());
        int colDiff = Math.abs(bat.getCol() - player.getCol());

        if (rowDiff == 0 && colDiff == 0) return false; // same tile, not "adjacent"

        if (bat.isMovesDiagonal()) {
            // 3rd dungeon bats: any of the 8 surrounding tiles counts
            return rowDiff <= 1 && colDiff <= 1;
        } else {
            // 1st/2nd dungeon bats: only up/down/left/right counts
            return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
        }
    }

    //------------------------
    // getters
    //------------------------

    public Tile[][] getMap() {
        return map;
    }

    public ArrayList<Bat> getBats() {
        return bats;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isEscaped() {
        return escaped;
    }

    public String getDungeonName() {
        return dungeonName;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    //------------------------
    // setters
    //------------------------

    public void setEscaped(boolean escaped) {
        this.escaped = escaped;
    }
}
