package Dungeon;

import LivingThings.Idol;
import LivingThings.Player;

import java.util.ArrayList;
import java.util.Collections;

public class Dungeon {
    private String dungeonName;
    private int dungeonOrder; // 1st, 2nd, or 3rd dungeon ENTERED this playthrough
    private ArrayList<Floor> floors;
    private int currentFloorIndex;
    private boolean cleared; // true if player reached the exit

    // Shared by ALL dungeons in the current playthrough, so the same map
    // layout never shows up twice. "static" means there's only ONE of
    // these lists, no matter how many Dungeon objects we create.
    private static ArrayList<String> mapPool;

    public Dungeon(String dungeonName, int dungeonOrder) {
        this.dungeonName = dungeonName;
        this.dungeonOrder = dungeonOrder;
        this.floors = new ArrayList<Floor>();
        this.currentFloorIndex = 0;
        buildFloors();
    }

    /**
     * Refills and shuffles the pool of 7 maps. Call this ONCE whenever a
     * new playthrough starts (New Game / New Game+), so every playthrough
     * gets a fresh random order.
     */
    public static void resetMapPool() {
        mapPool = new ArrayList<String>();
        mapPool.add("/floorMaps/map1.txt");
        mapPool.add("/floorMaps/map2.txt");
        mapPool.add("/floorMaps/map3.txt");
        mapPool.add("/floorMaps/map4.txt");
        mapPool.add("/floorMaps/map5.txt");
        mapPool.add("/floorMaps/map6.txt");
        mapPool.add("/floorMaps/map7.txt");
        Collections.shuffle(mapPool);
    }

    /**
     * Takes the next unused map filename off the pool so no two floors in
     * this playthrough reuse the same layout.
     */
    private static String drawMap() {
        if (mapPool == null || mapPool.isEmpty()) {
            resetMapPool(); // safety net so the game never crashes from running out
        }
        return mapPool.remove(mapPool.size() - 1);
    }

    /**
     * Decides how many floors this dungeon has based on when it was chosen
     * this playthrough (dungeonOrder: 1, 2, or 3), then builds each Floor
     * with a randomly drawn, unused map.
     */
    private void buildFloors() {
        int floorCount = dungeonOrder; // 1st dungeon->1 floor, 2nd->2, 3rd->3

        for (int i = 1; i <= floorCount; i++) {
            String mapFile = drawMap();
            Floor floor = new Floor(dungeonOrder, dungeonName, i, mapFile);
            floors.add(floor);
        }
    }

    /**
     * Advances to the next floor. Returns true if it succeeded, false if
     * the player was already on the last floor (meaning the dungeon is
     * actually cleared, not just this floor).
     */
    public boolean goToNextFloor() {
        if (currentFloorIndex + 1 < floors.size()) {
            currentFloorIndex++;
            return true;
        }
        return false;
    }

    public boolean isLastFloor() {
        return currentFloorIndex == floors.size() - 1;
    }

    //------------------------
    // getters
    //------------------------
    /**
     * Returns the floor the player is currently on.
     */
    public Floor getCurrentFloor() {
        return floors.get(currentFloorIndex);
    }

    public String getDungeonName() {
        return dungeonName;
    }

    public int getDungeonOrder() {
        return dungeonOrder;
    }

    public ArrayList<Floor> getFloors() { return floors; }

    public boolean isCleared() {
        return cleared;
    }

    //------------------------
    // setters
    //------------------------
    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }
}
