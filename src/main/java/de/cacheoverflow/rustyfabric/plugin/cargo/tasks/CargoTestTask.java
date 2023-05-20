package de.cacheoverflow.rustyfabric.plugin.cargo.tasks;

import de.cacheoverflow.rustyfabric.plugin.utils.ProcessTask;

import java.util.List;

public class CargoTestTask extends ProcessTask {

    public CargoTestTask() {
        super("cargo", List.of("test"));
    }

}
