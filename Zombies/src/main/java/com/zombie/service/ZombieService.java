package com.zombie.service;

import com.zombie.data.World;

/**
 * The interface for the the service relating to the zombie world
 */
public interface ZombieService {
    /**
     * Create the intial world given the supplied details
     *
     * @param args either empty (if using the default resources file) or a file path to an external input file
     * @return World object that is created and populated with creatures and the initial zombie (or null if set up fails)
     */
    World createWorld(String[] args);

    /**
     * Monitor the movement of the initial zombie (and any subsequent zombies) around the world
     *
     * @param world the world in which the creatures and zombies co-exist
     */
    void monitorZombie(World world);
}
