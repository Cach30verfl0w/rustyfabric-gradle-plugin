package de.cacheoverflow.rustyfabric.plugin.cargo.tasks;

import de.cacheoverflow.rustyfabric.plugin.utils.ProcessTask;

import java.util.List;

public class CargoBuildTask extends ProcessTask {

    public CargoBuildTask() {
        super("cargo", List.of("build"));
    }

}
