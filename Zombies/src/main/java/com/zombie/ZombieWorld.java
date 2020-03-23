package com.zombie;

import com.zombie.data.World;
import com.zombie.service.ZombieService;
import com.zombie.service.ZombieServiceImpl;

/**
 * The starting point for zombie adventures in a new world
 */
public class ZombieWorld {
    public static void main(String[] args) {
        ZombieService service = ZombieServiceImpl.getService();
        World world = service.createWorld(args);

        if (world != null) {
            while (world.isActiveZombies()) {
                service.monitorZombie(world);
            }

            System.out.println("zombies score: " + world.getScore());
            System.out.println("zombies positions: " + world.getZombiePositions());
        } else {
            System.out.println("The zombie world could not be set up...");
        }
    }
}
