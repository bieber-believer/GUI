package Dungeon;

import LivingThings.Bat;
import LivingThings.Lailaps;
import LivingThings.Player;
import LivingThings.Siren;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BossFight extends Floor{
    private Lailaps lailaps;
    private Siren siren;

    private int sirenStartRow, sirenStartCol; // where the exit spawns once she's defeated

    private int switchRow1, switchCol1; // 1st switch
    private int switchRow2, switchCol2; // 2nd switch
    private int switchTriggerCount; // how many times switch triggered

    private int moveCount;
    private boolean sirenReleased;

    private boolean battleWon;
    private boolean battleLost;
    private String lossCause;

    public BossFight(String fileName){
        super(); // no dungeonNumber/floorNumber needed
        this.fileName = fileName;
        this.dungeonName = "Final Battle: Siren of the Mirror World!"; // shown in the top bar, same as any Floor
    }

    //------------------------
    // Loading the fight
    //------------------------
    public void loadBossMap(Player player, Lailaps lailaps) throws IOException, IOException {
        this.player = player;
        this.lailaps = lailaps;

        int rows = 12, cols = 55;
        map = new Tile[rows][cols];

        InputStream stream = getClass().getResourceAsStream(this.fileName);
        if(stream == null){
            System.out.println("File not found: " + this.fileName);
            return;
        }

        ArrayList<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while((line = reader.readLine()) != null) lines.add(line);
        reader.close();

        for(int i = 0; i < rows; i++){
            String rowLine = lines.get(i);
            for(int j = 0; j < cols; j++){
                char symbol = rowLine.charAt(j);

                if(symbol == 'Y'){
                    player.setRow(i);
                    player.setCol(j);
                    symbol = '.';
                } else if(symbol == 'L'){
                    lailaps.setRow(i);
                    lailaps.setCol(j);
                    symbol = '.';
                } else if(symbol == 'S'){
                    siren = new Siren(i, j);
                    siren.loadImage();
                    sirenStartRow = i;
                    sirenStartCol = j;
                    symbol = '.';
                }

                Tile tile = new Tile(symbol, i, j);
                tile.tileProperties();
                map[i][j] = tile;
            }
        }

        spawnSwitchPair();
    }

    //------------------------
    // movement
    //-----------------------
    public void moveCharacters(int rowChange, int colChange){
        if(battleWon || battleLost) return;

        moveCount++;

        int yohaneNewRow = player.getRow() + rowChange;
        int yohaneNewCol = player.getCol() + colChange;
        int lailapsNewRow = lailaps.getRow() + rowChange;
        int lailapsNewCol = lailaps.getCol() + colChange;

        if(sirenReleased && yohaneNewRow == siren.getRow() && yohaneNewCol == siren.getCol()){
            defeatSiren();
            return;
        }

        if(canMoveTo(yohaneNewRow, yohaneNewCol, true)){
            // inherited from Floor — same "walking into a bat kills it" behavior
            if(!attackBatIfPresent(yohaneNewRow, yohaneNewCol)){
                player.setRow(yohaneNewRow);
                player.setCol(yohaneNewCol);
                collectGoldIfPresent(player, yohaneNewRow, yohaneNewCol); // inherited
            }
        }
        if(canMoveTo(lailapsNewRow, lailapsNewCol, false)){
            lailaps.setRow(lailapsNewRow);
            lailaps.setCol(lailapsNewCol);
        }

        if(!sirenReleased){
            checkSwitchTrigger();
            spawnBat();
            moveBats(); // OVERRIDDEN below to also threaten Lailaps
        } else {
            moveSiren();
            checkSirenAdjacency();
        }

        checkForGameOver();
    }

    private boolean canMoveTo(int row, int col, boolean isYohane){
        if(!isInsideMap(row, col)) return false; // inherited
        Tile tile = map[row][col];
        if(!tile.isPassable() && getBatAt(row, col) == null) return false; // bats "block" like walls until attacked
        if(sirenReleased && !isYohane && row == siren.getRow() && col == siren.getCol()) return false;
        return true;
    }

    //------------------------
    // movement
    //-----------------------
    private void checkSwitchTrigger(){
        boolean yohaneOn1 = player.getRow() == switchRow1 && player.getCol() == switchCol1;
        boolean yohaneOn2 = player.getRow() == switchRow2 && player.getCol() == switchCol2;
        boolean lailapsOn1 = lailaps.getRow() == switchRow1 && lailaps.getCol() == switchCol1;
        boolean lailapsOn2 = lailaps.getRow() == switchRow2 && lailaps.getCol() == switchCol2;

        boolean triggered = (yohaneOn1 && lailapsOn2) || (yohaneOn2 && lailapsOn1);
        if(!triggered) return;

        if(triggered){
            setTileSymbol(switchRow1, switchCol1, '.'); // inherited
            setTileSymbol(switchRow2, switchCol2, '.');
            switchTriggerCount++;
        }

        if(switchTriggerCount >= 3){
            breakBarrier();
            sirenReleased = true;
        } else {
            spawnSwitchPair();
        }
    }

    private void spawnSwitchPair(){
        int row1, col1;
        do {
            row1 = random.nextInt(map.length);       // inherited
            col1 = random.nextInt(map[0].length);
        } while(!isFreeFloorTile(row1, col1));

        int row2, col2, attempts = 0;
        do {
            row2 = row1 + (random.nextInt(5) - 2);
            col2 = col1 + (random.nextInt(11) - 5);
            attempts++;
        } while(attempts < 200 &&
                (!isInsideMap(row2, col2) || !isFreeFloorTile(row2, col2) || (row2 == row1 && col2 == col1)));

        switchRow1 = row1; switchCol1 = col1;
        switchRow2 = row2; switchCol2 = col2;
        setTileSymbol(row1, col1, '0');
        setTileSymbol(row2, col2, '0');
    }

    private boolean isFreeFloorTile(int row, int col){
        if(!isInsideMap(row, col)) return false;
        if(map[row][col].getSymbol() != '.') return false;
        if(row == player.getRow() && col == player.getCol()) return false;
        if(row == lailaps.getRow() && col == lailaps.getCol()) return false;
        return true;
    }

    private void breakBarrier(){
        setTileSymbol(0, 16, '.');
        setTileSymbol(1, 16, '.');
        setTileSymbol(0, 33, '.');
        setTileSymbol(1, 33, '.');
        for(int col = 16; col <= 33; col++) setTileSymbol(2, col, '.');
    }

    //------------------------
    // bats
    //------------------------
    private void spawnBat(){
        if(moveCount % 8 != 0) return;

        int row, col, attempts = 0;
        do {
            row = random.nextInt(map.length);
            col = random.nextInt(map[0].length);
            attempts++;
        } while(attempts < 200 && !isFreeFloorTile(row, col));

        Bat bat = createBatForTier(switchTriggerCount);
        bat.setRow(row);
        bat.setCol(col);
        bats.add(bat);
    }

    private Bat createBatForTier(int tier){
        Bat bat;
        switch(tier){
            case 0: bat = new Bat(0, 0, 5, false, 0.5f); break;
            case 1: bat = new Bat(0, 0, 10, false, 1.0f); break;
            default: bat = new Bat(0, 0, 15, true, 1.5f);
        }
        bat.loadImage();
        return bat;
    }

    /**
     * OVERRIDES Floor's moveBats() — same wandering logic, but bats here
     * threaten Lailaps too, not just Yohane.
     */
    @Override
    public void moveBats(){
        for(Bat bat : bats){
            if(isAdjacent(bat.getRow(), bat.getCol(), player.getRow(), player.getCol(), bat.isMovesDiagonal())){
                player.takeDamage(bat.getAtkDamage(), "Bat");
                continue;
            }
            if(isAdjacent(bat.getRow(), bat.getCol(), lailaps.getRow(), lailaps.getCol(), bat.isMovesDiagonal())){
                lailaps.takeDamage(bat.getAtkDamage());
                continue;
            }

            int[] rowOffsets = bat.isMovesDiagonal() ? ALL_ROW_OFFSETS : STRAIGHT_ROW_OFFSETS;
            int[] colOffsets = bat.isMovesDiagonal() ? ALL_COL_OFFSETS : STRAIGHT_COL_OFFSETS;
            int dir = random.nextInt(rowOffsets.length);
            int newRow = bat.getRow() + rowOffsets[dir];
            int newCol = bat.getCol() + colOffsets[dir];

            if(isInsideMap(newRow, newCol) && map[newRow][newCol].isPassable()){
                bat.setRow(newRow);
                bat.setCol(newCol);
            }
        }
    }

    //------------------------
    // The Siren, once released
    //------------------------
    private void moveSiren(){
        int rowStep = Integer.compare(player.getRow(), siren.getRow());
        int colStep = Integer.compare(player.getCol(), siren.getCol());

        if(tryMoveSiren(rowStep, colStep)) return;
        if(tryMoveSiren(rowStep, 0)) return;
        tryMoveSiren(0, colStep);
    }

    private boolean tryMoveSiren(int rowStep, int colStep){
        if(rowStep == 0 && colStep == 0) return false;
        int newRow = siren.getRow() + rowStep;
        int newCol = siren.getCol() + colStep;
        if(isInsideMap(newRow, newCol) && map[newRow][newCol].isPassable()){
            siren.setRow(newRow);
            siren.setCol(newCol);
            return true;
        }
        return false;
    }

    private void checkSirenAdjacency(){
        if(isAdjacent(siren.getRow(), siren.getCol(), player.getRow(), player.getCol(), true)){
            player.takeDamage(player.getHp(), "Siren");
        }
        if(isAdjacent(siren.getRow(), siren.getCol(), lailaps.getRow(), lailaps.getCol(), true)){
            lailaps.takeDamage(lailaps.getHp());
        }
    }

    private void defeatSiren(){
        battleWon = true;

        for(Bat bat : bats){
            setTileSymbol(bat.getRow(), bat.getCol(), 'g');
            map[bat.getRow()][bat.getCol()].setGoldValue(bat.getGoldDrop());
        }
        bats.clear();

        setTileSymbol(siren.getRow(), siren.getCol(), 'g');
        map[siren.getRow()][siren.getCol()].setGoldValue(siren.getGoldDrop()); // 750, from Siren's own field now

        setTileSymbol(sirenStartRow, sirenStartCol, 'E');
    }

    private void checkForGameOver(){
        if(!player.isAlive()){
            battleLost = true;
            lossCause = "Siren";
        } else if(!lailaps.isAlive()){
            battleLost = true;
            lossCause = "Siren (Lailaps was hit)";
        }
    }

    //------------------------
    // getters
    //------------------------
    public Lailaps getLailaps() { return lailaps; }
    public Siren getSiren() { return siren; }
    public boolean isSirenReleased() { return sirenReleased; }
    public boolean isBattleWon() { return battleWon; }
    public boolean isBattleLost() { return battleLost; }
    public String getLossCause() { return lossCause; }
}
