package de.cacheoverflow.rustyfabric.plugin.cargo.tasks;

import de.cacheoverflow.rustyfabric.plugin.utils.ProcessTask;

import java.util.List;

public class CargoCleanTask extends ProcessTask {

    public CargoCleanTask() {
        super("cargo", List.of("clean"));
    }

}
