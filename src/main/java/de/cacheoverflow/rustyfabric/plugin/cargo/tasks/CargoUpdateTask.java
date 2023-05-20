package de.cacheoverflow.rustyfabric.plugin.cargo.tasks;

import de.cacheoverflow.rustyfabric.plugin.utils.ProcessTask;

import java.util.List;

public class CargoUpdateTask extends ProcessTask {

    public CargoUpdateTask() {
        super("cargo", List.of("update"));
    }

}
