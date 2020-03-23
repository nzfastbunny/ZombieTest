package com.zombie.service;

import com.zombie.ZombieWorld;
import com.zombie.data.Creature;
import com.zombie.data.World;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The implementation of the zombie service
 */
public class ZombieServiceImpl implements ZombieService {
    /**
     * The name of the file in the resources folder used as the default input file
     */
    private static final String DEFAULT_FILE_NAME = "Input.txt";
    /**
     * The regex pattern to retrieve the co-ordinates of the creatures or zombies
     */
    private static final String COORDINATE_PATTERN = "\\((.*?)\\)";

    /**
     * The single instance of the zombie service
     */
    private static ZombieServiceImpl service = new ZombieServiceImpl();

    private ZombieServiceImpl() {
    }

    /**
     * Get the singleton instance of the zombie service implementation
     *
     * @return the singleton instance of the zombie service
     */
    public static ZombieServiceImpl getService() {
        return service;
    }

    /**
     * Set up the world either by the internal resources file information or the external supplied text file info
     *
     * @param args either empty (if using the default resources file) or a file path to an external input file
     * @return World object populated with creatures/zombies (or null if the set up instructions were invalid)
     */
    public World createWorld(String[] args) {
        List<String> setUp;
        World world = null;

        if (args.length > 0) {
            setUp = readFile(args[0]);
        } else {
            setUp = readDefault();
        }

        if (!setUp.isEmpty() && isValid(setUp)) {
            world = new World(Integer.parseInt(setUp.get(0)));
            addCreatures(world, setUp.get(1), true);
            addCreatures(world, setUp.get(2), false);
            world.setZombieMovements(setUp.get(3));
        }

        return world;
    }

    /**
     * Monitor the movement of the zombies around the world
     *
     * @param world the world in which the creatures and zombies co-exist
     */
    public void monitorZombie(World world) {
        Creature zombie = world.getActiveZombies().remove(0);

        String movements = world.getZombieMovements();

        for (char movement : movements.toUpperCase().toCharArray()) {
            processMovement(world, zombie, movement);
        }

        world.getFinishedZombies().add(zombie);
    }

    /**
     * Process the movement of a zombie around the world (grid)
     *
     * @param world    the world (or grid) in which the zombie lives
     * @param zombie   the active zombie (running around biting people)
     * @param movement the movement action (either U - Up, D - Down, L - Left or R - Right)
     */
    private void processMovement(World world, Creature zombie, char movement) {
        int max = world.getSize() - 1; // 0 to size - 1 is the horizontal and vertical dimensions
        switch (movement) {
            case 'U':
                zombie.moveUp(max);
                break;
            case 'D':
                zombie.moveDown(max);
                break;
            case 'L':
                zombie.moveLeft(max);
                break;
            case 'R':
                zombie.moveRight(max);
                break;
            default:
                System.err.println("Movement was skipped - Incorrect zombie movement specified: " + movement);
        }

        // Once the zombie has moved - check if any creatures have been bitten (and are now zombies)
        String key = zombie.getxPosition() + "-" + zombie.getyPosition();
        if (world.getCreatures().containsKey(key)) {
            List<Creature> creatures = world.getCreatures().get(key);
            world.addScore(creatures.size()); // update the zombie's overall score
            world.getActiveZombies().addAll(creatures); // add new zombies to start moving...

            world.getCreatures().get(key).clear(); // remove the creatures that are now zombies
            world.getCreatures().remove(key);
        }
    }

    /**
     * Add all the creatures (and one lone zombie) to the new world
     *
     * @param world     the new world (grid)
     * @param positions the list of creatures (or zombie) and their positions on the grid (world)
     * @param isZombie  true, if a zombie is being added, false if a series of creatures are being added
     */
    private void addCreatures(World world, String positions, boolean isZombie) {
        Pattern p = Pattern.compile(COORDINATE_PATTERN);
        Matcher m = p.matcher(positions);

        while (m.find()) {
            Creature creature = new Creature();
            String[] position = m.group(1).split(",");
            creature.setxPosition(Integer.parseInt(position[0].trim()));
            creature.setyPosition(Integer.parseInt(position[1].trim()));

            if (isZombie) {
                world.getActiveZombies().add(creature);
            } else {
                String key = creature.getxPosition() + "-" + creature.getyPosition();
                if (!world.getCreatures().containsKey(key)) {
                    world.getCreatures().put(key, new ArrayList<>());
                }
                world.getCreatures().get(key).add(creature);
            }
        }
    }

    /**
     * A validation check on the details supplied in the file
     *
     * @param setUp the list of set up information
     * @return true if valid set up information, false otherwise
     */
    private boolean isValid(List<String> setUp) {
        boolean valid = true;

        if (setUp.size() != 4) {
            System.err.println("Incorrect number of lines in the input file. Required 4 but found " + setUp.size());
            valid = false;
        } else if (!checkNumber(setUp.get(0))) {
            System.err.println("The 1st line should be a positive number defining the height and length of the world (grid)" +
                    " but found " + setUp.get(0));
            valid = false;
        } else if (!checkPositions(setUp.get(1), setUp.get(0), true)) {
            System.err.println("The 2nd line should be valid zombie co-ordinates but found " + setUp.get(1));
            valid = false;
        } else if (!checkPositions(setUp.get(2), setUp.get(0), false)) {
            System.err.println("The 3rd line should be valid creature co-ordinates but found " + setUp.get(2));
            valid = false;
        }

        return valid;
    }

    private boolean checkNumber(String number) {
        if (number == null || number.length() == 0) {
            System.err.println("No number is provided");
            return false;
        }

        int validNumber = -1;
        try {
            validNumber = Integer.parseInt(number);
        } catch (NumberFormatException nfe) {
            System.err.println("Number provided is not a valid number: " + number);
            return false;
        }

        if (validNumber >= 0) {
            return true;
        }

        System.err.println("Number provided is not a positive number: " + validNumber);
        return false;
    }

    /**
     * Check the zombie and creature co-ordinates to make sure they are valid
     *
     * @param position the zombie/creature co-ordinates
     * @param sizeStr  the size of the world (n by n)
     * @param isZombie whether the co-ordinates relate to a single zombie or a collection of creatures
     * @return true if the co-ordinates are valid, false otherwise
     */
    private boolean checkPositions(String position, String sizeStr, boolean isZombie) {
        boolean isValid = true;
        int size = Integer.parseInt(sizeStr); // we already checked if it was a valid number

        Pattern p = Pattern.compile(COORDINATE_PATTERN);
        Matcher m = p.matcher(position);

        int numberOfPositions = 0;
        while (m.find()) {
            String[] positionArray = m.group(1).split(",");
            String xStr = positionArray[0].trim();
            String yStr = positionArray[1].trim();

            if (checkNumber(xStr) && checkNumber(yStr)) {
                int x = Integer.parseInt(xStr);
                int y = Integer.parseInt(yStr);

                if (x >= size || y >= size) {
                    System.err.println("One of the position co-ordinates is outside the grid: " + m.group(1));
                    isValid = false;
                }
            } else {
                System.err.println("One of the position co-ordinates is not a valid number: " + m.group(1));
                isValid = false;
            }
            numberOfPositions++;
        }

        if (isZombie && numberOfPositions > 1) {
            System.err.println("More than one zombie has been provided: " + position);
            isValid = false;
        }

        return isValid;
    }

    /**
     * Read the external file provided in the command arguments for the world set up details
     *
     * @param fileName the external file name
     * @return the list of set up details for the zombie world
     */
    private List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<>();
        File initialFile = new File(fileName);

        try (InputStream inputStream = FileUtils.openInputStream(initialFile)) {
            lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * Read the internal resources file set up details
     *
     * @return the list of set up details for the zombie world
     */
    private List<String> readDefault() {
        List<String> lines = new ArrayList<>();
        ClassLoader classLoader = ZombieWorld.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(DEFAULT_FILE_NAME)) {
            lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
