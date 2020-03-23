package com.zombie.data;

import org.junit.Assert;
import org.junit.Test;

public class CreatureTest {
    @Test
    public void moveUp() {
        Creature creature = createCreature(3, 3);
        creature.moveUp(4);

        Assert.assertEquals(3, creature.getxPosition());
        Assert.assertEquals(2, creature.getyPosition());
    }

    @Test
    public void moveUp_topEdge() {
        Creature creature = createCreature(2, 0);
        creature.moveUp(4);

        Assert.assertEquals(2, creature.getxPosition());
        Assert.assertEquals(4, creature.getyPosition());
    }

    @Test
    public void moveDown() {
        Creature creature = createCreature(4, 3);
        creature.moveDown(4);

        Assert.assertEquals(4, creature.getxPosition());
        Assert.assertEquals(4, creature.getyPosition());
    }

    @Test
    public void moveDown_bottomEdge() {
        Creature creature = createCreature(1, 4);
        creature.moveDown(4);

        Assert.assertEquals(1, creature.getxPosition());
        Assert.assertEquals(0, creature.getyPosition());
    }

    @Test
    public void moveLeft() {
        Creature creature = createCreature(1, 3);
        creature.moveLeft(4);

        Assert.assertEquals(0, creature.getxPosition());
        Assert.assertEquals(3, creature.getyPosition());
    }

    @Test
    public void moveLeft_leftEdge() {
        Creature creature = createCreature(0, 2);
        creature.moveLeft(4);

        Assert.assertEquals(4, creature.getxPosition());
        Assert.assertEquals(2, creature.getyPosition());
    }

    @Test
    public void moveRight() {
        Creature creature = createCreature(2, 0);
        creature.moveRight(3);

        Assert.assertEquals(3, creature.getxPosition());
        Assert.assertEquals(0, creature.getyPosition());
    }

    @Test
    public void moveRight_rightEdge() {
        Creature creature = createCreature(3, 1);
        creature.moveRight(3);

        Assert.assertEquals(0, creature.getxPosition());
        Assert.assertEquals(1, creature.getyPosition());
    }

    @Test
    public void getPosition() {
        Creature creature = createCreature(3, 0);
        Assert.assertEquals("(3, 0)", creature.getPosition());

        creature.moveRight(3);
        Assert.assertEquals("(0, 0)", creature.getPosition());

        creature.moveLeft(3);
        Assert.assertEquals("(3, 0)", creature.getPosition());

        creature.moveUp(3);
        Assert.assertEquals("(3, 3)", creature.getPosition());

        creature.moveDown(3);
        Assert.assertEquals("(3, 0)", creature.getPosition());
    }

    private Creature createCreature(int x, int y) {
        Creature creature = new Creature();
        creature.setxPosition(x);
        creature.setyPosition(y);
        return creature;
    }
}
