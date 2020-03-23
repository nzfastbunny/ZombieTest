package com.zombie.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The world in which the zombies and creatures live
 */
public class World {
    /**
     * The length and height of the world (n by n) where n is size
     */
    private int size;
    /**
     * The predetermined movements of the zombies
     */
    private String zombieMovements;
    /**
     * The number of creatures that the initial zombie has infected
     */
    private int score;
    /**
     * A map of lists containing uninfected creatures keyed by their co-ordinates in the world (grid)
     */
    private Map<String, List<Creature>> creatures = new HashMap();
    /**
     * A list (stack) of zombies that are active and are yet to move (and infect creatures)
     */
    private List<Creature> activeZombies = new ArrayList<>();
    /**
     * A list of all the zombies that have finished their movements
     */
    private List<Creature> finishedZombies = new ArrayList<>();

    public World(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public String getZombieMovements() {
        return zombieMovements;
    }

    public void setZombieMovements(String zombieMovements) {
        this.zombieMovements = zombieMovements;
    }

    public int getScore() {
        return score;
    }

    public Map<String, List<Creature>> getCreatures() {
        return creatures;
    }

    public List<Creature> getActiveZombies() {
        return activeZombies;
    }

    public List<Creature> getFinishedZombies() {
        return finishedZombies;
    }

    public void addScore(int noOfVictims) {
        this.score += noOfVictims;
    }

    public boolean isActiveZombies() {
        return activeZombies.size() > 0;
    }

    public String getZombiePositions() {
        return finishedZombies.stream().map(Creature::getPosition).collect(Collectors.joining(" "));
    }
}
