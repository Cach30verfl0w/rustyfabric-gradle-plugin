package de.cacheoverflow.rustyfabric.plugin.webassembly.tasks;

import de.cacheoverflow.rustyfabric.plugin.utils.ProcessTask;

import java.util.List;

public class PrepareWebAssemblyTask extends ProcessTask {

    public PrepareWebAssemblyTask() {
        super("cargo", List.of("install", "wasm-pack"));
    }

}
