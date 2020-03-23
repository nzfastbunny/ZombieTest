package com.zombie.service;

import com.zombie.data.Creature;
import com.zombie.data.World;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

public class ZombieServiceTest {
    private final ZombieService service = ZombieServiceImpl.getService();
    private final ByteArrayOutputStream testError = new ByteArrayOutputStream();
    private final PrintStream systemError = System.err;

    @Before
    public void redirectError() {
        System.setErr(new PrintStream(testError));
    }

    @After
    public void restoreError() {
        System.setErr(systemError);
    }

    @Test
    public void createWorldTest_original() {
        World world = service.createWorld(new String[0]);

        Assert.assertNotNull(world);
        Assert.assertEquals(4, world.getSize());
        Assert.assertEquals(3, world.getCreatures().size());
        Assert.assertEquals(1, world.getActiveZombies().size());
        Assert.assertEquals(0, world.getFinishedZombies().size());
        Assert.assertEquals("DLUURR", world.getZombieMovements());
    }

    @Test
    public void createWorldTest_alternate() {
        String filePath = getTestInputFilePath("Input2.txt");
        World world = service.createWorld(new String[]{filePath});

        Assert.assertNotNull(world);
        Assert.assertEquals(3, world.getSize());
        Assert.assertEquals(5, world.getCreatures().size());
        Assert.assertEquals(1, world.getActiveZombies().size());
        Assert.assertEquals(0, world.getFinishedZombies().size());
        Assert.assertEquals("DDLUURLUR", world.getZombieMovements());
    }

    @Test
    public void createWorldTest_InvalidPath() {
        World world = service.createWorld(new String[]{"C:/InvalidPath.txt"});

        Assert.assertNull(world);
        Assert.assertThat(testError.toString(), CoreMatchers.containsString("FileNotFoundException"));
    }

    @Test
    public void createWorldTest_emptyFile() {
        String filePath = getTestInputFilePath("EmptyInput.txt");
        World world = service.createWorld(new String[]{filePath});

        Assert.assertNull(world);
    }

    @Test
    public void createWorldTest_MissingSize() {
        String filePath = getTestInputFilePath("MissingSize.txt");
        World world = service.createWorld(new String[]{filePath});

        Assert.assertNull(world);
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("1st line should be a positive number"));
    }

    @Test
    public void createWorldTest_InvalidNumber() {
        String filePath = getTestInputFilePath("InvalidNumber.txt");
        World world = service.createWorld(new String[]{filePath});

        Assert.assertNull(world);
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("1st line should be a positive number"));
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("Number provided is not a valid number: Three"));
    }

    @Test
    public void createWorldTest_TooFewLines() {
        String filePath = getTestInputFilePath("TooFewLines.txt");
        World world = service.createWorld(new String[]{filePath});

        Assert.assertNull(world);
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("Incorrect number of lines in the input file. Required 4 but found 3"));
    }

    @Test
    public void createWorldTest_TooManyLines() {
        String filePath = getTestInputFilePath("TooManyLines.txt");
        World world = service.createWorld(new String[]{filePath});

        Assert.assertNull(world);
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("Incorrect number of lines in the input file. Required 4 but found 5"));
    }

    @Test
    public void createWorldTest_MultipleZombies() {
        String filePath = getTestInputFilePath("MultipleZombies.txt");
        World world = service.createWorld(new String[]{filePath});

        Assert.assertNull(world);
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("More than one zombie has been provided"));
    }

    @Test
    public void createWorldTest_InvalidPositionCreature() {
        String filePath = getTestInputFilePath("InvalidPositionCreature.txt");
        World world = service.createWorld(new String[]{filePath});

        Assert.assertNull(world);
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("Number provided is not a valid number: Zero"));
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("One of the position co-ordinates is not a valid number: Zero,1"));
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("The 3rd line should be valid creature co-ordinates"));
    }

    @Test
    public void createWorldTest_InvalidPositionZombie() {
        String filePath = getTestInputFilePath("InvalidPositionZombie.txt");
        World world = service.createWorld(new String[]{filePath});

        Assert.assertNull(world);
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("Number provided is not a valid number: Two"));
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("One of the position co-ordinates is not a valid number: Two,One"));
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("The 2nd line should be valid zombie co-ordinates"));
    }

    @Test
    public void createWorldTest_NegativeNumber() {
        String filePath = getTestInputFilePath("NegativeNumber.txt");
        World world = service.createWorld(new String[]{filePath});

        Assert.assertNull(world);
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("Number provided is not a positive number: -3"));
    }

    @Test
    public void createWorldTest_PositionOutsideGrid() {
        String filePath = getTestInputFilePath("PositionOutsideGrid.txt");
        World world = service.createWorld(new String[]{filePath});

        Assert.assertNull(world);
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("One of the position co-ordinates is outside the grid: 3,1"));
    }

    @Test
    public void monitorZombie() {
        World world = new World(3);
        world.getActiveZombies().add(createCreature(0,0));
        world.setZombieMovements("UUUDLLRRR");
        service.monitorZombie(world);

        Assert.assertEquals(0, world.getActiveZombies().size());
        Assert.assertEquals(1, world.getFinishedZombies().size());
        Assert.assertEquals(1, world.getFinishedZombies().get(0).getxPosition());
        Assert.assertEquals(1, world.getFinishedZombies().get(0).getyPosition());
    }

    @Test
    public void monitorZombie_invalidMovement() {
        World world = new World(3);
        world.getActiveZombies().add(createCreature(0,0));
        world.setZombieMovements("GKWE"); // Invalid movements
        service.monitorZombie(world);

        Assert.assertEquals(0, world.getActiveZombies().size());
        Assert.assertEquals(1, world.getFinishedZombies().size());
        Assert.assertEquals(0, world.getFinishedZombies().get(0).getxPosition()); // no zombie movement
        Assert.assertEquals(0, world.getFinishedZombies().get(0).getyPosition());
        Assert.assertThat(testError.toString(),
                CoreMatchers.containsString("Movement was skipped - Incorrect zombie movement specified:"));
    }

    private String getTestInputFilePath(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file.getAbsolutePath();
    }

    private Creature createCreature(int x, int y) {
        Creature creature = new Creature();
        creature.setxPosition(x);
        creature.setyPosition(y);
        return creature;
    }
}
