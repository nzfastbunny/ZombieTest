package com.zombie.data;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Collectors;

public class WorldTest {
    @Test
    public void addScore() {
        World world = new World(2);

        world.addScore(3);
        Assert.assertEquals(3, world.getScore());

        world.addScore(1);
        Assert.assertEquals(4, world.getScore());

        world.addScore(5);
        Assert.assertEquals(9, world.getScore());
    }

    @Test
    public void isActiveZombies() {
        World world = new World(5);

        Assert.assertFalse(world.isActiveZombies());

        world.getActiveZombies().add(new Creature());
        Assert.assertTrue(world.isActiveZombies());
    }

    @Test
    public void getZombiePositions() {
        World world = new World(2);

        world.getFinishedZombies().add(createCreature(0, 0));
        world.getFinishedZombies().add(createCreature(0, 0));
        world.getFinishedZombies().add(createCreature(1, 0));
        world.getFinishedZombies().add(createCreature(1, 0));
        world.getFinishedZombies().add(createCreature(0, 1));
        world.getFinishedZombies().add(createCreature(0, 1));
        world.getFinishedZombies().add(createCreature(1, 1));

        Assert.assertEquals("(0, 0) (0, 0) (1, 0) (1, 0) (0, 1) (0, 1) (1, 1)", world.getZombiePositions());
    }

    private Creature createCreature(int x, int y) {
        Creature creature = new Creature();
        creature.setxPosition(x);
        creature.setyPosition(y);
        return creature;
    }
}
